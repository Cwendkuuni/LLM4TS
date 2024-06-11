import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeDeserializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
 }

 @Test
 public void testDefaultImplDeserialization() throws Exception {
 // Test deserialization using default impl
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "defaultImpl");
 node.put("value", "defaultValue");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 Object result = deserializer.deserialize(new JsonParser(node.traverse()), mapper.getDeserializationContext());
 assertEquals("defaultValue", result);
 }

 @Test
 public void testCustomDeserialization() throws Exception {
 // Test deserialization using custom deserializer
 JsonDeserializer<MyClass> customDeserializer = new MyClassDeserializer();
 mapper.registerDeserializer(MyClass.class, customDeserializer);
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "MyClass");
 node.put("value", "customValue");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 Object result = deserializer.deserialize(new JsonParser(node.traverse()), mapper.getDeserializationContext());
 assertEquals("customValue", result);
 }

 @Test
 public void testUnknownTypeId() throws Exception {
 // Test deserialization with unknown type id
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "unknownType");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 try {
 deserializer.deserialize(new JsonParser(node.traverse()), mapper.getDeserializationContext());
 fail("Should have thrown exception");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testMissingTypeId() throws Exception {
 // Test deserialization without type id
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "missingTypeIdValue");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 Object result = deserializer.deserialize(new JsonParser(node.traverse()), mapper.getDeserializationContext());
 assertEquals(null, result);
 }

 @Test
 public void testDeserializationWithNativeTypeId() throws Exception {
 // Test deserialization with native type id
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "java.lang.String");
 node.put("value", "nativeTypeIdValue");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 Object result = deserializer.deserialize(new JsonParser(node.traverse()), mapper.getDeserializationContext());
 assertEquals("nativeTypeIdValue", result);
 }

 @Test
 public void testDeserializationWithTypeIdAndWrapperType() throws Exception {
 // Test deserialization with type id and wrapper type
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "java.util.HashMap");
 node.put("value", "{\"key\":\"value\"}");
 TypeDeserializerBase deserializer = mapper.getTypeDeserializer().findValueDeserializer(node, null);
 ObjectMapper deserializationContextMapper = new ObjectMapper();
 deserializationContextMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 Object result = deserializer.deserialize(new JsonParser(node.traverse()), deserializationContextMapper.getDeserializationContext());
 assertEquals(new HashMap<String, String>(), result);
 }
}

class MyClassDeserializer extends StdDeserializer<MyClass> {
 MyClassDeserializer() { super(MyClass.class); }
 @Override
 public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyClass("customDeserializedValue");
 }
}

class MyClass {
 private String value;

 public MyClass(String value) { this.value = value; }

 @Override
 public String toString() { return value; }
}