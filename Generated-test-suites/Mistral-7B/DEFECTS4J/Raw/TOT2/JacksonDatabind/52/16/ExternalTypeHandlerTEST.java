import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

public class ExternalTypeHandlerTest {

 private static Object mapper;

 static {
 try {
 mapper = new ObjectMapper();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testStart() throws IOException {
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 ExternalTypeHandler testHandler = handler.start();
 Assert.assertNotNull(testHandler);
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", "type1");
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.handle(p, ctxt);
 Assert.assertEquals(ExternalTypeHandler.class, result.getClass());
 }

 @Test
 public void testHandlePropertyValueWithNullTokenBuffer() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", null);
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.handle(p, ctxt);
 Assert.assertEquals(ExternalTypeHandler.class, result.getClass());
 }

 @Test
 public void testHandlePropertyValueWithMissingProperty() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.handle(p, ctxt);
 Assert.assertEquals(ExternalTypeHandler.class, result.getClass());
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingTypeId() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", "type1");
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 handler.handle(p, ctxt, null, null);
 }

 @Test
 public void testComplete() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", "type1");
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.complete(p, ctxt, null);
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeId() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 handler.complete(p, ctxt, null, null);
 }

 @Test
 public void testCompleteWithNullTypeId() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", null);
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.complete(p, ctxt, null);
 Assert.assertNull(result);
 }

 @Test
 public void testCompleteWithNullBean() throws IOException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("prop1", "type1");
 rootNode.put("prop2", 1);
 JsonParser p = mapper.parserForObject(rootNode);
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(mapper))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer(mapper))
 .build());
 DeserializationContext ctxt = mapper.getContext(new StandardDeserializationContext());
 Object result = handler.complete(p, ctxt, null, null);
 Assert.assertNull(result);
 }
}

class StringTypeDeserializer extends TypeDeserializer {
 private final ObjectMapper mapper;

 public StringTypeDeserializer(ObjectMapper m) {
 this.mapper = m;
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getText();
 }

 @Override
 public TypeDeserializer forProperty(PropertyDescriptor pd) {
 return new StringTypeDeserializer(mapper);
 }

 @Override
 public void resolve(JsonParser p, DeserializationContext ctxt) throws IOException {
 // empty
 }

 @Override
 public void resolveTypes(JsonParser p, DeserializationContext ctxt) throws IOException {
 // empty
 }

 @Override
 public String getPropertyName() {
 return "type1";
 }
}

class IntegerTypeDeserializer extends TypeDeserializer {
 private final ObjectMapper mapper;

 public IntegerTypeDeserializer(ObjectMapper m) {
 this.mapper = m;
 }

 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getIntValue();
 }

 @Override
 public TypeDeserializer forProperty(PropertyDescriptor pd) {
 return new IntegerTypeDeserializer(mapper);
 }

 @Override
 public void resolve(JsonParser p, DeserializationContext ctxt) throws IOException {
 // empty
 }

 @Override
 public void resolveTypes(JsonParser p, DeserializationContext ctxt) throws IOException {
 // empty
 }

 @Override
 public Class<?> getType(DeserializationContext ctxt) {
 return Integer.class;
 }

 @Override
 public String getPropertyName() {
 return "type2";
 }
}