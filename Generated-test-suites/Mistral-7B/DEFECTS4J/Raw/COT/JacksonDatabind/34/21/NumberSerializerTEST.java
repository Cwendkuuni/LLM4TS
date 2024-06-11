import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator();

        Number[] numbers = new Number[] {
                null,
                new BigDecimal("123.45"),
                new BigInteger("12345678901234567890"),
                new Integer(1),
                new Long(2),
                new Double(3.14),
                new Float(4.5f),
                new Byte((byte) 5),
                new Short((short) 6),
                new BigDecimal("0.123"),
                new BigInteger("123"),
                new Integer(-1),
                new Long(-2),
                new Double(Math.PI),
                new Float(Math.E),
                new BigDecimal("NaN"),
                new BigDecimal("Infinity"),
                new BigDecimal("-Infinity")
        };

        for (Number number : numbers) {
            numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());
            String actual = jsonGenerator.getOutputBuffer().toString();
            String expected;
            if (number == null) {
                expected = "null";
            } else {
                expected = number.toString();
            }
            Assert.assertEquals(expected, actual);
            jsonGenerator.flush();
            jsonGenerator.close();
        }
    }

    @Test
    public void testDeserializeNumber() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = new JsonFactory().createParser("[]");

        Number[] numbers = new Number[] {
                null,
                new BigDecimal("123.45"),
                new BigInteger("12345678901234567890"),
                new Integer(1),
                new Long(2),
                new Double(3.14),
                new Float(4.5f),
                new Byte((byte) 5),
                new Short((short) 6),
                new BigDecimal("0.123"),
                new BigInteger("123"),
                new Integer(-1),
                new Long(-2),
                new Double(Math.PI),
                new Float(Math.E),
                new BigDecimal("NaN"),
                new BigDecimal("Infinity"),
                new BigDecimal("-Infinity")
        };

        for (Number number : numbers) {
            JsonNode rootNode = objectMapper.readValue(jsonParser, JsonNode.class);
            JsonNode node = rootNode.get(0);
            String nodeType = node.getNodeType().name();

            if (number == null) {
                Assert.assertEquals(JsonNodeType.NULL, nodeType);
            } else {
                Number deserializedNumber = number.getClass().cast(objectMapper.readValue(node.toString(), Number.class));
                Assert.assertEquals(number, deserializedNumber);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeUnknownType() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator();
        Object obj = new Object();
        numberSerializer.serialize(obj, jsonGenerator, new SerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = new JsonFactory().createParser("{}");
        Object obj = new Object();
        objectMapper.readValue(jsonParser, obj.getClass());
    }
}