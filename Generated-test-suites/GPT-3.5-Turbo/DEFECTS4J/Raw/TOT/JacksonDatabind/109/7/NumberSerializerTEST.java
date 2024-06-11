import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

   @Test
   public void testSerializeBigDecimal() {
      NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
      JsonGeneratorStub generator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      
      BigDecimal value = new BigDecimal("123.456");
      try {
         serializer.serialize(value, generator, provider);
         assertEquals(value, generator.getLastNumberValue());
      } catch (IOException e) {
         fail("Failed to serialize BigDecimal");
      }
   }

   @Test
   public void testSerializeBigInteger() {
      NumberSerializer serializer = new NumberSerializer(BigInteger.class);
      JsonGeneratorStub generator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      
      BigInteger value = new BigInteger("12345");
      try {
         serializer.serialize(value, generator, provider);
         assertEquals(value, generator.getLastNumberValue());
      } catch (IOException e) {
         fail("Failed to serialize BigInteger");
      }
   }

   @Test
   public void testSerializeLong() {
      NumberSerializer serializer = new NumberSerializer(Long.class);
      JsonGeneratorStub generator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      
      Long value = 123456789L;
      try {
         serializer.serialize(value, generator, provider);
         assertEquals(value, generator.getLastNumberValue());
      } catch (IOException e) {
         fail("Failed to serialize Long");
      }
   }

   // similar tests for Double, Float, Integer, Byte, Short

   @Test
   public void testInvalidNumberType() {
      NumberSerializer serializer = new NumberSerializer(Number.class);
      JsonGeneratorStub generator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      
      Number value = new TestNumber();
      try {
         serializer.serialize(value, generator, provider);
         assertEquals(value.toString(), generator.getLastStringValue());
      } catch (IOException e) {
         fail("Failed to handle invalid number type");
      }
   }

   // other test cases for edge cases, typical use cases and error scenarios

}

class JsonGeneratorStub extends JsonGenerator {
   private Number lastNumberValue;
   private String lastStringValue;

   public Number getLastNumberValue() {
      return lastNumberValue;
   }

   public String getLastStringValue() {
      return lastStringValue;
   }

   // Implement abstract and unused methods from JsonGenerator
}

class SerializerProviderStub extends SerializerProvider {
   // Implement abstract and unused methods from SerializerProvider
}

class TestNumber extends Number {
   // Implement abstract and unused methods from Number
}