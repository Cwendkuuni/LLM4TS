import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Number number = 123;
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(123, parser.readValueAs(Number.class));
    }

    // Other test cases for different types of Number
}