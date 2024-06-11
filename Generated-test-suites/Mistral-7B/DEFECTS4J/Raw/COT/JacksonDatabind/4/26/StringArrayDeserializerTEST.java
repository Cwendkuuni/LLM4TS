package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private static final StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\",\"world\"]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = mapper.deserializerFactory().findValueDeserializer(String.class, null);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(customElementDeserializer);
        String json = "[\"hello\",\"world\"]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testDeserializeWithoutElementDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\",\"world\"]";
        try {
            stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Cannot deserialize value of type [Ljava.lang.String; from ArrayContext: empty context", e.getMessage());
        }
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeNullArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeNullElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeEmptyStringElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"\"]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
        Assert.assertArrayEquals(new String[]{""}, result);
    }

    @Test
    public void testDeserializeCustomClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\",\"world\"]";
        String[] result = stringArrayDeserializer.deserialize(mapper.parser(), new TestContextWithCustomClass());
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{";
        stringArrayDeserializer.deserialize(mapper.parser(), new TestContext());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.deserializerFactory().findValueDeserializer(String[].class, null);
        String json = "[\"hello\",\"world\"]";
        Object result = stringArrayDeserializer.deserializeWithType(mapper.parser(), new TestContext(), typeDeserializer);
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.deserializerFactory().findValueDeserializer(Integer.class, null);
        String json = "[\"hello\",\"world\"]";
        stringArrayDeserializer.deserializeWithType(mapper.parser(), new TestContext(), typeDeserializer);
    }

    @Test
    public void testCreateContextual() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = mapper.deserializerFactory().findValueDeserializer(String.class, null);
        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(mapper.deserializationContext(), new StubProperty());
        Assert.assertNotNull(result);
        Assert.assertEquals(StringArrayDeserializer.class, result.getClass());
        Assert.assertNotNull(result.getNullValue());
        if (result instanceof StringArrayDeserializer) {
            StringArrayDeserializer deserializer = (StringArrayDeserializer) result;
            Assert.assertEquals(customElementDeserializer, deserializer._elementDeserializer);
        }
    }

    @Test
    public void testCreateContextualWithoutElementDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(mapper.deserializationContext(), new StubProperty());
        Assert.assertNull(result);
    }

    @Test
    public void testCreateContextualWithNullContextualDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(mapper.deserializationContext(), null);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateContextualWithContentConverter() {
        ObjectMapper mapper = new ObjectMapper();
        StringContentConverter contentConverter = mapper.getFactory().getSerializerFactory().findValueSerializer(String.class, null);
        JsonDeserializer<String> result = stringArrayDeserializer.createContextual(mapper.deserializationContext(), new StubProperty());
        Assert.assertNotNull(result);
        Assert.assertEquals(StringArrayDeserializer.class, result.getClass());
        Assert.assertNotNull(result.getNullValue());
        if (result instanceof StringArrayDeserializer) {
            StringArrayDeserializer deserializer = (StringArrayDeserializer) result;
            Assert.assertNull(deserializer._elementDeserializer);
            Assert.assertEquals(contentConverter, deserializer.findConvertingContentDeserializer(mapper.deserializationContext(), new StubProperty(null), null));
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _constructMapper() {
            return new ObjectMapper();
        }

        @Override
        public ObjectBuffer _leaseObjectBuffer() {
            return ObjectBuffer.allocate(16);
        }
    }

    private static class TestContextWithCustomClass extends TestContext {
        @Override
        public JavaType constructType(Class<?> baseType) {
            return new ArrayType(String.class, null);
        }
    }

    private static class StubProperty {
        private final String name;

        public StubProperty() {
            this(null);
        }

        public StubProperty(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}