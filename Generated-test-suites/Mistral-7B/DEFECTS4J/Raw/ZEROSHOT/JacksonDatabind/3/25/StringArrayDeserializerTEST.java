import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class StringArrayDeserializerTest {

 private ObjectMapper mapper;
 private JsonFactory jsonFactory;

 @Before
 public void setUp() {
 SimpleModule module = new SimpleModule();
 mapper = new ObjectMapper();
 mapper.registerModule(module);
 jsonFactory = mapper.getFactory();
 }

 @Test
 public void testEmptyArrayDeserialization() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = jsonFactory.createParser("[ ]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testStringArrayDeserialization() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = jsonFactory.createParser("[ \"hello\", \"world\", null ]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[] { "hello", "world", null }, result);
 }

 @Test
 public void testCustomElementDeserializer() throws Exception {
 JsonDeserializer<String> customDeserializer = mapper.getDeserializationConfig().findValueDeserializer(String.class);
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 JsonParser jp = jsonFactory.createParser("[ \"hello\", \"world\", null ]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[] { "hello", "world", null }, result);
 }

 @Test(expected = IOException.class)
 public void testNonArrayInput() throws Exception {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonParser jp = jsonFactory.createParser("{\"key\": \"value\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String[] result = deserializer.deserialize(jp, ctxt);
 fail("Expected IOException but received " + result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testNullElementDeserializer() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
 JsonParser jp = jsonFactory.createParser("[ \"hello\", \"world\" ]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserialize(jp, ctxt);
 fail("Expected IOException but received " + deserializer.getClass().getName());
 } catch (IOException e) {
 // Expected
 }
 }
}