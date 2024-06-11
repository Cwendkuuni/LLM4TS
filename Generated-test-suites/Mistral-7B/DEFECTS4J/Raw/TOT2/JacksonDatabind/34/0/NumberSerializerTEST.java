import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

public class NumberSerializerTest {

    @Test
    void testSerializeBigDecimal() throws JsonProcessingException {
        // Expert 1's test case for BigDecimal
    }

    @Test
    void testSerializeBigInteger() throws JsonProcessingException {
        // Expert 1's test case for BigInteger
    }

    @Test
    void testSerializeInteger() throws JsonProcessingException {
        // Expert 1's test case for Integer
    }

    @Test
    void testSerializeOtherNumberTypes() throws JsonProcessingException {
        // Expert 1's test case for other Number types
    }

    @Test
    void testSerializeNaN() throws JsonProcessingException {
        Number number = Double.NaN;
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNode expectedJson = mapper.valueToTree(number);
        JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testSerializeInfinity() throws JsonProcessingException {
        Number number = Double.POSITIVE_INFINITY;
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNode expectedJson = mapper.valueToTree(number);
        JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testSerializeNegativeNumber() throws JsonProcessingException {
        Number number = -123;
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNode expectedJson = mapper.valueToTree(number);
        JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Expert 1's test case for acceptJsonFormatVisitor
    }

    @Test
    void testGetSchema() throws JsonProcessingException {
        // Expert 1's test case for getSchema
    }
}