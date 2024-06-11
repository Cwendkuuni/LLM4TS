import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private SettableBeanProperty mockProperty;
 private TypeDeserializer mockTypeDeserializer;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockProperty = mock(SettableBeanProperty.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);

 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(mockProperty, mockTypeDeserializer)
 .build());
 }

 @Test
 public void testStart() throws IOException {
 handler.start();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockBean != null).thenReturn(true);
 when(mockTokens(0).getText()).thenReturn(null);

 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 verify(mockTypeDeserializer).deserialize(eq(mockParser), eq(mockContext), anyObject());

 when(mockTokens(0).getText()).thenReturn("typeId1");
 when(mockBean != null).thenReturn(false);

 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 verify(mockTypeDeserializer, never()).deserialize(eq(mockParser), eq(mockContext), anyObject());
 }

 @Test
 public void testHandlePropertyValueWithNullValue() throws IOException {
 when(mockParser.getText()).thenReturn(null);

 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 verifyZeroInteractions(mockParser, mockContext, mockTypeDeserializer);
 }

 @Test
 public void testHandlePropertyValueWithNonEmptyValue() throws IOException {
 when(mockParser.getText()).thenReturn("value1");
 when(mockBean != null).thenReturn(true);
 when(mockTokens(0).getText()).thenReturn(null);

 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 verifyZeroInteractions(mockParser, mockContext, mockTypeDeserializer);
 }

 @Test
 public void testHandlePropertyValueWithNonEmptyValueAndTypeId() throws IOException {
 when(mockParser.getText()).thenReturn("value1");
 when(mockBean != null).thenReturn(true);
 when(mockTokens(0).getText()).thenReturn("typeId1");

 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 verify(mockTypeDeserializer).deserialize(eq(mockParser), eq(mockContext), anyObject());
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingProperty() throws IOException {
 when(mockParser.getText()).thenReturn("value1");
 when(mockBean != null).thenReturn(true);
 when(mockTokens(0).getText()).thenReturn(null);
 when(mockTypeDeserializer.deserialize(eq(mockParser), eq(mockContext), anyObject()))
 .thenThrow(new IOException("test"));

 handler.handlePropertyValue(mockParser, mockContext, "missingPropertyName", mockBean);
 verify(mockParser).skipChildren();
 }

 @Test
 public void testCompleteWithNullBean() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.complete(mockParser, mockContext, null);
 }

 @Test
 public void testCompleteWithNonEmptyBean() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 Object result = handler.complete(mockParser, mockContext, mockBean);
 assertEquals(mockBean, result);
 }

 @Test
 public void testCompleteWithTypeIdMissing() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.handleTypePropertyValue(mockParser, mockContext, "missingPropertyName", mockBean);
 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingProperty() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.complete(mockParser, mockContext, mockBean);
 SettableBeanProperty property = mockProperty;
 doAnswer(invocationOnMock -> {
 throw new IOException("Missing property " + property.getName());
 }).when(mockContext).reportMappingException(eq("Missing property '"), anyString());
 handler.handlePropertyValue(mockParser, mockContext, "missingPropertyName", mockBean);
 }

 @Test
 public void testCompleteWithNullTypeId() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName3", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName4", mockBean);
 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test
 public void testCompleteWithValidTypeId() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName3", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName4", mockBean);
 Object result = handler.complete(mockParser, mockContext, mockBean);
 assertEquals(mockBean, result);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeId() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "propertyName1", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName2", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName3", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName4", mockBean);
 handler.complete(mockParser, mockContext, mockBean);
 SettableBeanProperty property = mockProperty;
 doAnswer(invocationOnMock -> {
 throw new IOException("Missing type id for property " + property.getName());
 }).when(mockContext).reportMappingException(eq("Missing type id for property '"), anyString());
 handler.handlePropertyValue(mockParser, mockContext, "missingPropertyName", mockBean);
 }

 private TokenBuffer mockTokens(int index) {
 TokenBuffer tokens = mock(TokenBuffer.class);
 when(tokens.getText()).thenReturn(null);
 when(tokens.asParser(mockParser)).thenReturn(mockParser);
 when(handler._tokens[index]).thenReturn(tokens);
 return tokens;
 }

 private void verifyZeroInteractions(JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) {
 verify(parser, times(0)).nextToken();
 verify(parser, times(0)).skipChildren();
 verify(context, times(0)).reportMappingException(anyString(), anyString());
 verify(typeDeserializer, times(0)).deserialize(parser, context, anyObject());
 }
}