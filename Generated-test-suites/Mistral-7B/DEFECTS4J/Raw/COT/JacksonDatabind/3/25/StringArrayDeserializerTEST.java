import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private static final StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

 @Test
 public void testDeserializeStandard() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createJsonParser("['hello', 'world']"), new DeserializationContext());
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createJsonParser("['hello', 'world']"), new DeserializationContext(), new StubStringDeserializer());
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 stringArrayDeserializer.deserialize(createJsonParser("{'key': 'value'}"), new DeserializationContext());
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createJsonParser("['null']"), new DeserializationContext());
 assertArrayEquals(new String[] {null}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createJsonParser("[]"), new DeserializationContext());
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 stringArrayDeserializer.deserialize(createJsonParser("{'key': 'value'}"), new DeserializationContext());
 }

 @Test
 public void testDeserializeCustomContextual() throws IOException {
 JsonDeserializer<String> stubStringDeserializer = new StubStringDeserializer();
 String[] result = stringArrayDeserializer.createContextual(new DeserializationContext(), new StubProperty("key")).deserialize(createJsonParser("['hello', 'world']"), new DeserializationContext(), new TypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new String[] {p.nextValue().asText(), p.nextValue().asText()};
 }
 });
 assertArrayEquals(new String[] {"hello", "world"}, result);
 }

 @Test
 public void testDeserializeEmptyCustomContextual() throws IOException {
 JsonDeserializer<String> stubStringDeserializer = new StubStringDeserializer();
 String[] result = stringArrayDeserializer.createContextual(new DeserializationContext(), new StubProperty("key")).deserialize(createJsonParser("[]"), new DeserializationContext(), new TypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new String[0];
 }
 });
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidContextual() throws IOException {
 JsonDeserializer<String> stubStringDeserializer = new StubStringDeserializer();
 String[] result = stringArrayDeserializer.createContextual(new DeserializationContext(), new StubProperty("key")).deserialize(createJsonParser("{'key': 'value'}"), new DeserializationContext(), new TypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new String[] {"invalid"};
 }
 });
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullContextual() throws IOException {
 JsonDeserializer<String> stubStringDeserializer = new StubStringDeserializer();
 String[] result = stringArrayDeserializer.createContextual(new DeserializationContext(), new StubProperty("key"))
 .deserialize(createJsonParser("['null']"), new DeserializationContext(), new TypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 });
 }

 private JsonParser createJsonParser(String jsonString) throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createParser(jsonString);
}

 private static class StubProperty {
 private final String name;

 public StubProperty(String name) {
 this.name = name;
 }
 }

 private static class StubStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.nextText();
 }
 }
}