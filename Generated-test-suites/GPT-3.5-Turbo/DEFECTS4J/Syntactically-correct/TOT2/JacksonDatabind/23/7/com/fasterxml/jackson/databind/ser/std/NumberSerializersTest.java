import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setup() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        ObjectMapper mapper = new ObjectMapper();
        Integer value = 42;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);
        SerializerProvider provider = mapper.getSerializerProvider();
        
        integerSerializer.serialize(value, gen, provider);
        gen.flush();
        
        String json = writer.toString();
        assertEquals("42", json);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        ObjectMapper mapper = new ObjectMapper();
        Short value = 10;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);
        SerializerProvider provider = mapper.getSerializerProvider();
        
        shortSerializer.serialize(value, gen, provider);
        gen.flush();
        
        String json = writer.toString();
        assertEquals("10", json);
    }

    // Add more test cases for the other serializers

    @Test
    public void testGetSchema() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = integerSerializer.getSchema(provider, null);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperBase();
        JavaType typeHint = new ObjectMapper().getTypeFactory().constructType(Integer.class);
        
        integerSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertions here
    }

    // Add more test cases for the shared methods in the base class

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        
        assertTrue(deserializers.containsKey(Integer.class.getName()));
        // Add more assertions for the added deserializers
    }

    // Add test cases for all other methods in the NumberSerializers class

}