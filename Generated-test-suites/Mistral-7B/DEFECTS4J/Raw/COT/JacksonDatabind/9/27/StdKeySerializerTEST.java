import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperAnnotationTestBase;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.serde.SerializerBuilder;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class StdKeySerializerTest extends JsonMapperAnnotationTestBase {

    private StdKeySerializer stdKeySerializer;

    @BeforeEach
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        stdKeySerializer = (StdKeySerializer) objectMapper.registerModule(new StandardSerializerModule())
                .findValueSerializer(Object.class);
    }

    @Test
    public void testSerialize_Date() throws JsonProcessingException {
        Date date = new Date();
        String expected = "\"$date\"";

        String result = stdKeySerializer.serialize(date, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_String() throws JsonProcessingException {
        String value = "test";
        String expected = "\"test\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_Null() throws JsonProcessingException {
        String result = stdKeySerializer.serialize(null, null, null);

        assertNull(result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expected = JsonNodeFactory.instance.textNode("string");
        JsonNode result = stdKeySerializer.getSchema(null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectNode expected = mapper.createObjectNode();
        expected.putPOJO("format", String.class);

        ObjectNode result = stdKeySerializer.acceptJsonFormatVisitor(mapper.getNodeFactory(), mapper.constructType(Object.class));

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_EmptyString() throws JsonProcessingException {
        String value = "";
        String expected = "\"\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_LongValue() throws JsonProcessingException {
        long value = 1234567890L;
        String expected = "\"1234567890\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_DoubleValue() throws JsonProcessingException {
        double value = 123.456789;
        String expected = "\"123.456789\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_BooleanValue() throws JsonProcessingException {
        boolean value = true;
        String expected = "true";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_CharacterValue() throws JsonProcessingException {
        char value = 'A';
        String expected = "\"A\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_ArrayValue() throws JsonProcessingException {
        Object[] value = {1, 2, 3};
        String expected = "[1,2,3]";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_CustomClassValue() throws JsonProcessingException {
        class CustomClass {
            private int id;

            public CustomClass(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }

            public String toString() {
                return "CustomClass{" +
                        "id=" + id +
                        '}';
            }
        }

        CustomClass value = new CustomClass(1);
        String expected = "\"CustomClass{id=1}\"";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_NullArray() throws JsonProcessingException {
        Object[] value = null;
        String expected = "null";

        String result = stdKeySerializer.serialize(value, null, null);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_Exception() {
        assertThrows(IllegalArgumentException.class, () -> stdKeySerializer.serialize(new Object() {}, null, null));
    }
}