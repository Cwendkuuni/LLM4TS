import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handleTypePropertyValue(parser, ctx, "property1", bean);

        assertNotNull(handler.getTypeIds);
        assertEquals("expectedTypeId", handler.getTypeIds[0]);

        parser.reset();
        handler.complete(parser, ctx, bean);
        assertNotNull(bean.getProperty1());
    }

    @Test
    public void testHandlePropertyValueWithTokenBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handlePropertyValue(parser, ctx, "property1", bean);

        assertNotNull(handler.getTokens());
        assertNotNull(handler.getTokens()[0]);

        parser.reset();
        handler.complete(parser, ctx, bean);
        assertNotNull(bean.getProperty1());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleTypePropertyValueWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handleTypePropertyValue(parser, ctx, "missingProperty", bean);

        fail("Expected JsonMappingException but got none.");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValueWithMissingTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handlePropertyValue(parser, ctx, "property1", bean);

        parser.nextToken();
        parser.nextToken();
        handler.complete(parser, ctx, bean);

        fail("Expected JsonMappingException but got none.");
    }

    @Test
    public void testHandlePropertyValueWithDefaultType() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        parser.nextToken(); // null
        handler.handlePropertyValue(parser, ctx, "property1", bean);

        String defaultTypeId = handler.getTypeDeserializer().getDefaultImpl().getClass().getName();
        String typeId = handler.getTypeIds[0];

        assertEquals(defaultTypeId, typeId);

        parser.reset();
        handler.complete(parser, ctx, bean);
        assertNotNull(bean.getProperty1());
        assertEquals(defaultTypeId, bean.getProperty1().getClass().getName());
    }

    @Test
    public void testCompleteWithNullProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handlePropertyValue(parser, ctx, "property1", bean);
        handler.handlePropertyValue(parser, ctx, "property2", bean);

        parser.nextToken(); // END_OBJECT
        handler.complete(parser, ctx, bean);

        assertNull(bean.getProperty2());
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handleTypePropertyValue(parser, ctx, "property1", bean);

        parser.reset();
        parser.nextToken(); // null
        handler.complete(parser, ctx, bean);

        assertNull(bean.getProperty1());
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handlePropertyValue(parser, ctx, "property1", bean);

        parser.reset();
        parser.nextToken(); // null
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // MISSING_PROPERTY
        handler.complete(parser, ctx, bean);

        fail("Expected JsonMappingException but got none.");
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteWithMissingTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), new TestTypeDeserializer())
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handleTypePropertyValue(parser, ctx, "property1", bean);

        parser.reset();
        parser.nextToken(); // null
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // PROPERTY_NAME
        parser.nextToken(); // VALUE
        parser.nextToken(); // END_OBJECT
        handler.complete(parser, ctx, bean);

        fail("Expected JsonMappingException but got none.");
    }

    @Test
    public void testCompleteWithNullTypeDeserializer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty(new TestBean(), "property1"), null)
                .build());
        JsonParser parser = createMockParser();
        DeserializationContext ctx = createMockContext();
        Object bean = new TestBean();

        handler.handleTypePropertyValue(parser, ctx, "property1", bean);
        handler.handlePropertyValue(parser, ctx, "property1", bean);

        parser.nextToken(); // END_OBJECT
        handler.complete(parser, ctx, bean);

        assertNull(bean.getProperty1());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private static class TestBean {
        private Object property1;

        public Object getProperty1() {
            return property1;
        }

        public void setProperty1(Object value) {
            property1 = value;
        }
    }

    private static class TestTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public String getPropertyName() {
            return "expectedTypePropertyName";
        }
    }
}