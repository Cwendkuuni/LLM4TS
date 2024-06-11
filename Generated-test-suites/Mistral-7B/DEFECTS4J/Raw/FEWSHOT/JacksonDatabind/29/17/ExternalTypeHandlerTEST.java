package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop1\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handleTypePropertyValue(p, ctxt, "prop1", bean);
        assertEquals("foo", bean.getProp1());
    }

    @Test
    public void testHandleTypePropertyValue_MissingProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"missingProp\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handleTypePropertyValue(p, ctxt, "missingProp", bean);
        assertNull(bean.getProp1());
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop1\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        assertEquals("foo", bean.getProp1());
    }

    @Test
    public void testHandlePropertyValue_MissingTypeProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop2\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handlePropertyValue(p, ctxt, "prop2", bean);
        assertNull(bean.getProp1());
    }

    @Test
    public void testComplete() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop1\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handleTypePropertyValue(p, ctxt, "prop1", bean);
        handler.complete(p, ctxt, bean);
        assertEquals("foo", bean.getProp1());
    }

    @Test
    public void testComplete_MissingTypeId() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.complete(p, ctxt, bean);
        assertNull(bean.getProp1());
    }

    @Test
    public void testComplete_MissingProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop2\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handleTypePropertyValue(p, ctxt, "prop2", bean);
        handler.complete(p, ctxt, bean);
        assertNull(bean.getProp1());
    }

    @Test
    public void testComplete_WithDefaultType() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), new Object(), null, null);
        Object bean = new TestBean();
        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        handler.complete(p, ctxt, bean);
        assertNotNull(bean.getProp1());
        assertEquals("", bean.getProp1());
    }

    @Test
    public void testComplete_WithCreator() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBeanCreator.TestCreatorProperty("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{\"prop1\":\"foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), null, null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object bean = handler.complete(p, ctxt, buffer, new TestBeanCreator());
        assertEquals("foo", ((TestBean) bean).getProp1());
    }

    @Test
    public void testComplete_WithCreator_MissingProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new TestBeanCreator.TestCreatorProperty("prop1"), new StringTypeDeserializer())
                .build();
        JsonParser p = createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(p.getCodec(), null, null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object bean = handler.complete(p, ctxt, buffer, new TestBeanCreator());
        assertNull(((TestBean) bean).getProp1());
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        ObjectCodec codec = new ObjectMapper().findValueCodec(ReaderBasedModel.Class, reader);
        return ((JsonFactory) codec.getCodec()).createParser(reader);
    }

    static class TestBean {
        private String prop1;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    static class TestBeanCreator {
        public static class TestCreatorProperty {
            private final String name;

            public TestCreatorProperty(String name) {
                this.name = name;
            }

            public SettableBeanProperty findCreatorProperty(String name) {
                if (this.name.equals(name)) {
                    return TestBean.class.getDeclaredField("prop1").getAnnouncedSetter();
                }
                return null;
            }
        }

        public Object build(DeserializationContext ctxt, PropertyValueBuffer v) throws IOException {
            TestBean bean = new TestBean();
            for (Map.Entry<String, Object> entry : v.asMap().entrySet()) {
                SettableBeanProperty prop = findCreatorProperty(entry.getKey());
                if (prop != null) {
                    prop.set(bean, entry.getValue());
                }
            }
            return bean;
        }
    }

    static class StringTypeDeserializer extends TypeDeserializer {
        @Override
        public String getPropertyName() {
            return "typeId";
        }

        @Override
        public JsonDeserializer<?> createContextualDeserializer(DeserializationContext ctxt, BeanProperty property)
                throws JsonMappingException {
            return new StringDeserializer();
        }
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class Builder {
        private final ArrayList<ExtTypedProperty> _properties = new ArrayList<ExtTypedProperty>();
        private final HashMap<String, Integer> _nameToPropertyIndex = new HashMap<String, Integer>();

        public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
            Integer index = _properties.size();
            _properties.add(new ExtTypedProperty(property, typeDeser));
            _nameToPropertyIndex.put(property.getName(), index);
            _nameToPropertyIndex.put(typeDeser.getPropertyName(), index);
        }

        public ExternalTypeHandler build() {
            return new ExternalTypeHandler(_properties.toArray(new ExtTypedProperty[_properties.size()]),
                    _nameToPropertyIndex, null, null);
        }
    }

    private static class ExtTypedProperty {
        private final SettableBeanProperty _property;
        private final TypeDeserializer _typeDeserializer;
        private final String _typePropertyName;

        public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser) {
            _property = property;
            _typeDeserializer = typeDeser;
            _typePropertyName = typeDeser.getPropertyName();
        }

        public boolean hasTypePropertyName(String n) {
            return n.equals(_typePropertyName);
        }

        public boolean hasDefaultType() {
            return _typeDeserializer.getDefaultImpl() != null;
        }

        public String getDefaultTypeId() {
            Class<?> defaultType = _typeDeserializer.getDefaultImpl();
            if (defaultType == null) {
                return null;
            }
            return _typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
        }

        public String getTypePropertyName() {
            return _typePropertyName;
        }

        public SettableBeanProperty getProperty() {
            return _property;
        }
    }
}