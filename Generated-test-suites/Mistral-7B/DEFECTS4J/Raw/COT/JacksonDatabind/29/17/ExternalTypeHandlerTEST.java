import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectNode testNode;
 private DeserializationContext context;
 private JsonParser parser;

 @Before
 public void setup() {
 testNode = ObjectMapper.getInstance().createObjectNode();
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String propName = "typeProperty";
 String typeId = "TypeId";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop, typeDeserializer)
 .build());
 handler.start();

 ArgumentCaptor<String> propNameCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<ObjectNode> nodeCaptor = ArgumentCaptor.forClass(ObjectNode.class);
 ArgumentCaptor<TypeDeserializer> typeDeserializerCaptor = ArgumentCaptor.forClass(TypeDeserializer.class);
 ArgumentCaptor<Object> beanCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<String> typeIdCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> indexCaptor2 = ArgumentCaptor.forClass(Integer.class);

 Mockito.doAnswer(invocation -> {
 prop.set(invocation.getArgument(3), invocation.getArgument(1));
 return null;
 }).when(prop).set(Mockito.any(Object.class), Mockito.any());

 Mockito.doReturn(typeDeserializer).when(typeDeserializer).getTypeIdResolver();

 handler.handleTypePropertyValue(parser, context, propName, null);

 verify(typeDeserializer).deserializeTypedFromObject(testNode, context, typeDeserializerCaptor.capture());
 Object deserializedType = typeDeserializerCaptor.getValue();
 assertEquals(typeId, deserializedType.getClass().getName());

 verify(beanDeserializer).deserializeFromObject(parser, context, beanCaptor.capture());
 Object bean = beanCaptor.getValue();
 assertEquals(typeId, bean.getClass().getName());

 verify(context).handleMissingValue(Mockito.eq(prop), Mockito.eq(bean), Mockito.eq(propName), typeIdCaptor.capture());
 assertEquals(typeId, typeIdCaptor.getValue());

 verify(context).handleUnexpectedValue(parser, Mockito.eq(parser.getCurrentLocation()), Mockito.eq(parser.getCurrentName()),
 Mockito.eq(parser.getCurrentToken()), Mockito.eq(typeId), Mockito.eq(null), Mockito.eq(prop), Mockito.eq(bean),
 Mockito.eq(propName), Mockito.anyInt());
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String propName = "property";
 String typeId = "TypeId";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop, null)
 .build());
 handler.start();
 handler.handlePropertyValue(parser, context, propName, null);
 verify(parser).nextToken();
 verify(parser).skipChildren();
 assertNull(handler._typeIds);
 assertNull(handler._tokens);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeIdProperty() throws Exception {
 String propName = "missingTypeIdProperty";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop, null)
 .build());
 handler.start();
 handler.handleTypePropertyValue(parser, context, propName, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeIdAndProperty() throws Exception {
 String propName = "missingTypeIdAndProperty";
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .build()
 );
 handler.start();
 handler.handleTypePropertyValue(parser, context, propName, null);
 }

 @Test
 public void testHandlePropertyValueWithDefaultType() throws Exception {
 String propName = "defaultTypeProperty";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop, null)
 .build()
 );
 handler.start();
 handler.handleTypePropertyValue(parser, context, propName, null);

 Mockito.verify(context).handleMissingValue(Mockito.eq(prop), Mockito.any(Object.class), Mockito.eq(propName),
 Mockito.eq(ExternalTypeHandler.class.getName()));
 }

 @Test
 public void testComplete() throws Exception {
 String propName1 = "property1";
 String propName2 = "property2";
 SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
 SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop1, null)
 .addExternal(prop2, null)
 .build());
 handler.start();

 handler.handleTypePropertyValue(parser, context, propName1, null);
 handler.handleTypePropertyValue(parser, context, propName2, null);

 handler.complete(parser, context, null);

 verify(prop1).set(Mockito.any(Object.class), Mockito.eq(parser.readValueAs(prop1.getType())));
 verify(prop2).set(Mockito.any(Object.class), Mockito.eq(parser.readValueAs(prop2.getType())));
 }

 @Test
 public void testCompleteWithCreator() throws Exception {
 String propName1 = "property1";
 String propName2 = "property2";
 SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
 SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
 PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(prop1, null)
 .addExternal(prop2, null)
 .build());
 handler.start();

 handler.handleTypePropertyValue(parser, context, propName1, null);
 handler.handleTypePropertyValue(parser, context, propName2, null);

 ObjectNode node = testNode.objectNode();
 node.put("property1", "value1");
 node.put("property2", "value2");
 handler.complete(parser, context, node, creator);

 verify(creator).build(context, Mockito.any(PropertyValueBuffer.class));
 verify(prop1).set(Mockito.any(Object.class), Mockito.eq("value1"));
 verify(prop2).set(Mockito.any(Object.class), Mockito.eq("value2"));
 }
}