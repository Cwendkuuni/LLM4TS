package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for TypeDeserializerBase class
 */
public class TypeDeserializerBaseTest {

    private static final String TEST_CLASS_NAME = "com.test.MyTestClass";
    private static final String TEST_BASE_TYPE_NAME = "java.lang.String";
    private static final String TEST_TYPE_PROPERTY_NAME = "typeProperty";
    private static final String TEST_TYPE_ID = "testTypeId";
    private static final String TEST_TYPE_ID_RESOLVER_NAME = "TestTypeIdResolver";
    private static final JsonTypeInfo.As TEST_TYPE_INCLUCTION = JsonTypeInfo.As.EXTERNAL_PROPERTY;
    private static final boolean TEST_TYPE_ID_VISIBLE = true;

    private TypeDeserializerBase _typeDeserializerBase;
    private TypeIdResolver _typeIdResolver;

    @Before
    public void setUp() throws Exception {
        JavaType baseType = new TypeFactory().constructType(TEST_BASE_TYPE_NAME);
        _typeIdResolver = new TestTypeIdResolver();
        _typeDeserializerBase = new TypeDeserializerBase(baseType, _typeIdResolver, TEST_TYPE_PROPERTY_NAME, TEST_TYPE_ID_VISIBLE, null);
    }

    /**
     * Test for the default constructor
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(_typeDeserializerBase);
        assertEquals(TEST_BASE_TYPE_NAME, _typeDeserializerBase.baseTypeName());
        assertNull(_typeDeserializerBase.getPropertyName());
        assertNull(_typeDeserializerBase.getTypeIdResolver());
        assertNull(_typeDeserializerBase.getDefaultImpl());
        assertNull(_typeDeserializerBase.toString());
    }

    /**
     * Test for the constructor with parameters
     */
    @Test
    public void testConstructorWithParameters() {
        BeanProperty property = new BeanProperty("testProperty", JsonType.INT_CLASS, null);
        JavaType defaultImpl = new TypeFactory().constructType(Integer.class);
        _typeDeserializerBase = new TypeDeserializerBase(new TypeFactory().constructType(TEST_BASE_TYPE_NAME), _typeIdResolver, TEST_TYPE_PROPERTY_NAME, TEST_TYPE_ID_VISIBLE, defaultImpl);
        assertEquals(TEST_BASE_TYPE_NAME, _typeDeserializerBase.baseTypeName());
        assertEquals(TEST_TYPE_PROPERTY_NAME, _typeDeserializerBase.getPropertyName());
        assertEquals(_typeIdResolver, _typeDeserializerBase.getTypeIdResolver());
        assertEquals(defaultImpl, _typeDeserializerBase.getDefaultImpl());
        assertEquals("[" + TEST_BASE_TYPE_NAME + "; typeIdResolver:" + _typeIdResolver + "]", _typeDeserializerBase.toString());
    }

    /**
     * Test for the forProperty() method
     */
    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty("testProperty", JsonType.INT_CLASS, null);
        TypeDeserializer typeDeserializer = _typeDeserializerBase.forProperty(property);
        assertNotNull(typeDeserializer);
        assertFalse(typeDeserializer instanceof TypeDeserializerBase);
    }

    /**
     * Test for the getTypeInclusion() method
     */
    @Test
    public void testGetTypeInclusion() {
        assertEquals(TEST_TYPE_INCLUSSION, _typeDeserializerBase.getTypeInclusion());
    }

    /**
     * Test for the baseTypeName() method
     */
    @Test
    public void testBaseTypeName() {
        assertEquals(TEST_BASE_TYPE_NAME, _typeDeserializerBase.baseTypeName());
    }

    /**
     * Test for the getPropertyName() method
     */
    @Test
    public void testGetPropertyName() {
        assertEquals(TEST_TYPE_PROPERTY_NAME, _typeDeserializerBase.getPropertyName());
    }

    /**
     * Test for the getTypeIdResolver() method
     */
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(_typeIdResolver, _typeDeserializerBase.getTypeIdResolver());
    }

    /**
     * Test for the getDefaultImpl() method
     */
    @Test
    public void testGetDefaultImpl() {
        assertNull(_typeDeserializerBase.getDefaultImpl());
    }

    /**
     * Test for the findDeserializer() method with a valid typeId
     */
    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        JsonDeserializer<Object> deserializer = _typeDeserializerBase._findDeserializer(new TestDeserializationContext(), TEST_TYPE_ID);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof TestJsonDeserializer);
    }

    /**
     * Test for the findDeserializer() method with an invalid typeId
     */
    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        _typeDeserializerBase._findDeserializer(new TestDeserializationContext(), "TestInvalidTypeId");
    }

    /**
     * Test for the findDefaultImplDeserializer() method
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = _typeDeserializerBase._findDefaultImplDeserializer(new TestDeserializationContext());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    /**
     * Test for the deserializeWithNativeTypeId() method with a null context
     */
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullContext() throws IOException {
        _typeDeserializerBase._deserializeWithNativeTypeId(null, new TestDeserializationContext(), null);
    }

    /**
     * Test for the deserializeWithNativeTypeId() method with a null typeId
     */
    @Test
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws IOException {
        JsonParser parser = new TestJsonParser();
        DeserializationContext ctxt = new TestDeserializationContext();
        Object result = _typeDeserializerBase._deserializeWithNativeTypeId(parser, ctxt, null);
        assertNull(result);
    }

    /**
     * Test for the deserializeWithNativeTypeId() method with a valid typeId
     */
    @Test
    public void testDeserializeWithNativeTypeIdWithValidTypeId() throws IOException {
        JsonParser parser = new TestJsonParser();
        DeserializationContext ctxt = new TestDeserializationContext();
        Object result = _typeDeserializerBase._deserializeWithNativeTypeId(parser, ctxt, TEST_TYPE_ID);
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
    }

    /**
     * Test for the handleUnknownTypeId() method
     */
    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        _typeDeserializerBase._handleUnknownTypeId(new TestDeserializationContext(), "TestUnknownTypeId", _typeIdResolver, null);
    }

    /**
     * Test class for JsonDeserializer
     */
    public static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new TestObject();
        }
    }

    /**
     * Test class for TestDeserializationContext
     */
    public static class TestDeserializationContext extends DeserializationContext {
        @Override
        public JavaType findContextualValueType(Class<?> type, BeanProperty property) throws IOException {
            return new TypeFactory().constructType(TestObject.class);
        }
    }

    /**
     * Test class for TestJsonParser
     */
    public static class TestJsonParser extends JsonParser {
        @Override
        public Object getValueAsTree() throws IOException {
            return null;
        }

        @Override
        public String getValueAsString() throws IOException {
            return null;
        }

        @Override
        public String getValueAsText() throws IOException {
            return null;
        }

        @Override
        public intgetValueAsInt() throws IOException {
            return 0;
        }

        @Override
        public longgetValueAsLong() throws IOException {
            return 0;
        }

        @Override
        public double getValueAsDouble() throws IOException {
            return 0.0;
        }

        @Override
        public boolean getValueAsBoolean() throws IOException {
            return false;
        }

        @Override
        public byte[] getValueAsBinary() throws IOException {
            return new byte[0];
        }

        @Override
        public Object readValueAs(Class<?> valueType) throws IOException {
            if (valueType.isAssignableFrom(TestObject.class)) {
                return new TestObject();
            }
            throw new IOException("Unable to parse value as " + valueType.getName());
        }

        @Override
        public Object readValueAs(Class<Object> valueType) throws IOException {
            return null;
        }
    }

    /**
     * Test class for TestObject
     */
    public static class TestObject {
    }

    /**
     * Test class for TestTypeIdResolver
     */
    public static class TestTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JavaType idFromValue(Object value) {
            if (value instanceof TestObject) {
                return new TypeFactory().constructType(TestObject.class);
            }
            return null;
        }

        @Override
        public String idFromValueAsString(Object value) {
            if (value instanceof TestObject) {
                return TEST_TYPE_ID;
            }
            return null;
        }
    }
}