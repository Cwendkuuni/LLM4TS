import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeDefault() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String json = "[\"test1\",\"test2\",\"test3\"]";
 String[] result = deserializer.deserialize(mapper.parse(json), new DeserializationContext());
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(new MyStringDeserializer());
 String json = "[\"test1\",\"test2\",\"test3\"]";
 String[] result = deserializer.deserialize(mapper.parse(json), new DeserializationContext());
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 String json = "[]";
 String[] result = StringArrayDeserializer.instance.deserialize(mapper.parse(json), new DeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullArray() throws Exception {
 String json = "null";
 String[] result = StringArrayDeserializer.instance.deserialize(mapper.parse(json), new DeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullElement() throws Exception {
 String json = "[null]";
 String[] result = StringArrayDeserializer.instance.deserialize(mapper.parse(json), new DeserializationContext());
 assertArrayEquals(new String[]{null}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String json = "[\"\",\"test2\",\"test3\"]";
 String[] result = deserializer.deserialize(mapper.parse(json), new DeserializationContext());
 String[] expected = {null, "test2", "test3"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 String json = "{ \"invalid\": \"json\" }";
 String[] result = StringArrayDeserializer.instance.deserialize(mapper.parse(json), new DeserializationContext());
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws Exception {
 String json = "[1, 2, 3]";
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = deserializer.deserialize(mapper.parse(json), new DeserializationContext());
 fail("Expected JsonMappingException but no exception was thrown");
 }
}

 class MyStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
}