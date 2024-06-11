import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

  @Test
  public void testSerializeBigDecimal() {
    NumberSerializer serializer = new NumberSerializer(Number.class);
    BigDecimal value = new BigDecimal("1234.56");
    JsonGenerator g = // create mock JsonGenerator object;
    // call the serialize method of the serializer
    serializer.serialize(value, g, null);
    // verify the outcome is correct
    // for example, assert that the correct method of the JsonGenerator is called with the correct arguments
    // assert that the value is written to the generator in the expected format
  }

  @Test
  public void testSerializeBigInteger() {
    // similar to the testSerializeBigDecimal method, but with BigInteger value
  }

  @Test
  public void testSerializeLong() {
    // similar to the testSerializeBigDecimal method, but with Long value
  }

  @Test
  public void testSerializeDouble() {
    // similar to the testSerializeBigDecimal method, but with Double value
  }

  @Test
  public void testSerializeFloat() {
    // similar to the testSerializeBigDecimal method, but with Float value
  }

  @Test
  public void testSerializeInteger() {
    // similar to the testSerializeBigDecimal method, but with Integer value
  }

  @Test
  public void testSerializeByte() {
    // similar to the testSerializeBigDecimal method, but with Byte value
  }

  @Test
  public void testSerializeShort() {
    // similar to the testSerializeBigDecimal method, but with Short value
  }

  @Test
  public void testSerializeOtherNumber() {
    // similar to the testSerializeBigDecimal method, but with an instance of a custom Number subclass
  }

  @Test
  public void testSerializeNullValue() {
    // similar to the testSerializeBigDecimal method, but with null value
  }

  @Test
  public void testGetSchema() {
    // create a mock SerializerProvider object
    // call the getSchema method of the serializer with a typeHint
    // verify the outcome is correct
    // for example, assert that the returned JsonNode has the correct type and properties
  }

  @Test
  public void testAcceptJsonFormatVisitor() {
    // create a mock JsonFormatVisitorWrapper object
    // create a mock JavaType object
    // call the acceptJsonFormatVisitor method of the serializer with the mock objects
    // verify the outcome is correct
    // for example, assert that the appropriate visit method of the JsonFormatVisitorWrapper is called with the correct arguments
  }

}