import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;

 @Before
 public void setUp() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 }

 @Test
 public void testHandlePropertyValueNormalCase() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.getText()).thenReturn("type1");
 handler.handlePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(2)).getText();
 verify(mockParser, times(1)).nextToken();
 assertEquals("type1", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValueMissingProperty() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("missingProp");
 handler.handlePropertyValue(mockParser, mockContext, "missingProp", mockBean);
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValueMissingTypeId() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 handler.handlePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValueNormalCase() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.getText()).thenReturn("type1");
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(2)).getText();
 verify(mockParser, times(1)).nextToken();
 assertEquals("type1", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValueMissingProperty() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("missingProp");
 handler.handleTypePropertyValue(mockParser, mockContext, "missingProp", mockBean);
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValueMissingTypeId() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testCompleteNormalCase() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop2");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_INT);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 verify(mockParser, times(1)).nextToken();
 assertEquals("value1", handler.getProperty(0).get(mockBean));
 assertEquals(1, (int) handler.getProperty(1).get(mockBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteMissingProperty() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("missingProp");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testCompleteMissingTypeId() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop2");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test
 public void testCompleteNullValue() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop2");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_INT);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 assertNull(handler.getProperty(0).get(mockBean));
 assertEquals(0, (int) handler.getProperty(1).get(mockBean));
 }

 @Test
 public void testCompleteMultipleNullValues() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop2");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop3");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_INT);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 assertNull(handler.getProperty(0).get(mockBean));
 assertNull(handler.getProperty(1).get(mockBean));
 assertEquals(0, (int) handler.getProperty(2).get(mockBean));
 }

 @Test
 public void testCompleteMultipleValues() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop1");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("prop2");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_INT);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.complete(mockParser, mockContext, mockBean);
 assertEquals("value1", handler.getProperty(0).get(mockBean));
 assertEquals(1, (int) handler.getProperty(1).get(mockBean));
 }
}

class StringTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getText();
 }
}

class IntegerTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.nextIntValue();
 }
}