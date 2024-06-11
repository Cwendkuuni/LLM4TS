import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.*;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ExtTypedProperty[] properties;
 private HashMap<String, Integer> nameToPropertyIndex;
 private Object testBean;

 @Before
 public void setup() {
 properties = new ExtTypedProperty[2];
 nameToPropertyIndex = new HashMap<>();
 testBean = new Object();
 handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 String propName = "propertyName";
 Object bean = testBean;
 String typeId = "testTypeId";

 when(mockParser.getText()).thenReturn(typeId);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

 handler.handleTypePropertyValue(mockParser, mockCtx, propName, bean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 assertEquals("testTypeId", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 String propName = "propertyName";
 Object bean = testBean;
 String typeId = "testTypeId";

 when(mockParser.getText()).thenReturn(typeId);
 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

 handler.handlePropertyValue(mockParser, mockCtx, propName, bean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).skipChildren();
 assertEquals("testTypeId", handler.getTypeIds()[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingProperty() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 String propName = "nonExistentProperty";
 Object bean = testBean;

 handler.handlePropertyValue(mockParser, mockCtx, propName, bean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).skipChildren();
 verifyNoMoreInteractions(mockParser);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingTypeId() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 Object bean = testBean;
 String propName = "propertyName";

 handler.handlePropertyValue(mockParser, mockCtx, propName, bean);

 verify(mockParser, times(1)).getText();
 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).skipChildren();
 verifyNoMoreInteractions(mockParser);
 }

 @Test
 public void testComplete() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 Object bean = testBean;
 String typeId = "testTypeId";
 properties[0] = new ExtTypedProperty(mock(SettableBeanProperty.class), null);
 properties[0].getProperty().set(bean, "testValue");
 handler.typeIds = new String[] {typeId};

 when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockParser.getText()).thenReturn("propertyName");

 handler.complete(mockParser, mockCtx, bean);

 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(1)).getText();
 assertEquals("propertyName", handler.getNameToPropertyIndex().get("propertyName"));
 assertEquals("testValue", handler.getProperties()[0].getProperty().get(bean));
 }

 @Test
 public void testCompleteWithNullTypeId() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 Object bean = testBean;
 properties[0] = new ExtTypedProperty(mock(SettableBeanProperty.class), null);
 handler.typeIds = new String[0];

 when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(mockParser.getText()).thenReturn("propertyName");

 handler.complete(mockParser, mockCtx, bean);

 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(3)).getText();
 verify(mockParser, times(1)).skipChildren();
 assertEquals("propertyName", handler.getNameToPropertyIndex().get("propertyName"));
 assertTrue(handler.getProperties()[0].getProperty().get(bean) == null);
 }

 @Test
 public void testCompleteWithDefaultType() throws Exception {
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext mockCtx = mock(DeserializationContext.class);
 Object bean = testBean;
 properties[0] = new ExtTypedProperty(mock(SettableBeanProperty.class), new StdDeserializer());
 handler.typeIds = new String[0];

 when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(mockParser.getText()).thenReturn("propertyName");

 handler.complete(mockParser, mockCtx, bean);

 verify(mockParser, times(1)).nextToken();
 verify(mockParser, times(3)).getText();
 verify(mockParser, times(1)).skipChildren();
 assertEquals("propertyName", handler.getNameToPropertyIndex().get("propertyName"));
 assertTrue(handler.getProperties()[0].getProperty().get(bean) == null);
 }
}

class ExtTypedProperty {
 private SettableBeanProperty property;
 private TypeDeserializer typeDeserializer;
 private String typePropertyName;

 public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
 this.property = property;
 this.typeDeserializer = typeDeserializer;
 this.typePropertyName = typeDeserializer.getPropertyName();
 }

 public boolean hasTypePropertyName(String n) {
 return n.equals(typePropertyName);
 }

 public boolean hasDefaultType() {
 return typeDeserializer.getDefaultImpl() != null;
 }

 public String getDefaultTypeId() {
 Class<?> defaultType = typeDeserializer.getDefaultImpl();
 if (defaultType == null) {
 return null;
 }
 return typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
 }

 public String getTypePropertyName() {
 return typePropertyName;
 }

 public SettableBeanProperty getProperty() {
 return property;
 }
}