import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializingSimpleValue() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotation();
 String expected = "{\"value\":42}";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializingValueMethodReturnsNull() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationNullValue();
 String expected = "null";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializingValueMethodThrowsException() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationThrowingException();
 assertThrows(RuntimeException.class, () -> mapper.writeValueAsString(obj));
 }

 @Test
 public void testSerializingValueMethodReturnsComplexObject() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationComplexValue();
 String expected = "{\"value\":{\"name\":\"John\",\"age\":30}}";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializingValueMethodReturnsPrimitiveType() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationPrimitiveType();
 int expected = 42;
 int actual = mapper.readerForUpdating(obj).readValueAs(Integer.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializingValueMethodReturnsEnum() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationEnum();
 String expected = "\"RED\"";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationDifferentSerializer();
 String expected = "{\"value\":{\"name\":\"John\",\"age\":30,\"class\":\"com.example.MyOtherClass\"}}";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testContextualSerializationWithTypeId() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationDifferentSerializerTypeId();
 String expected = "[\"MyOtherClass\",{\"name\":\"John\",\"age\":30}]";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testContextualSerializationWithTypeIdAndComplexValue() throws JsonProcessingException {
 Object obj = new MyClassWithJsonValueAnnotationDifferentSerializerComplexValue();
 String expected = "[\"MyOtherClass\",{\"name\":\"John\",\"address\":{\"street\":\"Main St\",\"city\":\"Anytown\"}}]";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 class MyClassWithJsonValueAnnotation {
 @JsonValue
 public int value() {
 return 42;
 }
 }

 class MyClassWithJsonValueAnnotationNullValue {
 @JsonValue
 public Integer value() {
 return null;
 }
 }

 class MyClassWithJsonValueAnnotationThrowingException {
 @JsonValue
 public int value() throws RuntimeException {
 throw new RuntimeException("This should never happen");
 }
 }

 class MyClassWithJsonValueAnnotationComplexValue {
 @JsonValue
 public MyComplexValue value() {
 return new MyComplexValue("John", 30);
 }

 static class MyComplexValue {
 String name;
 int age;

 public MyComplexValue(String name, int age) {
 this.name = name;
 this.age = age;
 }
 }
 }

 class MyClassWithJsonValueAnnotationPrimitiveType {
 @JsonValue
 public int value() {
 return 42;
 }
 }

 class MyClassWithJsonValueAnnotationEnum {
 @JsonValue
 public Enum value() {
 return Enum.RED;
 }

 enum Enum {
 RED,
 GREEN,
 BLUE
 }
 }

 class MyClassWithJsonValueAnnotationDifferentSerializer {
 @JsonValue
 public MyOtherClass value() {
 return new MyOtherClass();
 }

 static class MyOtherClass {
 String name;
 int age;
 }
 }

 class MyClassWithJsonValueAnnotationDifferentSerializerTypeId {
 @JsonValue
 public MyOtherClass value() {
 return new MyOtherClass();
 }
 }

 class MyClassWithJsonValueAnnotationDifferentSerializerComplexValue {
 @JsonValue
 public MyOtherClass value() {
 return new MyOtherClass();
 }

 static class MyOtherClass {
 String name;
 MyAddress address;

 static class MyAddress {
 String street;
 String city;
 }
 }
 }
}