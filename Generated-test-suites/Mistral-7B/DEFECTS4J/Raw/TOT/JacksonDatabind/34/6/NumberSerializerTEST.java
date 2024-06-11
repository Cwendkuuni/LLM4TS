import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private ObjectMapper objectMapper = new JsonMapper();

    @Test
    public void testSerializeSmallNumber() throws JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        Number number = 1;
        String expectedJson = "1";

        String actualJson = serializeAndRead(numberSerializer, number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigNumber() throws JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);
        String expectedJson = Long.toString(Long.MAX_VALUE);

        String actualJson = serializeAndRead(numberSerializer, bigNumber);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("9999999999999999999999999999999999999999.9999999999999999999999999999999999999999");
        String expectedJson = bigDecimal.toPlainString();

        String actualJson = serializeAndRead(numberSerializer, bigDecimal);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeUnsupportedType() throws JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(String.class);
        String stringValue = "Hello World!";

        String actualJson = serializeAndRead(numberSerializer, stringValue);
    }

    @Test
    public void testDeserializeSmallNumber() throws IOException, JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        String jsonString = "1";
        Number number = deserialize(numberSerializer, jsonString, Number.class);

        Assert.assertEquals(1, number.intValue());
    }

    @Test
    public void testDeserializeBigNumber() throws IOException, JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        String jsonString = Long.MAX_VALUE + "";
        Number number = deserialize(numberSerializer, jsonString, Number.class);

        Assert.assertEquals(Long.MAX_VALUE, number.longValue());
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException, JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        String jsonString = "9999999999999999999999999999999999999999.9999999999999999999999999999999999999999";
        Number number = deserialize(numberSerializer, jsonString, Number.class);

        BigDecimal actualBigDecimal = (BigDecimal) number;
        BigDecimal expectedBigDecimal = new BigDecimal("9999999999999999999999999999999999999999.9999999999999999999999999999999999999999");
        Assert.assertEquals(expectedBigDecimal, actualBigDecimal);
    }

    private String serializeAndRead(NumberSerializer numberSerializer, Number number) throws JsonProcessingException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        numberSerializer.serialize(number, jsonGenerator, objectMapper.getSerializationConfig());
        jsonGenerator.flush();
        jsonGenerator.close();

        String jsonString = stringWriter.toString();
        ObjectReader objectReader = objectMapper.getFactory().createParser(new StringReader(jsonString));
        JsonParser jsonParser = objectMapper.getFactory().createParser(objectReader);

        Number deserializedNumber = numberSerializer.deserialize(jsonParser, Number.class, null);
        jsonParser.close();
        objectReader.close();

        return jsonString;
    }

    private <T> T deserialize(NumberSerializer numberSerializer, String jsonString, Class<T> clazz) throws IOException, JsonMappingException {
        ObjectReader objectReader = objectMapper.readTree(jsonString);
        JsonParser jsonParser = objectMapper.getFactory().createParser(objectReader);

        T deserializedObject = numberSerializer.deserialize(jsonParser, clazz, null);
        jsonParser.close();
        objectReader.close();

        return deserializedObject;
    }
}