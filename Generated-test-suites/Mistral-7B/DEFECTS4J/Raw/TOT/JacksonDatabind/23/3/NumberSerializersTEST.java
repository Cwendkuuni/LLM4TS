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
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 String json = mapper.writeValueAsString(value);
 assertEquals("123", json);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer value = -123;
 String json = mapper.writeValueAsString(value);
 assertEquals("-123", json);
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer value = Integer.MAX_VALUE;
 String json = mapper.writeValueAsString(value);
 assertEquals(String.valueOf(Integer.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 Integer value = 123;
 String json = "\"abc\"";
 Object result = mapper.readValue(json, Integer.class);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short value = (short) 123;
 String json = mapper.writeValueAsString(value);
 assertEquals("123", json);
 }

 // ... Add tests for other serializers (LongSerializer, FloatSerializer, DoubleSerializer)
}