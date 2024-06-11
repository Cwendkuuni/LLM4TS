import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

 private static final Object DUMMY_BEAN = new Object();
 private static final String DUMMY_PROPERTY_NAME = "dummyProperty";
 private static final String DUMMY_TYPE_ID = "dummyTypeId";
 private static final TypeDeserializer DUMMY_TYPE_DESERIALIZER = mock(TypeDeserializer.class);
 private static final SettableBeanProperty DUMMY_PROPERTY = mock(SettableBeanProperty.class);
 private static final ExternalTypeHandler handler;

 static {
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(DUMMY_PROPERTY, DUMMY_TYPE_DESERIALIZER);
 handler = builder.build();
 }

 @Test
 public void testHandleTypePropertyValue_WhenPropertyNameMatchesTypeId_ThenHandleIt() throws Exception {
 String json = "{\"dummyProperty\":\"dummyTypeId\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();
 when(DUMMY_TYPE_DESERIALIZER.deserialize(jp, ctxt, DUMMY_BEAN)).thenReturn("handledValue");

 handler.handleTypePropertyValue(jp, ctxt, DUMMY_PROPERTY_NAME, DUMMY_BEAN);
 verify(DUMMY_TYPE_DESERIALIZER).deserialize(jp, ctxt, DUMMY_BEAN);
 assertEquals("handledValue", DUMMY_BEAN);
 }

 @Test
 public void testHandleTypePropertyValue_WhenPropertyNameDoesntMatchTypeId_ThenDontHandleIt() throws Exception {
 String json = "{\"dummyProperty\":\"dummyTypeId\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();

 handler.handleTypePropertyValue(jp, ctxt, "otherPropertyName", DUMMY_BEAN);
 verify(DUMMY_TYPE_DESERIALIZER, never()).deserialize(any(), any(), eq(DUMMY_BEAN));
 assertNull(DUMMY_BEAN);
 }

 @Test
 public void testHandlePropertyValue_WhenPropertyNameAndTypeIdMismatch_ThenBufferIt() throws Exception {
 String json = "{\"dummyProperty\":\"dummyProperty\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();
 TokenBuffer tokenBuffer = mock(TokenBuffer.class);
 when(tokenBuffer.asParser(jp)).thenReturn(jp);
 handler._tokens[0] = tokenBuffer;

 handler.handlePropertyValue(jp, ctxt, DUMMY_PROPERTY_NAME, DUMMY_BEAN);
 verify(tokenBuffer).copyCurrentStructure(jp);
 assertNull(DUMMY_BEAN);
 }

 @Test
 public void testHandlePropertyValue_WhenPropertyNameMatchesTypeId_ThenDeserializeIt() throws Exception {
 String json = "{\"dummyProperty\":\"dummyTypeId\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();
 when(DUMMY_TYPE_DESERIALIZER.deserialize(jp, ctxt, DUMMY_BEAN)).thenReturn("deserializedValue");

 handler.handlePropertyValue(jp, ctxt, DUMMY_PROPERTY_NAME, DUMMY_BEAN);
 verify(DUMMY_TYPE_DESERIALIZER).deserialize(jp, ctxt, DUMMY_BEAN);
 assertEquals("deserializedValue", DUMMY_BEAN);
 }

 @Test
 public void testComplete_WhenAllDataIsPresent_ThenSetProperties() throws Exception {
 String json = "{\"dummyProperty\":\"dummyTypeId\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();
 when(DUMMY_TYPE_DESERIALIZER.deserialize(jp, ctxt, DUMMY_BEAN)).thenReturn("deserializedValue");

 Object result = handler.complete(jp, ctxt, DUMMY_BEAN);
 assertEquals(DUMMY_BEAN, result);
 verify(DUMMY_PROPERTY).set(eq(DUMMY_BEAN), eq("deserializedValue"));
 }

 @Test(expected = IOException.class)
 public void testComplete_WhenSomeDataIsMissing_ThenThrowError() throws Exception {
 String json = "{\"dummyProperty\":\"dummyProperty\"}";
 JsonParser jp = createMockJsonParser(json);
 DeserializationContext ctxt = createMockDeserializationContext();
 handler._tokens[0] = null;

 handler.complete(jp, ctxt, DUMMY_BEAN);
 }

 private JsonParser createMockJsonParser(String json) throws IOException {
 JsonFactory factory = new JsonFactory();
 ObjectCodec oc = factory.getObjectCodec();
 return oc.createParser(new StringReader(json));
 }

 private DeserializationContext createMockDeserializationContext() {
 return mock(DeserializationContext.class);
 }
}