import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectTestObject testObject;
 private JsonSerializer<Object> valueSerializerMock;
 private JsonValueSerializer jsonValueSerializer;
 private BeanProperty propertyMock;

 @Before
 public void setup() {
 testObject = new ObjectTestObject();
 testObject.setValue(42);
 valueSerializerMock = mock(JsonSerializer.class);
 propertyMock = mock(BeanProperty.class);
 jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializerMock);
 }

 @Test
 public void testSerializationWithDefaultSerializer() throws Exception {
 Object testObject = new ObjectTestObject();
 testObject.setValue("Hello World");
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serialize(eq("Hello World"), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenReturn("\"Hello World\"");
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":\"Hello World\"}", json);
 }

 @Test
 public void testSerializationWithCustomSerializer() throws Exception {
 Object testObject = new ObjectTestObject();
 testObject.setValue(new MyCustomClass());
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serialize(eq(new MyCustomClass()), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenReturn("{\"custom\":\"value\"}");
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":{\"custom\":\"value\"}}", json);
 }

 @Test
 public void testNullValue() throws Exception {
 Object testObject = new ObjectTestObject();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertTrue(json.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testExceptionDuringValueSerialization() throws Exception {
 Object testObject = new ObjectTestObject();
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenThrow(new IOException("Simulated IOException"));
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 ObjectMapper mapper = new ObjectMapper();
 mapper.writeValueAsString(testObject);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testEnumSerialization() throws Exception {
 ObjectTestObjectWithEnum testObject = new ObjectTestObjectWithEnum();
 testObject.setEnumValue(ObjectTestEnum.VALUE_1);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializerMock);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("[\"VALUE_1\"]", json);
 }

 @Test
 public void testSerializationWithNestedValue() throws Exception {
 ObjectTestObjectWithNested testObject = new ObjectTestObjectWithNested();
 testObject.setValue(new NestedObject());
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serialize(eq(new NestedObject()), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenReturn("{\"nested\":\"value\"}");
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":{\"nested\":\"value\"}}", json);
 }

 @Test
 public void testSerializationWithTypeInformation() throws Exception {
 ObjectTestObjectWithTypeInformation testObject = new ObjectTestObjectWithTypeInformation();
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}}", json);
 }

 @Test
 public void testSerializationWithNullTypeInformation() throws Exception {
 ObjectTestObjectWithTypeInformation testObject = new ObjectTestObjectWithTypeInformation();
 testObject._forceTypeInformation = false;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":\"Hello World\"}", json);
 }

 @Test
 public void testSerializationWithCustomTypeSerializer() throws Exception {
 ObjectTestObjectWithCustomTypeInformation testObject = new ObjectTestObjectWithCustomTypeInformation();
 JsonSerializer<String> customTypeSerializer = mock(JsonSerializer.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerFactory(mapper.getDefaultSerializerFactory().withSerializerInstances(String.class, customTypeSerializer));
 String json = mapper.writeValueAsString(testObject);
 assertEquals("{\"value\":\"CustomSerializedString\"}", json);
 }

 @Test
 public void testContextualization() throws Exception {
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 JsonSerializer<Object> serializedJsonValueSerializer = jsonValueSerializer.createContextual(new ObjectMapper().getSerializerProvider(), propertyMock);
 assertEquals(jsonValueSerializer.getClass(), serializedJsonValueSerializer.getClass());
 }

 @Test
 public void testContextualizationWithDifferentReturnType() throws Exception {
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.createContextual(any(SerializerProvider.class), any(BeanProperty.class)))
 .thenReturn(mock(JsonSerializer.class));
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod, valueSerializer);
 JsonSerializer<Object> serializedJsonValueSerializer = jsonValueSerializer.createContextual(new ObjectMapper().getSerializerProvider(), propertyMock);
 assertNotEquals(jsonValueSerializer.getClass(), serializedJsonValueSerializer.getClass());
 }

 @Test
 public void testContextualizationWithDifferentProperty() throws Exception {
 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testObject._accessorMethod1, valueSerializer);
 JsonSerializer<Object> serializedJsonValueSerializer1 = jsonValueSerializer.createContextual(new ObjectMapper().getSerializerProvider(), propertyMock);
 JsonSerializer<Object> serializedJsonValueSerializer2 = jsonValueSerializer.createContextual(new ObjectMapper().getSerializerProvider(), propertyMock1);
 assertNotEquals(serializedJsonValueSerializer1.getClass(), serializedJsonValueSerializer2.getClass());
 }
}

class ObjectTestObject {
 private int _value;
 private AnnotatedMethod _accessorMethod;

 public int getValue() {
 return _value;
 }

 @JsonSerialize(using = JsonValueSerializer.class)
 public int setValue(int value) {
 _value = value;
 return value;
 }
}

class ObjectTestObjectWithEnum {
 private ObjectTestEnum _enumValue;
 private AnnotatedMethod _accessorMethod;

 public ObjectTestEnum getEnumValue() {
 return _enumValue;
 }

 @JsonSerialize(using = JsonValueSerializer.class)
 public void setEnumValue(ObjectTestEnum value) {
 _enumValue = value;
 }

 enum ObjectTestEnum {
 @JsonValue("VALUE_1")
 VALUE_1,
 @JsonValue("VALUE_2")
 VALUE_2
 }
}

class ObjectTestObjectWithNested {
 private NestedObject _nested;
 private AnnotatedMethod _accessorMethod;

 public NestedObject getNested() {
 return _nested;
 }

 @JsonSerialize(using = JsonValueSerializer.class)
 public void setNested(NestedObject value) {
 _nested = value;
 }

 static class NestedObject {
 // empty class
 }
}

class ObjectTestObjectWithTypeInformation {
 private String _value;
 private AnnotatedMethod _accessorMethod;
 private boolean _forceTypeInformation;

 public String getValue() {
 return _value;
 }

 @JsonSerialize(using = JsonValueSerializer.class)
 public void setValue(String value) {
 _value = value;
 }
}

class ObjectTestObjectWithCustomTypeInformation {
 private String _value;
 private AnnotatedMethod _accessorMethod;
 private boolean _forceTypeInformation;

 public String getValue() {
 return _value;
 }

 @JsonSerialize(using = JsonValueSerializer.class)
 public void setValue(String value) {
 _value = value;
 }
}