import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = Short.MIN_VALUE;
 Short output = objectMapper.readValue(objectMapper.writeValueAsString(input), Short.class);
 assertEquals(input, output);

 input = Short.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Short.class);
 assertEquals(input, output);
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = Integer.MIN_VALUE;
 Integer output = objectMapper.readValue(objectMapper.writeValueAsString(input), Integer.class);
 assertEquals(input, output);

 input = Integer.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Integer.class);
 assertEquals(input, output);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number input = Byte.MIN_VALUE;
 Number output = objectMapper.readValue(objectMapper.writeValueAsString(input), Number.class);
 assertEquals(input, output);

 input = Byte.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Number.class);
 assertEquals(input, output);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = Long.MIN_VALUE;
 Long output = objectMapper.readValue(objectMapper.writeValueAsString(input), Long.class);
 assertEquals(input, output);

 input = Long.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Long.class);
 assertEquals(input, output);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = Float.MIN_VALUE;
 Float output = objectMapper.readValue(objectMapper.writeValueAsString(input), Float.class);
 assertEquals(input, output, 0.001f);

 input = Float.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Float.class);
 assertEquals(input, output, 0.001f);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = Double.MIN_VALUE;
 Double output = objectMapper.readValue(objectMapper.writeValueAsString(input), Double.class);
 assertEquals(input, output, 0.00001);

 input = Double.MAX_VALUE;
 output = objectMapper.readValue(objectMapper.writeValueAsString(input), Double.class);
 assertEquals(input, output, 0.00001);
 }
}