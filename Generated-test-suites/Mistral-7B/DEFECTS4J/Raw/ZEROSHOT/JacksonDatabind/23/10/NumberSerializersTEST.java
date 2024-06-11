import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 Integer number = 123;
 String json = mapper.writeValueAsString(number);
 assertEquals("123", json);
 }

 @Test
 public void testIntegerDeserialization() throws JsonProcessingException {
 String json = "123";
 Integer number = mapper.readValue(json, Integer.class);
 assertEquals(123, number.intValue());
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException {
 Long number = 456L;
 String json = mapper.writeValueAsString(number);
 assertEquals("456", json);
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 String json = "456";
 Long number = mapper.readValue(json, Long.class);
 assertEquals(456L, number);
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException {
 Float number = 123.45f;
 String json = mapper.writeValueAsString(number);
 assertEquals("123.45", json);
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 String json = "123.45";
 Float number = mapper.readValue(json, Float.class);
 assertEquals(123.45f, number, 0.01);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException {
 Double number = 123.456;
 String json = mapper.writeValueAsString(number);
 assertEquals("123.456", json);
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 String json = "123.456";
 Double number = mapper.readValue(json, Double.class);
 assertEquals(123.456, number, 0.001);
 }
}