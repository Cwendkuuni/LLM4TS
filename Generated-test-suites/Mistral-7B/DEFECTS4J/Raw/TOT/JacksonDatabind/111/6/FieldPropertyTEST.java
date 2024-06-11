import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FieldProperty;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

 private static final ObjectNode TEST_DATA = ObjectMapper.findValueToBeSerializedAsObject(
 "{ \"field\": null, \"anotherField\": 42, \"listField\": [1, 2, 3], \"annotatedField\": 45.5 }");

 private ObjectMapper objectMapper;
 private JsonDeserializer<Object> testDeserializer;
 private JsonDeserializer<String> stringDeserializer;
 private Object targetInstance;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 testDeserializer = new TestDeserializer();
 stringDeserializer = new StandardDeserializers.StringDeserializer();
 targetInstance = new TestTarget();
 }

 @Test
 public void testNullValue() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property.deserializeAndSet(parser, null, targetInstance);
 Assert.assertNull(targetInstance.getFieldValue());
 }

 @Test
 public void testValidValue() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property.deserializeAndSet(parser, null, targetInstance);
 Assert.assertEquals(45.5, targetInstance.getFieldValue(), 0.001);
 }

 @Test
 public void testListValue() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property.deserializeAndSet(parser, null, targetInstance);
 Assert.assertArrayEquals(new int[]{1, 2, 3}, targetInstance.getListFieldValue());
 }

 @Test
 public void testValueTypeDeserializer() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 ObjectTypeDeserializer objectTypeDeserializer = new ObjectTypeDeserializer();
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property = new FieldProperty(property, null, objectTypeDeserializer);
 property.deserializeAndSet(parser, null, targetInstance);
 Assert.assertEquals("TestStringValue", targetInstance.getStringFieldValue());
 }

 @Test(expected = IOException.class)
 public void testNullDeserializer() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 JsonDeserializer<?> nullDeserializer = null;
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property = new FieldProperty(property, nullDeserializer, nullValueProvider);
 property.deserializeAndSet(parser, null, targetInstance);
 }

 @Test(expected = IOException.class)
 public void testDeserializationFailed() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 ObjectTypeDeserializer objectTypeDeserializer = new ObjectTypeDeserializer();
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property = new FieldProperty(property, testDeserializer, nullValueProvider);
 property = new FieldProperty(property, null, objectTypeDeserializer);
 JsonParser jsonParser = parser;
 jsonParser.nextToken(); //read token
 property.deserializeAndSet(jsonParser, null, targetInstance);
 }

 @Test
 public void testSetValue() throws IOException {
 targetInstance.setFieldValue(123);
 Assert.assertEquals(123, targetInstance.getFieldValue(), 0.001);
 }

 @Test
 public void testSetStringValue() {
 targetInstance.setStringFieldValue("TestStringValue");
 Assert.assertEquals("TestStringValue", targetInstance.getStringFieldValue());
 }

 @Test
 public void testSetListValue() {
 targetInstance.setListFieldValue(new int[]{4, 5, 6});
 Assert.assertArrayEquals(new int[]{4, 5, 6}, targetInstance.getListFieldValue());
 }

 @Test
 public void testSetValueWithJsonParser() throws IOException {
 JsonParser parser = objectMapper.getFactory().createParser(TEST_DATA);
 FieldProperty property = objectMapper.findDeserializer(parser, TestTarget.class, null);
 property.set(targetInstance, parser);
 Assert.assertEquals(45.5, targetInstance.getFieldValue(), 0.001);
 }

 @Test
 public void testSetValueWithObject() throws IOException {
 ObjectNode jsonNode = (ObjectNode) TEST_DATA;
 jsonNode.put("field", 123.5);
 targetInstance.setFieldValue(jsonNode.get("field"));
 Assert.assertEquals(123.5, targetInstance.getFieldValue(), 0.001);
 }

 @Test
 public void testSetValueWithDeserializer() throws IOException {
 ObjectNode jsonNode = (ObjectNode) TEST_DATA;
 targetInstance.setFieldValue(stringDeserializer.deserialize(jsonNode.getObject("stringField")));
 Assert.assertEquals("TestStringValue", targetInstance.getStringFieldValue());
 }

 @Test
 public void testSetValueWithTypeDeserializer() throws IOException {
 ObjectNode jsonNode = (ObjectNode) TEST_DATA;
 ObjectTypeDeserializer objectTypeDeserializer = new ObjectTypeDeserializer();
 targetInstance.setFieldValue(objectTypeDeserializer.deserialize(jsonNode, TestTarget.Class));
 Assert.assertEquals("TestStringValue", targetInstance.getStringFieldValue());
 }

 private static class TestDeserializer extends JsonDeserializer<Object> {
 @Override public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "TestStringValue";
 }
 }

 private static class TestTarget {
 private float fieldValue;
 private String stringFieldValue;
 private int[] listFieldValue;

 public float getFieldValue() { return fieldValue; }
 public void setFieldValue(float value) { this.fieldValue = value; }

 public String getStringFieldValue() { return stringFieldValue; }
 public void setStringFieldValue(String value) { this.stringFieldValue = value; }

 public int[] getListFieldValue() { return listFieldValue; }
 public void setListFieldValue(int[] value) { this.listFieldValue = value; }
 }

 private static class NullValueProvider implements NullValueProvider {
 @Override public Object getNullValue(DeserializationContext ctxt) { return null; }
 }
}