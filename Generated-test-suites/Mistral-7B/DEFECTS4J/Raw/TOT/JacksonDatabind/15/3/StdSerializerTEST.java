import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testStringSerializer() throws JsonMappingException, JsonProcessingException {
 // Given
 String input = "Hello, world!";
 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 // When
 String output = stringSerializer.serialize(input, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
 // Then
 assertEquals("\"Hello, world!\"", output.toString());
 }

 @Test
 public void testNumberSerializer() throws JsonMappingException, JsonProcessingException {
 // Given
 int input = 42;
 StdSerializer<Integer> numberSerializer = objectMapper.findValueSerializer(Integer.class);
 // When
 String output = numberSerializer.serialize(input, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
 // Then
 assertEquals("42", output.toString());
 }

 @Test
 public void testCustomSerializer() throws JsonMappingException, JsonProcessingException {
 // Given
 MyClass input = new MyClass();
 StdSerializer<MyClass> customSerializer = new MyClassSerializer();
 // When
 String output = customSerializer.serialize(input, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
 // Then
 assertEquals("{\"name\":\"John Doe\",\"age\":30}", output.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testNullValue() throws JsonMappingException, JsonProcessingException {
 // Given
 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 // When, Then
 stringSerializer.serialize(null, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownType() throws JsonMappingException, JsonProcessingException {
 // Given
 Object input = new Object();
 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 // When, Then
 stringSerializer.serialize(input, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
 }
}

class MyClass {
 private String name;
 private int age;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
}

class MyClassSerializer extends StdSerializer<MyClass> {
 protected MyClassSerializer() {
 this(null);
 }

 @SuppressWarnings("unchecked")
 protected MyClassSerializer(Class<MyClass> t) {
 super(t);
 }

 @Override
 public void serialize(MyClass value, JsonGenerator jgen, SerializerProvider provider)
 throws IOException, JsonGenerationException {
 jgen.writeStartObject();
 jgen.writeFieldName("name");
 jgen.writeString(value.getName());
 jgen.writeFieldName("age");
 jgen.writeNumber(value.getAge());
 jgen.writeEndObject();
 }
}