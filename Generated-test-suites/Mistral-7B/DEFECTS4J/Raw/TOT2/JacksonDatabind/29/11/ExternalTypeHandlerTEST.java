import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;
 private Object bean;

 @Before
 public void setUp() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 parser = mapper.getFactory().createParser("{}");
 bean = new Object();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 when(parser.getText()).thenReturn("testTypeId");

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "test"
 parser.nextToken(); // VALUE_STRING "testTypeId"

 assertTrue(handler.handleTypePropertyValue(parser, context, "test", bean));
 assertEquals("testTypeId", handler._typeIds[0]);
 assertNull(handler._tokens[0]);

 parser.nextToken(); // END_OBJECT
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueMissingProperty() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "missingProperty"
 parser.nextToken(); // VALUE_STRING "testTypeId"

 handler.handleTypePropertyValue(parser, context, "missingProperty", bean);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "test"
 parser.nextToken(); // VALUE_STRING "testValue"

 assertTrue(handler.handlePropertyValue(parser, context, "test", bean));
 assertNull(handler._typeIds[0]);
 assertNull(handler._tokens[0]);

 parser.nextToken(); // END_OBJECT
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueMissingProperty() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "missingProperty"
 parser.nextToken(); // VALUE_STRING "testValue"

 handler.handlePropertyValue(parser, context, "missingProperty", bean);
 }

 @Test
 public void testComplete() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);
 when(property.getDefaultType()).thenReturn(String.class);
 when(property.getDefaultValue()).thenReturn("defaultValue");

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "test"
 parser.nextToken(); // VALUE_STRING "testTypeId"

 handler.handlePropertyValue(parser, context, "test", bean);

 handler.complete(parser, context, bean);
 assertEquals("defaultValue", property.get(bean));

 parser.nextToken(); // END_OBJECT
 }

 @Test
 public void testCompleteMissingProperty() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "missingProperty"
 parser.nextToken(); // VALUE_STRING "testTypeId"

 handler.handlePropertyValue(parser, context, "missingProperty", bean);
 handler.complete(parser, context, bean);
 assertNull(property.get(bean));

 parser.nextToken(); // END_OBJECT
 }

 @Test
 public void testCompleteMissingTypeId() throws IOException {
 SettableBeanProperty property = mock(SettableBeanProperty.class);
 when(property.getTypePropertyName()).thenReturn("test");
 when(property.getName()).thenReturn("test");
 when(property.getPropertyName()).thenReturn("testProperty");
 when(property.getType()).thenReturn(String.class);

 ExtTypedProperty extProperty = new ExtTypedProperty(property, mock(TypeDeserializer.class));
 HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("test", 0);

 handler = new ExternalTypeHandler(new ExtTypedProperty[] { extProperty }, nameToPropertyIndex, null, null);
 handler.start();

 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME "test"
 parser.nextToken(); // VALUE_NULL

 handler.handlePropertyValue(parser, context, "test", bean);
 handler.complete(parser, context, bean);
 assertNull(property.get(bean));

 parser.nextToken(); // END_OBJECT
 }
}