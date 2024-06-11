import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 try {
 serializer = mapper.findValueSerializer(String.class, null);
 } catch (JsonMappingException e) {
 // Serializer not found; let it be
 }
 }

 @Test
 public void testBasicFunctionality() throws JsonProcessingException {
 Object testObject = new TestClass("Test Value");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};
 serializer.serialize(testObject, gen, mapper.getSerializerProvider());
 String expected = "Test Value";
 assertEquals(expected, gen.toString());
 }

 @Test
 public void testSerializationOfNull() throws JsonProcessingException {
 TestClass nullTestObject = null;
 serializer.serialize(nullTestObject, null, mapper.getSerializerProvider());
 assertTrue(mapper.getSerializerProvider().getDefaultSerializer().isEmptyElementSerializer());
 }

 @Test
 public void testSerializationOfPrimitiveTypes() throws JsonProcessingException {
 int testInt = 42;
 double testDouble = 3.14;
 boolean testBool = true;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(testInt, gen, mapper.getSerializerProvider());
 gen.flush();
 assertEquals(Integer.toString(testInt), gen.toString());
 gen.reset();
 serializer.serialize(testDouble, gen, mapper.getSerializerProvider());
 gen.flush();
 assertEquals(Double.toString(testDouble), gen.toString());
 gen.reset();
 serializer.serialize(testBool, gen, mapper.getSerializerProvider());
 gen.flush();
 assertEquals(Boolean.toString(testBool), gen.toString());
 }

 @Test
 public void testSerializationOfStringType() throws JsonProcessingException {
 String testString = "Test String";
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(testString, gen, mapper.getSerializerProvider());
 gen.flush();
 assertEquals(testString, gen.toString());
 }

 @Test
 public void testSerializationOfCustomType() throws JsonProcessingException {
 TestClassWithJsonValue customTestObject = new TestClassWithJsonValue();
 customTestObject.setValue("Custom Test Value");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(TestClassWithJsonValue.class) {};
 JsonValueSerializer customJsonSerializer = new JsonValueSerializer(
 _ -> customTestObject.getValueMethod(),
 valueSerializer,
 null,
 false
 );
 serializer = customJsonSerializer;
 serializer.serialize(customTestObject, gen, mapper.getSerializerProvider());
 String expected = "Custom Test Value";
 assertEquals(expected, gen.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfUnsupportedType() throws JsonProcessingException {
 TestClassWithUnsupportedValue unsupportedTestObject = new TestClassWithUnsupportedValue();
 serializer.serialize(unsupportedTestObject, null, mapper.getSerializerProvider());
 }

 @Test
 public void testSerializationWithTypeHint() throws JsonProcessingException {
 TestClassWithJsonValue customTestObject = new TestClassWithJsonValue();
 customTestObject.setValue("Custom Test Value");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 typeSerializer = mapper.getTypeSerializer();
 serializer.serializeWithType(customTestObject, gen, mapper.getSerializerProvider(), typeSerializer);
 gen.flush();
 String expected = "TestClassWithJsonValue";
 assertEquals(expected, gen.readTree().getClass().getSimpleName());
 assertEquals("Custom Test Value", gen.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidTypeHint() throws JsonProcessingException {
 TestClassWithJsonValue customTestObject = new TestClassWithJsonValue();
 customTestObject.setValue("Custom Test Value");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 typeSerializer = mapper.getTypeSerializer();
 JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};
 JsonValueSerializer serializerWithInvalidTypeHint = new JsonValueSerializer(
 _ -> customTestObject.getValueMethod(),
 valueSerializer,
 null,
 false
 );
 serializerWithInvalidTypeHint.serializeWithType(customTestObject, gen, mapper.getSerializerProvider(), typeSerializer);
 }

 @Test
 public void testSerializationWithEnumType() throws JsonProcessingException {
 TestEnum testEnum = TestEnum.VALUE_ONE;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(TestEnum.class) {};
 JsonValueSerializer serializerWithEnumType = new JsonValueSerializer(
 _ -> testEnum.getValueMethod(),
 valueSerializer,
 null,
 false
 );
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSchemaFactory().getJsonFormatVisitor(JsonFormatVisitorWrapper.Std, null);
 serializerWithEnumType.acceptJsonFormatVisitor(visitor, null);
 assertEquals("VALUE_ONE", visitor.getTextFormat().getTextValue());
 serializerWithEnumType.serialize(testEnum, gen, mapper.getSerializerProvider());
 assertEquals("VALUE_ONE", gen.toString());
 }

 @Test
 public void testSerializationWithEnumTypeWithCustomSerializer() throws JsonProcessingException {
 TestEnum testEnum = TestEnum.VALUE_TWO;
 JsonSerializer<Object> customEnumSerializer = new StdSerializer<Object>(TestEnum.class) {
 @Override
 public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
 visitor.writeStringFormat(JsonFormat.Value.DUMMY);
 }
 };
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(TestEnum.class) {};
 JsonValueSerializer serializerWithEnumTypeAndCustomSerializer = new JsonValueSerializer(
 _ -> testEnum.getValueMethod(),
 valueSerializer,
 null,
 false
 );
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSchemaFactory().getJsonFormatVisitor(JsonFormatVisitorWrapper.Std, null);
 serializerWithEnumTypeAndCustomSerializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals("VALUE_TWO", visitor.getTextFormat().getTextValue());
 serializerWithEnumTypeAndCustomSerializer.serialize(testEnum, gen, mapper.getSerializerProvider());
 assertEquals("VALUE_TWO", gen.toString());
 }
}

class TestClass {
 @JsonValue
 public String getValue() {
 return "Test Value";
 }
}

class TestClassWithJsonValue {
 @JsonValue
 public String getValue() {
 return "Custom Test Value";
 }

 private final Method valueMethod = this.getClass().getMethod("getValue");
}

class TestClassWithUnsupportedValue {
 @JsonValue
 public Float getValue() {
 return 42.0f;
 }
}

enum TestEnum {
 @JsonValue
 VALUE_ONE,
 @JsonValue
 VALUE_TWO;

 private final Method getValueMethod;

 TestEnum() {
 this.getValueMethod = this.getClass().getMethod("getValue");
 }

 public String getValue() {
 return getValueMethod.invoke(this).toString();
 }
}