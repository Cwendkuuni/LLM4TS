package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectNode node;

    @Before
    public void setUp() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "prop1"), new StringTypeDeserializer(String.class));
        properties[1] = new ExtTypedProperty(new SettableBeanProperty(TestBean.class, "prop2"), new NumberTypeDeserializer(Integer.class));

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null).start();

        ObjectMapper mapper = new ObjectMapper();
        node = mapper.createObjectNode();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        node.put("prop1", "type1");
        handler.handleTypePropertyValue(mapper.getFactory().createParser(node), mockContext(), "prop1", new TestBean());
        assertEquals("type1", handler.getTypeIds()[0]);

        node.put("prop2", "type2");
        handler.handleTypePropertyValue(mapper.getFactory().createParser(node), mockContext(), "prop2", new TestBean());
        assertEquals("type2", handler.getTypeIds()[1]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        node.put("prop1", "value1");
        handler.handlePropertyValue(mapper.getFactory().createParser(node), mockContext(), "prop1", new TestBean());
        assertEquals("value1", handler.getProperties()[0].getProperty().get(new TestBean()));

        node.put("prop2", "value2");
        handler.handlePropertyValue(mapper.getFactory().createParser(node), mockContext(), "prop2", new TestBean());
        assertEquals(2, ((Number) handler.getProperties()[1].getProperty().get(new TestBean())).intValue());

        node.put("prop1", "value3");
        handler.handlePropertyValue(mapper.getFactory().createParser(node), mockContext(), "prop1", new TestBean());
        assertEquals("value3", handler.getProperties()[0].getProperty().get(new TestBean()));
    }

    @Test
    public void testComplete() throws IOException {
        node.put("prop1", "type1");
        node.put("prop1", "value1");
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertEquals("value1", handler.getProperties()[0].getProperty().get(new TestBean()));

        node.put("prop2", "type2");
        node.put("prop2", 2);
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertEquals(2, ((Number) handler.getProperties()[1].getProperty().get(new TestBean())));

        node.put("prop1", null);
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertNull(handler.getProperties()[0].getProperty().get(new TestBean()));

        node.put("prop2", null);
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertNull(handler.getProperties()[1].getProperty().get(new TestBean()));

        node.put("prop1", "missingTypeId");
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertNull(handler.getTypeIds()[0]);

        node.put("prop2", "missingTypeId");
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertNull(handler.getTypeIds()[1]);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new SettableBeanProperty(TestBeanWithCreator.class, "prop1"), new CreatorTypeDeserializer(String.class)));
        properties.add(new ExtTypedProperty(new SettableBeanProperty(TestBeanWithCreator.class, "prop2"), new CreatorTypeDeserializer(Integer.class)));

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null).start();

        node.put("prop1", "type1");
        node.put("prop1", "value1");
        TestBeanWithCreator bean = handler.complete(mapper.getFactory().createParser(node), mockContext(), null, null);
        assertEquals("value1", handler.getProperties()[0].getProperty().get(bean));

        node.put("prop2", "type2");
        node.put("prop2", 2);
        bean = handler.complete(mapper.getFactory().createParser(node), mockContext(), null, null);
        assertEquals(2, ((Number) handler.getProperties()[1].getProperty().get(bean)));
    }

    @Test
    public void testEmpty() throws IOException {
        handler.complete(mapper.getFactory().createParser(node), mockContext(), new TestBean());
        assertEquals(0, handler.getTypeIds().length);
        assertEquals(0, handler.getProperties().length);
    }

    private static DeserializationContext mockContext() {
        return new TestContext();
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public void reportMappingError(MessageBase msg) throws IOException {
            fail(msg.getMessage());
        }

        @Override
        public Object findValueId(TypeDeserializer deserializer, Object value, Class<?> valueType) {
            return null;
        }
    }

    private static class CreatorTypeDeserializer extends TypeDeserializer {
        private final Class<?> valueClass;

        public CreatorTypeDeserializer(Class<?> vc) {
            super(null, null);
            valueClass = vc;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            String id = p.getText();
            JsonParser parser = p.getCodec().treeAsTokens(p.getParser(), null);
            JsonToken root = parser.nextToken();
            if (root.isValueIdentifier()) {
                IdReader reader = ctxt.findValueIdResolver(valueClass);
                return reader.readValue(parser, valueClass);
            }
            return vd.deserialize(parser, ctxt);
        }

        @Override
        public String getPropertyName() {
            return null;
        }
    }

    private static class TestBean {
        private String prop1;
        private Integer prop2;
    }

    private static class TestBeanWithCreator {
        private String prop1;
        private Integer prop2;

        public static Creator<TestBeanWithCreator> create(ObjectCodec codec, MapperFactory factory) throws IOException {
            return TestBeanWithCreator.class.newInstance();
        }

        static {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) throws Exception {
                    SettableBeanPropertyCreator creator = new SettableBeanPropertyCreator(TestBeanWithCreator.class);
                    context.addBeanFactory(creator);
                }
            });
            creator = mapper.getTypeFactory().constructValueType(TestBeanWithCreator.class, String.class, Integer.class);
            ObjectFactory factory = mapper.getTypeFactory();
            creator.setCreator(factory.constructCreator(TestBeanWithCreator.class, null));
        }

        private static final Creator<TestBeanWithCreator> creator;

        public static TestBeanWithCreator create(ObjectCodec codec) throws IOException {
            return creator.createInstance(codec);
        }
    }
}