
import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {
  
  @Test
  public void testIntegerSerializer() {
    IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
  
    assertEquals("{\"@type\":\"integer\",\"value\":5}", serializer.serialize(5));
    assertEquals("{\"@type\":\"integer\",\"value\":-5}", serializer.serialize(-5));
    assertEquals("{\"@type\":\"integer\",\"value\":0}", serializer.serialize(0));
  }
  
  @Test
  public void testShortSerializer() {
    ShortSerializer serializer = new NumberSerializers.ShortSerializer();
  
    assertEquals("{\"@type\":\"number\",\"value\":5}", serializer.serialize((short)5));
    assertEquals("{\"@type\":\"number\",\"value\":-5}", serializer.serialize((short)-5));
  }
  
  @Test
  public void testIntLikeSerializer() {
    IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
  
    assertEquals("{\"@type\":\"integer\",\"value\":5}", serializer.serialize(5));
    assertEquals("{\"@type\":\"integer\",\"value\":-5}", serializer.serialize(-5));
  }
  
  @Test
  public void testLongSerializer() {
    LongSerializer serializer = new NumberSerializers.LongSerializer();
  
    assertEquals("{\"@type\":\"number\",\"value\":5000000000}", serializer.serialize(5000000000L));
    assertEquals("{\"@type\":\"number\",\"value\":-5000000000}", serializer.serialize(-5000000000L));
  }
  
  @Test
  public void testFloatSerializer() {
    FloatSerializer serializer = new NumberSerializers.FloatSerializer();
  
    assertEquals("{\"@type\":\"number\",\"value\":5.5}", serializer.serialize(5.5f));
    assertEquals("{\"@type\":\"number\",\"value\":-5.5}", serializer.serialize(-5.5f));
  }
  
  @Test
  public void testDoubleSerializer() {
    DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
  
    assertEquals("{\"@type\":\"number\",\"value\":5.5}", serializer.serialize(5.5));
    assertEquals("{\"@type\":\"number\",\"value\":-5.5}", serializer.serialize(-5.5));
  }
  
}
