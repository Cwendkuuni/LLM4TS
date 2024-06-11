import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class ValueInstantiatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDefaultCreator() throws Exception {
 ValueInstantiator instantiator = StdValueInstantiator.instance(String.class);
 assertEquals("Default creator test", instantiator.createUsingDefault(MAPPER.getDeserializationContext()));
 }

 @Test(expected = IOException.class)
 public void testMissingCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
 throw new IOException("Missing default creator");
 }
 };
 String json = "{}";
 MAPPER.readValue(json, instantiator.getValueClass());
 }

 @Test
 public void testStringCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return value;
 }
 };
 String json = "{\"value\": \"test\"}";
 assertEquals("test", instantiator.createValue(MAPPER.readTree(json)));
 }

 @Test(expected = IOException.class)
 public void testMissingStringCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 throw new IOException("Missing string creator");
 }
 };
 String json = "{\"value\": \"test\"}";
 instantiator.createValue(MAPPER.readTree(json));
 }

 @Test
 public void testIntegerCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromInt() {
 return true;
 }

 @Override
 public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
 return value;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1);
 assertEquals(1, instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null)));
 }

 @Test(expected = IOException.class)
 public void testMissingIntegerCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromInt() {
 return false;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1);
 instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null));
 }

 @Test
 public void testLongCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromLong() {
 return true;
 }

 @Override
 public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
 return value;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1L);
 assertEquals(1L, instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null)));
 }

 @Test(expected = IOException.class)
 public void testMissingLongCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromLong() {
 return false;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1L);
 instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null));
 }

 @Test
 public void testDoubleCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromDouble() {
 return true;
 }

 @Override
 public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
 return value;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1.0);
 assertEquals(1.0, instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null)), 0.001);
 }

 @Test(expected = IOException.class)
 public void testMissingDoubleCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromDouble() {
 return false;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", 1.0);
 instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null));
 }

 @Test
 public void testBooleanCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromBoolean() {
 return true;
 }

 @Override
 public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
 return value;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", true);
 assertEquals(true, instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null)));
 }

 @Test(expected = IOException.class)
 public void testMissingBooleanCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromBoolean() {
 return false;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", true);
 instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null));
 }

 @Test
 public void testPropertyCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromObjectWith() {
 return true;
 }

 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
 return args[0];
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.set("arg", 1);
 assertEquals(1, instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null)));
 }

 @Test(expected = IOException.class)
 public void testMissingPropertyCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiator() {
 @Override
 public boolean canCreateFromObjectWith() {
 return false;
 }
 };
 ObjectNode node = MAPPER.createObjectNode();
 node.set("arg", 1);
 instantiator.createValue(MAPPER.treeToValue(node, instantiator.getValueClass(), null));
 }
}