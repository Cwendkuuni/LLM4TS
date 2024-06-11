import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private Object createTestBean() {
 return new Object() {};
 }

 @Test
 public void testStart() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 ExternalTypeHandler startedHandler = externalTypeHandler.start();
 assertTrue(startedHandler instanceof ExternalTypeHandler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartWithNullProperties() {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartWithNullNameToPropertyIndex() {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build(), null);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.setText(typeId);
 mockParser.nextToken(); // TEXT_VALUE
 Integer I = new Integer(0);
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean);
 assertTrue(result);
 }

 @Test
 public void testHandleTypePropertyValueWithNullBean() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 String propName = "testProp";
 String typeId = "testTypeId";
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 mockParser.setText(typeId);
 mockParser.nextToken(); // TEXT_VALUE
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handleTypePropertyValue(mockParser, mockContext, propName, null);
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValueWithNullTypeId() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 mockParser.nextValue(); // NULL
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean);
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValueWithNullPropertyIndex() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.setText(typeId);
 mockParser.nextToken(); // TEXT_VALUE
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler, null, new String[0], new TokenBuffer[0]);
 boolean result = handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText(typeId);
 mockParser.nextToken(); // END_OBJECT
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handlePropertyValue(mockParser, mockContext, propName, testBean);
 assertTrue(result);
 }

 @Test
 public void testHandlePropertyValueWithNotNullTokens() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText(typeId);
 mockParser.nextToken(); // END_OBJECT
 mockParser.nextToken(); // END_OBJECT
 mockParser.nextToken(); // START_ARRAY
 mockParser.nextToken(); // END_ARRAY
 mockParser.nextToken(); // NULL
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handlePropertyValue(mockParser, mockContext, propName, testBean);
 assertTrue(result);
 }

 @Test
 public void testHandlePropertyValueWithNullBean() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText(typeId);
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handlePropertyValue(mockParser, mockContext, propName, null);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueWithNullTypeId() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.nextToken(); // NULL
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 boolean result = handler.handlePropertyValue(mockParser, mockContext, propName, testBean);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueWithNullPropertyIndex() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 String typeId = "testTypeId";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText(typeId);
 mockParser.nextToken(); // END_OBJECT
 mockParser.nextToken(); // START_ARRAY
 mockParser.nextToken(); // END_ARRAY
 mockParser.nextToken(); // NULL
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler, null, new String[0], new TokenBuffer[0]);
 boolean result = handler.handlePropertyValue(mockParser, mockContext, propName, testBean);
 assertFalse(result);
 }

 @Test
 public void testComplete() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp1");
 mockParser.setText("typeId1");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp2");
 mockParser.setText("typeId2");
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 Object result = handler.complete(mockParser, mockContext, testBean);
 assertNotNull(result);
 }

 @Test
 public void testCompleteWithNullParser() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(null, mockContext, testBean);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithNullContext() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 Object testBean = createTestBean();
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(mockParser, null, testBean);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithNullBean() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(mockParser, mockContext, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithNoMissingProperties() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 Object testBean = createTestBean();
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp1");
 mockParser.setText("typeId1");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp2");
 mockParser.setText("typeId2");
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 Object result = handler.complete(mockParser, mockContext, testBean);
 assertNotNull(result);
 }

 @Test
 public void testCompleteWithMissingTypeId() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 Object testBean = createTestBean();
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp1");
 mockParser.setText("typeId1");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp2");
 mockParser.nextToken(); // END_OBJECT
 mockParser.nextToken(); // START_ARRAY
 mockParser.nextToken(); // END_ARRAY
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(mockParser, mockContext, testBean);
 fail("Expected MappingException");
 } catch (MappingException e) {
 // Expected
 assertTrue(e.getMessage().contains("Missing external type id property 'testProp2'"));
 }
 }

 @Test
 public void testCompleteWithMissingProperty() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 Object testBean = createTestBean();
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp1");
 mockParser.setText("typeId1");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.nextToken(); // NULL
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp3");
 mockParser.setText("typeId3");
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(mockParser, mockContext, testBean);
 fail("Expected MappingException");
 } catch (MappingException e) {
 // Expected
 assertTrue(e.getMessage().contains("Missing property 'testProp2' for external type id 'testProp1'"));
 }
 }

 @Test
 public void testCompleteWithMissingTypeProperty() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 Object testBean = createTestBean();
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp1");
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.nextToken(); // NULL
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText("testProp3");
 mockParser.setText("typeId3");
 mockParser.nextToken(); // END_OBJECT
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler);
 try {
 handler.complete(mockParser, mockContext, testBean);
 fail("Expected MappingException");
 } catch (MappingException e) {
 // Expected
 assertTrue(e.getMessage().contains("Missing type property name 'typeId2' for property 'testProp1'"));
 }
 }

 @Test
 public void testCompleteWithMissingDefaultType() throws Exception {
 ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 Object testBean = createTestBean();
 JsonParser mockParser = new JsonParser();
 DeserializationContext mockContext = new MinimalDeserializationContext();
 String propName = "testProp";
 mockParser.nextToken(); // START_OBJECT
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.setText(propName);
 mockParser.nextToken(); // PROPERTY_NAME
 mockParser.nextToken(); // NULL
 mockParser.nextToken(); // END_OBJECT
 TypeDeserializer mockTypeDeserializer = new MockTypeDeserializer();
 ExternalTypeHandler handler = new ExternalTypeHandler(externalTypeHandler, new ExternalTypeHandler.Builder().addExternal(new SettableBeanProperty(propName, mockTypeDeserializer), mockTypeDeserializer).build());
 try {
 handler.complete(mockParser, mockContext, testBean);
 fail("Expected MappingException");
 } catch (MappingException e) {
 // Expected
 assertTrue(e.getMessage().contains("Missing external type id property '"));
 }
 }
}