import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
  
  // Test for NumberSerializers.addAll() method
  @Test
  public void testAddAll() {
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    NumberSerializers.addAll(allDeserializers);

    assertEquals(8, allDeserializers.size());

    assertTrue(allDeserializers.containsKey(Integer.class.getName()));
    assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Long.class.getName()));
    assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Byte.class.getName()));
    assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
    assertTrue(allDeserializers.containsKey(Short.class.getName()));
    assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
  }
  
  // Test for NumberSerializers.Base.getSchema() method
  @Test
  public void testGetSchema() {
    JsonSerializer<Integer> serializer = new NumberSerializers.IntegerSerializer();
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    assertNull(serializer.getSchema(provider, null));
  }

  // Test for NumberSerializers.Base.acceptJsonFormatVisitor() method
  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    JsonSerializer<Integer> serializer = new NumberSerializers.IntegerSerializer();
    JsonFormatVisitorWrapper visitor = new ObjectMapper()
        .getDeserializationContext()
        .getStdDeserializationContext()
        .expectType(Integer.class)
        .visitor();
    serializer.acceptJsonFormatVisitor(visitor, null);
    JsonIntegerFormatVisitor intVisitor = visitor
        .asFormatVisitor(JsonNumberFormatVisitor.class);
    assertEquals(JsonParser.NumberType.INT, intVisitor.getNumberType());
  }
  
  // Test for NumberSerializers.Base.createContextual() method
  @Test
  public void testCreateContextual() throws JsonMappingException {
    NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
    ObjectMapper mapper = new ObjectMapper();
    SerializerProvider provider = mapper.getSerializerProvider();
    
    JsonFormat.Value format = new JsonFormat.Value();
    format.setShape(JsonFormat.Shape.STRING);
    
    BeanProperty property = new BeanProperty.Simple(mapper.constructType(Integer.class),
                                                     null, null, null);
    JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);
    
    assertTrue(contextualSerializer instanceof ToStringSerializer);
  }
  
  // Test for NumberSerializers.ShortSerializer.serialize() method
  @Test
  public void testShortSerializerSerialize() throws IOException {
    JsonSerializer<Short> serializer = new NumberSerializers.ShortSerializer();
    Short value = 5;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("5", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.IntegerSerializer.serialize() method
  @Test
  public void testIntegerSerializerSerialize() throws IOException {
    JsonSerializer<Object> serializer = new NumberSerializers.IntegerSerializer();
    Integer value = 10;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("10", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.IntegerSerializer.serializeWithType() method
  @Test
  public void testIntegerSerializerSerializeWithType() throws IOException {
    JsonSerializer<Object> serializer = new NumberSerializers.IntegerSerializer();
    Integer value = 20;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    TypeSerializer typeSer = null;
    
    serializer.serializeWithType(value, gen, provider, typeSer);
    
    assertEquals("20", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.IntLikeSerializer.serialize() method
  @Test
  public void testIntLikeSerializerSerialize() throws IOException {
    JsonSerializer<Number> serializer = new NumberSerializers.IntLikeSerializer();
    Number value = 15;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("15", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.LongSerializer.serialize() method
  @Test
  public void testLongSerializerSerialize() throws IOException {
    JsonSerializer<Object> serializer = new NumberSerializers.LongSerializer();
    Long value = 100L;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("100", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.FloatSerializer.serialize() method
  @Test
  public void testFloatSerializerSerialize() throws IOException {
    JsonSerializer<Object> serializer = new NumberSerializers.FloatSerializer();
    Float value = 3.14F;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("3.14", gen.getOutputTarget());
  }
  
  // Test for NumberSerializers.DoubleSerializer.serialize() method
  @Test
  public void testDoubleSerializerSerialize() throws IOException {
    JsonSerializer<Object> serializer = new NumberSerializers.DoubleSerializer();
    Double value = 2.71828;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    
    serializer.serialize(value, gen, provider);
    
    assertEquals("2.71828", gen.getOutputTarget());
  }

}