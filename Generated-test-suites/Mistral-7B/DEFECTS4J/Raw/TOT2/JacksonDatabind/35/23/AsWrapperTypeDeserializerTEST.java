import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"typeId\": \"com.example.TestClass\", \"value\": 42}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(NullType.INSTANCE, new StandardTypeIdResolver(), "_class", true, TestClass.class);
        Object result = deserializer.deserializeTypedFromObject(mapper.parserForTesting(), new TestContext());
        assertEquals(TestClass.class, result.getClass());
        assertEquals(42, ((TestClass) result).getValue());
    }
}