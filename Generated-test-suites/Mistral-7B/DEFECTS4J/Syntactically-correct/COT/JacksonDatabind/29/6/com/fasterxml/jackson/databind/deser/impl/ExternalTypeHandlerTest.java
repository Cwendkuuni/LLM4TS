import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private SettableBeanProperty mockProperty;
 private TypeDeserializer mockTypeDeserializer;
 private Builder mockBuilder;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockProperty = mock(SettableBeanProperty.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);
 mockBuilder = new Builder();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 when(mockParser.getText()).thenReturn("typeId");
 when(mockContext.findBeanProperty("bean")).thenReturn(mockBean);
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockProperty.getPropertyName()).thenReturn("typePropertyName");
 when(mockTypeDeserializer.getPropertyName()).thenReturn("typePropertyName");
 when(mockBuilder.findIndex("propertyName")).thenReturn(0);
 when(mockBuilder.findIndex("typePropertyName")).thenReturn(0);
 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 verify(mockParser).nextToken(); // skip START_OBJECT
 verify(mockTypeDeserializer).deserialize(mockParser, mockContext, eq(mockBean), eq(String.class));
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_whenNoMatchingProperty() throws IOException {
 when(mockParser.getText()).thenReturn("typeId");
 when(mockContext.findBeanProperty("bean")).thenReturn(mockBean);
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockProperty.getPropertyName()).thenReturn("typePropertyName");
 when(mockTypeDeserializer.getPropertyName()).thenReturn("otherTypePropertyName");
 when(mockBuilder.findIndex("propertyName")).thenReturn(-1);
 handler.handleTypePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 }

 @Test
 public void testHandlePropertyValue_whenHandlingValue() throws IOException {
 when(mockParser.getText()).thenReturn("value");
 when(mockContext.findBeanProperty("bean")).thenReturn(mockBean);
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockTypeDeserializer.getPropertyName()).thenReturn("typePropertyName");
 when(mockBuilder.findIndex("propertyName")).thenReturn(0);
 when(mockBuilder.findIndex("typePropertyName")).thenReturn(-1);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 verify(mockParser).nextToken(); // skip START_OBJECT and value
 }

 @Test
 public void testHandlePropertyValue_whenBufferingValue() throws IOException {
 when(mockParser.getText()).thenReturn("value");
 when(mockContext.findBeanProperty("bean")).thenReturn(mockBean);
 when(mockProperty.getName()).thenReturn("propertyName");
 when(mockProperty.getType()).thenReturn(String.class);
 when(mockTypeDeserializer.getPropertyName()).thenReturn("typePropertyName");
 when(mockBuilder.findIndex("propertyName")).thenReturn(0);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 TokenBuffer tokens = handler.getTokens()[0];
 JsonParser parsedTokens = tokens.asParser(mockParser);
 verify(parsedTokens).nextToken(); // skip START_OBJECT
 }

 @Test
 public void testComplete_whenAllPropertiesAreHandled() throws IOException {
 when(mockParser.getValueAsToken()).thenReturn(null);
 when(mockBuilder.build().getProperties()).thenReturn(new ExtTypedProperty[]{new ExtTypedProperty(mockProperty, mockTypeDeserializer)});
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "typePropertyName", mockBean);
 handler.complete(mockParser, mockContext, mockBean);
 verifyNoMoreInteractions(mockParser);
 }

 @Test
 public void testComplete_whenTypeIdIsMissing() throws IOException {
 when(mockParser.getValueAsToken()).thenReturn(null);
 when(mockBuilder.build().getProperties()).thenReturn(new ExtTypedProperty[]{new ExtTypedProperty(mockProperty, mockTypeDeserializer)});
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "typePropertyName", mockBean);
 handler.getTokens()[0] = null;
 handler.complete(mockParser, mockContext, mockBean);
 verify(mockContext).mappingException("Missing property 'propertyName' for external type id 'typePropertyName'");
 }

 @Test
 public void testComplete_whenCreatorIsUsed() throws IOException {
 when(mockParser.getValueAsToken()).thenReturn(null);
 when(mockBuilder.build().getProperties()).thenReturn(new ExtTypedProperty[]{new ExtTypedProperty(mockProperty, mockTypeDeserializer)});
 PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
 PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
 handler.handlePropertyValue(mockParser, mockContext, "propertyName", mockBean);
 handler.handlePropertyValue(mockParser, mockContext, "typePropertyName", mockBean);
 handler.complete(mockParser, mockContext, buffer, creator);
 verify(creator).build(eq(mockContext), eq(buffer));
 }
}