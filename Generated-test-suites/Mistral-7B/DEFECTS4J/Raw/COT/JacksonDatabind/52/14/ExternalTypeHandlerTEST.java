import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.util.TestUtil.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonFactory jsonFactory;
 private ObjectMapper mapper;

 @Before
 public void setup() throws IOException {
 jsonFactory = new JsonFactory();
 mapper = new ObjectMapper(jsonFactory);
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "value1");
 node.put("typeId1", "Type1");
 node.put("prop2", "value2");
 handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), null)
 .addExternal(new SettableBeanProperty("typeId1", Type1.class), new TypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Type1();
 }

 @Override
 public TypeDeserializer createContextualTypeDeserializer(DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
 return null;
 }

 @Override
 public String getPropertyName() {
 return "typeId1";
 }
 })
 .build());
 handler.start();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 JsonParser parser = jsonFactory.createParser(node);
 handler.handleTypePropertyValue(parser, null, "typeId1", new Object());
 assertEquals("Type1", handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_invalidProperty() throws IOException {
 JsonParser parser = jsonFactory.createParser(node);
 handler.handlePropertyValue(parser, null, "invalidProp", new Object());
 fail("Expected IOException");
 }

 @Test
 public void testHandlePropertyValue_validProperty() throws IOException {
 JsonParser parser = jsonFactory.createParser(node);
 handler.handlePropertyValue(parser, null, "prop1", new Object());
 assertEquals("value1", handler._tokens[0].getText());
 }

 @Test
 public void testComplete_validTypeIds() throws IOException {
 JsonParser parser = jsonFactory.createParser(node);
 Object result = handler.complete(parser, null, new Object());
 assertEquals("value1", result.getClass().getField("prop1").get(result));
 assertEquals("Type1", result.getClass().getField("typeId1").get(result).getClass().getName());
 }

 @Test
 public void testComplete_missingTypeIds() throws IOException {
 JsonFactory jsonFactory2 = new JsonFactory();
 ObjectMapper mapper2 = new ObjectMapper(jsonFactory2);
 ObjectNode node2 = mapper2.createObjectNode();
 node2.put("prop1", "value1");
 handler.start();
 handler.handlePropertyValue(jsonFactory2.createParser(node2), null, "prop1", new Object());
 handler.handlePropertyValue(jsonFactory2.createParser(node2), null, "typeId1", new Object());
 handler.handlePropertyValue(jsonFactory2.createParser(node2), null, "prop2", new Object());
 Object result = handler.complete(jsonFactory2.createParser(node2), null, new Object());
 assertEquals("value1", result.getClass().getField("prop1").get(result));
 assertEquals(null, result.getClass().getField("typeId1").get(result));
 assertEquals("value2", result.getClass().getField("prop2").get(result));
 }

 @Test(expected = IOException.class)
 public void testComplete_missingProperties() throws IOException {
 JsonFactory jsonFactory3 = new JsonFactory();
 ObjectMapper mapper3 = new ObjectMapper(jsonFactory3);
 ObjectNode node3 = mapper3.createObjectNode();
 node3.put("prop1", "value1");
 node3.put("typeId1", "Type1");
 handler.start();
 handler.handlePropertyValue(jsonFactory3.createParser(node3), null, "prop1", new Object());
 handler.handlePropertyValue(jsonFactory3.createParser(node3), null, "typeId1", new Object());handler.complete(jsonFactory3.createParser(node3), null, new Object());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testComplete_invalidTypeId() throws IOException {
 JsonFactory jsonFactory4 = new JsonFactory();
 ObjectMapper mapper4 = new ObjectMapper(jsonFactory4);
 ObjectNode node4 = mapper4.createObjectNode();
 node4.put("prop1", "value1");
 node4.put("typeId1", "invalidType");
 handler.start();
 handler.handlePropertyValue(jsonFactory4.createParser(node4), null, "prop1", new Object());
 handler.handlePropertyValue(jsonFactory4.createParser(node4), null, "typeId1", new Object());
 handler.complete(jsonFactory4.createParser(node4), null, new Object());
 fail("Expected IOException");
 }

 @Test
 public void testBuilder() throws IOException {
 ExternalTypeHandler handler2 = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), null)
 .addExternal(new SettableBeanProperty("typeId1", Type1.class), new TypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Type1();
 }

 @Override
 public TypeDeserializer createContextualTypeDeserializer(DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
 return null;
 }

 @Override
 public String getPropertyName() {
 return "typeId1";
 }
 })
 .build());
 assertEquals(handler._properties.size(), handler2._properties.size());
 }

 @Test
 public void testExtTypedProperty() throws IOException {
 ExternalTypeHandler.ExtTypedProperty prop1 = handler._properties[0];
 ExternalTypeHandler.ExtTypedProperty propType1 = handler._properties[0].getTypeProperty();
 assertEquals("prop1", prop1.getProperty().getName());
 assertEquals("Type1", propType1.getTypePropertyName());
 assertEquals(String.class, prop1.getProperty().getType());
 assertEquals(Type1.class, propType1.getType());
 }
}