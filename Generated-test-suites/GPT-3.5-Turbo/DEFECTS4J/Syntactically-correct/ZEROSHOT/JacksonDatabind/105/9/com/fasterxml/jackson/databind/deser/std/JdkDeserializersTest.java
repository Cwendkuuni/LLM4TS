import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class JdkDeserializersTest {
  
  private JdkDeserializers deserializers;
  
  @Before
  public void setUp() {
    deserializers = new JdkDeserializers();
  }
  
  @Test
  public void testFind_withUUIDClass_returnsUUIDDeserializer() {
    Class<?> rawType = UUID.class;
    String clsName = rawType.getName();
    
    JsonDeserializer<?> result = deserializers.find(rawType, clsName);
    
    assertNotNull(result);
    assertTrue(result instanceof UUIDDeserializer);
  }
  
  @Test
  public void testFind_withStackTraceElementClass_returnsStackTraceElementDeserializer() {
    Class<?> rawType = StackTraceElement.class;
    String clsName = rawType.getName();
    
    JsonDeserializer<?> result = deserializers.find(rawType, clsName);
    
    assertNotNull(result);
    assertTrue(result instanceof StackTraceElementDeserializer);
  }
  
  @Test
  public void testFind_withAtomicBooleanClass_returnsAtomicBooleanDeserializer() {
    Class<?> rawType = AtomicBoolean.class;
    String clsName = rawType.getName();

    JsonDeserializer<?> result = deserializers.find(rawType, clsName);

    assertNotNull(result);
    assertTrue(result instanceof AtomicBooleanDeserializer);
  }

  @Test
  public void testFind_withByteBufferClass_returnsByteBufferDeserializer() {
    Class<?> rawType = ByteBuffer.class;
    String clsName = rawType.getName();

    JsonDeserializer<?> result = deserializers.find(rawType, clsName);

    assertNotNull(result);
    assertTrue(result instanceof ByteBufferDeserializer);
  }

  @Test
  public void testFind_withInvalidClass_returnsNullDeserializer() {
    Class<?> rawType = String.class;
    String clsName = rawType.getName();

    JsonDeserializer<?> result = deserializers.find(rawType, clsName);

    assertNull(result);
  }

  @Test
  public void testFind_withFromStringDeserializerTypes_returnsFromStingDeserializer() {
    Class<?> rawType = FromStringDeserializer.class;
    String clsName = rawType.getName();

    JsonDeserializer<?> result = deserializers.find(rawType, clsName);

    assertNotNull(result);
    assertTrue(result instanceof FromStringDeserializer);

  }
}