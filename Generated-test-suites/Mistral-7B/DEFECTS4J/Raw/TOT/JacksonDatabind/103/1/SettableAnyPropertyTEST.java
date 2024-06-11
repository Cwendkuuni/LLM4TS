import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty settableProperty;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 Object pojo = new MyPojo();
 settableProperty = mapper.findSpecialProperty("anyProperty", pojo.getClass(), mapper.getConfiguration());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John Doe\", \"age\":42}");
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 assertEquals("John Doe", pojo.getProperty().get(0).getName());
 assertEquals(42, pojo.getProperty().get(0).getValue().hashCode());
 }

 @Test
 public void testDeserializeAndSetNullValue() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"anyProperty\":null}");
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 assertNull(pojo.getProperty().get(0).getValue());
 }

 @Test
 public void testDeserializeAndSetEmptyObject() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"anyProperty\":{}}");
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 assertTrue(pojo.getProperty().isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetInvalidType() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"anyProperty\":42}");
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 fail("Expected JsonMappingException but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeAndSetInvalidProperty() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"invalidProperty\":42}");
 settableProperty = mapper.findSpecialProperty("invalidProperty", pojo.getClass(), mapper.getConfiguration());
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "invalidProperty");
 fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetIOException() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("{\"anyProperty\":[1, 2, 3]}");
 ObjectReader reader = mapper.readerForUpdating(pojo.getClass());
 reader.accept(parser);
 settableProperty = mapper.findSpecialProperty("anyProperty", pojo.getClass(), mapper.getConfiguration());
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 fail("Expected IOException but got none.");
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 settableProperty = settableProperty.withValueDeserializer(new StdSerializer<>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "Test";
 }
 });
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John Doe\", \"anyProperty\":\"Test\"}");
 settableProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "anyProperty");
 assertEquals("Test", pojo.getProperty().get(0).getValue());
 }
}

class MyPojo {
 private Map<Object, Object> property = new java.util.HashMap<>();

 public Map<Object, Object> getProperty() { return property; }
}