import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.*;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private Builder mockBuilder;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockBuilder = spy(new Builder());

 handler = new ExternalTypeHandler(mockBuilder.build().start());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";

 when(mockParser.getText()).thenReturn(typeId);
 when(mockContext.findBeanProperty(eq(propName), any(Object.class))).thenReturn(null);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser, times(1)).skipChildren();
 verify(mockParser, never()).nextToken();

 // Check that the type id was stored
 assertEquals(typeId, handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_whenPropertyExists() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(mockParser.getText()).thenReturn(typeId);
 when(mockContext.findBeanProperty(eq(propName), any(Object.class))).thenReturn(property);

 handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser, times(1)).skipChildren();
 verify(mockParser, never()).nextToken();

 // Check that the type id was stored
 assertEquals(typeId, handler._typeIds[0]);
 // Check that the property was set
 verify(property).setValue(mockBean, handler._deserialize(mockParser, mockContext, 0, typeId));
 }

 @Test
 public void testHandlePropertyValue_whenTypePropertyNameExists() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(mockParser.getText()).thenReturn(typeId);
 when(mockContext.findBeanProperty(eq(propName), any(Object.class))).thenReturn(property);

 handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser, times(1)).skipChildren();
 verify(mockParser, times(1)).nextToken();

 // Check that the type id was stored
 assertEquals(typeId, handler._typeIds[0]);
 // Check that the property was set
 verify(property).setValue(mockBean, handler._deserialize(mockParser, mockContext, 0, typeId));
 }

 @Test
 public void testHandlePropertyValue_whenTypePropertyNameDoesNotExist() throws IOException {
 String propName = "testProp";
 String typeId = "testTypeId";
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(mockParser.getText()).thenReturn(typeId);
 when(mockContext.findBeanProperty(eq(propName), any(Object.class))).thenReturn(property);

 handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);

 verify(mockParser, never()).skipChildren();
 verify(mockParser, never()).nextToken();

 // Check that the type id was stored
 assertEquals(typeId, handler._typeIds[0]);
 }

 @Test
 public void testComplete_whenAllPropertiesHaveValues() throws IOException {
 Object[] values = new Object[2];
 values[0] = "value1";
 values[1] = 1;
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockContext.constructValue(eq(Object.class), anyInt(), any(JsonParser.class)))
 .thenReturn(values[0]);
 when(mockContext.constructValue(eq(Integer.class), anyInt(), any(JsonParser.class)))
 .thenReturn(values[1]);

 handler.complete(mockParser, mockContext, mockBean);

 // Check that all properties were deserialized and set
 SettableBeanProperty property1 = mock(SettableBeanProperty.class);
 SettableBeanProperty property2 = mock(SettableBeanProperty.class);
 verify(property1).setValue(mockBean, values[0]);
 verify(property2).setValue(mockBean, values[1]);
 }

 @Test(expected = MappingException.class)
 public void testComplete_whenMissingProperty() throws IOException {
 String missingPropName = "missingProp";
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockContext.findBeanProperty(eq(missingPropName), any(Object.class)))
 .thenReturn(null);

 handler.complete(mockParser, mockContext, mockBean);

 // Check that an exception was thrown
 fail("Expected MappingException to be thrown");
 }

 @Test(expected = MappingException.class)
 public void testComplete_whenMissingTypeId() throws IOException {
 String propName = "testProp";
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(mockContext.findBeanProperty(eq(propName), any(Object.class))).thenReturn(property);

 handler.complete(mockParser, mockContext, mockBean);

 // Check that an exception was thrown
 fail("Expected MappingException to be thrown");
 }
}