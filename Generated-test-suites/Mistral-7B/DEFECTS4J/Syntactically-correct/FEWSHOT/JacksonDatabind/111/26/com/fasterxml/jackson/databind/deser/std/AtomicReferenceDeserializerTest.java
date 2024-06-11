package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() {
        JavaType javaType = new TypeReference<AtomicReference<Object>>() {}.getType();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaType, null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        DeserializationContext context = new TestContext();
        AtomicReference<Object> nullValue = AtomicReferenceDeserializer.getNullValue(context);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> emptyValue = new AtomicReference<Object>();
        assertEquals(emptyValue, AtomicReferenceDeserializer.getEmptyValue(null));
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        AtomicReference<Object> newReference = AtomicReferenceDeserializer.referenceValue(contents);
        assertEquals(reference, newReference);
        assertEquals(contents, newReference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        assertEquals(reference.get(), AtomicReferenceDeserializer.getReferenced(reference));
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        AtomicReference<Object> updatedReference = AtomicReferenceDeserializer.updateReference(reference, newValue);
        assertEquals(updatedReference, reference);
        assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        assertTrue(AtomicReferenceDeserializer.supportsUpdate(config));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestClass\",\"value\":42}";
        JsonNode node = mapper.readTree(json);
        JsonDeserializer<TestClass> deserializer = new TestClassDeserializer();
        TypeDeserializer typeDeserializer = mapper.getPolymorphicTypeDeserializer(TestClass.class, null);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(TestClass.class), null, typeDeserializer, deserializer);
        AtomicReference<TestClass> result = mapper.readerForUpdating(new AtomicReference<TestClass>())
                .readValue(node);
        TestClass expected = new TestClass(42);
        assertEquals(expected, result.get());
    }

    static class TestClass {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
        private int value;

        public TestClass() {}

        public TestClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static class TestClassDeserializer extends JsonDeserializer<TestClass> {
        @Override
        public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectCodec oc = p.getCodec();
            ObjectNode node = (ObjectNode) p.readValueAsTree();
            TestClass result = new TestClass(node.getInt("value"));
            setValue(result, node);
            return result;
        }

        static void setValue(TestClass obj, ObjectNode node) {
            try {
                Field field = TestClass.class.getDeclaredField("value");
                field.setAccessible(true);
                field.set(obj, node.getInt("value"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}