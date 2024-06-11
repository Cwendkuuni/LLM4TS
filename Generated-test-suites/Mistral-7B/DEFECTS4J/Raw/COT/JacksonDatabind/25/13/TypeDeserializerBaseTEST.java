package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final TypeIdResolver ID_RESOLVER = new DefaultTypeIdResolver();

    private JsonDeserializer<Object> defaultDeserializer;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() throws IOException {
        defaultDeserializer = JSON_FACTORY.getDeserializerFactory().findValueDeserializer(Integer.class, null);
        typeDeserializer = new TypeDeserializerBase(
                JavaType.constructParametricType(TypeDeserializerBase.class, Integer.class),
                ID_RESOLVER, "typePropertyName", true, Integer.class) {
                    @Override
                    public TypeDeserializer forProperty(BeanProperty prop) {
                        return this;
                    }

                    @Override
                    public JsonTypeInfo.As getTypeInclusion() {
                        return JsonTypeInfo.As.PROPERTY;
                    }

                    @Override
                    protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                        return defaultDeserializer;
                    }
                };
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = JSON_FACTORY.findBeanProperty("propertyName", Integer.class);
        TypeDeserializer deserializer = typeDeserializer.forProperty(property);
        assertEquals("Expected 'TypeDeserializerBase' instance", typeDeserializer.getClass(), deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testGetTypeInclusionWithMissingProperty() throws IOException {
        typeDeserializer = new TypeDeserializerBase(
                JavaType.constructParametricType(TypeDeserializerBase.class, Integer.class),
                ID_RESOLVER, null, true, Integer.class);
        typeDeserializer.getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() throws IOException {
        assertEquals("Expected 'Integer' class name", "Integer", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws IOException {
        assertEquals("Expected 'typePropertyName' property name", "typePropertyName", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        assertEquals("Expected 'ID_RESOLVER' instance", ID_RESOLVER, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        Class<Integer> defaultImpl = (Class<Integer>) typeDeserializer.getDefaultImpl();
        assertEquals("Expected 'Integer' class", Integer.class, defaultImpl);
    }

    @Test
    public void testToString() throws IOException {
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.Integer; id-resolver:" + ID_RESOLVER.getClass().getName() + "]";
        assertEquals("Expected '" + expectedToString + "' string representation", expectedToString, typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializerWithKnownTypeId() throws IOException {
        String knownTypeId = "com.example.MyType";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(JSON_FACTORY.getDeserializationContext(), knownTypeId);
        assertNotNull("Expected deserializer instance for known type id", deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        typeDeserializer._deserializers.clear();
        String unknownTypeId = "unknownTypeId";
        typeDeserializer._findDeserializer(JSON_FACTORY.getDeserializationContext(), unknownTypeId);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        typeDeserializer._defaultImplDeserializer = null;
        typeDeserializer._findDefaultImplDeserializer(null);
    }

    @Test
    public void testFindDefaultImplDeserializerWithContext() throws IOException {
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(JSON_FACTORY.getDeserializationContext());
        assertNotNull("Expected deserializer instance for default implementation", deserializer);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullContext() throws IOException {
        typeDeserializer._deserializeWithContext(JSON_FACTORY.getJsonParser(), JSON_FACTORY.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithContextAndDefaultImpl() throws IOException {
        JsonParser jsonParser = JSON_FACTORY.createParser("{\"typeId\":\"java.lang.Integer\"}");
        Object result = typeDeserializer._deserializeWithContext(jsonParser, JSON_FACTORY.getDeserializationContext());
        assertEquals("Expected 'Integer' instance", 42, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithContextAndUnknownTypeId() throws IOException {
        JsonParser jsonParser = JSON_FACTORY.createParser("{\"typeId\":\"unknownTypeId\"}");
        typeDeserializer._deserializeWithContext(jsonParser, JSON_FACTORY.getDeserializationContext());
    }
}

class DefaultTypeIdResolver implements TypeIdResolver {
    @Override
    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
        return null;
    }

    @Override
    public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
        return null;
    }

    @Override
    public String idFromValueAndType(DeserializationContext ctxt, Class<?> cls) throws IOException {
        return null;
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }
}