import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.*;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class JdkDeserializersTest {
  
  @Test
  public void testFind() {
    Class<?> rawType = UUID.class;
    String clsName = rawType.getName();
    JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof UUIDDeserializer);
  }
  
  @Test
  public void testFind2() {
    Class<?> rawType = AtomicBoolean.class;
    String clsName = rawType.getName();
    JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof AtomicBooleanDeserializer);
  }
  
  @Test
  public void testFind3() {
    Class<?> rawType = StackTraceElement.class;
    String clsName = rawType.getName();
    JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof StackTraceElementDeserializer);
  }
  
  @Test
  public void testFind4() {
    Class<?> rawType = ByteBuffer.class;
    String clsName = rawType.getName();
    JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof ByteBufferDeserializer);
  }
}