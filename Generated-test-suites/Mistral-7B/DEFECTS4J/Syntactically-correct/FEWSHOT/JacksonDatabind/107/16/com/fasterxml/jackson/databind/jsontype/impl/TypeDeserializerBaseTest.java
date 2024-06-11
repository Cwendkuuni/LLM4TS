package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyConstructor() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, null, null, false, null);
        assertNull(tdb._property);
        assertNull(tdb._defaultImplDeserializer);
        assertNotNull(tdb._idResolver);
        assertNotNull(tdb._deserializers);
        assertEquals(_defaultDeserializerClass, tdb.getClass());
    }

    @Test
    public void testTypeDeserializerBaseWithProperty() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                MAPPER.findBeanProperty(TestBaseType.class, "testProperty"),
                "testPropertyName", true, new TestType());
        assertNotNull(tdb._property);
        assertNull(tdb._defaultImplDeserializer);
        assertNotNull(tdb._idResolver);
        assertNotNull(tdb._deserializers);
        assertEquals(TestType.class, tdb._defaultImpl.getRawClass());
        assertEquals("testPropertyName", tdb.getPropertyName());
        assertEquals(TestDeserializer.class, tdb.getClass());
    }

    @Test
    public void testFindDeserializerWithEmptyCache() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        JsonDeserializer<Object> deserializer = tdb.findDeserializer(MAPPER.getDeserializationContext(), "TestType1");
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithCachedDeserializer() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        tdb._deserializers.put("TestType1", new TestDeserializer1());

        JsonDeserializer<Object> deserializer = tdb.findDeserializer(MAPPER.getDeserializationContext(), "TestType1");
        assertSame(new TestDeserializer1(), deserializer);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        JsonDeserializer<Object> deserializer = tdb.findDeserializer(MAPPER.getDeserializationContext(), null);
        assertSame(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDeserializerWithMissingTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        try {
            tdb.findDeserializer(MAPPER.getDeserializationContext(), "MissingType");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        JsonDeserializer<Object> deserializer = tdb.findDeserializer(MAPPER.getDeserializationContext(), "UnknownType");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        JsonDeserializer<Object> deserializer = tdb.findDefaultImplDeserializer(MAPPER.getDeserializationContext());
        assertSame(TestDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        try {
            tdb.handleUnknownTypeId(MAPPER.getDeserializationContext(), "UnknownType");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase(
                new TypeDeserializerBaseTest_TestBaseType(),
                null,
                "testPropertyName",
                false,
                new TestType());

        try {
            tdb.handleMissingTypeId(MAPPER.getDeserializationContext(), "TestBaseType");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private static class TestDeserializer1 extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class TestDeserializer2 extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class TestTypeDeserializer extends TypeDeserializerBase<Object> {
        protected TestTypeDeserializer(TypeDeserializerBase<Object> src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializer(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.WRAP_ROOT_VALUE;
        }
    }

    private static class TestBaseType {
        String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    private static class TestType extends TestBaseType {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAP_ROOT_VALUE)
        String testTypeProperty;

        public String getTestTypeProperty() {
            return testTypeProperty;
        }

        public void setTestTypeProperty(String testTypeProperty) {
            this.testTypeProperty = testTypeProperty;
        }
    }

    private static Class<? extends TypeDeserializerBase<?>> _defaultDeserializerClass = TypeDeserializerBase.class;
}