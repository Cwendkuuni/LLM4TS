import com.fasterxml.jackson.databind.*;
import junit.framework.TestCase;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {
  
  @Test
  public void testFindWithValidClass() {
    Class<?> rawType = UUID.class;
    String clsName = UUID.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNotNull(result);
    assertTrue(result instanceof UUIDDeserializer);
  }
  
  @Test
  public void testFindWithInvalidClass() {
    Class<?> rawType = Integer.class;
    String clsName = Integer.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNull(result);
  }
  
  @Test
  public void testFindWithAtomicBooleanClass() {
    Class<?> rawType = AtomicBoolean.class;
    String clsName = AtomicBoolean.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNotNull(result);
    assertTrue(result instanceof AtomicBooleanDeserializer);
  }
  
  @Test
  public void testFindWithStackTraceElementClass() {
    Class<?> rawType = StackTraceElement.class;
    String clsName = StackTraceElement.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNotNull(result);
    assertTrue(result instanceof StackTraceElementDeserializer);
  }
  
  @Test
  public void testFindWithByteBufferClass() {
    Class<?> rawType = ByteBuffer.class;
    String clsName = ByteBuffer.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNotNull(result);
    assertTrue(result instanceof ByteBufferDeserializer);
  }
  
  @Test
  public void testFindWithFromStringDeserializerTypes() {
    Class<?> rawType = String.class;
    String clsName = String.class.getName();
    JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
    assertNotNull(result);
    assertTrue(result instanceof FromStringDeserializer);
  }
}