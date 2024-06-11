package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaults() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(Vanilla.TYPE_COLLECTION, inst.getClass().getField("_type").getInt(inst));
    }

    @Test
    public void testCreatorCollectorAddDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("defaultConstructor"), null);
        collector.setDefaultCreator(creator);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getDefaultCreator());
    }

    @Test
    public void testCreatorCollectorAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("stringCreator"), null);
        collector.addStringCreator(creator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getStringCreator());
    }

    @Test
    public void testCreatorCollectorAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("intCreator"), null);
        collector.addIntCreator(creator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getIntCreator());
    }

    @Test
    public void testCreatorCollectorAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("longCreator"), null);
        collector.addLongCreator(creator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getLongCreator());
    }

    @Test
    public void testCreatorCollectorAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("doubleCreator"), null);
        collector.addDoubleCreator(creator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getDoubleCreator());
    }

    @Test
    public void testCreatorCollectorAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("booleanCreator"), null);
        collector.addBooleanCreator(creator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getBooleanCreator());
    }

    @Test
    public void testCreatorCollectorAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("delegateCreator"), null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("injectable", TestBean.class.getDeclaredField("injectableField"));
        collector.addDelegatingCreator(creator, false, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getDelegateCreator());
        assertEquals(1, inst.getDelegateArguments().length);
        assertEquals("injectable", inst.getDelegateArguments()[0].getName());
    }

    @Test
    public void testCreatorCollectorAddPropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("propertyBasedCreator"), null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", TestBean.class.getDeclaredField("property1Field"), null);
        properties[1] = new CreatorProperty("property2", TestBean.class.getDeclaredField("property2Field"), null);
        collector.addPropertyCreator(creator, false, properties);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(inst.getPropertyBasedCreator());
        assertEquals(2, inst.getPropertyBasedArguments().length);
        assertTrue(inst.getPropertyBasedArguments()[0].getName().equals("property1"));
        assertTrue(inst.getPropertyBasedArguments()[1].getName().equals("property2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorAddDuplicateCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("stringCreator"), null);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorAddDuplicateExplicitCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("stringCreator"), null);
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorAddDuplicateNonExplicitCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("stringCreator"), null);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorAddDuplicatePropertyCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("propertyBasedCreator"), null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", TestBean.class.getDeclaredField("property1Field"), null);
        properties[1] = new CreatorProperty("property1", TestBean.class.getDeclaredField("property1Field"), null);
        collector.addPropertyCreator(creator, false, properties);
    }

    @Test
    public void testCreatorCollectorRemovePropertyCreator() {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams(TestBean.class.getDeclaredMethod("propertyBasedCreator"), null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", TestBean.class.getDeclaredField("property1Field"), null);
        properties[1] = new CreatorProperty("property2", TestBean.class.getDeclaredField("property2Field"), null);
        collector.addPropertyCreator(creator, false, properties);

        collector.removePropertyCreator("property1");

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertEquals(1, inst.getPropertyBasedArguments().length);
        assertFalse(inst.getPropertyBasedArguments()[0].getName().equals("property1"));
        assertTrue(inst.getPropertyBasedArguments()[0].getName().equals("property2"));
    }

    private static class TestBean {
        public TestBean() {}

        public TestBean(String s) {}

        public TestBean(int i) {}

        public TestBean(long l) {}

        public TestBean(double d) {}

        public TestBean(boolean b) {}

        public TestBean(String s, int i) {}

        public TestBean(String s, long l) {}

        public TestBean(String s, double d) {}

        public TestBean(boolean b, int i) {}

        public TestBean(boolean b, long l) {}

        public TestBean(boolean b, double d) {}

        public TestBean(String s, int i, long l) {}

        public TestBean(String s, double d, int i) {}

        public TestBean(String s, double d, long l) {}

        public TestBean(boolean b, int i, long l) {}

        public TestBean(boolean b, int i, double d) {}

        public TestBean(boolean b, long l, double d) {}

        @javax.annotation.PostConstruct
        public void postConstruct() {}

        public void defaultConstructor() {}

        public TestBean(String s, int i, long l, double d, boolean b) {}

        public String stringCreator(String s) { return new TestBean(s); }

        public int intCreator(int i) { return new TestBean(i); }

        public long longCreator(long l) { return new TestBean(l); }

        public double doubleCreator(double d) { return new TestBean(d); }

        public boolean booleanCreator(boolean b) { return new TestBean(b); }

        public TestBean delegateCreator(TestBean delegate) {
            field = delegate;
            return this;
        }

        public TestBean propertyBasedCreator(String p1, int p2) {
            property1Field = p1;
            property2Field = p2;
            return this;
        }

        private String field;
        private String property1Field;
        private int property2Field;
    }
}