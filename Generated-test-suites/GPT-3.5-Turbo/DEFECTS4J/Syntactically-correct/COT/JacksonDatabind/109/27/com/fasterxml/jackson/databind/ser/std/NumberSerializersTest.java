
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
  @Test
  public void testAddAll() {
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    NumberSerializers.addAll(allDeserializers);
    
    assertEquals(9, allDeserializers.size());
    assertNotNull(allDeserializers.get(Integer.class.getName()));
    assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
    assertNotNull(allDeserializers.get(Long.class.getName()));
    assertNotNull(allDeserializers.get(Long.TYPE.getName()));
    assertNotNull(allDeserializers.get(Byte.class.getName()));
    assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
    assertNotNull(allDeserializers.get(Short.class.getName()));
    assertNotNull(allDeserializers.get(Short.TYPE.getName()));
    assertNotNull(allDeserializers.get(Double.class.getName()));
    assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    assertNotNull(allDeserializers.get(Float.class.getName()));
    assertNotNull(allDeserializers.get(Float.TYPE.getName()));
  }

  @Test
  public void testShortSerializer() throws IOException {
    NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
    Short value = new Short((short) 10);
    
    JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    gen.flush();
    
    String output = gen.getOutputTarget().toString();
    assertEquals("10", output);
  }

  // Add tests for other serializers

}