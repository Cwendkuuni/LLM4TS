package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializers;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createParser("[ ]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithOneValue() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createParser("[\"hello\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserializeArrayWithMultipleValues() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createParser("[\"hello\", \"world\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullValues() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createParser("[null, \"world\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[]{null, "world"}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(StdSerializers.StringSerializer.instance);
        JsonParser jsonParser = createParser("[\"hello\", \"world\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createParser("{ \"name\": \"John\" }");
        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return deserializer.deserialize(p, ctxt);
            }
        };
        JsonParser jsonParser = createParser("[\"hello\", \"world\"]");
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"hello", "world"}, result.getClass().getComponentType().getComponentValues(result));
    }

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringProperty property = mapper.findProperty("test", String[].class);
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new CustomStringDeserializer()));
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(property, customDeserializer), property);
        assertNotNull(contextualDeserializer);
        assertTrue(contextualDeserializer instanceof StringArrayDeserializer);
        StringArrayDeserializer stringArrayDeserializer = (StringArrayDeserializer) contextualDeserializer;
        assertEquals(customDeserializer, stringArrayDeserializer._elementDeserializer);
    }

    private JsonParser createParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(jsonString));
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText().toUpperCase();
        }
    }

    private static class TestContext extends DeserializationContext {
        private TestContext() {
            super(ObjectMapper.defaultInstance(), new Object(), null);
        }

        private TestContext(BeanProperty property, JsonDeserializer<?> deserializer) {
            super(ObjectMapper.defaultInstance(), new Object(), new StandardDeserializationContext.Builder()
                    .withActiveContext(ObjectMapper.defaultInstance().getSerializationConfig().getDefaultContext())
                    .withProperty(property)
                    .withValueDeserializer(deserializer)
                    .build());
        }
    }

    private static void assertArrayEquals(Object expected, Object actual) {
        if (!expected.getClass().isArray() || !actual.getClass().isArray()) {
            super.assertArrayEquals(expected, actual);
        } else {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            if (expectedArray.length != actualArray.length) {
                fail("Array lengths are not equal: expected=" + expectedArray.length + ", actual=" + actualArray.length);
            }
            for (int i = 0; i < expectedArray.length; i++) {
                assertEquals(expectedArray[i].getClass().getComponentType(), actualArray[i].getClass().getComponentType());
                assertEquals(expectedArray[i], actualArray[i]);
            }
        }
    }
}