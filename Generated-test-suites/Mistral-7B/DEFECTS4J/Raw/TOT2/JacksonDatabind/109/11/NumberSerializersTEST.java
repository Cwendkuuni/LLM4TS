import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer intValue = 123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(intValue);
 assertEquals("123", json);
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws Exception {
 Integer negativeNumber = -123;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(negativeNumber);
 assertEquals("-123", json);
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws Exception {
 Integer maxValue = Integer.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(maxValue);
 assertEquals(Integer.toString(Integer.MAX_VALUE), json);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegerSerializerWithNull() throws Exception {
 Integer nullValue = null;
 mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nullValue);
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short shortValue = 127;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortValue);
 assertEquals("127", json);
 }

 @Test
 public void testShortSerializerWithMinValue() throws Exception {
 Short minValue = Short.MIN_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(minValue);
 assertEquals(Short.toString(Short.MIN_VALUE), json);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float floatValue = 123.45f;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(floatValue);
 assertEquals("123.45", json);
 }

 @Test
 public void testFloatSerializerWithNaN() throws Exception {
 Float nanValue = Float.NaN;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nanValue);
 assertEquals("NaN", json);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double doubleValue = 123.45;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(doubleValue);
 assertEquals("123.45", json);
 }

 @Test
 public void testDoubleSerializerWithInfinity() throws Exception {
 Double infinityValue = Double.POSITIVE_INFINITY;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(infinityValue);
 assertEquals("Infinity", json);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long longValue = 123456789012345L;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(longValue);
 assertEquals(Long.toString(longValue), json);
 }

 @Test
 public void testLongSerializerWithMaxValue() throws Exception {
 Long maxValue = Long.MAX_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(maxValue);
 assertEquals(Long.toString(Long.MAX_VALUE), json);
 }

 @Test
 public void testLongSerializerWithMinValue() throws Exception {
 Long minValue = Long.MIN_VALUE;
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(minValue);
 assertEquals(Long.toString(Long.MIN_VALUE), json);
 }

 @Test
 public void testSerializationWithJsonFormat() throws Exception {
 // Assuming a class named MyClass with @JsonFormat annotation
 MyClass myClass = new MyClass();
 String jsonWithFormat = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(myClass);
 assertEquals("[\"MyClass\",{ \"number\":\"123\" }]", jsonWithFormat);
 }
}