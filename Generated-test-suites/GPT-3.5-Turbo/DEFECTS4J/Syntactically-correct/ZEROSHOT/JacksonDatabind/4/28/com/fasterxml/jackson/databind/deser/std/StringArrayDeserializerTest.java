import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer = new StringArrayDeserializer();
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"Hello\", \"World\"]";
        String[] expected = new String[] {"Hello", "World"};
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithNull() throws IOException {
        String json = "[\"Hello\", null, \"World\"]";
        String[] expected = new String[] {"Hello", null, "World"};
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        String[] expected = new String[] {};
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws IOException {
        String json = "\"Hello\"";
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonStringElement() throws IOException {
        String json = "[12, 34]";
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[\"Hello\", \"World\"]";
        String[] expected = new String[] {"Hello", "World"};
        String[] result = (String[]) deserializer.deserializeWithType(mapper.getFactory().createParser(json), mapper.getDeserializationContext(), mock(TypeDeserializer.class));
        assertArrayEquals(expected, result);
    }

}