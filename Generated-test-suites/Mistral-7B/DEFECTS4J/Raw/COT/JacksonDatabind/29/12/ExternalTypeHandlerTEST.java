import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":\"type1\"}");
        boolean result = handler.handleTypePropertyValue(parser, new TestContext(), "prop1", null);
        assertTrue(result);
        assertEquals("type1", handler._typeIds[0]);

        parser = createParser("{\"prop1\":\"value1\"}");
        result = handler.handleTypePropertyValue(parser, new TestContext(), "prop1", new Object());
        assertFalse(result);

        parser = createParser("{\"anotherProp\":\"type1\"}");
        result = handler.handleTypePropertyValue(parser, new TestContext(), "anotherProp", null);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":\"value1\"}");
        boolean result = handler.handlePropertyValue(parser, new TestContext(), "prop1", null);
        assertTrue(result);
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler._typeIds[0] = "type1";
        handler._tokens[0] = new TokenBuffer(parser);
        result = handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        assertFalse(result);

        parser = createParser("{\"anotherProp\":\"value1\"}");
        result = handler.handlePropertyValue(parser, new TestContext(), "prop1", null);
        assertFalse(result);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithNullBean() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":\"value1\"}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", null);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithNullTokens() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        handler._typeIds = new String[1];
        handler._tokens = new TokenBuffer[1];
        JsonParser parser = createParser("{\"prop1\":\"value1\"}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":\"type1\"}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        parser.nextToken();
        Object result = handler.complete(parser, new TestContext(), null);
        assertEquals("type1", result.getClass().getField("prop1").get(result));

        handler._typeIds[0] = null;
        handler._tokens[0] = null;
        assertEquals(null, handler.complete(parser, new TestContext(), null));

        handler._tokens = new TokenBuffer[1];
        handler._typeIds = new String[1];
        parser = createParser("{\"prop1\":null}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        parser.nextToken();
        assertEquals(null, handler.complete(parser, new TestContext(), null).getClass().getField("prop1").get(handler.complete(parser, new TestContext(), null)));

        parser = createParser("{\"prop1\":[1,2,3]}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        parser.nextToken();
        assertEquals(java.util.Arrays.asList(1, 2, 3), handler.complete(parser, new TestContext(), null).getClass().getField("prop1").get(handler.complete(parser, new TestContext(), null)));
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{ \"prop1\": null }");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        parser.nextToken();
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{ \"anotherProp\": null }");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        parser.nextToken();
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":[1, 2, 3], \"creatorProp\":{ \"creatorProp\": \"creatorValue\" }}");
        Object result = handler.complete(parser, new TestContext(), null);
        assertEquals("creatorValue", result.getClass().getMethod("getCreatorProp").invoke(result));

        handler._typeIds[0] = null;
        handler._tokens[0] = null;
        Object creatorResult = handler.complete(parser, new TestContext(), null, new CreatorClass());
        assertEquals("creatorValue", creatorResult.getClass().getMethod("getCreatorProp").invoke(creatorResult));
    }

    @Test
    public void testCompleteWithBufferedData() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":null}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", null);
        handler.handlePropertyValue(parser, new TestContext(), "prop1", new Object());
        String expected = "expectedValue";
        handler._typeIds[0] = "typeId";
        handler._tokens[0] = new TokenBuffer(parser);
        Object result = handler.complete(parser, new TestContext(), null);
        assertEquals(expected, result.getClass().getField("prop1").get(result));
    }

    @Test
    public void testHandlePropertyValueWithNaturalType() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("prop1", Number.class), new NumberTypeDeserializer())
                .build());
        JsonParser parser = createParser("{\"prop1\":1}");
        handler.handlePropertyValue(parser, new TestContext(), "prop1", null);
        Object result = handler.complete(parser, new TestContext(), null);
        assertEquals(1, result.getClass().getField("prop1").get(result));
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectReader reader = new ObjectMapper().readerFor(Object.class);
        return reader.readValueAsTree(new StringReader(json)).asParser();
    }

    static class TestContext extends JsonDeserializationContext {}

    static class CreatorClass {
        private String creatorProp;

        public String getCreatorProp() {
            return creatorProp;
        }

        public void setCreatorProp(String prop) {
            this.creatorProp = prop;
        }
    }

    static class StringTypeDeserializer extends StdScalarDeserializer {
        @Override
        public String getPropertyName() {
            return "typePropertyName";
        }
    }

    static class NumberTypeDeserializer extends StdScalarDeserializer {
        @Override
        public Class<Number> getNullValue() {
            return Number.class;
        }

        @Override
        public Class<?> getBindingClass() {
            return Number.class;
        }

        @Override
        public String getPropertyName() {
            return "typePropertyName";
        }
    }
}