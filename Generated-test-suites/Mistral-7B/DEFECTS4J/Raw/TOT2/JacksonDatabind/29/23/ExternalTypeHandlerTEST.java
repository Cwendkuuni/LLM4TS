import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ObjectMapper objectMapper;
 private ExternalTypeHandler handler;
 private DeserializationContext context;
 private JsonParser jsonParser;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 context = objectMapper.getDeserializationContext();
 jsonParser = objectMapper.getFactory().createParser("{}");
 }

 @Test
 public void testHandleTypePropertyValue_ValidTypeId() throws Exception {
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(true);
 when(prop.getTypePropertyName()).thenReturn("testName");
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 assertEquals("test", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_InvalidTypeId() throws Exception {
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(false);
 when(prop.getTypePropertyName()).thenReturn("testName");
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_ValidValue() throws Exception {
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(false);
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handlePropertyValue(jsonParser, context, "test", null);
 assertNull(handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_MissingProperty() throws Exception {
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(false);
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handlePropertyValue(jsonParser, context, "missingProperty", null);
 }

 @Test
 public void testComplete_ValidTypeIds() throws Exception {
 String json = "{\"test\":\"value\"}";
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.hasTypePropertyName("test")).thenReturn(true);
 when(prop1.getTypePropertyName()).thenReturn("testName1");
 when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.hasTypePropertyName("test2")).thenReturn(true);
 when(prop2.getTypePropertyName()).thenReturn("testName2");
 when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 handler.handlePropertyValue(jsonParser, context, "test2", null);
 jsonParser.nextToken();

 Object result = handler.complete(jsonParser, context, null);
 assertEquals(2, result.getClass().getDeclaredFields().length);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeId1() throws Exception {
 String json = "{\"test\":\"value\"}";
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.hasTypePropertyName("test")).thenReturn(true);
 when(prop1.getTypePropertyName()).thenReturn("testName1");
 when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.hasTypePropertyName("test2")).thenReturn(true);
 when(prop2.getTypePropertyName()).thenReturn("testName2");
 when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 handler.handlePropertyValue(jsonParser, context, "test2", null);
 jsonParser.nextToken();
 handler.handlePropertyValue(jsonParser, context, "test", null); // missing _typeIds[1]

 handler.complete(jsonParser, context, null);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeId2() throws Exception {
 String json = "{\"test\":\"value\"}";
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.hasTypePropertyName("test")).thenReturn(true);
 when(prop1.getTypePropertyName()).thenReturn("testName1");
 when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.hasTypePropertyName("test2")).thenReturn(true);
 when(prop2.getTypePropertyName()).thenReturn("testName2");
 when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 handler.handlePropertyValue(jsonParser, context, "test2", null);
 jsonParser.nextToken();
 handler.handleTypePropertyValue(jsonParser, context, "test", null); // missing _typeIds[1]
 handler._typeIds[1] = "typeId2"; // set typeId manually

 handler.complete(jsonParser, context, null);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws Exception {
 String json = "{\"test\":\"value\"}";
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.hasTypePropertyName("test")).thenReturn(true);
 when(prop1.getTypePropertyName()).thenReturn("testName1");
 when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.hasTypePropertyName("test2")).thenReturn(true);
 when(prop2.getTypePropertyName()).thenReturn("testName2");
 when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 handler.handlePropertyValue(jsonParser, context, "test2", null);
 jsonParser.nextToken();
 handler.handlePropertyValue(jsonParser, context, "test", null); // missing _tokens[1]
 handler._typeIds[1] = "typeId2"; // set typeId manually
 handler._tokens[1] = new TokenBuffer(jsonParser); // set token buffer manually

 handler.complete(jsonParser, context, null);
 }

 @Test
 public void testComplete_NullProperties() throws Exception {
 String json = "{\"test\":\"value\"}";
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.hasTypePropertyName("test")).thenReturn(true);
 when(prop1.getTypePropertyName()).thenReturn("testName1");
 when(prop1.getProperty()).thenReturn(null);

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.hasTypePropertyName("test2")).thenReturn(true);
 when(prop2.getTypePropertyName()).thenReturn("testName2");
 when(prop2.getProperty()).thenReturn(null);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop1, prop2 }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 handler.handlePropertyValue(jsonParser, context, "test2", null);
 jsonParser.nextToken();

 Object result = handler.complete(jsonParser, context, null);
 assertNull(result);
 }

 @Test
 public void testComplete_NullValue() throws Exception {
 String json = "{\"test\":null}";
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(true);
 when(prop.getTypePropertyName()).thenReturn("testName");
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();

 Object result = handler.complete(jsonParser, context, null);
 assertNull(result);
 }

 @Test
 public void testComplete_DefaultValue() throws Exception {
 String json = "{\"test\":null}";
 ExtTypedProperty prop = mock(ExtTypedProperty.class);
 when(prop.hasTypePropertyName("test")).thenReturn(true);
 when(prop.getTypePropertyName()).thenReturn("testName");
 when(prop.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { prop }, null, null, null);
 handler.start();

 handler.handleTypePropertyValue(jsonParser, context, "test", null);
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();

 Object result = handler.complete(jsonParser, context, null);
 // default value should be returned if no type id is present
 assertEquals(null, result);
 }

 @Test
 public void testComplete_Builder() throws Exception {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.getPropertyName()).thenReturn("testProperty");
 when(typeDeserializer.getDefaultImpl()).thenReturn(Integer.class);
 TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
 when(typeIdResolver.idFromValueAndType(null, Integer.class)).thenReturn("intId");

 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = mock(ObjectMapper.DefaultTypingResolver.class);
 when(defaultTypingResolver.findValueDeserializer(ObjectMapper.DefaultTypingResolver.handleUnknownTypeIds(), Object.class, typeDeserializer)).thenReturn(typeDeserializer);
 when(objectMapper.getFactory().getTypingResolver()).thenReturn(defaultTypingResolver);
 when(typeDeserializer.getPropertyName()).thenReturn("testProperty");
 when(typeDeserializer.getDefaultImpl()).thenReturn(Integer.class);
 when(typeDeserializer.getTypeIdResolver()).thenReturn(typeIdResolver);

 JsonFactory factory = objectMapper.getFactory();
 JsonParser parser = factory.createParser("{\"testProperty\":1}");
 JsonToken token = parser.nextToken();

 ExternalTypeHandler handler = new ExternalTypeHandler.Builder()
 .addExternal(mock(SettableBeanProperty.class), typeDeserializer)
 .build()
 .start();

 handler.handlePropertyValue(parser, context, "testProperty", null);
 Object result = handler.complete(parser, context, null);
 assertEquals(1, result.getClass().getDeclaredFields()[0].get(result));
 }

}