import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        // Typical use case
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null); 
        String json = mapper.writeValueAsString(serializer);
        assertEquals("{}", json);

        // Edge case
        serializer = new StdDelegatingSerializer(SerializationFeature.WRAP_ROOT_VALUE);
        json = mapper.writeValueAsString(serializer);
        assertEquals("{\"wrapped\":{}}", json);
        
        // Error scenario
        serializer = new StdDelegatingSerializer(new ObjectMapper());
        try {
            json = mapper.writeValueAsString(serializer);
            fail("Expected Exception IllegalArgumentException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testSerializeWithType() throws Exception {
        // Typical use case
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null); 
        String json = mapper.writeValueAsString(serializer);
        assertEquals("{}", json);

        // Edge case
        serializer = new StdDelegatingSerializer(SerializationFeature.WRAP_ROOT_VALUE);
        json = mapper.writeValueAsString(serializer);
        assertEquals("{\"wrapped\":{}}", json);
        
        // Error scenario
        serializer = new StdDelegatingSerializer(new ObjectMapper());
        try {
            json = mapper.writeValueAsString(serializer);
            fail("Expected Exception IllegalArgumentException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

}