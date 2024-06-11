import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 @BeforeAll
 static void setup() {
 // Initialize the JUnit test runner with Jackson2ObjectMapperFactory
 ObjectMapper objectMapper = new ObjectMapper();
 // Perform any additional setup here if needed
 }

 @Test
 public void testNumberSerializersAddAll() throws Exception {
 // Create an instance of the NumberSerializers class
 NumberSerializers numberSerializers = new NumberSerializers();
 // Create a mutable map for storing the deserializers
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 // Call the addAll method and store the deserializers in the map
 numberSerializers.addAll(allDeserializers);
 // Test each deserializer by checking if it's present in the map
 for (String key : allDeserializers.keySet()) {
 JsonSerializer<?> deserializer = allDeserializers.get(key);
 assertEquals(deserializer.getClass(), IntegerSerializer.class, "Incorrect deserializer for Integer: " + key);
 key = Integer.TYPE.getName();
 assertEquals(deserializer.getClass(), IntegerSerializer.class, "Incorrect deserializer for Integer type: " + key);
 key = Long.class.getName();
 assertEquals(deserializer.getClass(), LongSerializer.class, "Incorrect deserializer for Long: " + key);
 key = Long.TYPE.getName();
 assertEquals(deserializer.getClass(), LongSerializer.class, "Incorrect deserializer for Long type: " + key);
 key = Byte.class.getName();
 assertEquals(deserializer.getClass(), IntLikeSerializer.class, "Incorrect deserializer for Byte: " + key);
 key = Byte.TYPE.getName();
 assertEquals(deserializer.getClass(), IntLikeSerializer.class, "Incorrect deserializer for Byte type: " + key);
 key = Short.class.getName();
 assertEquals(deserializer.getClass(), ShortSerializer.class, "Incorrect deserializer for Short: " + key);
 key = Short.TYPE.getName();
 assertEquals(deserializer.getClass(), ShortSerializer.class, "Incorrect deserializer for Short type: " + key);
 key = Float.class.getName();
 assertEquals(deserializer.getClass(), FloatSerializer.class, "Incorrect deserializer for Float: " + key);
 key = Float.TYPE.getName();
 assertEquals(deserializer.getClass(), FloatSerializer.class, "Incorrect deserializer for Float type: " + key);
 key = Double.class.getName();
 assertEquals(deserializer.getClass(), DoubleSerializer.class, "Incorrect deserializer for Double: " + key);
 key = Double.TYPE.getName();
 assertEquals(deserializer.getClass(), DoubleSerializer.class, "Incorrect deserializer for Double type: " + key);
 }
}

 @Test
 public void testNumberSerializerContextualSerialization() throws Exception {
 // Create an instance of the ObjectMapper with the NumberSerializers class in the serializer provider
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(Integer.TYPE, new IntegerSerializer());
 module.addSerializer(Integer.class, new IntegerSerializer());
 module.addSerializer(Long.TYPE, new LongSerializer());
 module.addSerializer(Long.class, new LongSerializer());
 module.addSerializer(Float.TYPE, new FloatSerializer());
 module.addSerializer(Float.class, new FloatSerializer());
 module.addSerializer(Double.TYPE, new DoubleSerializer());
 module.addSerializer(Double.class, new DoubleSerializer());
 objectMapper.registerModule(module);
 // Define test classes with @JsonFormat annotations
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 static class TestWithStringFormat {
 int intField;
 long longField;
 float floatField;
 double doubleField;
 }

 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 static class TestWithNumberFormat {
 @JsonFormat(shape = JsonFormat.Shape.NUMBER, numberFormat = "0.00")
 int intField;
 @JsonFormat(shape = JsonFormat.Shape.NUMBER, numberFormat = "0.00")
 long longField;
 @JsonFormat(shape = JsonFormat.Shape.NUMBER, numberFormat = "0.00")
 float floatField;
 @JsonFormat(shape = JsonFormat.Shape.NUMBER, numberFormat = "0.00")
 double doubleField;
 }

 // Serialize test objects
 TestWithStringFormat testStringFormat = new TestWithStringFormat();
 testStringFormat.intField = 123;
 testStringFormat.longField = 456L;
 testStringFormat.floatField = 123.45f;
 testStringFormat.doubleField = 456.789;
 String jsonString1 = objectMapper.writeValueAsString(testStringFormat);
 TestWithNumberFormat testNumberFormat = new TestWithNumberFormat();
 testNumberFormat.intField = 123;
 testNumberFormat.longField = 456L;
 testNumberFormat.floatField = 123.45f;
 testNumberFormat.doubleField = 456.789;
 String jsonString2 = objectMapper.writeValueAsString(testNumberFormat);
 // Deserialize JSON strings back into test objects
 TestWithStringFormat deserializedStringFormat1 = objectMapper.readValue(jsonString1, TestWithStringFormat.class);
 TestWithNumberFormat deserializedNumberFormat1 = objectMapper.readValue(jsonString2, TestWithNumberFormat.class);
 // Compare the deserialized objects with the original ones
 assertEquals(deserializedStringFormat1, testStringFormat, "Deserialization failed for TestWithStringFormat");
 assertEquals(deserializedNumberFormat1, testNumberFormat, "Deserialization failed for TestWithNumberFormat");
 }

 @AfterAll
 static void tearDown() {
 // Perform any cleanup here if needed
 }
}