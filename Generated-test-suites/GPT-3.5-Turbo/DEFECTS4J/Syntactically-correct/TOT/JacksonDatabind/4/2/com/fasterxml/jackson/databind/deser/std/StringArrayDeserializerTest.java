package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private ObjectBuffer objectBuffer;
    private String[] testArr;

    @BeforeEach
    void setup() throws IOException {
        stringArrayDeserializer = StringArrayDeserializer.instance;
        deserializationContext = new ObjectMapper().getDeserializationContext();
        testArr = new String[]{"test1", "test2"};
        String json = "[\"test1\", \"test2\"]";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        objectBuffer = deserializationContext.leaseObjectBuffer();
    }

    @Test
    void deserialize() throws IOException {
        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(testArr, deserializedArr));
    }

    @Test
    void deserialize_CustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                    return p.getText();
                }
                return null;
            }
        };
        stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);

        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(testArr, deserializedArr));
    }

    @Test
    void deserialize_NullElements() throws IOException {
        testArr = new String[]{"test1", null, "test2"};
        String json = "[\"test1\", null, \"test2\"]";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();

        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(testArr, deserializedArr));
    }

    @Test
    void deserialize_EmptyArray() throws IOException {
        String json = "[]";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();

        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(new String[0], deserializedArr));
    }

    @Test
    void deserialize_NotArray() throws IOException {
        String json = "\"test\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();

        assertThrows(IOException.class, () -> stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    void deserialize_SingleValueNotAllowed() throws IOException {
        String json = "\"test\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertThrows(IOException.class, () -> stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    void deserialize_SingleValueAllowed() throws IOException {
        String json = "\"test\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(new String[]{"test"}, deserializedArr));
    }

    @Test
    void deserialize_EmptyStringValueAllowed() throws IOException {
        String json = "\"\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        deserializationContext.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(null, deserializedArr));
    }

    @Test
    void deserialize_WithContextualDeserializer() throws IOException {
        String[] deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        JsonDeserializer<String> deserializer = new JsonDeserializer<>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (text.equals("test1")) {
                    return "modified1";
                } else if (text.equals("test2")) {
                    return "modified2";
                }
                return text;
            }
        };

        deserializationContext = deserializationContext.createInstance(deserializer, null);
        jsonParser.nextToken();
        deserializationContext.initSelfReferences(null); // Necessary to prevent StackOverflowError

        deserializedArr = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(new String[]{"modified1", "modified2"}, deserializedArr));
    }

    @Test
    void deserialize_WithNullValueDeserializer() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[\"test1\", null, \"test2\"]");
        parser.nextToken();
        deserializationContext.initSelfReferences(null);

        String[] deserializedArr = stringArrayDeserializer.deserialize(parser, deserializationContext);

        assertTrue(Arrays.equals(testArr, deserializedArr));
    }

    @Test
    void deserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = null;

        Object deserializedObj = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertTrue(Arrays.equals(testArr, (String[]) deserializedObj));
    }

    @Test
    void deserialize_WithInvalidType() throws IOException {
        TypeDeserializer typeDeserializer = null;
        jsonParser.nextToken();

        assertThrows(IOException.class, () -> stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    void handleNonArray_NullValueDeserializationNotEnabled() throws IOException {
        jsonParser.nextToken();
        deserializationContext.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertThrows(IOException.class, () -> stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext));
    }

    @Test
    void handleNonArray_NullValueDeserializationEnabled() throws IOException {
        String json = "\"\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        deserializationContext.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String[] deserializedArr = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(null, deserializedArr));
    }

    @Test
    void handleNonArray_NonNullJsonValueDeserializationNotEnabled() throws IOException {
        String json = "\"test\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertThrows(IOException.class, () -> stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext));
    }

    @Test
    void handleNonArray_NonNullJsonValueDeserializationEnabled() throws IOException {
        String json = "\"test\"";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] deserializedArr = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(new String[]{"test"}, deserializedArr));
    }

    @Test
    void handleNonArray_EmptyArrayValueDeserializationNotEnabled() throws IOException {
        String json = "[]";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        assertThrows(IOException.class, () -> stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext));
    }

    @Test
    void handleNonArray_EmptyArrayValueDeserializationEnabled() throws IOException {
        String json = "[]";
        jsonParser = new ObjectMapper().getFactory().createParser(json);
        jsonParser.nextToken();
        deserializationContext.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] deserializedArr = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        assertTrue(Arrays.equals(new String[0], deserializedArr));
    }

    @Test
    void createContextual_DefaultSerializer() throws IOException {
        BeanProperty property = null;

        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(deserializationContext, property);
        assertSame(stringArrayDeserializer, deserializer);
    }

    @Test
    void createContextual_CustomSerializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                    return p.getText();
                }
                return null;
            }
        };
        stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        BeanProperty property = null;

        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(deserializationContext, property);
        assertNotSame(stringArrayDeserializer, deserializer);
        assertTrue(deserializer instanceof StringArrayDeserializer);
        StringArrayDeserializer newDeserializer = (StringArrayDeserializer) deserializer;
        assertSame(customDeserializer, newDeserializer._elementDeserializer);
    }

    private void assertSame(Object expected, Object actual) {
        assertTrue(expected == actual, "Expected: " + expected + ", actual: " + actual);
    }

    private void assertNotSame(Object expected, Object actual) {
        assertTrue(expected != actual, "Expected: " + expected + ", actual: " + actual);
    }
}