import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ObjectMapper objectMapper;
    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree("{\"typeId1\":1,\"typeId2\":2,\"prop1\":\"value1\",\"prop2\":\"value2\"}");
        externalTypeHandler = new ExternalTypeHandler(ExternalTypeHandler.Builder.class.getDeclaredConstructor().newInstance()) {
            @Override
            protected Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId) throws IOException {
                return super._deserialize(p, ctxt, index, typeId);
            };
        };
        externalTypeHandler.setUpDeserialization(objectMapper.getFactory(), node);
    }

    @Test
    public void testStart() throws Exception {
        ExternalTypeHandler handler = externalTypeHandler.start();
        Assert.assertNotNull(handler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullProperties() throws Exception {
        externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        externalTypeHandler.handleTypePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullNameToPropertyIndex() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], null, null, null);
        handler.handleTypePropertyValue(mockJsonParser(), mockDeserializationContext(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullTypeIds() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);
        handler.handleTypePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullTokens() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);
        handler.handleTypePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        String typeId = "typeId1";
        Object bean = new Object();

        externalTypeHandler.handleTypePropertyValue(mockJsonParser(), mockDeserializationContext(), "typeId1", bean);
        Assert.assertEquals(typeId, externalTypeHandler._typeIds[0]);
        Assert.assertNull(externalTypeHandler._tokens[0]);

        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop1", bean);
        Assert.assertNotNull(externalTypeHandler._tokens[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithNullProperties() throws Exception {
        externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithNullNameToPropertyIndex() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], null, null, null);
        handler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithNullTypeIds() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);
        handler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithNullTokens() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);
        handler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "test", null);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        String propName = "prop1";
        String typeId = "typeId1";
        Object bean = new Object();

        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), propName, bean);
        Assert.assertEquals(typeId, externalTypeHandler._typeIds[0]);
        Assert.assertNotNull(externalTypeHandler._tokens[0]);

        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop2", bean);
        Assert.assertNotNull(externalTypeHandler._tokens[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testCompleteWithNullTokens() throws Exception {
        Object bean = new Object();
        externalTypeHandler.complete(mockJsonParser(), mockDeserializationContext(), bean);
    }

    @Test
    public void testComplete() throws Exception {
        Object bean = new Object();
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop1", bean);
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop2", bean);
        externalTypeHandler.complete(mockJsonParser(), mockDeserializationContext(), bean);

        Object result = bean;
        SettableBeanProperty prop1 = new SimpleValueProperty("prop1", String.class);
        SettableBeanProperty prop2 = new SimpleValueProperty("prop2", String.class);
        SettableBeanProperty typeId1 = new SimpleValueProperty("typeId1", String.class);

        Assert.assertEquals(typeId1.getValue(), result.getClass().getField("typeId1").get(result));
        Assert.assertEquals(prop1.getValue(), result.getClass().getField("prop1").get(result));
        Assert.assertEquals(prop2.getValue(), result.getClass().getField("prop2").get(result));
    }

    @Test
    public void testCompleteWithNullBean() throws Exception {
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop1", null);
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop2", null);
        Object bean = externalTypeHandler.complete(mockJsonParser(), mockDeserializationContext(), null);

        Assert.assertNotNull(bean);
        SettableBeanProperty prop1 = new SimpleValueProperty("prop1", String.class);
        SettableBeanProperty prop2 = new SimpleValueProperty("prop2", String.class);
        SettableBeanProperty typeId1 = new SimpleValueProperty("typeId1", String.class);

        Assert.assertEquals(typeId1.getValue(), bean.getClass().getField("typeId1").get(bean));
        Assert.assertEquals(prop1.getValue(), bean.getClass().getField("prop1").get(bean));
        Assert.assertEquals(prop2.getValue(), bean.getClass().getField("prop2").get(bean));
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws Exception {
        Object bean = new Object();
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop1", bean);
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "typeId1", bean);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty prop1 = new SimpleValueProperty("prop1", String.class);
        SettableBeanProperty typeId1 = new SimpleValueProperty("typeId1", String.class);
        builder.addExternal(prop1, new SimpleTypeDeserializer(String.class));
        builder.addExternal(typeId1, new SimpleTypeDeserializer(String.class));
        builder.linkTypeProperty(typeId1);

        Object result = externalTypeHandler.complete(mockJsonParser(), mockDeserializationContext(), builder.build(), null);

        Assert.assertEquals(prop1.getValue(), result.getClass().getField("prop1").get(result));
        Assert.assertEquals(typeId1.getValue(), result.getClass().getField("typeId1").get(result));
    }

    @Test
    public void testCompleteWithCreatorProperty() throws Exception {
        Object bean = new Object();
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "prop1", bean);
        externalTypeHandler.handlePropertyValue(mockJsonParser(), mockDeserializationContext(), "typeId1", bean);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty prop1 = new SimpleValueProperty("prop1", String.class);
        SettableBeanProperty typeId1 = new SimpleValueProperty("typeId1", String.class);
        SettableBeanProperty creatorProp = new SimpleValueProperty("creatorProp", String.class);
        builder.addExternal(prop1, new SimpleTypeDeserializer(String.class));
        builder.addExternal(typeId1, new SimpleTypeDeserializer(String.class));
        builder.linkTypeProperty(typeId1);
        builder.linkTypeProperty(creatorProp);

        Object result = externalTypeHandler.complete(mockJsonParser(), mockDeserializationContext(), builder.build(), new SimpleCreator());

        Assert.assertEquals(prop1.getValue(), result.getClass().getField("prop1").get(result));
        Assert.assertEquals(typeId1.getValue(), result.getClass().getField("typeId1").get(result));
        Assert.assertEquals(creatorProp.getValue(), result.getClass().getField("creatorProp").get(result));
    }

    private JsonParser mockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext mockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private static class SimpleValueProperty extends PropertyName {
        private final Object value;
        private final Class<?> propertyType;

        public SimpleValueProperty(String name, Class<?> propertyType) {
            super(name);
            this.propertyType = propertyType;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Class<?> getType() {
            return propertyType;
        }

        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object result = p.getValueAsToken().getValueAsString();
            setValue(result);
            return result;
        }

        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            Object value = deserialize(p, ctxt);
            set(bean, value);
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private final Class<?> type;

        public SimpleTypeDeserializer(Class<?> type) {
            this.type = type;
        }

        @Override
        public Object deserializeTyped(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ctxt.findValueSerializer(type).deserialize(p, ctxt);
        }

        @Override
        public String getPropertyName() {
            return null;
        }

        @Override
        public JsonTypeId idFromValueAndType(String typeName, Class<?> valueType) {
            return null;
        }

        @Override
        public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        }
    }

    private static class SimpleCreator {
        public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
            return new Object();
        }
    }
}