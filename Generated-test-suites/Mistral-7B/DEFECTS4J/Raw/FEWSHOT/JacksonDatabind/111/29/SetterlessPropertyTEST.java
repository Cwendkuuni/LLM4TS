package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object target = new Object() {
            List<Integer> list = new ArrayList<>();
        };

        JsonParser p = createParser("[]");
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "getList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null, Annotations.empty(), new AnnotatedMethod(
                        new MethodTestClass(), "setList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                int expectedSize = 2;
                List<Integer> list = (List<Integer>) BeanUtil.findProperty(instance, "list");
                list.clear();
                list.add(p.nextIntValue());
                list.add(p.nextIntValue());
                assertEquals(expectedSize, list.size());
            }
        };

        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume ','
        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume ']'

        List<Integer> list = (List<Integer>) BeanUtil.findProperty(target, "list");
        assertEquals(2, list.size());
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        Object target = new Object() {
            Map<String, Integer> map = new HashMap<>();
        };

        JsonParser p = createParser("{}");
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "getMap"),
                        Map.class, new TypeDeserializer() {
                            @Override
                            public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
                                    TypeDeserializer vtd) throws IOException {
                                Map<String, Integer> map = new HashMap<>();
                                map.put(p.nextTextValue(), p.nextIntValue());
                                return map;
                            }
                        }), null, Annotations.empty(), new AnnotatedMethod(
                        new MethodTestClass(), "setMap")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Map<String, Integer> map = (Map<String, Integer>) BeanUtil.findProperty(instance, "map");
                map.clear();
                map.put(p.nextTextValue(), p.nextIntValue());
            }
        };

        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume ','
        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume '}'

        Map<String, Integer> map = (Map<String, Integer>) BeanUtil.findProperty(target, "map");
        assertEquals("key1", map.keySet().iterator().next());
        assertEquals(1, map.size());
    }

    @Test
    public void testDeserializeAndSetWithAnnotation() throws Exception {
        Object target = new Object() {
            @JsonProperty("foo")
            List<Integer> list = new ArrayList<>();
        };

        JsonParser p = createParser("[]");
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "getList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null,
                new Annotations() {{
                    put(JsonProperty.class, new JsonProperty("foo"));
                }}, new AnnotatedMethod(new MethodTestClass(), "setList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                List<Integer> list = (List<Integer>) BeanUtil.findProperty(instance, "list");
                list.clear();
                list.add(p.nextIntValue());
            }
        };

        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume ','
        property.deserializeAndSet(p, new TestContext(), target);
        p.nextToken(); // consume ']'

        List<Integer> list = (List<Integer>) BeanUtil.findProperty(target, "list");
        assertEquals(2, list.size());
    }

    @Test
    public void testDeserializeAndSetNullValue() throws Exception {
        Object target = new Object() {
            List<Integer> list = new ArrayList<>();
        };

        JsonParser p = createParser("null");
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "getList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null, Annotations.empty(),
                new AnnotatedMethod(new MethodTestClass(), "setList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                List<Integer> list = (List<Integer>) BeanUtil.findProperty(instance, "list");
                list.clear();
            }
        };

        property.deserializeAndSet(p, new TestContext(), target);
        List<Integer> list = (List<Integer>) BeanUtil.findProperty(target, "list");
        assertNull(list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object target = new Object();
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "setList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null, Annotations.empty(),
                new AnnotatedMethod(new MethodTestClass(), "getList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                // Do nothing
            }
        };

        property.set(target, new int[0]);
    }

    @Test
    public void testNullGetMethod() throws Exception {
        Object target = new Object();
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(null, "getList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null, Annotations.empty(),
                new AnnotatedMethod(new MethodTestClass(), "setList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                fail("get method should be null");
            }
        };

        try {
            property.deserializeAndSet(createParser("[]"), new TestContext(), target);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCannotInvokeGetMethod() throws Exception {
        Object target = new Object();
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(new MethodTestClass(), "getList"), Integer[].class),
                ObjectTypeFactories.listType(Integer.class), null, Annotations.empty(),
                new AnnotatedMethod(new MethodTestClass(), "setList")) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                fail("deserializeAndSet should not be invoked");
            }

            @Override
            public Object getValue(Object c, Object inst) throws IOException {
                throw new UnsupportedOperationException("getProperty() should not be called");
            }
        };

        try {
            property.deserializeAndSet(createParser("[]"), new TestContext(), target);
        } catch (InvocationTargetException e) {
            // Expected
        }
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        JsonFactory factory = new JsonFactory();
        return factory.createParser(reader);
    }

    static class TestContext extends DeserializationContext {
        @Override
        public JavaType getType(String name) {
            return null;
        }

        @Override
        public Object instantiate(BeanDescription beanDesc, Object beanInstance) throws InstantiationException {
            return beanInstance;
        }
    }

    static class MethodTestClass {
        List<Integer> getList() {
            return list;
        }

        void setList(List<Integer> list) {
            this.list = list;
        }

        List<Integer> list = new ArrayList<>();
    }
}