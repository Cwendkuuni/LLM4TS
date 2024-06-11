package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    private ObjectMapper mapper;
    private DeserializationConfig config;
    private CreatorCollector collector;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        config = mapper.getConfig();
        BeanDescription beanDesc = mapper.introspect(DummyBean.class);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        collector.setDefaultCreator(new AnnotatedWithParams(DummyBean.class.getDeclaredConstructor()));
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        Object result = valueInstantiator.createValue("{}");
        assertEquals(new DummyBean(), result);
    }

    @Test
    public void testStringCreator() throws IOException {
        DummyStringConstructor dummyStringConstructor = new DummyStringConstructor();
        collector.addStringCreator(new AnnotatedWithParams(dummyStringConstructor, false));
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree("{\"name\":\"John\"}");
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean("John"), result);
    }

    @Test
    public void testIntCreator() throws IOException {
        DummyIntConstructor dummyIntConstructor = new DummyIntConstructor();
        collector.addIntCreator(new AnnotatedWithParams(dummyIntConstructor, false), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree(42);
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean(42), result);
    }

    @Test
    public void testLongCreator() throws IOException {
        DummyLongConstructor dummyLongConstructor = new DummyLongConstructor();
        collector.addLongCreator(new AnnotatedWithParams(dummyLongConstructor, false), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree(42L);
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean(42L), result);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        DummyDoubleConstructor dummyDoubleConstructor = new DummyDoubleConstructor();
        collector.addDoubleCreator(new AnnotatedWithParams(dummyDoubleConstructor, false), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree(42.0);
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean(42.0), result);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        DummyBooleanConstructor dummyBooleanConstructor = new DummyBooleanConstructor();
        collector.addBooleanCreator(new AnnotatedWithParams(dummyBooleanConstructor, false), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree(true);
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean(true), result);
    }

    @Test
    public void testDelegatingCreator() throws IOException {
        DummyDelegateConstructor dummyDelegateConstructor = new DummyDelegateConstructor();
        SettableBeanProperty[] injectables = new SettableBeanProperty[] {
            new SettableBeanProperty("delegate", new BeanPropertyDefinition(DummyDelegate.class))
        };
        collector.addDelegatingCreator(new AnnotatedWithParams(dummyDelegateConstructor, false, injectables), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectNode node = mapper.valueToTree(DummyDelegate.class.getName());
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean(new DummyDelegate()), result);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        DummyPropertyBasedConstructor dummyPropertyBasedConstructor = new DummyPropertyBasedConstructor();
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
            new SettableBeanProperty("property1", new BeanPropertyDefinition("property1", String.class)),
            new SettableBeanProperty("property2", new BeanPropertyDefinition("property2", Integer.class))
        };
        collector.addPropertyCreator(new AnnotatedWithParams(dummyPropertyBasedConstructor, false, properties), false);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(config);
        ObjectReader reader = mapper.readerForUpdating(DummyBean.class);
        ObjectNode node = reader.readValue(mapper.readTree("{\"property1\":\"value1\", \"property2\":42}"));
        Object result = valueInstantiator.createValue(node);
        assertEquals(new DummyBean("value1", 42), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        DummyStringConstructor dummyStringConstructor = new DummyStringConstructor();
        collector.addStringCreator(new AnnotatedWithParams(dummyStringConstructor, false));
        collector.addStringCreator(new AnnotatedWithParams(dummyStringConstructor, false));
        collector.constructValueInstantiator(config);
    }

    @Test
    public void testHasDefaultCreator() {
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        DummyDelegateConstructor dummyDelegateConstructor = new DummyDelegateConstructor();
        collector.addDelegatingCreator(new AnnotatedWithParams(dummyDelegateConstructor, false, new SettableBeanProperty[0]), false);
        assertTrue(collector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        DummyPropertyBasedConstructor dummyPropertyBasedConstructor = new DummyPropertyBasedConstructor();
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
            new SettableBeanProperty("property", new BeanPropertyDefinition("property", String.class))
        };
        collector.addPropertyCreator(new AnnotatedWithParams(dummyPropertyBasedConstructor, false, properties), false);
        assertTrue(collector.hasPropertyBasedCreator());
    }

    private static class AnnotatedWithParams extends AnnotatedElement {
        private final AnnotatedMember _member;

        public AnnotatedWithParams(AnnotatedMember member) {
            super(member.getAnnotated());
            _member = member;
        }

        public AnnotatedMember getAnnotatedMember() {
            return _member;
        }

        @Override
        public AnnotatedClass getAnnotated() {
            return new NopAnnotationIntrospector().findCreatorAnnotation(_member);
        }

        @Override
        public AnnotatedMethod getMethod() {
            return (AnnotatedMethod) _member;
        }

        @Override
        public AnnotatedField getField() {
            return (AnnotatedField) _member;
        }

        @Override
        public AnnotatedConstructor getConstructor() {
            return (AnnotatedConstructor) _member;
        }

        @Override
        public JavaType getParameterType(int index) {
            return _member.getDeclaringClass().getDeclaredConstructors()[0].getParameterTypes()[index];
        }
    }

    private static class DummyBean {
        private String name;
        private int number;
        private double price;
        private boolean flag;

        public DummyBean() {
        }

        public DummyBean(String name) {
            this.name = name;
        }

        public DummyBean(int number) {
            this.number = number;
        }

        public DummyBean(double price) {
            this.price = price;
        }

        public DummyBean(boolean flag) {
            this.flag = flag;
        }

        public DummyBean(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public DummyBean(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public DummyBean(String name, int number, double price) {
            this.name = name;
            this.number = number;
            this.price = price;
        }

        public DummyBean(boolean flag, DummyDelegate delegate) {
            this.flag = flag;
            this.delegate = delegate;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public double getPrice() {
            return price;
        }

        public boolean isFlag() {
            return flag;
        }

        public DummyDelegate getDelegate() {
            return delegate;
        }

        private DummyDelegate delegate;

        @Override
        public Class<?> getClass() {
            return DummyBean.class;
        }

        @Override
        public JavaType getValueType() {
            return mapper.getTypeFactory().constructType(DummyBean.class);
        }
    }

    private static class DummyDelegate {
        // empty
    }

    private static class DummyStringConstructor
            extends DummyCreator<DummyBean, String> {

        @JsonCreator
        public DummyStringConstructor(@JsonProperty("name") String name) {
            super(name);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, String value)
                throws IOException {
            return new DummyBean(value);
        }
    }

    private static class DummyIntConstructor
            extends DummyCreator<DummyBean, Integer> {

        @JsonCreator
        public DummyIntConstructor(@JsonProperty("number") int number) {
            super(number);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, Integer value)
                throws IOException {
            return new DummyBean(value);
        }
    }

    private static class DummyLongConstructor
            extends DummyCreator<DummyBean, Long> {

        @JsonCreator
        public DummyLongConstructor(@JsonProperty("number") long number) {
            super(number);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, Long value)
                throws IOException {
            return new DummyBean(value);
        }
    }

    private static class DummyDoubleConstructor
            extends DummyCreator<DummyBean, Double> {

        @JsonCreator
        public DummyDoubleConstructor(@JsonProperty("price") double price) {
            super(price);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, Double value)
                throws IOException {
            return new DummyBean(value);
        }
    }

    private static class DummyBooleanConstructor
            extends DummyCreator<DummyBean, Boolean> {

        @JsonCreator
        public DummyBooleanConstructor(@JsonProperty("flag") Boolean flag) {
            super(flag);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, Boolean value)
                throws IOException {
            return new DummyBean(value);
        }
    }

    private static class DummyDelegateConstructor
            extends DummyCreator<DummyBean, Object> {

        @JsonCreator
        public DummyDelegateConstructor(@JsonProperty("delegate") Object delegate) {
            super(delegate);
        }

        @Override
        public DummyBean create(DeserializationContext ctxt, BeanProperty property, Object value)
                throws IOException {
            return new DummyBean(true, (DummyDelegate) value);
        }
    }

    private static class DummyCreator<T, U> {
        private final U value;

        public DummyCreator(U value) {
            this.value = value;
        }

        public T create(DeserializationContext ctxt, BeanProperty property, U value)
                throws IOException {
            ObjectMapper mapper = (ObjectMapper) ctxt.getDefaultInstance();
            ObjectReader reader = mapper.readerForUpdating(property.getType());
            ObjectNode node = mapper.valueToTree(value);
            Object result = reader.readValue(node);
            @SuppressWarnings("unchecked")
            T typeResult = (T) result;
            return typeResult;
        }

        @Override
        public String toString() {
            return "DummyCreator{" + "value=" + value + '}';
        }
    }
}