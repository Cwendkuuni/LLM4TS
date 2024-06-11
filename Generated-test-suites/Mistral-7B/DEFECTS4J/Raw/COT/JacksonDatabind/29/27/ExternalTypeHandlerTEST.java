import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private SettableBeanProperty mockProperty;
 private TypeDeserializer mockTypeDeserializer;
 private ExtTypedProperty mockExtTypedProperty;
 private Builder mockBuilder;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockProperty = mock(SettableBeanProperty.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);
 mockExtTypedProperty = mock(ExtTypedProperty.class);
 mockBuilder = mock(Builder.class);
 handler = new ExternalTypeHandler(null, null, null, null);
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 when(mockTypeDeserializer.getPropertyName()).thenReturn("typePropertyName");
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockParser.getText()).thenReturn("typeId");

 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, mockTypeDeserializer);

 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName", mockBean);

 verify(mockParser, times(1)).nextToken();
 verify(mockContext, times(1)).mappingException(anyString());
 verify(mockParser, never()).skipChildren();

 handler._tokens[0] = new TokenBuffer(mockParser);
 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName", mockBean);

 verify(mockParser, times(1)).nextToken();
 verify(mockTypeDeserializer, times(1)).deserialize(mockParser, mockContext);
 }

 @Test
 public void testHandlePropertyValueWithTypeId() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockParser.getText()).thenReturn("typeId");

 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);

 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);

 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).skipChildren();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 }

 @Test
 public void testHandlePropertyValueWithoutTypeId() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);

 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);

 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);

 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).getText();
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingTypePropertyName() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);

 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);
 when(mockParser.getText()).thenReturn("wrongTypePropertyName");

 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 }

 @Test
 public void testHandlePropertyValueWithMissingProperty() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);

 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);

 handler.handlePropertyValue(mockParser, mockContext, "wrongPropertyName", mockBean);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingTokenBuffer() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockParser.getText()).thenReturn("typeId");
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, mockTypeDeserializer);

 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);

 handler._tokens[0] = null;
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingBean() throws IOException {
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockParser.getText()).thenReturn("typeId");
 handler._nameToPropertyIndex.put("propertyName", 0);
 handler._properties[0] = new ExtTypedProperty(mockProperty, mockTypeDeserializer);
 handler._tokens[0] = new TokenBuffer(mockParser);

 handler.handlePropertyValue(mockParser, null, "propertyName", mockBean);
 }

 @Test
 public void testCompleteWithNoMissingData() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockParser.getText()).thenReturn("typeId2");
 when(mockParser.getText()).thenReturn("typeId3");
 when(mockParser.getText()).thenReturn("typeId4");
 when(mockParser.getText()).thenReturn("null");
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.getText()).thenReturn("propertyName1");
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.getText()).thenReturn("propertyName2");
 when(mockParser.getText()).thenReturn("value2");
 when(mockParser.getText()).thenReturn("propertyName3");
 when(mockParser.getText()).thenReturn("value3");
 when(mockParser.getText()).thenReturn("propertyName4");
 when(mockParser.getText()).thenReturn("value4");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(mockParser.getText()).thenReturn(null);

 handler._nameToPropertyIndex.put("propertyName1", 0);
 handler._nameToPropertyIndex.put("propertyName2", 1);
 handler._nameToPropertyIndex.put("propertyName3", 2);
 handler._nameToPropertyIndex.put("propertyName4", 3);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);
 handler._properties[1] = new ExtTypedProperty(mockProperty, null);
 handler._properties[2] = new ExtTypedProperty(mockProperty, null);
 handler._properties[3] = new ExtTypedProperty(mockProperty, null);
 handler._tokens = new TokenBuffer[4];
 handler._typeIds = new String[4];

 handler.complete(mockParser, mockContext, mockBean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockBean, times(4)).getClass();
 }

 @Test
 public void testCompleteWithMissingTypeId() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockParser.getText()).thenReturn("typeId2");
 when(mockParser.getText()).thenReturn("null");
 when(mockParser.getText()).thenReturn("propertyName1");
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.getText()).thenReturn("propertyName2");
 when(mockParser.getText()).thenReturn("value2");
 when(mockParser.getText()).thenReturn("propertyName3");
 when(mockParser.getText()).thenReturn("value3");
 when(mockParser.getText()).thenReturn("propertyName4");
 when(mockParser.getText()).thenReturn("value4");
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.getText()).thenReturn("propertyName1");
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.getText()).thenReturn("propertyName2");
 when(mockParser.getText()).thenReturn("value2");
 when(mockParser.getText()).thenReturn("propertyName3");
 when(mockParser.getText()).thenReturn("value3");
 when(mockParser.getText()).thenReturn("propertyName4");
 when(mockParser.getText()).thenReturn("value4");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(mockParser.getText()).thenReturn(null);
 when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 handler._nameToPropertyIndex.put("propertyName1", 0);
 handler._nameToPropertyIndex.put("propertyName2", 1);
 handler._nameToPropertyIndex.put("propertyName3", 2);
 handler._nameToPropertyIndex.put("propertyName4", 3);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);
 handler._properties[1] = new ExtTypedProperty(mockProperty, null);
 handler._properties[2] = new ExtTypedProperty(mockProperty, null);
 handler._properties[3] = new ExtTypedProperty(mockProperty, null);
 handler._tokens = new TokenBuffer[4];
 handler._typeIds = new String[4];

 handler.complete(mockParser, mockContext, mockBean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).getText();
 verify(mockBean, times(4)).getClass();
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingBean() throws IOException {
 when(mockParser.getText()).thenReturn("typeId1");
 when(mockParser.getText()).thenReturn("typeId2");
 when(mockParser.getText()).thenReturn("typeId3");
 when(mockParser.getText()).thenReturn("typeId4");
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.getText()).thenReturn("propertyName1");
 when(mockParser.getText()).thenReturn("value1");
 when(mockParser.getText()).thenReturn("propertyName2");
 when(mockParser.getText()).thenReturn("value2");
 when(mockParser.getText()).thenReturn("propertyName3");
 when(mockParser.getText()).thenReturn("value3");
 when(mockParser.getText()).thenReturn("propertyName4");
 when(mockParser.getText()).thenReturn("value4");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(mockParser.getText()).thenReturn(null);
 handler._nameToPropertyIndex.put("propertyName1", 0);
 handler._nameToPropertyIndex.put("propertyName2", 1);
 handler._nameToPropertyIndex.put("propertyName3", 2);
 handler._nameToPropertyIndex.put("propertyName4", 3);
 handler._properties[0] = new ExtTypedProperty(mockProperty, null);
 handler._properties[1] = new ExtTypedProperty(mockProperty, null);
 handler._properties[2] = new ExtTypedProperty(mockProperty, null);
 handler._properties[3] = new ExtTypedProperty(mockProperty, null);
 handler._tokens = new TokenBuffer[4];
 handler._typeIds = new String[4];

 handler.complete(mockParser, null, mockBean);
 }

 @Test
 public void testBuilder() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 Object bean = mock(Object.class);
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

 handler.new Builder().addExternal(property, typeDeserializer).build();

 verify(parser, never()).nextToken();
 verify(context, never()).mappingException(anyString());
 }
}