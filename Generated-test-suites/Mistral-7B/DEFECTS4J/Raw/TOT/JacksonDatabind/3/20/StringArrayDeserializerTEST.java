import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class StringArrayDeserializerTest {

 private ObjectMapper mapper;
 private JsonFactory jsonFactory;
 private JsonParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 jsonFactory = mapper.getFactory();
 parser = jsonFactory.createParser("[]");
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 String[] result = new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws Exception {
 parser.nextValue("abc");
 String[] result = new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws Exception {
 String[] elementDeserializerResult = new StringArrayDeserializer(new MyStringDeserializer()).deserialize(parser, mapper.getDeserializationContext());
 assertArrayEquals(new String[]{"custom"}, elementDeserializerResult);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 parser.nextValue("{invalidJson}");
 new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullElement() throws Exception {
 parser.nextValue(JsonToken.VALUE_NULL);
 new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws Exception {
 parser.nextValue("abc");
 parser.nextToken(); // skip [
 new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyStringAsNull() throws Exception {
 mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 parser.nextValue("");
 new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 String[] result = new StringArrayDeserializer().deserializeWithType(parser, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(String[].class));
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeMalformedJson() throws Exception {
 parser.nextValue("[abc");
 new StringArrayDeserializer().deserialize(parser, mapper.getDeserializationContext());
 }
}

class MyStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return "custom";
 }
}