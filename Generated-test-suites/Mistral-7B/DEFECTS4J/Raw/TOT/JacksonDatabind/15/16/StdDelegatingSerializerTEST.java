import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer serializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 serializer = new StdDelegatingSerializer(String.class, new StringToNumberConverter());
 }

 @Test
 public void testSerializingStringToNumber() throws JsonGenerationException, IOException {
 String input = "42";
 Object output = serializer.serialize(input, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider());
 assertEquals(42.0, output);
 }

 @Test
 public void testSerializingNull() throws JsonGenerationException, IOException {
 Object input = null;
 Object output = serializer.serialize(input, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider());
 assertNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializingDifferentTypes() throws JsonGenerationException, IOException {
 Object input = new Object();
 serializer = new StdDelegatingSerializer(Integer.class, new StringToNumberConverter());
 serializer.serialize(input, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider());
 fail("Expected IllegalArgumentException");
 }
}