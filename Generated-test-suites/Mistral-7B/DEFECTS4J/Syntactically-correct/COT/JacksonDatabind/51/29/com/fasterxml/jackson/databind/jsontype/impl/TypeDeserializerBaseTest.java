package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final Class<TypeDeserializerBase> TYPE_DESERIALIZER_BASE = (Class<TypeDeserializerBase>) Class.forName("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase");

    @SuppressWarnings("rawtypes")
    private static final Type DESERIALIZER_BASE_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_baseType").getType();
    @SuppressWarnings("rawtypes")
    private static final Type ID_RESOLVER_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_idResolver").getType();
    @SuppressWarnings("rawtypes")
    private static final Type PROPERTY_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_property").getType();
    @SuppressWarnings("rawtypes")
    private static final Type TYPE_PROPERTY_NAME_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_typePropertyName").getType();
    @SuppressWarnings("rawtypes")
    private static final Type TYPE_ID_VISIBLE_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_typeIdVisible").getType();
    @SuppressWarnings("rawtypes")
    private static final Type DESERIALIZERS_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_deserializers").getType();
    @SuppressWarnings("rawtypes")
    private static final Type DEFAULT_IMPL_TYPE = TYPE_DESERIALIZER_BASE.getDeclaredField("_defaultImpl").getType();

    @SuppressWarnings("unchecked")
    private static final Class<ConcurrentHashMap> CONCURRENT_HASH_MAP = (Class<ConcurrentHashMap>) Class.forName("java.util.concurrent.ConcurrentHashMap");

    @BeforeClass
    public static void setup() throws Exception {
        // Set up the class to be testable
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(DESERIALIZER_BASE_TYPE, ID_RESOLVER_TYPE, null, true, DEFAULT_IMPL_TYPE) {
            @SuppressWarnings("unchecked")
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        TypeDeserializerBase testObject = new TypeDeserializerBase(typeDeserializerBase, null);
        java.lang.reflect.Field fields = TYPE_DESERIALIZER_BASE.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            field.set(testObject, field.get(typeDeserializerBase));
        }
        TestCase.currentTest = this; // needed for JsonTestBase
    }

    @AfterClass
    public static void teardown() {
        TestCase.currentTest = null;
    }

    /**
     * Test for constructor.
     */
    @Test
    public void testTypeDeserializerBaseConstruction() {
        assertNotNull(new TypeDeserializerBase(null, null, null, false, null));
    }

    /**
     * Test for constructor with properties.
     */
    @Test
    public void testTypeDeserializerBaseConstructionWithProperties() {
        assertNotNull(new TypeDeserializerBase(null, null, null, false, null, null, null, null, null, null));
    }

    /**
     * Test for method forProperty.
     */
    @Test
    public void testForProperty() throws Exception {
        // Create test object
        Object testObject = new TypeDeserializerBase(DESERIALIZER_BASE_TYPE, ID_RESOLVER_TYPE, null, true, DEFAULT_IMPL_TYPE) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return new TypeDeserializerBase(this, prop);
            }
        };
        // Test call
        TypeDeserializer result = ((TypeDeserializerBase) testObject.forProperty(null)).forProperty(null);
        // Verify result
        assertNotNull(result);
    }

    /**
     * Test for method getTypeInclusion.
     */
    @Test
    public void testGetTypeInclusion() {
        assertNull(new TypeDeserializerBase(null, null, null, false, null).getTypeInclusion());
    }

    /**
     * Test for method baseTypeName.
     */
    @Test
    public void testBaseTypeName() {
        String result = new TypeDeserializerBase(null, null, null, false, null).baseTypeName();
        // Verify result
        assertNotNull(result);
    }

    /**
     * Test for method getPropertyName.
     */
    @Test
    public void testGetPropertyName() {
        String result = new TypeDeserializerBase(null, null, null, false, null).getPropertyName();
        // Verify result
        assertNull(result);
    }

    /**
     * Test for method getTypeIdResolver.
     */
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = new TypeDeserializerBase(null, null, null, false, null).getTypeIdResolver();
        // Verify result
        assertNotNull(result);
    }

    /**
     * Test for method getDefaultImpl.
     */
    @Test
    public void testGetDefaultImpl() {
        Class<?> result = new TypeDeserializerBase(null, null, null, false, null).getDefaultImpl();
        // Verify result
        assertNull(result);
    }

    /**
     * Test for method toString.
     */
    @Test
    public void testToString() {
        String result = new TypeDeserializerBase(null, null, null, false, null).toString();
        // Verify result
        assertNotNull(result);
    }

    /**
     * Test for method _findDeserializer.
     */
    @Test
    public void testFindDeserializer() throws IOException {
        Object testObject = new TypeDeserializerBase(DESERIALIZER_BASE_TYPE, ID_RESOLVER_TYPE, null, true, DEFAULT_IMPL_TYPE) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return (Map<String, JsonDeserializer<Object>>) new ConcurrentHashMap<>();
            }
        };

        JsonDeserializer<Object> result = testObject._findDeserializer(new DeserializationContext(new ObjectMapper()), "test");
        // Verify result
        assertNull(result);
    }

    /**
     * Test for method _findDefaultImplDeserializer.
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        Object testObject = new TypeDeserializerBase(DESERIALIZER_BASE_TYPE, ID_RESOLVER_TYPE, null, true, null) {
            @Override
            protected JsonDeserializer<Object> _defaultImplDeserializer() {
                return NullifyingDeserializer.instance;
            }
        };

        JsonDeserializer<Object> result = testObject._findDefaultImplDeserializer(new DeserializationContext(new ObjectMapper()));
        // Verify result
        assertNotNull(result);
    }

    /**
     * Test for method _deserializeWithNativeTypeId.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object testObject = new TypeDeserializerBase(DESERIALIZER_BASE_TYPE, ID_RESOLVER_TYPE, null, true, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                throw new IOException("Test");
            }
        };

        testObject._deserializeWithNativeTypeId(null, new DeserializationContext(new ObjectMapper()), null);
    }

    /**
     * Test for method _handleUnknownTypeId.
     */
    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        Object testObject = new TypeDeserializerBase(DESERIALIZER_BAS_TYPE, null, null, true, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                throw new IOException("Test");
            }
        };

        testObject._handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "test", null, null);
    }
}