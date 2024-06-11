import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        String json = "{\"foo\":{\"type\":\"Bar\",\"value\":1},\"baz\":\"qux\"}";
        handler = mapper.readValue(json, ExternalTypeHandler.class);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"Bar\",\"name\":\"Foo\"}");
        DeserializationContext ctxt = new TestContext();
        assertFalse(handler.handleTypePropertyValue(jp, ctxt, "baz", null));

        jp.nextToken(); // move to "name" token
        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "foo", null));
        assertEquals("Bar", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_withTypeProperty() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"Foo\",\"value\":1}");
        DeserializationContext ctxt = new TestContext();
        assertTrue(handler.handlePropertyValue(jp, ctxt, "foo", null));
        assertEquals("1", handler._tokens[0].toString());
    }

    @Test
    public void testHandlePropertyValue_withoutTypeProperty() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"Foo\",\"value\":1}");
        DeserializationContext ctxt = new TestContext();
        assertTrue(handler.handlePropertyValue(jp, ctxt, "baz", null));
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testHandlePropertyValue_nullValue() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"Foo\",\"value\":null}");
        DeserializationContext ctxt = new TestContext();
        assertTrue(handler.handlePropertyValue(jp, ctxt, "foo", null));
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_nullTypeId() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"Foo\"}");
        DeserializationContext ctxt = new TestContext();
        assertFalse(handler.handlePropertyValue(jp, ctxt, "foo", null));
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_withSettableBeanProperty() throws Exception {
        Object bean = new Object();

        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"Foo\",\"value\":1}");
        DeserializationContext ctxt = new TestContext();
        handler.handlePropertyValue(jp, ctxt, "foo", bean);
        assertEquals(1, handler._tokens[0].asInt());
        assertEquals(1, ((Number)bean).intValue());
    }

    @Test
    public void testComplete() throws Exception {
        Object bean = new Object();

        handler.complete(mapper.getFactory().createParser("{}"), new TestContext(), bean);
        assertNull(bean);

        handler = new ExternalTypeHandler(handler._properties, handler._nameToPropertyIndex, handler._typeIds, handler._tokens);

        handler.complete(mapper.getFactory().createParser("{\"type\":\"Bar\"}"), new TestContext(), bean);
        assertNull(bean);

        JsonParser jp = mapper.getFactory().createParser("{\"foo\":{\"type\":\"Bar\"},\"baz\":\"qux\"}");
        handler.complete(jp, new TestContext(), bean);
        assertNotNull(bean);
        assertEquals("Bar", handler._typeIds[0]);
        assertEquals("qux", handler._typeIds[1]);
    }

    @Test
    public void testComplete_withPropertyBasedCreator() throws Exception {
        Object bean = new Object();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object();
            }
        };

        handler.complete(mapper.getFactory().createParser("{\"type\":\"Bar\"}"), new TestContext(), null, creator);
        assertNull(bean);

        handler = new ExternalTypeHandler(handler._properties, handler._nameToPropertyIndex, handler._typeIds, handler._tokens);

        handler.complete(mapper.getFactory().createParser("{\"foo\":{\"type\":\"Bar\",\"value\":1},\"baz\":\"qux\"}"), new TestContext(), null, creator);
        assertNotNull(bean);
        assertEquals(1, ((Number)bean).intValue());
    }

    @Test(expected = IOException.class)
    public void testComplete_missingProperty() throws Exception {
        Object bean = new Object();

        handler.complete(mapper.getFactory().createParser("{\"type\":\"Bar\"}"), new TestContext(), bean);
        assertNull(bean);

        handler = new ExternalTypeHandler(handler._properties, handler._nameToPropertyIndex, handler._typeIds, handler._tokens);

        handler.complete(mapper.getFactory().createParser("{\"type\":\"Bar\"}"), new TestContext(), bean);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testComplete_missingTypeId() throws Exception {
        Object bean = new Object();

        handler.complete(mapper.getFactory().createParser("{\"foo\":{\"value\":1},\"baz\":\"qux\"}"), new TestContext(), bean);
        fail("Expected IOException");
    }

    @Rule
    public TestContext testContext = new TestContext();

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() { return null; }

        @Override
        public BeanDescription getRootValueInstance() { return null; }

        @Override
        public ObjectFinder findCreatorProperties(BeanPropertyPropertyName propertyName, Class<?> containerType) { return null; }
    }

    static class PropertyBasedCreator {
        public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException { return null; }
    }
}