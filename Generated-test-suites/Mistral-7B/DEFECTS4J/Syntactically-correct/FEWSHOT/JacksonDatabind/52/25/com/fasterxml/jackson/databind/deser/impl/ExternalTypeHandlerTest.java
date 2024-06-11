package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{\"type\":\"StringType\"}");
        handler.handleTypePropertyValue(p, new TestContext(), "type", obj);
        assertEquals("StringType", obj.getType());
    }

    @Test
    public void testHandleTypePropertyValue_NullBean() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{\"type\":\"StringType\"}");
        assertFalse(handler.handleTypePropertyValue(p, new TestContext(), "type", null));
    }

    @Test
    public void testHandleTypePropertyValue_NoMatchingProperty() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{\"unknown\":\"StringType\"}");
        assertFalse(handler.handleTypePropertyValue(p, new TestContext(), "unknown", obj));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{\"property1\":1}");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        assertEquals(1, obj.getProperty1());
    }

    @Test
    public void testHandlePropertyValue_NoMatchingProperty() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{\"unknown\":1}");
        assertFalse(handler.handlePropertyValue(p, new TestContext(), "unknown", obj));
    }

    @Test
    public void testComplete() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{" +
                "\"type\":\"StringType\"," +
                "\"property1\":1" +
                "}");
        handler.complete(p, new TestContext(), obj);
        assertEquals("StringType", obj.getType());
        assertEquals(1, obj.getProperty1());
    }

    @Test
    public void testComplete_NullBean() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{" +
                "\"type\":\"StringType\"" +
                "}");
        handler.complete(p, new TestContext(), null);
        assertNull(handler.getBean());
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_MissingProperty() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{" +
                "\"type\":\"StringType\"" +
                "}");
        handler.complete(p, new TestContext(), obj);
        SettableBeanProperty property = obj.getProperty1Property();
        assertNull(property.getValue());
        property.set(obj, "value");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        handler.complete(p, new TestContext(), obj);
        assertNull(obj.getType());
        assertEquals("value", obj.getProperty1());
        String typeId = handler.getTypeIds()[0];
        assertEquals("StringType", typeId);
        // This should throw an exception
        handler.complete(p, new TestContext(), obj);
    }

    @Test
    public void testComplete_MissingTypeId() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{" +
                "\"property1\":1" +
                "}");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        handler.complete(p, new TestContext(), obj);
        SettableBeanProperty property = obj.getProperty1Property();
        assertNull(property.getValue());
        property.set(obj, "value");
        handler.handleTypePropertyValue(p, new TestContext(), "type", obj);
        handler.complete(p, new TestContext(), obj);
        assertNull(obj.getType());
        assertEquals("value", obj.getProperty1());
        String typeId = handler.getTypeIds()[0];
        assertEquals("", typeId);
        // This should not throw an exception
        handler.complete(p, new TestContext(), obj);
    }

    @Test
    public void testComplete_MissingPropertyAndTypeId() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{1}");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        handler.complete(p, new TestContext(), obj);
        SettableBeanProperty property = obj.getProperty1Property();
        assertNull(property.getValue());
        property.set(obj, "value");
        handler.handleTypePropertyValue(p, new TestContext(), "type", obj);
        handler.complete(p, new TestContext(), obj);
        assertNull(obj.getType());
        assertEquals("value", obj.getProperty1());
        String typeId = handler.getTypeIds()[0];
        assertEquals("", typeId);
        // This should not throw an exception
        handler.complete(p, new TestContext(), obj);
    }

    @Test
    public void testComplete_MissingPropertyAndTypeId_WithDefaultType() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{1}");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        handler.handleTypePropertyValue(p, new TestContext(), "type", obj);
        handler.complete(p, new TestContext(), obj);
        SettableBeanProperty property = obj.getProperty1Property();
        assertNull(property.getValue());
        property.set(obj, "value");
        handler.handlePropertyValue(p, new TestContext(), "property1", obj);
        handler.complete(p, new TestContext(), obj);
        assertNull(obj.getType());
        assertEquals("value", obj.getProperty1());
        String typeId = handler.getTypeIds()[0];
        assertEquals("", typeId);
        // This should not throw an exception
        handler.complete(p, new TestContext(), obj);
    }

    @Test
    public void testComplete_WithCreator() throws IOException {
        ObjectBean obj = new ObjectBean();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(ObjectBean.TYPE_PROPERTY, new StringTypeDeserializer())
                .addExternal(new Property1(), new Property1Deserializer())
                .build());
        JsonParser p = new JsonFactory().createParser("{" +
                "\"type\":\"StringType\"," +
                "\"creator\":{" +
                "\"property1\":1," +
                "\"property2\":2" +
                "}" +
                "}");
        handler.handleTypePropertyValue(p, new TestContext(), "type", obj);
        handler.handlePropertyValue(p, new TestContext(), "creator.property1", obj);
        handler.handlePropertyValue(p, new TestContext(), "creator.property2", obj);
        handler.complete(p, new TestContext(), obj);
        SettableBeanProperty property1 = obj.getProperty1Property();
        assertEquals(1, property1.getValue());
        SettableBeanProperty property2 = obj.getProperty2Property();
        assertEquals(2, property2.getValue());
        Object creator = obj.getCreator();
        assertNotNull(creator);
        Map creatorMap = (Map) creator;
        assertEquals(2, creatorMap.size());
        assertEquals(1, creatorMap.get("property1"));
        assertEquals(2, creatorMap.get("property2"));
    }

    private static class ObjectBean {
        private static final String TYPE_PROPERTY = "type";
        private static final String PROPERTY1 = "property1";
        private static final String PROPERTY2 = "creator.property1";

        private String type;
        private int property1;
        private Map creator;

        public String getType() {
            return type;
        }

        public int getProperty1() {
            return property1;
        }

        public Map getCreator() {
            return creator;
        }

        public SettableBeanProperty getProperty1Property() {
            return (SettableBeanProperty) this.getClass().getDeclaredField("property1")
                    .get(this);
        }

        public SettableBeanProperty getTypeProperty() {
            return (SettableBeanProperty) this.getClass().getDeclaredField("type")
                    .get(this);
        }

        public static class Property1Deserializer extends StdDeserializer<Integer> {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.getValueAsInt();
            }
        }

        public static class StringTypeDeserializer extends StdDeserializer<String> {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.getValueAsString();
            }

            @Override
            public TypeDeserializer getTypeDeserializer() {
                return null;
            }

            @Override
            public PropertyName getPropertyName() {
                return new PropertyName("StringType");
            }

            @Override
            public JavaType getValueType(DeserializationContext ctxt) {
                return ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(
                        String.class, new Class[0]);
            }
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return null;
        }

        @Override
        public BeanDescription getCurrentValueDescription() {
            return null;
        }

        @Override
        public Object constructorArg(String name) throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                JavaType valueType) throws IOException {
            return null;
        }

        @Override
        public PropertyName findPropertyName(String name) {
            return null;
        }

        @Override
        public String getBaseTypeName(Class<?> cls) {
            return null;
        }

        @Override
        public JavaType findValueType(DeserializationConfig config, BeanDescription beanDesc,
                JavaType propertyType, JavaType valueType) throws IOException {
            return null;
        }

        @Override
        public JsonFactory getFactoryUnderInput(JsonParser p) throws IOException {
            return null;
        }

        @Override
        public Object findCreator(DeserializationConfig config, BeanDescription beanDesc,
                Class<?> creatorType) throws IOException {
            return null;
        }

        @Override
        public void reportMappingException(MappingException me) throws IOException {
            throw me;
        }

        @Override
        public void reportMappingException(MappingException me, Object value) throws IOException {
            throw me;
        }

        @Override
        public void reportMappingException(MappingException me, Object value, String msgTemplate,
                Object... params) throws IOException {
            throw me;
        }

        @Override
        public void reportMissingValueException(String msgTemplate, Object[] params)
                throws IOException {
            throw new JsonMappingException(msgTemplate, params);
        }
    }
}