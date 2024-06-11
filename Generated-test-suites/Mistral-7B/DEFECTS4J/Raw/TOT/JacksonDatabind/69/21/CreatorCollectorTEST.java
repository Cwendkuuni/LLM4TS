package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreatorCollectorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void testDefaultCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(mapper.findCreator(Foo.class));
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(null);
        Assert.assertNotNull(foo);
    }

    @Test
    public void testStringCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addStringCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromString", String.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList("bar"));
        Assert.assertEquals("bar", foo.getBar());
    }

    @Test
    public void testIntCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addIntCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromInt", int.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList(42));
        Assert.assertEquals(42, foo.getBar());
    }

    @Test
    public void testLongCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addLongCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromLong", long.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList(42L));
        Assert.assertEquals(42, foo.getBar());
    }

    @Test
    public void testDoubleCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addDoubleCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromDouble", double.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList(42.0));
        Assert.assertEquals(42.0, foo.getBar(), 0.001);
    }

    @Test
    public void testBooleanCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addBooleanCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromBoolean", boolean.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList(true));
        Assert.assertTrue(foo.isBool());
    }

    @Test
    public void testDelegateCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooDelegate fooDelegate = new FooDelegate();
        collector.addDelegatingCreator(mapper.constructMethod(FooDelegate.class.getDeclaredMethod("createFoo", Object.class)), true, new SettableBeanProperty[] {new SettableBeanProperty.Std("bar", String.class)});
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList("bar"));
        Assert.assertEquals("bar", foo.getBar());
    }

    @Test
    public void testArrayDelegateCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooDelegate fooDelegate = new FooDelegate();
        Object[] args = {new String[] {"bar1", "bar2"}};
        collector.addDelegatingCreator(mapper.constructMethod(FooDelegate.class.getDeclaredMethod("createFooArray", Object[].class)), true, new SettableBeanProperty[] {new SettableBeanProperty.Std("bar", String[].class)});
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo[] foos = inst.createValueInstance(Arrays.asList(args));
        Assert.assertEquals("bar1", foos[0].getBar());
        Assert.assertEquals("bar2", foos[1].getBar());
    }

    @Test
    public void testPropertyBasedCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();
        collector.addPropertyCreator(mapper.constructMethod(FooBuilder.class.getDeclaredMethod("fromValues", Map.class)), true, new SettableBeanProperty[] {
                new SettableBeanProperty.Std("bar", String.class),
                new SettableBeanProperty.Std("bool", Boolean.class)
        });
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Map<String, Object> values = new HashMap<>();
        values.put("bar", "bar");
        values.put("bool", true);
        Foo foo = inst.createValueInstance(Arrays.asList(values));
        Assert.assertEquals("bar", foo.getBar());
        Assert.assertTrue(foo.isBool());
    }

    @Test
    public void testIncompleteParameterCreator() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addIncompleteParameter(mapper.constructParam(FooBuilder.class, "builder", 0));
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Map<String, Object> values = new HashMap<>();
        values.put("bar", "bar");
        FooBuilder builder = mapper.readerForUpdating(FooBuilder.class).readValue(mapper.readTree(mapper.writerWithView(mapper).writeValueAsString(values)));
        Foo foo = inst.createValueInstance(Arrays.asList(builder));
        Assert.assertEquals("bar", foo.getBar());
        Assert.assertNull(foo.getBool());
    }

    @Test
    public void testDefaultCreatorWithExplicitCreators() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addStringCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromString", String.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList("bar"));
        Assert.assertEquals("bar", foo.getBar());
        
        collector.setDefaultCreator(mapper.findCreator(Foo.class));
        inst = collector.constructValueInstantiator(mapper.getConfig());
        
        foo = inst.createValueInstance(null);
        Assert.assertNotNull(foo);
    }

    @Test
    public void testMultipleCreatorsWithSameType() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();

        collector.addStringCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromString", String.class)), true);
        collector.addStringCreator(mapper.constructMethod((Method) Foo.class.getDeclaredMethod("fromString2", String.class)), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

        Foo foo = inst.createValueInstance(Arrays.asList("bar"));
        Assert.assertEquals("bar", foo.getBar());

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = mapper.writerWithView(mapper.getSerializationConfig().with(new DefaultPrettyPrinter())).writeValueAsString(foo);
        Foo foo2 = mapper.readerForUpdating(Foo.class).readValue(mapper.readerForUpdating(Foo.class).readTree(json));
        Assert.assertEquals("bar", foo2.getBar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreatorProperties() throws JsonProcessingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        FooBuilder builder = new FooBuilder();

        collector.addPropertyCreator(mapper.constructMethod(FooBuilder.class.getDeclaredMethod("fromValues", Map.class)), true, new SettableBeanProperty[] {
                new SettableBeanProperty.Std("bar1", String.class),
                new SettableBeanProperty.Std("bar1", String.class)
        });
    }

    static class Foo {
        private String bar;
        private boolean bool;

        public Foo() {}

        public Foo(String bar) {
            this.bar = bar;
        }

        public Foo(int i) {
            this.bool = (i > 0);
        }

        public Foo(double d) {
            this.bool = (d > 0.0);
        }

        public Foo(boolean b) {
            this.bool = b;
        }

        public String getBar() {
            return bar;
        }

        public boolean isBool() {
            return bool;
        }

        public static Foo fromString(String str) {
            return new Foo(str);
        }

        public static Foo fromInt(int i) {
            return new Foo(i);
        }

        public static Foo fromLong(long l) {
            return new Foo(l);
        }

        public static Foo fromDouble(double d) {
            return new Foo(d);
        }

        public static Foo fromBoolean(boolean b) {
            return new Foo(b);
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "bar='" + bar + '\'' +
                    ", bool=" + bool +
                    '}';
        }
    }

    static class FooBuilder {
        private String bar;
        private boolean bool;

        public FooBuilder() {}

        public FooBuilder withBar(String bar) {
            this.bar = bar;
            return this;
        }

        public FooBuilder withBool(boolean bool) {
            this.bool = bool;
            return this;
        }

        public Foo build() {
            Foo foo = new Foo();
            BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
            SettableBeanProperty.Builder propBuilder = BeanProperty.stdBuilder(beanDesc.findProperty("bar"));
            SettableBeanProperty prop = propBuilder.build();
            prop.set(foo, bar);
            SettableBeanProperty boolProp = BeanProperty.stdBuilder(beanDesc.findProperty("bool")).build();
            boolProp.set(foo, bool);
            return foo;
        }

        public static FooBuilder fromValues(Map<String, Object> values) {
            FooBuilder builder = new FooBuilder();
            builder.withBar((String) values.getOrDefault("bar", null));
            builder.withBool((Boolean) values.getOrDefault("bool", null));
            return builder;
        }

        public static Foo fromString(String str) {
            FooBuilder builder = new FooBuilder();
            builder.withBar(str);
            return builder.build();
        }
    }

    static class FooDelegate {
        public Foo createFoo(Object arg) {
            if (arg instanceof String[]) {
                String[] args = (String[]) arg;
                return new Foo(args[0]);
            } else if (arg instanceof Object[]) {
                Object[] args = (Object[]) arg;
                return new Foo((String[]) args[0]);
            }
            throw new IllegalArgumentException("Unexpected argument type: " + arg.getClass());
        }
    }
}