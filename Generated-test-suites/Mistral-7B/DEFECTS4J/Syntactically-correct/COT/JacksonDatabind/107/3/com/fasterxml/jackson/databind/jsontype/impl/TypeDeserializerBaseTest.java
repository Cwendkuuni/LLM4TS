package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    private static final String CLASS_NAME = "com.example.MyClass";
    private static final String PROPERTY_NAME = "property";
    private static final String TYPE_PROPERTY_NAME = "typeProperty";
    private static final String TYPE_ID = "typeId";
    private static final String TYPE_ID_RESOLVER = "TypeIdResolverImpl";
    private static final String DEFAULT_IMPL = "DefaultImpl";

    private TypeDeserializerBase typeDeserializer;
    private DeserializationContext ctxt;
    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        ctxt = Mockito.mock(DeserializationContext.class);
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        typeDeserializer = new TypeDeserializerBase(
                TypeFactory.defaultInstance.constructType(CLASS_NAME),
                Mockito.mock(TypeIdResolver.class),
                TYPE_PROPERTY_NAME,
                true,
                TypeFactory.defaultInstance.constructType(DEFAULT_IMPL)) {
                    @Override
                    protected TypeDeserializer forProperty(BeanProperty prop) {
                        return new StdTypeDeserializer() {};
                    }
                };
        typeDeserializer.setTypeIdResolver(Mockito.mock(TypeIdResolver.class));
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        // Given
        BeanProperty property = Mockito.mock(BeanProperty.class);
        TypeDeserializer expected = new StdTypeDeserializer() {};

        // When
        TypeDeserializer actual = typeDeserializer.forProperty(property);

        // Then
        assertSame(expected, actual);
    }

    @Test
    public void testGetTypeInclusion() {
        // Given

        // When
        JsonTypeInfo.As actual = typeDeserializer.getTypeInclusion();

        // Then
        // The TypeDeserializerBase abstract class does not provide an implementation for getTypeInclusion().
        // Thus, this test case should fail.
        assertNull(actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTypeInclusionThrowsException() throws IOException, JsonProcessingException {
        // Given
        BeanProperty property = Mockito.mock(BeanProperty.class);
        TypeDeserializer baseType = Mockito.mock(TypeDeserializer.class);
        typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                throw new IllegalStateException("getTypeInclusion should not be called.");
            }
        };

        // When
        // The TypeDeserializerBase abstract class does not provide a default implementation for getTypeInclusion().
        // Thus, any subclass that does not override this method will throw an IllegalStateException when it is called.
        typeDeserializer.forProperty(property);
    }

    @Test
    public void testBaseTypeName() throws IOException, JsonProcessingException {
        // Given

        // When
        String actual = typeDeserializer.baseTypeName();

        // Then
        assertSame(CLASS_NAME, actual);
    }

    @Test
    public void testGetPropertyName() {
        // Given

        // When
        String actual = typeDeserializer.getPropertyName();

        // Then
        assertSame(TYPE_PROPERTY_NAME, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        // Given

        // When
        TypeIdResolver actual = typeDeserializer.getTypeIdResolver();

        // Then
        assertSame(Mockito.mock(TypeIdResolver.class), actual);
    }

    @Test
    public void testGetBaseType() throws IOException, JsonProcessingException {
        // Given

        // When
        JavaType actual = typeDeserializer.baseType();

        // Then
        JavaType expected = TypeFactory.defaultInstance.constructType(CLASS_NAME);
        assertSame(expected, actual);
    }

    @Test
    public void testToString() throws IOException, JsonProcessingException {
        // Given

        // When
        String actual = typeDeserializer.toString();

        // Then
        String expected = "[TypeDeserializerBase; base-type:class com.example.MyClass; id-resolver:mockTypeIdResolver]";
        assertSame(expected, actual);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Given
        String typeId = TYPE_ID;
        JsonDeserializer<Object> expected = Mockito.mock(JsonDeserializer.class);

        // When
        JsonDeserializer<Object> actual = typeDeserializer._findDeserializer(ctxt, typeId);

        // Then
        Mockito.verify(ctxt).findContextualValueDeserializer(Mockito.eq(TypeFactory.defaultInstance.constructType(CLASS_NAME)), Mockito.eq(Mockito.mock(BeanProperty.class)));
        Mockito.verifyNoMoreInteractions(ctxt);
        assertSame(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerThrowsIOException() throws IOException {
        // Given
        String typeId = TYPE_ID;
        Mockito.when(ctxt.findContextualValueDeserializer(Mockito.eq(TypeFactory.defaultInstance.constructType(CLASS_NAME)), Mockito.eq(Mockito.mock(BeanProperty.class))))
                .thenThrow(new IOException());

        // When & Then
        typeDeserializer._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Given

        // When
        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImpl() throws IOException, JsonProcessingException {
        // Given
        typeDeserializer = new TypeDeserializerBase(TypeFactory.defaultInstance.constructType(CLASS_NAME), null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        // When
        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImplDeserializer() throws IOException, JsonProcessingException {
        // Given
        typeDeserializer = new TypeDeserializerBase(TypeFactory.defaultInstance.constructType(CLASS_NAME), null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getDefaultImplDeserializer() {
                return null;
            }
        };

        // When
        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImplDeserializer() throws IOException, JsonProcessingException {
        // Given
        JsonDeserializer<Object> defaultImplDeserializer = Mockito.mock(JsonDeserializer.class);
        typeDeserializer = new TypeDeserializerBase(TypeFactory.defaultInstance.constructType(CLASS_NAME), null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return defaultImplDeserializer;
            }
        };

        // When
        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);

        // Then
        assertSame(defaultImplDeserializer, actual);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException, JsonProcessingException {
        // Given
        String typeId = TYPE_ID;

        // When
        JavaType actual = typeDeserializer._handleUnknownTypeId(ctxt, typeId);

        // Then
        assertNull(actual);
    }

    @Test
    public void testHandleUnknownTypeIdWithTypeIdResolver() throws IOException, JsonProcessingException {
        // Given
        String typeId = TYPE_ID;
        Mockito.when(ctxt.getTypeFactory().constructType(CLASS_NAME))
                .thenReturn(TypeFactory.defaultInstance.constructType(CLASS_NAME));
        Mockito.when(ctxt.handleUnknownTypeId(Mockito.eq(TypeFactory.defaultInstance.constructType(CLASS_NAME)), Mockito.eq(Mockito.mock(TypeIdResolver.class)), Mockito.anyString()))
                .thenReturn(TypeFactory.defaultInstance.constructType(DEFAULT_IMPL));

        // When
        JavaType actual = typeDeserializer._handleUnknownTypeId(ctxt, typeId);

        // Then
        assertSame(TypeFactory.defaultInstance.constructType(DEFAULT_IMPL), actual);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException, JsonProcessingException {
        // Given

        // When
        JavaType actual = typeDeserializer._handleMissingTypeId(ctxt, "extraDesc");

        // Then
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        // Given
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":42}";
        JsonParser parser = jsonFactory.createParser(json);

        // When
        Object actual = typeDeserializer._deserializeWithNativeTypeId(parser, ctxt);

        // Then
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException, JsonProcessingException {
        // Given
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":42}";
        JsonParser parser = jsonFactory.createParser(json);

        // When
        Object actual = typeDeserializer._deserializeWithNativeTypeId(parser, ctxt, TYPE_ID);

        // Then
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeIdResolver() throws IOException, JsonProcessingException {
        // Given
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":42}";
        JsonParser parser = jsonFactory.createParser(json);
        Mockito.when(ctxt.getTypeIdResolver().idFromValue(Mockito.eq(TYPE_ID_RESOLVER), Mockito.eq(parser)))
                .thenReturn(TYPE_ID);

        // When
        Object actual = typeDeserializer._deserializeWithNativeTypeId(parser, ctxt);

        // Then
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeIdAndTypeIdResolver() throws IOException, JsonProcessingException {
        // Given
        String json = "{\"typeId\":\"" + TYPE_ID + "\", \"value\":42}";
        JsonParser parser = jsonFactory.createParser(json);
        Mockito.when(ctxt.getTypeIdResolver().idFromValue(Mockito.eq(TYPE_ID_RESOLVER), Mockito.eq(parser)))
                .thenReturn(TYPE_ID);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(ctxt.findContextualValueDeserializer(Mockito.eq(TypeFactory.defaultInstance.constructType(CLASS_NAME)), Mockito.eq(Mockito.mock(BeanProperty.class))))
                .thenReturn(deserializer);

        // When
        Object actual = typeDeserializer._deserializeWithNativeTypeId(parser, ctxt);

        // Then
        Mockito.verify(deserializer).deserialize(parser, ctxt);
        assertSame(deserializer.getContentClass(), actual.getClass());
    }
}