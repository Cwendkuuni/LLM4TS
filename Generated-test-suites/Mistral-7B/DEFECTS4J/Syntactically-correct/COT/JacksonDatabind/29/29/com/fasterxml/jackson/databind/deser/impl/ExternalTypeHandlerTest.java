import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext context;

    @Before
    public void setUp() throws IOException {
        parser = new ObjectMapper().getFactory().createParser("{\"prop1\":\"val1\",\"prop2\":1,\"@type\":\"MyClassName\"}");
        context = new TestDeserializationContext();
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        Object bean = new Object();
        String propName = "prop1";
        Assert.assertTrue(handler.handleTypePropertyValue(parser, context, propName, bean));
        Assert.assertEquals("val1", handler._typeIds[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleTypePropertyValueMissingTypeId() throws IOException {
        Object bean = new Object();
        String propName = "prop1";
        Assert.assertTrue(handler.handleTypePropertyValue(parser, context, propName, bean));
        Assert.assertNull(handler._typeIds[0]);
        parser.nextToken(); // skip to next token
        Assert.assertNull(handler._typeIds[0]);
        handler.complete(parser, context, bean);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        Object bean = new Object();
        String propName = "nonExistentProperty";
        Assert.assertTrue(handler.handleTypePropertyValue(parser, context, propName, bean));
        handler.complete(parser, context, bean);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        Object bean = new Object();
        String propName = "prop2";
        Assert.assertTrue(handler.handlePropertyValue(parser, context, propName, bean));
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT.id(), handler._tokens[0].getCurrentToken().getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testHandlePropertyValueMissingTypeIdAndProperty() throws IOException {
        Object bean = new Object();
        String propName = "nonExistentProperty";
        Assert.assertTrue(handler.handlePropertyValue(parser, context, propName, bean));
        handler.complete(parser, context, bean);
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);
        handler.complete(parser, context, bean);
        Assert.assertNull(parser.getCurrentToken());
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);
        handler.complete(parser, context, bean);
        Assert.assertNull(parser.getCurrentToken());
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingProperty() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "nonExistentProperty", bean);
        handler.complete(parser, context, bean);
    }

    @Test
    public void testCompleteWithNullValues() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);
        handler.handleTypePropertyValue(parser, context, "prop2", bean);
        handler.complete(parser, context, bean);
        Assert.assertNull(parser.getCurrentToken());
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                Object result = pvb.getParameter("prop1");
                return new Object(){{setProp1(result);}};
            }
        };
        handler.handlePropertyValue(parser, context, "prop2", bean);
        handler.handleTypePropertyValue(parser, context, "prop2", bean);
        handler.complete(parser, context, bean, null, creator);
        Assert.assertNull(parser.getCurrentToken());
    }

    @Test
    public void testDeserialize() throws IOException {
        Object bean = new Object();
        handler.handleTypePropertyValue(parser, context, "typeId", bean);
        Object result = handler._deserialize(parser, context, 0, "typeId");
        Assert.assertEquals(Integer.class, result.getClass());
        SettableBeanProperty prop = new SettableBeanProperty("prop", Integer.class);
        prop.set(bean, result);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        Object bean = new Object();
        handler.handleTypePropertyValue(parser, context, "typeId", bean);
        handler.nextToken(parser);
        Object result = handler._deserialize(parser, context, 0, "typeId");
        Assert.assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMissingTypeId() throws IOException {
        Object bean = new Object();
        handler.handleTypePropertyValue(parser, context, "typeId", bean);
        handler.nextToken(parser);
        Object result = handler._deserialize(parser, context, 0, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object bean = new Object();
        handler.handleTypePropertyValue(parser, context, "typeId", bean);
        Object result = handler._typeIds[0];
        handler.handlePropertyValue(parser, context, "prop", bean);
        handler._deserializeAndSet(parser, context, bean, 0, "typeId");
        Assert.assertEquals(result, handler._properties[0].getProperty().get(bean));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetMissingTypeId() throws IOException {
        Object bean = new Object();
        handler.handlePropertyValue(parser, context, "prop", bean);
        handler._deserializeAndSet(parser, context, bean, 0, null);
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findBeanProperty(String name) {
            return null;
        }

        @Override
        public Object findCreatorProperty(String name) {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType, JavaType type) {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType) {
            return null;
        }

        @Override
        public Object findValueDeserializer(String name, JavaType type) {
            return null;
        }

        @Override
        public Object findValueDeserializer(String name) {
            return null;
        }
    }
}