package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"Foo\", \"data\":123}");
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Foo.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals(Foo.class, result.getClass());
        parser.close();

        parser = mapper.getFactory().createParser("{\"data\":123}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertNull(result);

        parser = mapper.getFactory().createParser("{\"type\":\"Bar\", \"data\":456}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertNull(result);

        parser = mapper.getFactory().createParser("{\"type\":\"Foo\", \"data\":\"abc\"}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals("abc", result);

        parser = mapper.getFactory().createParser("{\"type\":\"Foo\", \"data\":[]}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(0, list.size());

        parser = mapper.getFactory().createParser("{\"type\":\"Foo\", \"data\":[1, 2, 3]}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        List list2 = (List) result;
        assertEquals(3, list2.size());
        assertEquals(1, list2.get(0));
        assertEquals(2, list2.get(1));
        assertEquals(3, list2.get(2));

        parser = mapper.getFactory().createParser("{\"type\":\"Foo\", \"data\":{\"name\":\"John\", \"age\":30}}");
        result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        Foo foo = (Foo) result;
        assertEquals("John", foo.getName());
        assertEquals(30, foo.getAge());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"Foo\", 123]");
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Foo.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertEquals(Foo.class, result.getClass());
        parser.close();

        parser = mapper.getFactory().createParser("[\"Bar\", 456]");
        result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertNull(result);

        parser = mapper.getFactory().createParser("[\"Foo\", \"abc\"]");
        result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertEquals("abc", result);

        parser = mapper.getFactory().createParser("[\"Foo\", []]");
        result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(0, list.size());

        parser = mapper.getFactory().createParser("[\"Foo\", [1, 2, 3]]");
        result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        List list2 = (List) result;
        assertEquals(3, list2.size());
        assertEquals(1, list2.get(0));
        assertEquals(2, list2.get(1));
        assertEquals(3, list2.get(2));

        parser = mapper.getFactory().createParser("[\"Foo\", {\"name\":\"John\", \"age\":30}]");
        result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        Foo foo = (Foo) result;
        assertEquals("John", foo.getName());
        assertEquals(30, foo.getAge());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123");
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Number.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, null, false, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertEquals(123, result.doubleValue());
        parser.close();

        parser = mapper.getFactory().createParser("\"abc\"");
        result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertEquals("abc", result);
        parser.close();

        parser = mapper.getFactory().createParser("null");
        result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertNull(result);
        parser.close();

        parser = mapper.getFactory().createParser("true");
        result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        parser.close();

        parser = mapper.getFactory().createParser("false");
        result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        parser.close();
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Foo.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, null, As.PROPERTY);

        JsonDeserializer<Foo> fooDeserializer = new FooDeserializer();
        JsonDeserializer<Bar> barDeserializer = new BarDeserializer();
        idResolver.addDeserializer(Foo.class, fooDeserializer);
        idResolver.addDeserializer(Bar.class, barDeserializer);

        JsonDeserializer<Object> result = deserializer.findDeserializer(new TestContext(), "Foo");
        assertEquals(fooDeserializer, result);

        result = deserializer.findDeserializer(new TestContext(), "Bar");
        assertEquals(barDeserializer, result);

        result = deserializer.findDeserializer(new TestContext(), "Baz");
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Foo.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, null, As.PROPERTY);

        JsonDeserializer<Object> result = deserializer.findDefaultImplDeserializer(new TestContext());
        assertNull(result);

        idResolver.setValueInjector(Foo.class, new ToStringValueInjector());
        result = deserializer.findDefaultImplDeserializer(new TestContext());
        assertEquals(ToStringDeserializer.class, result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testReportWrongTokenException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"notype\":\"Foo\", \"data\":123}");
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType baseType = mapper.getTypeFactory().constructType(Foo.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, null, As.PROPERTY);

        deserializer.deserializeTypedFromObject(parser, new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectInstanceFinder _findAbstractInstance(DeserializationConfig config, BeanDescription beanDesc, JavaType baseType) {
            return null;
        }

        @Override
        public Object readValueAs(Class<?> clazz, JsonParser p) throws IOException {
            throw new RuntimeException("not reached");
        }

        @Override
        public Object readValueAs(Class<?> clazz) throws IOException {
            throw new RuntimeException("not reached");
        }

        @Override
        public Object readValueAs(Class<?> clazz, JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new RuntimeException("not reached");
        }

        @Override
        public Object instantiateValue(Serializable value, Type type, AnnotationMap annotations) throws InstantiationException {
            throw new RuntimeException("not reached");
        }
    }

    private static class Foo {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private static class FooDeserializer extends StdDeserializer<Foo> {
        public FooDeserializer() {
            this(null);
        }

        protected FooDeserializer(Class<Foo> t) {
            super(t);
        }

        @Override
        public Foo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Foo foo = new Foo();
            String name = p.getText();
            p.nextToken();
            foo.setName(name);
            foo.setAge(p.nextIntValue());
            return foo;
        }
    }

    private static class Bar {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class BarDeserializer extends StdDeserializer<Bar> {
        public BarDeserializer() {
            this(null);
        }

        protected BarDeserializer(Class<Bar> t) {
            super(t);
        }

        @Override
        public Bar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Bar bar = new Bar();
            bar.setName(p.getText());
            return bar;
        }
    }

    private static class ToStringDeserializer extends DeserializerProvider.BaseDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}