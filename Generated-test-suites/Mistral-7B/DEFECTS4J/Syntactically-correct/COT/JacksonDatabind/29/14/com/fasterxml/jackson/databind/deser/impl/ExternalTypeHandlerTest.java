import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object bean;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws IOException {
 JsonParser jp = new JsonParser();
 ObjectMapper mapper = new ObjectMapper();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(new SimplePojo().getClass().getDeclaredField("p1"), mapper.getTypeFactory().constructType(String.class)).addExternal(new SimplePojo().getClass().getDeclaredField("p2"), mapper.getTypeFactory().constructType(Integer.class)).build());
 bean = new SimplePojo();
 }

 @Test
 public void testHandleTypePropertyValue_ReturnsFalse_WhenPropertyNameDoesNotMatch() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"prop1\": \"type1\"}"));
 assertFalse(handler.handleTypePropertyValue(jp, new TestContext(), "prop2", bean));
 }

 @Test
 public void testHandleTypePropertyValue_ReturnsTrue_WhenPropertyNameMatches() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"p1\": \"type1\"}"));
 assertTrue(handler.handleTypePropertyValue(jp, new TestContext(), "p1", bean));
 }

 @Test
 public void testHandleTypePropertyValue_SetsTypeId_WhenPropertyNameMatches() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"p1\": \"type1\"}"));
 handler.handleTypePropertyValue(jp, new TestContext(), "p1", bean);
 assertEquals("type1", handler.getNextTypeId());
 }

 @Test
 public void testHandlePropertyValue_ReturnsFalse_WhenPropertyNameDoesNotMatch() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"prop1\": \"value1\"}"));
 assertFalse(handler.handlePropertyValue(jp, new TestContext(), "prop2", bean));
 }

 @Test
 public void testHandlePropertyValue_SetsTypeId_WhenTypePropertyNameMatches() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"typePropertyName\": \"type1\"}"));
 handler.handlePropertyValue(jp, new TestContext(), "typePropertyName", bean);
 assertEquals("type1", handler.getNextTypeId());
 }

 @Test
 public void testHandlePropertyValue_BuffersToken_WhenTypePropertyNameDoesNotMatch() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"prop1\": \"value1\"}"));
 handler.handlePropertyValue(jp, new TestContext(), "prop2", bean);
 assertEquals(JsonToken.START_OBJECT, handler.getNextToken().asInt());
 }

 @Test
 public void testHandlePropertyValue_BuffersTokenAndSetsValue_WhenPropertyNameDoesNotMatchButTypeIsKnown() throws IOException {
 JsonParser jp = new JsonParser(new StringReader("{\"prop1\": 42}"));
 handler.handlePropertyValue(jp, new TestContext(), "prop2", bean);
 assertEquals(JsonToken.START_ARRAY, handler.getNextToken().asInt());
 assertEquals("42", handler.getNextToken().getText());
 assertEquals(JsonToken.END_ARRAY, handler.getNextToken().asInt());
 }

 @Test(expected = MappingException.class)
 public void testComplete_ThrowsMappingException_WhenPropertyIsMissing() throws IOException {
 handler.complete(new JsonParser(new StringReader("{}")), new TestContext(), bean);
 }

 @Test
 public void testComplete_SetsProperties_WhenAllDataIsAvailable() throws IOException {
 handler.handleTypePropertyValue(new JsonParser(new StringReader("{\"p1\": \"type1\"}")), new TestContext(), "p1", bean);
 handler.handlePropertyValue(new JsonParser(new StringReader("{\"p2\": 42}")), new TestContext(), "p2", bean);
 handler.complete(new JsonParser(new StringReader("}")), new TestContext(), bean);
 assertEquals("type1", handler.getNextTypeId());
 assertEquals(42, bean.getClass().getDeclaredField("p2").get(bean));
 }

 @Test(expected = MappingException.class)
 public void testComplete_ThrowsMappingException_WhenTypeIdIsMissing() throws IOException {
 handler.handlePropertyValue(new JsonParser(new StringReader("{\"p1\": \"type1\"}")), new TestContext(), "p1", bean);
 handler.complete(new JsonParser(new StringReader("}")), new TestContext(), bean);
 }

 @Test(expected = MappingException.class)
 public void testComplete_ThrowsMappingException_WhenPropertyIsMissing() throws IOException {
 handler.handleTypePropertyValue(new JsonParser(new StringReader("{\"p1\": \"type1\"}")), new TestContext(), "p1", bean);
 handler.complete(new JsonParser(new StringReader("{\"prop2\": 42}")), new TestContext(), bean);
 }

 @Test
 public void testComplete_SetsProperty_WhenTypeIdIsKnown() throws IOException {
 handler.handleTypePropertyValue(new JsonParser(new StringReader("{\"p1\": \"type1\"}")), new TestContext(), "p1", bean);
 handler.handlePropertyValue(new JsonParser(new StringReader("{\"p2\": 42}")), new TestContext(), "p2", bean);
 handler.complete(new JsonParser(new StringReader("}")), new TestContext(), bean);
 assertEquals("type1", handler.getNextTypeId());
 assertEquals(42, bean.getClass().getDeclaredField("p2").get(bean));
 }

 private String getNextTypeId() throws IOException {
 String typeId = null;
 if (handler._typeIds != null) {
 typeId = handler._typeIds[0];
 handler._typeIds = Arrays.copyOfRange(handler._typeIds, 1, handler._typeIds.length);
 }
 return typeId;
 }

 private JsonToken getNextToken() throws IOException {
 TokenBuffer tokens = handler._tokens[0];
 JsonToken token = tokens.firstToken();
 handler._tokens[0] = Arrays.copyOfRange(handler._tokens, 1, handler._tokens.length);
 return token;
 }

 private static class TestContext extends DeserializationContext {
 }

 private static class SimplePojo {
 private String p1;
 private int p2;
 }

}