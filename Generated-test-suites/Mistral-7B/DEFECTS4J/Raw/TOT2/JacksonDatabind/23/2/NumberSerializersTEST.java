import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        NumberSerializers serializers = new NumberSerializers();

        JsonNode expectedSchema = mapper.readTree("{\"type\": \"number\"}");
        JsonNode actualSchema = serializers.getSchema(provider, TypeFactory.defaultInstance.constructType(Number.class));

        assertEquals(expectedSchema, actualSchema);
    }
}