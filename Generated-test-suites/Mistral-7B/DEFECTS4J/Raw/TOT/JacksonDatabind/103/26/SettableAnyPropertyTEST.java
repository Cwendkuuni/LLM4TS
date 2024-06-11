import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper objectMapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 settableAnyProperty = new SettableAnyProperty(new BeanProperty(new AnnotatedField(Object.class, "anyProperty")),
 new AnnotatedField(Object.class, "anyProperty"),
 JavaType.constructUncheckedType(Object.class),
 null,
 null,
 null);
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("[{\"anyProperty\":{\"value\":1}}]");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, "anyProperty");
 assertEquals(1, pojo.getClass().getField("anyProperty").get(pojo));
 }

 @Test
 public void testSettableAnyPropertyWithoutValueDeserializer() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("[{\"anyProperty\":null}]");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, "anyProperty");
 assertNull(pojo.getClass().getField("anyProperty").get(pojo));
 }

 @Test
 public void testSettableAnyPropertyWithInvalidJson() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("[{\"anyProperty\":1}]"); // missing value
 settableAnyProperty.deserializeAndSet(parser, null, pojo, "anyProperty");
 assertNull(pojo.getClass().getField("anyProperty").get(pojo));
 }

 @Test
 public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
 Object pojo = new Object();
 SettableAnyProperty settableAnyPropertyWithKeyDeserializer = new SettableAnyProperty(new BeanProperty(new AnnotatedField(Object.class, "anyProperty")),
 new AnnotatedField(Object.class, "anyProperty"),
 JavaType.constructUncheckedType(Map.class, String.class, Object.class),
 new SimpleKeyDeserializer(),
 null,
 null);
 ObjectMapper objectMapperWithKeyDeserializer = new ObjectMapper();
 objectMapperWithKeyDeserializer.registerKeyDeserializer(String.class, new SimpleKeyDeserializer());
 ObjectMapperContext ctxt = objectMapperWithKeyDeserializer.treeAsTokens(parser);
 settableAnyPropertyWithKeyDeserializer.deserializeAndSet(parser, ctxt, pojo, "anyProperty");
 Map<String, Object> map = (Map<String, Object>) pojo.getClass().getField("anyProperty").get(pojo);
 assertEquals("1", map.get("1"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithInvalidType() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("[{\"anyProperty\":\"string\"}]");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, "anyProperty");
 fail("Expected JsonMappingException to be thrown");
 }
}