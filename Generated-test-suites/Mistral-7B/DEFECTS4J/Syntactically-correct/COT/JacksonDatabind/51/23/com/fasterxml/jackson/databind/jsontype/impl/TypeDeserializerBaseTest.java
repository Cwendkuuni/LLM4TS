package com.example.test; // Replace 'com.example.test' with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class TypeDeserializerBaseTest {

    private static final String CLASS_NAME = "com.example.MyClass";
    private static final String PROPERTY_NAME = "myProperty";
    private static final String TYPE_ID = "myTypeId";
    private static final String SUB_TYPE_ID = "subTypeId";
    private static final String UNRESOLVABLE_TYPE_ID = "unresolvableTypeId";

    private DeserializationContext context;
    private JsonParser parser;
    private ObjectMapper mapper;
    private TypeDeserializerBase deserializer;

    @Before
    public void setUp() throws Exception {
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        mapper = new ObjectMapper();

        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        JavaType myClassType = mapper.getTypeFactory().constructType(CLASS_NAME);
        JavaType myClassSubType = mapper.getTypeFactory().constructType(CLASS_NAME + "$$SubClass");

        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromEmptyContext(context)).thenReturn(null);
        when(idResolver.getDeserializer(context, myClassType)).thenReturn(createMockDeserializer());
        when(idResolver.getDeserializer(context, myClassSubType)).thenReturn(createMockDeserializer());

        deserializer = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
    }

    @Test
    public void testBasicFunctionality() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(TYPE_ID);

        Object result = deserializer.deserialize(parser, context);
        assertEquals(createMockDeserializer().getClass(), result.getClass());
    }

    @Test
    public void testNullTypeId() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(null);

        Object result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testUnknownTypeId() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(UNRESOLVABLE_TYPE_ID);

        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerNotFound() throws IOException, JsonProcessingException {
        when(idResolver.getDeserializer(context, myClassType)).thenReturn(null);

        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerNotFoundSubType() throws IOException, JsonProcessingException {
        when(idResolver.getDeserializer(context, myClassSubType)).thenReturn(null);

        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDefaultImplDeserializer() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(null);

        Object result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDefaultImplDeserializerWithProperty() throws IOException, JsonProcessingException {
        BeanProperty property = new BeanProperty("myProperty", null, null);
        when(parser.getText()).thenReturn(null);

        Object result = deserializer.deserialize(parser, context, property);
        assertNull(result);
    }

    @Test
    public void testDefaultImplWithEmptyContext() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(null);
        when(idResolver.idFromEmptyContext(context)).thenReturn(TYPE_ID);

        Object result = deserializer.deserialize(parser, context);
        assertEquals(createMockDeserializer().getClass(), result.getClass());
    }

    @Test
    public void testDeserializerWithProperty() throws IOException, JsonProcessingException {
        BeanProperty property = new BeanProperty("myProperty", null, null);

        when(parser.getText()).thenReturn(TYPE_ID);
        when(context.findContextualValueDeserializer(myClassType, property)).thenReturn(createMockDeserializer());

        Object result = deserializer.deserialize(parser, context, property);
        assertEquals(createMockDeserializer().getClass(), result.getClass());
    }

    @Test
    public void testDeserializerWithNativeTypeId() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(TYPE_ID);

        Object result = deserializer.deserializeWithNativeTypeId(parser, context);
        assertEquals(createMockDeserializer().getClass(), result.getClass());
    }

    @Test
    public void testDeserializerWithNativeTypeIdAndNoTypeId() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn(null);

        Object result = deserializer.deserializeWithNativeTypeId(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializerWithNativeTypeIdAndInvalidTypeId() throws IOException, JsonProcessingException {
        when(parser.getText()).thenReturn("invalidTypeId");

        try {
            deserializer.deserializeWithNativeTypeId(parser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private JsonDeserializer<Object> createMockDeserializer() {
        return mock(JsonDeserializer.class);
    }
}