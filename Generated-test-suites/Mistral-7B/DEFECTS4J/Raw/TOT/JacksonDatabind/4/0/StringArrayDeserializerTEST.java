package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testStandardDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"apple\",\"banana\",\"cherry\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.findValueDeserializer(String.class));
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{"apple", "banana", "cherry"}, result);
    }

    @Test
    public void testNullValue() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testEmptyStringAsNull() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testHandlingNonArray() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"abc\"");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assertions.assertArrayEquals(new String[]{"abc"}, result);
    }

    @Test
    public void testFindingContextualDeserializer() throws IOException {
        ObjectMapper contextualMapper = new ObjectMapper();
        contextualMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        contextualMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        contextualMapper.registerModule(new SimpleModule().addDeserializer(String[].class, new StringArrayContextualDeserializer()));

        JsonParser jp = contextualMapper.getFactory().createParser("[\"apple\",\"banana\",\"cherry\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(contextualMapper));
        Assertions.assertArrayEquals(new String[]{"apple", "banana", "cherry"}, result);

        Assertions.assertEquals(StringArrayContextualDeserializer.class, deserializer._elementDeserializer.getClass());
    }

    @Test
    public void testHandlingTypeDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, false);

        JsonParser jp = factory.createParser("[\"alpha\",\"beta\",\"gamma\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String[].class, String.class);
        String[] result = deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer);
        Assertions.assertArrayEquals(new String[]{"alpha", "beta", "gamma"}, result);
    }

    static class TestContext extends DeserializationContext {
        TestContext() {
            super(ObjectMapper.getDefaultInstance(), new JsonFactory(), null);
        }

        TestContext(ObjectMapper contextualMapper) {
            super(contextualMapper, new JsonFactory(), null);
        }
    }

    static class StringArrayContextualDeserializer extends StdDeserializer<String[]> implements ContextualDeserializer {
        StringArrayContextualDeserializer() {
            this(null);
        }

        StringArrayContextualDeserializer(JsonDeserializer<String> deser) {
            super(String[].class);
            _elementDeserializer = deser;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return _elementDeserializer;
        }
    }
}