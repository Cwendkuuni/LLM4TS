import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setup() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();

        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
    }

    @Test
    public void testIntegerSerializerSerializesInteger() throws IOException {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator jsonGenerator = new JsonGenerator() {
            @Override
            public JsonGenerator setCodec(ObjectCodec oc) {
                return this;
            }

            @Override
            public void writeNumber(int v) throws IOException {
                assertEquals(123, v);
            }

            // Other overridden methods...
        };

        serializer.serialize(123, jsonGenerator, null);
    }

    @Test
    public void testIntegerSerializerThrowsExceptionIfInputIsNotInteger() throws IOException {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator jsonGenerator = new JsonGenerator() {
            @Override
            public JsonGenerator setCodec(ObjectCodec oc) {
                return this;
            }

            @Override
            public void writeNumber(int v) throws IOException {
                throw new IOException("Should not be called");
            }

            // Other overridden methods...
        };

        try {
            serializer.serialize(123.45, jsonGenerator, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Should not be called", e.getMessage());
        }
    }

    // Add more test cases for the remaining serializers...

}