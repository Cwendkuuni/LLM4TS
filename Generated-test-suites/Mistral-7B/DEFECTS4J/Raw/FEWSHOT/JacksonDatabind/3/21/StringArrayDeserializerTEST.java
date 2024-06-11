package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithEmptyString() throws IOException {
        JsonParser jp = createParser("['']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{""}, result);
    }

    @Test
    public void testDeserializeArrayWithStrings() throws IOException {
        JsonParser jp = createParser("['foo', 'bar', 'baz']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullValues() throws IOException {
        JsonParser jp = createParser("['foo', null, 'baz']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"foo", null, "baz"}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new BaseTestDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser jp = createParser("['foo', 'bar', 'baz']");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"customFoo", "customBar", "customBaz"}, result);
    }

    @Test
    public void testDeserializeArrayWithTypeDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new BaseTestDeserializer();
        TypeDeserializer typeDeserializer = new BaseTypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = createParser("['foo', 'bar', 'baz']");
        Object result = deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeCustomContextual() throws IOException {
        JsonDeserializer<String> customDeserializer = new BaseTestDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = createParser("['foo', 'bar', 'baz']");
        DeserializationContext ctxt = new TestContext();
        ctxt.setActiveContext(new TestContextualContext(customDeserializer));
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"customFoo", "customBar", "customBaz"}, result);
    }

    @Test
    public void testDeserializeNullContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = createParser("['foo', 'bar', 'baz']");
        DeserializationContext ctxt = new TestContext();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = createParser("'foo'");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(jp, new TestContext());
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeNullInput() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = createParser("null");
        try {
            deserializer.deserialize(jp, new TestContext());
            fail("Expected null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readerForElementValue(String[].class).readValue(reader);
    }

    private static class BaseTestDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "custom" + p.getValueAsString();
        }
    }

    private static class BaseTypeDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class TestContextualContext implements ContextualSerializationContext {
        private final JsonDeserializer<?> _deserializer;

        TestContextualContext(JsonDeserializer<?> deserializer) {
            _deserializer = deserializer;
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(DeserializationContext ctxt, BeanProperty property)
                throws JsonMappingException {
            return _deserializer;
        }
    }

    private static class TestContext extends TestBaseContext {
        // Empty
    }

    private static class TestBaseContext extends AbstractTestContext {
        // Empty
    }
}