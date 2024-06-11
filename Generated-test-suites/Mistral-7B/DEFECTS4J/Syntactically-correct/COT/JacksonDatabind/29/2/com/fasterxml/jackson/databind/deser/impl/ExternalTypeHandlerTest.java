import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.util.TestObjectMapper.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;
 private JsonParser parser;
 private DeserializationContext ctxt;
 private Object bean;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 parser = mapper.getFactory().createParser("{}");
 ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 bean = new Object();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handleTypePropertyValue(parser, ctxt, "testProp", bean);
 verify(property.getProperty()).set(bean, any());
 }

 @Test
 public void testHandleTypePropertyValue_NoMatchingProperty() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(false);

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handleTypePropertyValue(parser, ctxt, "testProp", bean);
 // Verify no property was set
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullBean() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handleTypePropertyValue(parser, ctxt, "testProp", null);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullTokens() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler._tokens = new TokenBuffer[1];
 handler._typeIds = new String[1];

 handler.handleTypePropertyValue(parser, ctxt, "testProp", bean);
 }

 @Test
 public void testHandlePropertyValue_HasTypePropertyName() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handlePropertyValue(parser, ctxt, "testProp", bean);
 verify(property.getProperty()).set(bean, any());
 }

 @Test
 public void testHandlePropertyValue_NoMatchingProperty() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(false);

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handlePropertyValue(parser, ctxt, "testProp", bean);
 // Verify no property was set
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_NullBean() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler.handlePropertyValue(parser, ctxt, "testProp", null);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_NullTokens() throws Exception {
 ExtTypedProperty property = mock(ExtTypedProperty.class);
 when(property.hasTypePropertyName(eq("testProp"))).thenReturn(true);
 when(property.getProperty()).thenReturn(mock(SettableBeanProperty.class));

 handler._properties = new ExtTypedProperty[]{property};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("testProp", 0);

 handler._tokens = new TokenBuffer[1];
 handler._typeIds = new String[1];

 handler.handlePropertyValue(parser, ctxt, "testProp", bean);
 }

 @Test
 public void testComplete_AllPropertiesSet() throws Exception {
 String[] typeIds = new String[]{"StringType", "NumberType"};
 Object[] expected = new Object[]{new String(), new Number()};

 handler._properties = new ExtTypedProperty[]{
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(String.class), "strProp"),
                 new StringDeserializer(mapper.getTypeFactory().constructType(String.class))),
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(Number.class), "numProp"),
                 new NumberDeserializer(mapper.getTypeFactory().constructType(Number.class)))};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("strProp", 0);
 handler._nameToPropertyIndex.put("numProp", 1);
 handler._typeIds = typeIds;
 handler._tokens = new TokenBuffer[2];

 handler.complete(parser, ctxt, bean);

 for (int i = 0; i < handler._properties.length; i++) {
 SettableBeanProperty prop = handler._properties[i].getProperty();
 assertEquals(expected[i], prop.get(bean));
 }
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeId() throws Exception {
 handler._properties = new ExtTypedProperty[]{
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(String.class), "strProp"),
                 new StringDeserializer(mapper.getTypeFactory().constructType(String.class))),
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(Number.class), "numProp"),
                 new NumberDeserializer(mapper.getTypeFactory().constructType(Number.class)))};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("strProp", 0);
 handler._nameToPropertyIndex.put("numProp", 1);
 handler._typeIds = new String[2];

 handler.complete(parser, ctxt, bean);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws Exception {
 handler._properties = new ExtTypedProperty[]{
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(String.class), "strProp"),
                 new StringDeserializer(mapper.getTypeFactory().constructType(String.class))),
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(Number.class), "numProp"),
                 new NumberDeserializer(mapper.getTypeFactory().constructType(Number.class)))};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("strProp", 0);
 handler._nameToPropertyIndex.put("numProp", 1);
 handler._typeIds = new String[2];

 handler._tokens = new TokenBuffer[2];
 handler._tokens[0] = new TokenBuffer(parser);
 handler._tokens[0].copyCurrentStructure(parser);
 handler._tokens[0].writeEndArray();

 handler.complete(parser, ctxt, bean);
 }

 @Test(expected = IOException.class)
 public void testComplete_NullBean() throws Exception {
 handler._properties = new ExtTypedProperty[]{
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(String.class), "strProp"),
                 new StringDeserializer(mapper.getTypeFactory().constructType(String.class))),
         new ExtTypedProperty(new SettableBeanProperty(mapper.findValueDeserializer(Number.class), "numProp"),
                 new NumberDeserializer(mapper.getTypeFactory().constructType(Number.class)))};
 handler._nameToPropertyIndex = new HashMap<>();
 handler._nameToPropertyIndex.put("strProp", 0);
 handler._nameToPropertyIndex.put("numProp", 1);
 handler._typeIds = new String[2];
 handler._tokens = new TokenBuffer[2];

 handler.complete(parser, ctxt, null);
 }
}