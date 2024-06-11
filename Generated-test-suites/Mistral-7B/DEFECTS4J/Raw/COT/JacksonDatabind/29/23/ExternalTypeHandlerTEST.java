import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testBean;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws JsonProcessingException {
 JsonParser mockParser = mock(JsonParser.class);
 ObjectMapper mapper = new ObjectMapper();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(mock(SettableBeanProperty.class, "testProp"), mock(TypeDeserializer.class))
 .build());
 ObjectWriter writer = mapper.writer();
 testBean = mapper.readValue(writer.writeValueAsString(new Object()), Object.class);
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 when(mockParser.getText()).thenReturn(typeId);

 assertTrue(handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean));
 assertEquals(typeId, handler._typeIds[0]);
 }

 @Test(expected = AssertionError.class)
 public void testHandleTypePropertyValue_propertyNotFound() throws IOException {
 String propName = "nonexistentProp";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);

 assertFalse(handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean));
 }

 @Test(expected = AssertionError.class)
 public void testHandleTypePropertyValue_invalidTypePropertyName() throws IOException {
 String propName = "testProp";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 when(mockParser.getText()).thenReturn("invalidTypePropertyName");

 assertFalse(handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean));
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 when(mockParser.getText()).thenReturn(typeId);

 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];
 assertTrue(handler.handlePropertyValue(mockParser, mockContext, propName, testBean));
 assertEquals(typeId, handler._typeIds[0]);
 }

 @Test(expected = AssertionError.class)
 public void testHandlePropertyValue_propertyNotFound() throws IOException {
 String propName = "nonexistentProp";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);

 assertFalse(handler.handlePropertyValue(mockParser, mockContext, propName, testBean));
 }

 @Test(expected = AssertionError.class)
 public void testHandlePropertyValue_invalidTokenBuffer() throws IOException {
 String propName = "testProp";
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 when(mockParser.getText()).thenReturn("invalidTypePropertyName");

 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];
 assertFalse(handler.handlePropertyValue(mockParser, mockContext, propName, testBean));
 }

 @Test
 public void testComplete() throws IOException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object result = handler.complete(mockParser, mockContext, testBean);
 assertNotNull(result);
 }

 @Test(expected = AssertionError.class)
 public void testComplete_missingTypeId() throws IOException {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockContext = mock(DeserializationContext.class);

 assertFalse(handler.handleTypePropertyValue(mockParser, mockContext, "testProp", testBean));
 assertFalse(handler.handlePropertyValue(mockParser, mockContext, "testProp", testBean));
 assertNull(handler.complete(mockParser, mockContext, testBean));
 }

 @Test(expected = AssertionError.class)public void testComplete_missingProperty() throws IOException { SettableBeanProperty mockProp = mock(SettableBeanProperty.class); String propName = mockProp.getName(); assertFalse(handler.handleTypePropertyValue(mockParser, mockContext, propName, testBean)); assertFalse(handler.handlePropertyValue(mockParser, mockContext, propName, testBean)); JsonParser mockParser2 = mock(JsonParser.class); when(mockParser2.nextToken()).thenReturn(JsonToken.START_OBJECT); Object result = handler.complete(mockParser2, mockContext, testBean); assertNull(result); }

 @SuppressWarnings("unchecked") @Test public void testComplete_withCreator() throws IOException { PropertyBasedCreator creator = mock(PropertyBasedCreator.class); JsonParser mockParser = mock(JsonParser.class); DeserializationContext mockContext = mock(DeserializationContext.class); PropertyValueBuffer buffer = mock(PropertyValueBuffer.class); Object result = handler.complete(mockParser, mockContext, buffer, creator); assertNotNull(result); }

}