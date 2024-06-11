package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link StringArrayDeserializer}.
 */
public class StringArrayDeserializerTest {

    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        JsonFactoryBuilder factoryBuilder = new JsonFactoryBuilder();
        jsonFactory = factoryBuilder.build();
    }

    @Test
    public void testEmptyArrayDeserialization() throws Exception {
        String[] expected = new String[0];
        JsonParser jp = jsonFactory.createParser("[ ]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{"element1", "element2", "element3"};
        JsonParser jp = jsonFactory.createParser("[\"element1\",\"element2\",\"element3\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNullElementInStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{null, "element1", null};
        JsonParser jp = jsonFactory.createParser("[null,\"element1\",null]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyStringAsNullElementInStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{null};
        JsonParser jp = jsonFactory.createParser("[\"\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        ctxt.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCustomDeserializerStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{"customElement1", "customElement2"};
        JsonParser jp = jsonFactory.createParser("[\"customElement1\",\"customElement2\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<String> customDeserializer = new StubStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNullCustomDeserializerStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{"element1", "element2", "element3"};
        JsonParser jp = jsonFactory.createParser("[\"element1\",\"element2\",\"element3\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testNonArrayDeserialization() throws Exception {
        JsonParser jp = jsonFactory.createParser("{\"key\":\"value\"}");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testNonStartArrayTokenDeserialization() throws Exception {
        JsonParser jp = jsonFactory.createParser("{\"key\":[\"value\"]}");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestDeserializationContext());
    }

    @Test
    public void testValueNullDeserialization() throws Exception {
        String[] expected = new String[]{null};
        JsonParser jp = jsonFactory.createParser("[null]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testValueEmptyStringDeserialization() throws Exception {
        String[] expected = new String[0];
        JsonParser jp = jsonFactory.createParser("[\"\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testValueStringDeserialization() throws Exception {
        String[] expected = new String[]{"element"};
        JsonParser jp = jsonFactory.createParser("[\"element\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testValueStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{"element1", "element2", "element3"};
        JsonParser jp = jsonFactory.createParser("[\"element1\",\"element2\",\"element3\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testValueArrayDeserialization() throws Exception {
        String[] expected = new String[]{"element1", "element2", "element3"};
        JsonParser jp = jsonFactory.createParser("[1,\"element1\",2,\"element2\",3,\"element3\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object[] actual = deserializer.deserialize(jp, ctxt);
        String[] result = new String[actual.length];
        for (int i = 0; i < actual.length; i++) {
            result[i] = (String) actual[i];
        }
        assertArrayEquals(expected, result);
    }

    @Test
    public void testTypeDeserializerStringArrayDeserialization() throws Exception {
        String[] expected = new String[]{"customType1", "customType2"};
        JsonParser jp = jsonFactory.createParser("[\"customType1\",\"customType2\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        TypeDeserializer typeDeserializer = new StubTypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithContextualDeserializer() throws Exception {
        String[] expected = new String[]{"customType1", "customType2"};
        JsonParser jp = jsonFactory.createParser("[\"customType1\",\"customType2\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<String> customDeserializer = new StubStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.createContextual(ctxt, null).deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithNullContextualDeserializer() throws Exception {
        String[] expected = new String[]{"element1", "element2", "element3"};
        JsonParser jp = jsonFactory.createParser("[\"element1\",\"element2\",\"element3\"]");
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.createContextual(ctxt, null).deserialize(jp, ctxt);
        assertArrayEquals(expected, actual);
    }

    private static class StubStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "customValue";
        }
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.nextToken();
            if (t != JsonToken.START_ARRAY) {
                throw ctxt.mappingException("Expected START_ARRAY, but found " + t);
            }
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            String value;
            while ((value = jp.nextTextValue()) != null) {
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
            Object[] result = buffer.completeAndClearBuffer(chunk, ix, String.class);
            ctxt.returnObjectBuffer(buffer);
            return result;
        }

        @Override
        public void reset() {}

        @Override
        public void serializeWithType(Serializer serializer, Object value, TypeSerializer typeSerializer) throws IOException {
            // Not used in this test
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        private TestDeserializationContext() {
            super(ObjectMapper.getDefaultInstance(), new TestAnnotationIntrospector());
        }

        @Override
        public void reportError(String msg) {
            throw new RuntimeException(msg);
        }

        @Override
        public void reportMappingException(MappingException me) {
            throw me;
        }

        @Override
        public void close() {
            // Not used in this test
        }

        @Override
        public ObjectMapper getFactory() {
            return ObjectMapper.getDefaultInstance();
        }

        @Override
        public ObjectFinder findValueDeserializer(Class<?> valueType, JsonDeserializer<?> propertyDeserializer) {
            return new StdScalarValueDeserializer(valueType, null);
        }

        @Override
        public ObjectFinder findBeanDeserializer(Class<?> beanType) {
            return new StdBeanDeserializer(beanType, null);
        }

        @Override
        public ObjectFinder findValueDeserializer(Class<?> valueType) {
            return new StdScalarValueDeserializer(valueType, null);
        }

        @Override
        public ObjectFinder findContextualValueDeserializer(Class<?> valueType, BeanProperty property) {
            return new StdScalarValueDeserializer(valueType, null);
        }

        @Override
        public ObjectFinder findContextualValueDeserializer(BeanProperty property) {
            return null;
        }

        @Override
        public Object findValueWithType(JsonParser p, TypeDeserializer deserializer) throws IOException {
            return deserializer.deserializeTypedFromArray(p, this);
        }

        @Override
        public Object findValue(JsonParser p) throws IOException {
            return p.getValueAsTree().getValueAsString();
        }
    }
}