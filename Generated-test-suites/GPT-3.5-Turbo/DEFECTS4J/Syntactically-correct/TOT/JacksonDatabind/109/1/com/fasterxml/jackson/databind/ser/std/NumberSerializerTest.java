import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

  @Test
  public void testSerializeBigDecimal() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    BigDecimal bigDecimal = new BigDecimal("12345.6789");
    String expected = "12345.6789";
    String result = mapper.writeValueAsString(bigDecimal);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeBigInteger() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    BigInteger bigInteger = new BigInteger("9876543210");
    String expected = "9876543210";
    String result = mapper.writeValueAsString(bigInteger);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeLong() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Long longNumber = 1234567890L;
    String expected = "1234567890";
    String result = mapper.writeValueAsString(longNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeDouble() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Double doubleNumber = 12345.6789;
    String expected = "12345.6789";
    String result = mapper.writeValueAsString(doubleNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeFloat() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Float floatNumber = 12345.6789F;
    String expected = "12345.6789";
    String result = mapper.writeValueAsString(floatNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeInt() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Integer intNumber = 12345;
    String expected = "12345";
    String result = mapper.writeValueAsString(intNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeByte() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Byte byteNumber = 123;
    String expected = "123";
    String result = mapper.writeValueAsString(byteNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeShort() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Short shortNumber = 12345;
    String expected = "12345";
    String result = mapper.writeValueAsString(shortNumber);
    assertEquals(expected, result);
  }

  @Test
  public void testSerializeUnknownNumberType() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Number unknownNumber = new Number() {
      public long longValue() { return 0L; }
      public float floatValue() { return 0.0F; }
      public double doubleValue() { return 0.0; }
      public int intValue() { return 0; }
    };
    String expected = "0";
    String result = mapper.writeValueAsString(unknownNumber);
    assertEquals(expected, result);
  }
}