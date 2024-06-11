import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for {@link ExternalTypeHandler} class
 */
public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);

 handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
 new SettableBeanProperty(new SimpleBean(), "prop1"), new StringTypeDeserializer())},
 new HashMap<>(), null, null);
 }

 /**
 * Test handling of a valid type id property
 */
 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String typeId = "StringType";
 when(mockParser.getText()).thenReturn(typeId);
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(1)).nextToken();
 verify(mockContext, times(1)).handleUnknownProperty("prop1", mockBean);
 }

 /**
 * Test handling of a missing type id property
 */
 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissing() throws IOException {
 when(mockParser.getText()).thenReturn("");
 handler.handleTypePropertyValue(mockParser, mockContext, "prop1", mockBean);
 }

 /**
 * Test handling of a property that is not an external type id
 */
 @Test
 public void testHandlePropertyValue() throws IOException {
 String propName = "prop1";
 String value = "{\"name\":\"John\", \"age\":30}";
 when(mockParser.getText()).thenReturn(propName);
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn("name");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("John");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);
 verify(mockParser, times(1)).nextToken();
 verifyNoMoreInteractions(mockContext);
 }

 /**
 * Test handling of a missing property value
 */
 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissing() throws IOException {
 handler.handlePropertyValue(mockParser, mockContext, "prop1", mockBean);
 }

 /**
 * Test handling of a property value that is an external type id
 */
 @Test
 public void testHandlePropertyValueTypeId() throws IOException {
 String typeId = "StringType";
 when(mockParser.getText()).thenReturn(typeId);
 handler.handlePropertyValue(mockParser, mockContext, "prop1", mockBean);
 verify(mockParser, times(1)).nextToken();
 verify(mockContext, times(1)).handleUnknownProperty("prop1", mockBean);
 }

 /**
 * Test handling of a missing property and a missing type id
 */
 @Test(expected = IOException.class)
 public void testCompleteMissingPropertyAndTypeId() throws IOException {
 handler.complete(mockParser, mockContext, mockBean);
 }

 /**
 * Test handling of a missing property and a present type id
 */
 @Test(expected = IOException.class)
 public void testCompleteMissingPropertyAndPresentTypeId() throws IOException {
 String typeId = "StringType";
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn(typeId);
 handler.complete(mockParser, mockContext, mockBean);
 }

 /**
 * Test handling of a present property and a missing type id
 */
 @Test(expected = IOException.class)
 public void testCompletePresentPropertyAndMissingTypeId() throws IOException {
 String propName = "prop1";
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn(propName);
 handler.complete(mockParser, mockContext, mockBean);
 }

 /**
 * Test handling of a present property and a present type id
 */
 @Test
 public void testComplete() throws IOException {
 String typeId = "StringType";
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn(typeId);
 handler.complete(mockParser, mockContext, mockBean);
 verify(mockParser, times(1)).nextToken();
 verify(mockContext, times(1)).handleUnknownProperty("prop1", mockBean);
 }

 /**
 * Test handling of a missing default type for a property
 */
 @Test(expected = IOException.class)
 public void testCompleteMissingDefaultType() throws IOException {
 String propName = "prop1";
 handler.complete(mockParser, mockContext, mockBean);
 }

 /**
 * Test handling of a property with a null value
 */
 @Test
 public void testCompleteNullValue() throws IOException {
 handler.complete(mockParser, mockContext, mockBean);
 }

 /**
 * Test handling of a property with a valid value deserialized using a custom type deserializer
 */
 @Test
 public void testCompleteCustomTypeDeserializer() throws IOException {
 String propName = "prop1";
 TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
 SettableBeanProperty mockProperty = mock(SettableBeanProperty.class);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getText()).thenReturn(propName);
 when(mockTypeDeserializer.deserialize(mockParser, mockContext)).thenReturn("Value");
 handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
 mockProperty, mockTypeDeserializer)}, new HashMap<>(), null, null);
 handler.complete(mockParser, mockContext, mockBean);
 verify(mockParser, times(1)).nextToken();
 verify(mockContext, times(1)).handleUnknownProperty("prop1", mockBean);
 verify(mockTypeDeserializer, times(1)).deserialize(mockParser, mockContext);
 }
}