package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT = new TypeDeserializerBase(
            Object.class.getTypeName(), null, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    return null;
                }
            }, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER_TYPEID = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    return Map.class;
                }
            }, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER_MISSING = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    throw new IOException("Missing type id");
                }
            }, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER_INVALID = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    return String.class;
                }
            }, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_DEFAULT_IMPL = new TypeDeserializerBase(
            Object.class.getTypeName(), null, null, false, String.class.getTypeName()) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return new NullifyingDeserializer.Instance();
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER_AND_DEFAULT_IMPL = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    return null;
                }
            }, null, false, String.class.getTypeName()) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return new NullifyingDeserializer.Instance();
        }
    };

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_OBJECT_WITH_ID_RESOLVER_MISSING_IMPL = new TypeDeserializerBase(
            Object.class.getTypeName(), new TypeIdResolver() {
                @Override
                public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                        throws IOException {
                    return null;
                }
            }, null, false, null) {
        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return null;
        }
    };

    @SuppressWarnings("result")
    @Test
    public void testForProperty() throws Exception {
        // Test with no property
        TypeDeserializerBase tdb = TEST_OBJECT;
        assertNotNull(tdb.forProperty(null));

        // Test with property
        BeanProperty prop = new BeanProperty("testProperty", String.class);
        TypeDeserializerBase tdbWithProp = TEST_OBJECT_WITH_ID_RESOLVER;
        TypeDeserializer expected = tdbWithProp;
        TypeDeserializer actual = tdbWithProp.forProperty(prop);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseTypeName() throws Exception {
        assertEquals(Object.class.getTypeName(), TEST_OBJECT.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals(null, TEST_OBJECT.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertNull(TEST_OBJECT.getTypeIdResolver());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializer_NullContext() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_ID_RESOLVER;
        tdb.findDeserializer(null, null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializer_NullTypeId() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_ID_RESOLVER;
        tdb.findDeserializer(null, null);
    }

    @Test
    public void testFindDeserializer_ValidTypeId() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_ID_RESOLVER;
        String typeId = "testTypeId";
        JsonDeserializer<Object> expected = new NullifyingDeserializer.Instance();
        JsonDeserializer<Object> actual = tdb.findDeserializer(null, typeId);
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializer_NullContext() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_DEFAULT_IMPL;
        tdb.findDefaultImplDeserializer(null);
    }

    @Test
    public void testFindDefaultImplDeserializer_DefaultImplIsNotNull() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_DEFAULT_IMPL;
        JsonDeserializer<Object> expected = new NullifyingDeserializer.Instance();
        JsonDeserializer<Object> actual = tdb.findDefaultImplDeserializer(null);
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializer_DefaultImplIsNull() throws Exception {
        TypeDeserializerBase tdb = TEST_OBJECT_WITH_ID_RESOLVER_MISSING_IMPL;
        tdb.findDefaultImplDeserializer(null);
    }
}