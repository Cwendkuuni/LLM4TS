package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

public class TypeDeserializerBaseTest extends BaseMapTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Test for base class constructor.
     * @throws IOException
     */
    @Test
    public void testTypeDeserializerBaseConstruction() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class));
        assertEquals("String", td.baseTypeName());
        assertEquals("typePropertyName", td.getPropertyName());
        assertEquals(idResolver, td.getTypeIdResolver());
        assertEquals(Integer.class, td.getDefaultImpl());
        assertNull(td._property);
    }

    /**
     * Test for copy constructor.
     * @throws IOException
     */
    @Test
    public void testTypeDeserializerBaseCopyConstruction() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        BeanProperty property = MAPPER.findProperty("test", String[].class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class));
        TypeDeserializerBase tdCopy = new TypeDeserializerBase(td, property);
        assertEquals(td, tdCopy);
        assertEquals("String", tdCopy.baseTypeName());
        assertEquals("typePropertyName", tdCopy.getPropertyName());
        assertEquals(idResolver, tdCopy.getTypeIdResolver());
        assertEquals(Integer.class, tdCopy.getDefaultImpl());
        assertEquals(property, tdCopy._property);
    }

    /**
     * Test for {@link #forProperty(BeanProperty)}.
     * @throws IOException
     */
    @Test
    public void testForProperty() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        BeanProperty property = MAPPER.findProperty("test", String[].class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class));
        TypeDeserializerBase tdForProperty = td.forProperty(property);
        assertEquals(td, tdForProperty);
        assertNull(tdForProperty._property);
    }

    /**
     * Test for {@link #getTypeInclusion()}.
     * @throws IOException
     */
    @Test
    public void testgetTypeInclusion() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class));
        JsonTypeInfo.As actual = td.getTypeInclusion();
        JsonTypeInfo.As expected = JsonTypeInfo.As.PROPERTY;
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link #toString()}.
     * @throws IOException
     */
    @Test
    public void testToString() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class));
        String expected = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver: com.fasterxml.jackson.databind.JsonDeserializerFactory$ReferenceTypeIdResolver@1c21f3ba";
        String actual = td.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link #_findDeserializer(DeserializationContext, String)} and {@link #_findDefaultImplDeserializer(DeserializationContext)}.
     * @throws IOException
     */
    @Test
    public void testFindDeserializerAndFindDefaultImplDeserializer() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        JsonDeserializer<Object> stringDeserializer = new StringDeserializer();
        JsonDeserializer<Object> integerDeserializer = new IntegerDeserializer();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class)) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if ("string".equals(typeId)) {
                    return stringDeserializer;
                }
                return super._findDeserializer(ctxt, typeId);
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return integerDeserializer;
            }
        };

        String jsonString = "{\"typeId\":\"string\", \"value\":\"foo\"}";
        JsonParser jsonParser = MAPPER.getFactory().createParser(jsonString);
        DeserializationContext context = MAPPER.getFactory().createContext();
        JsonDeserializer<Object> deserializer = td._findDeserializer(context, "string");
        Object result = deserializer.deserialize(jsonParser, context);
        assertEquals("foo", result);

        jsonParser = MAPPER.getFactory().createParser(jsonString);
        context = MAPPER.getFactory().createContext();
        deserializer = td._findDeserializer(context, "integer");
        assertNull(deserializer);

        jsonParser = MAPPER.getFactory().createParser(jsonString);
        context = MAPPER.getFactory().createContext();
        deserializer = td._findDefaultImplDeserializer(context);
        assertEquals(integerDeserializer, deserializer);

        jsonParser = MAPPER.getFactory().createParser("{\"value\":1}");
        context = MAPPER.getFactory().createContext();
        deserializer = td._findDefaultImplDeserializer(context);
        Object resultInt = deserializer.deserialize(jsonParser, context);
        assertEquals(1, resultInt);
    }

    /**
     * Test for {@link #_handleUnknownTypeId(DeserializationContext, String)} and {@link #_handleMissingTypeId(DeserializationContext, String)}.
     * @throws IOException
     */
    @Test
    public void testHandleUnknownTypeIdAndHandleMissingTypeId() throws IOException {
        TypeIdResolver idResolver = MAPPER.getTypeFactory().getPolymorphicTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, MAPPER.getTypeFactory().constructType(Integer.class)) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
                return MAPPER.getTypeFactory().constructType(Float.class);
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
                return MAPPER.getTypeFactory().constructType(Double.class);
            }
        };

        String jsonString1 = "{\"typeId\":\"unknown\"}";
        JsonParser jsonParser1 = MAPPER.getFactory().createParser(jsonString1);
        DeserializationContext context1 = MAPPER.getFactory().createContext();
        JsonDeserializer<Object> deserializer1 = td._findDefaultImplDeserializer(context1);
        Object result1 = deserializer1.deserialize(jsonParser1, context1);
        assertEquals(Double.class, result1.getClass());

        String jsonString2 = "{\"typeId\":\"missing\"}";
        JsonParser jsonParser2 = MAPPER.getFactory().createParser(jsonString2);
        DeserializationContext context2 = MAPPER.getFactory().createContext();
        JsonDeserializer<Object> deserializer2 = td._findDefaultImplDeserializer(context2);
        Object result2 = deserializer2.deserialize(jsonParser2, context2);
        assertEquals(Float.class, result2.getClass());
    }

    /**
     * Stub classes for testing.
     */
    private static class StringDeserializer extends JsonDeserializer<String> {
    }

    private static class IntegerDeserializer extends JsonDeserializer<Integer> {
    }
}