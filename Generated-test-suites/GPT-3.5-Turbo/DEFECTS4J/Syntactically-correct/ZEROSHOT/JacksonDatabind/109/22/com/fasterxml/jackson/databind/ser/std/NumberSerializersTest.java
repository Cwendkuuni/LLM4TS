import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void addAllTest() {
        // Arrange
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        
        // Act
        NumberSerializers.addAll(allDeserializers);
        
        // Assert
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }
    
    @Test
    public void shortSerializerSerializeTest() throws IOException {
        // Arrange
        ShortSerializer serializer = new ShortSerializer();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Short value = 10;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("10", writer.toString());
    }
    
    @Test
    public void integerSerializerSerializeTest() throws IOException {
        // Arrange
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Integer value = 100;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("100", writer.toString());
    }
    
    @Test
    public void intLikeSerializerSerializeTest() throws IOException {
        // Arrange
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Number value = 100.0;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("100", writer.toString());
    }
    
    @Test
    public void longSerializerSerializeTest() throws IOException {
        // Arrange
        LongSerializer serializer = new LongSerializer(Long.class);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Long value = 1000L;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("1000", writer.toString());
    }
    
    @Test
    public void floatSerializerSerializeTest() throws IOException {
        // Arrange
        FloatSerializer serializer = FloatSerializer.instance;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Float value = 10.5f;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("10.5", writer.toString());
    }
    
    @Test
    public void doubleSerializerSerializeTest() throws IOException {
        // Arrange
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Double value = 10.5678;
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        assertEquals("10.5678", writer.toString());
    }
    
    @Test
    public void baseGetSchemaTest() {
        // Arrange
        Base<Object> serializer = new ShortSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = provider.constructType(Short.class);
        
        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }
    
    @Test
    public void baseAcceptJsonFormatVisitorTest() throws JsonMappingException {
        // Arrange
        Base<Object> serializer = new DoubleSerializer(Double.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = provider.constructType(Double.class);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.DOUBLE);
    }
    
    @Test
    public void baseCreateContextualTest() throws JsonMappingException {
        // Arrange
        Base<Object> serializer = new DoubleSerializer(Double.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = mock(BeanProperty.class);
        
        // Act
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        
        // Assert
        assertEquals(serializer, result);
    }

}