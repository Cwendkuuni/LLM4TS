import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 @Test
 public void testJsonValueSerializerWithStringValueMethod() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Define a sample class with a @JsonValue annotated method
 class TestBean {
 private String value;

 @JsonValue
 public String getValue() {
 return value;
 }

 public void setValue(String value) {
 this.value = value;
 }
 }

 // Create an instance of the TestBean and set its value
 TestBean bean = new TestBean();
 bean.setValue("test");

 // Use the ObjectMapper to serialize the bean using the JsonValueSerializer
 JsonSerializer<Object> jsonValueSerializer = mapper.findValueSerializer(TestBean.class, null);
 String serialized = mapper.writerWithView(StandardSerializer.getInstance(JsonValueSerializer.class)).writeValueAsString(bean);

 // Verify that the serialized output matches the expected value
 assertEquals("\"test\"", serialized);
 }

 @Test
 public void testJsonValueSerializerWithIntegerValueMethod() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Define a sample class with a @JsonValue annotated method that returns an integer
 class TestBean {
 private int value;

 @JsonValue
 public int getValue() {
 return value;
 }

 public void setValue(int value) {
 this.value = value;
 }
 }

 // Create an instance of the TestBean and set its value
 TestBean bean = new TestBean();
 bean.setValue(42);

 // Use the ObjectMapper to serialize the bean using the JsonValueSerializer
 JsonSerializer<Object> jsonValueSerializer = mapper.findValueSerializer(TestBean.class, null);
 String serialized = mapper.writerWithView(StandardSerializer.getInstance(JsonValueSerializer.class)).writeValueAsString(bean);

 // Verify that the serialized output matches the expected value
 assertEquals("42", serialized);
 }

 @Test
 public void testJsonValueSerializerWithNullValueMethod() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Define a sample class with a @JsonValue annotated method that can return null
 class TestBean {
 private String value;

 @JsonValue
 public String getValue() {
 return value;
 }

 public void setValue(String value) {
 this.value = value;
 }
 }

 // Create an instance of the TestBean and set its value to null
 TestBean bean = new TestBean();
 bean.setValue(null);

 // Use the ObjectMapper to serialize the bean using the JsonValueSerializer
 JsonSerializer<Object> jsonValueSerializer = mapper.findValueSerializer(TestBean.class, null);
 String serialized = mapper.writerWithView(StandardSerializer.getInstance(JsonValueSerializer.class)).writeValueAsString(bean);

 // Verify that the serialized output matches the expected value
 assertEquals("null", serialized);
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializerWithThrowingValueMethod() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Define a sample class with a @JsonValue annotated method that throws an exception
 class TestBean {
 private String value;

 @JsonValue
 public String getValue() throws IOException {
 throw new IOException("Test exception");
 }

 public void setValue(String value) {
 this.value = value;
 }
 }

 // Create an instance of the TestBean and set its value
 TestBean bean = new TestBean();

 // Use the ObjectMapper to serialize the bean using the JsonValueSerializer
 JsonSerializer<Object> jsonValueSerializer = mapper.findValueSerializer(TestBean.class, null);
 try {
 String serialized = mapper.writerWithView(StandardSerializer.getInstance(JsonValueSerializer.class)).writeValueAsString(bean);
 fail("Expected JsonMappingException but got: " + serialized);
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }
}