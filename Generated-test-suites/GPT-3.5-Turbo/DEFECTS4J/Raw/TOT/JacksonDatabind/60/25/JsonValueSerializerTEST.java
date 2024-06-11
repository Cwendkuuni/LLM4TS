import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.*;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;
    private JsonValueSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        serializer = new JsonValueSerializer();
    }

    @Test
    public void testSerialize_NullValue_Successful() {
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider prov = mapper.getSerializerProvider();
        Object bean = null;

        try {
            serializer.serialize(bean, gen, prov);
            String output = gen.toString();
            assertEquals("null", output);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSerialize_NonNullValue_FindsValueSerializer() {
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider prov = mapper.getSerializerProvider();
        Object bean = new TestBean();

        try {
            serializer.serialize(bean, gen, prov);
            String output = gen.toString();
            assertEquals("{\"name\":\"test\"}", output);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_IOException_ThrowsJsonMappingException() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider prov = mapper.getSerializerProvider();
        Object bean = new TestBean();

        serializer.serialize(bean, gen, prov);
    }

    private static class TestBean {
        @JsonValue
        public String getName() {
            return "test";
        }
    }
}