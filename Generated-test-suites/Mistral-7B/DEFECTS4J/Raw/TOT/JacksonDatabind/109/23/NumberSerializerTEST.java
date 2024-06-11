import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializedBigDecimal() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.45");
 String json = mapper.writerWithView(NumberSerializer.class)
 .writeValueAsString(bigDecimal);
 assertNotNull(json);
 Number deserializedNumber = mapper.readerForUpdating(Number.class)
 .readValue(json);
 assertEquals(bigDecimal, deserializedNumber);
 }

 @Test
 public void testSerializedBigInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = BigInteger.valueOf(1234567890L);
 String json = mapper.writerWithView(NumberSerializer.class)
 .writeValueAsString(bigInteger);
 assertNotNull(json);
 Number deserializedNumber = mapper.readerForUpdating(Number.class)
 .readValue(json);
 assertEquals(bigInteger, deserializedNumber);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializedNonNumber() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String nonNumber = "not a number";
 String json = mapper.writerWithView(NumberSerializer.class)
 .writeValueAsString(nonNumber);
 mapper.readerForUpdating(Number.class)
 .readValue(json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializedNaN() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Double nan = Double.NaN;
 String json = mapper.writerWithView(NumberSerializer.class)
 .writeValueAsString(nan);
 mapper.readerForUpdating(Number.class)
 .readValue(json);
 }
}