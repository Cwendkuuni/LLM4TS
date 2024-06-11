package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private DeserializationContext context;
    private JsonDeserializer<String> stringDeserializer;
    private ObjectMapper objectMapper;
    private ObjectReader objectReader;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        stringDeserializer = mock(JsonDeserializer.class);
        objectMapper = new ObjectMapper();
        objectReader = objectMapper.readerForArrayOf(String.class);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("[]");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, context);
        assertEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        String expected = "test";
        JsonParser jp = objectMapper.getFactory().createParser("[\"" + expected + "\"]");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, context);
        assertEquals(new String[] { expected }, result);
    }

    @Test
    public void testDeserializeMultiElementArray() throws IOException {
        String[] expected = new String[] { "test1", "test2" };
        JsonParser jp = objectMapper.getFactory().createParser("[\"" + expected[0] + "\",\"" + expected[1] + "\"]");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, context);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("\"test\"");
        StringArrayDeserializer.instance.deserialize(jp, context);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeNullValue() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("null");
        StringArrayDeserializer.instance.deserialize(jp, context);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        String[] expected = new String[] { "test1", "test2" };
        when(stringDeserializer.deserialize(context.getParser(), context)).thenReturn(expected);
        StringParser jsonParser = new StringParser("[\"" + expected[0] + "\",\"" + expected[1] + "\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String[] expected = new String[] { "test1", "test2" };
        TypeDeserializer emptyTypeDeserializer = new EmptyTypeDeserializer();
        ObjectBuffer objectBuffer = new ObjectBuffer(16);
        when(context.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(context.findValueDeserializer(String.class)).thenReturn(stringDeserializer);
        when(context.findTypeDeserializer(String[].class, null)).thenReturn(emptyTypeDeserializer);

        JsonParser jp = objectMapper.getFactory().createParser("[\"" + expected[0] + "\",\"" + expected[1] + "\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(jp, context, emptyTypeDeserializer);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() {
        String[] expected = new String[] { "test1", "test2" };
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getContentDeserializer()).thenReturn(stringDeserializer);
        when(context.findContextualValueDeserializer(String[].class, null)).thenReturn(deserializer);

        StringArrayDeserializer result = StringArrayDeserializer.instance.createContextual(context, null);
        assertEquals(new StringArrayDeserializer(deserializer), result);
    }

    @Test
    public void testCreateContextual_withNullDeserializer() {
        StringArrayDeserializer result = StringArrayDeserializer.instance.createContextual(context, null);
        assertEquals(StringArrayDeserializer.instance, result);
    }
}

class StringParser implements JsonParser {

    private String jsonString;
    private int currentIndex;
    private int currentLength;

    public StringParser(String jsonString) {
        this.jsonString = jsonString;
        this.currentIndex = 0;
        this.currentLength = jsonString.length();
    }

    @Override
    public boolean nextToken() throws IOException {
        if (currentIndex >= currentLength) {
            throw new IOException("No more input to parse.");
        }

        char c = jsonString.charAt(currentIndex);
        currentIndex++;

        switch (c) {
            case '{':
                return JsonToken.START_OBJECT;
            case '[':
                return JsonToken.START_ARRAY;
            case '"':
                if (jsonString.charAt(currentIndex) == '"') {
                    currentIndex++;
                    return JsonToken.VALUE_STRING;
                } else {
                    return JsonToken.VALUE_STRING;
                }
            case 'n':
                if (jsonString.regionMatches(currentIndex, "null", 0, 4)) {
                    currentIndex += 4;
                    return JsonToken.VALUE_NULL;
                }
            case 'f':
                if (jsonString.regionMatches(currentIndex, "false", 0, 5)) {
                    currentIndex += 5;
                    return JsonToken.VALUE_FALSE;
                }
            case 't':
                if (jsonString.regionMatches(currentIndex, "true", 0, 4)) {
                    currentIndex += 4;
                    return JsonToken.VALUE_TRUE;
                }
            case ']':
                return JsonToken.END_ARRAY;
            case '}':
                return JsonToken.END_OBJECT;
            default:
                throw new IOException("Unexpected character '" + c + "'.");
        }
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void reset() throws IOException {
        currentIndex = 0;
    }

    @Override
    public String getText() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (nextToken() != JsonToken.END_OBJECT) {
            if (getTextValue() != null) {
                stringBuilder.append(getTextValue());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void setCodec(JsonParser.DefaultCodec defaultCodec) {
    }

    @Override
    public void setBounds(int min, int max) {
    }

    @Override
    public void setEventReader(JsonParser.EventReader eventReader) {
    }

    @Override
    public void setFeatures(JsonParser.FeatureSet features) {
    }

    @Override
    public void setPropertyEvaluator(JsonParser.PropertyEvaluator propertyEvaluator) {
    }

    @Override
    public void setReaderFeatures(JsonParser.FeatureSet features) {
    }

    @Override
    public void setStreamManager(JsonParser.StreamManager streamManager) {
    }

    @Override
    public void setTextFactory(JsonParser.TextFactory textFactory) {
    }

    @Override
    public boolean nextValue() throws IOException {
        if (getTextValue() != null) {
            return true;
        }
        if (nextToken() == JsonToken.VALUE_NULL) {
            return true;
        }
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public void reset() {
    }

    @Override
    public Object getValueAsArray() throws IOException {
        return getValueAsTypedArray(String[].class);
    }

    @Override
    public Object getValueAsNumber() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getValueAsObject() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getValueAsValue(Class<T> valueType) throws IOException {
        return (T) getValueAsArray();
    }

    @Override
    public <T> T getValueAsTyped(Class<T> valueType) throws IOException {
        return (T) getValueAsArray();
    }

    @Override
    public <T> T getValueAsTyped(Class<T> valueType, TypeDeserializer typeDeserializer) throws IOException {
        return (T) getValueAsArray();
    }

    @Override
    public <T> T deserializeValueAs(Class<T> valueType) throws IOException {
        return (T) getValueAsArray();
    }

    @Override
    public <T> T deserializeValueAs(Class<T> valueType, TypeDeserializer typeDeserializer) throws IOException {
        return (T) getValueAsArray();
    }

    @Override
    public void setSerializationConfig(JsonSerializer.SerializerConfig serializationConfig) {
    }

    @Override
    public void setSerializers(JsonSerializer.Serializers serializers) {
    }

    @Override
    public void setFeatures(JsonParser.FeatureSet features) {
    }

    @Override
    public void setBaseHierarchy(JsonParser.BaseHierarchy baseHierarchy) {
    }

    @Override
    public void setContextualHandler(JsonParser.ContextualHandler contextualHandler) {
    }

    @Override
    public void setCodec(JsonParser.DefaultCodec defaultCodec) {
    }

    @Override
    public void setEventReader(JsonParser.EventReader eventReader) {
    }

    @Override
    public void setReaderFeatures(JsonParser.FeatureSet features) {
    }

    @Override
    public void setStreamManager(JsonParser.StreamManager streamManager) {
    }

    @Override
    public void setTextFactory(JsonParser.TextFactory textFactory) {
    }

    @Override
    public void setUnwrappingHandler(JsonParser.UnwrappingHandler unwrappingHandler) {
    }

    @Override
    public void setUnwrappedTypeDeserializer(TypeDeserializer unwrappedTypeDeserializer) {
    }

    @Override
    public void setCharacterEscapes(JsonParser.CharacterEscapes characterEscapes) {
    }

    @Override
    public void setCodecProvider(JsonParser.CodecProvider codecProvider) {
    }

    @Override
    public void setFeatureSet(JsonParser.FeatureSet features) {
    }

    @Override
    public void setCodecProviderDelegate(JsonParser.CodecProviderDelegate codecProviderDelegate) {
    }

    @Override
    public void setDefaultUseWrapper(boolean defaultUseWrapper) {
    }

    @Override
    public void setDefaultUseWrapper(Class<?> defaultUseWrapperType) {
    }

    @Override
    public void setEventReaderDelegate(JsonParser.EventReaderDelegate eventReaderDelegate) {
    }

    @Override
    public void setUnwrappingHandlerDelegate(JsonParser.UnwrappingHandlerDelegate unwrappingHandlerDelegate) {
    }

    @Override
    public <T> T getValueAsValue(Class<T> valueType) throws IOException {
        return valueType.cast(getValueAsArray());
    }

    @Override
    public <T> T getValueAsValue(Class<T> valueType, TypeDeserializer typeDeserializer) throws IOException {
        return valueType.cast(typeDeserializer.deserializeValueAs(valueType, this));
    }

    @Override
    public <T> T deserializeValueAs(Class<T> valueType) throws IOException {
        return valueType.cast(getValueAsArray());
    }

    @Override
    public <T> T deserializeValueAs(Class<T> valueType, TypeDeserializer typeDeserializer) throws IOException {
        return valueType.cast(typeDeserializer.deserializeValueAs(valueType, this));
    }

    @Override
    public JsonParser setAttribute(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttributeFilter(JsonParser.AttributeFilter attributeFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeEvent(JsonParser.EventType eventType) throws IOException {
    }

    @Override
    public Object getTextValue() throws IOException {
        if (nextToken() == JsonToken.VALUE_STRING) {
            String text = getText();
            nextToken(); // consume the '"' character
            return text;
        }
        return null;
    }

    @Override
    public int nextIntValue() throws IOException {
        throw new IOException("nextIntValue is not supported.");
    }

    @Override
    public long nextLongValue() throws IOException {
        throw new IOException("nextLongValue is not supported.");
    }

    @Override
    public boolean nextBoolean() throws IOException {
        throw new IOException("nextBoolean is not supported.");
    }

    @Override
    public float nextFloatValue() throws IOException {
        throw new IOException("nextFloatValue is not supported.");
    }

    @Override
    public double nextDoubleValue() throws IOException {
        throw new IOException("nextDoubleValue is not supported.");
    }

    @Override
    public void nextValue(Class<?> localType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nextValue(Class<?> localType, TypeDeserializer typeDeserializer) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptChar(int c) throws IOException {
    }

    @Override
    public void nextToken() throws IOException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void close() {
    }
}