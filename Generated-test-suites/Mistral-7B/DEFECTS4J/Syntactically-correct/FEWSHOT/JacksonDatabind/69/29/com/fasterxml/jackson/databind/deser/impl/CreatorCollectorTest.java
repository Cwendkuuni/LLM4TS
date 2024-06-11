package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        assertNotNull(collector);
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        DeserializationConfig config = createTestConfig();
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method defaultMethod = TestBean.class.getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams creator = new AnnotatedWithParams(defaultMethod, null);
        collector.setDefaultCreator(creator);

        AnnotatedWithParams defaultCreator = collector._creators[CreatorCollector.C_DEFAULT];
        assertNotNull(defaultCreator);
        assertTrue(defaultCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method stringMethod = TestBean.class.getDeclaredMethod("stringCreator");
        AnnotatedWithParams creator = new AnnotatedWithParams(stringMethod, null);
        collector.addStringCreator(creator, false);

        AnnotatedWithParams stringCreator = collector._creators[CreatorCollector.C_STRING];
        assertNotNull(stringCreator);
        assertTrue(stringCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method intMethod = TestBean.class.getDeclaredMethod("intCreator");
        AnnotatedWithParams creator = new AnnotatedWithParams(intMethod, null);
        collector.addIntCreator(creator, false);

        AnnotatedWithParams intCreator = collector._creators[CreatorCollector.C_INT];
        assertNotNull(intCreator);
        assertTrue(intCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method longMethod = TestBean.class.getDeclaredMethod("longCreator");
        AnnotatedWithParams creator = new AnnotatedWithParams(longMethod, null);
        collector.addLongCreator(creator, false);

        AnnotatedWithParams longCreator = collector._creators[CreatorCollector.C_LONG];
        assertNotNull(longCreator);
        assertTrue(longCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method doubleMethod = TestBean.class.getDeclaredMethod("doubleCreator");
        AnnotatedWithParams creator = new AnnotatedWithParams(doubleMethod, null);
        collector.addDoubleCreator(creator, false);

        AnnotatedWithParams doubleCreator = collector._creators[CreatorCollector.C_DOUBLE];
        assertNotNull(doubleCreator);
        assertTrue(doubleCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method booleanMethod = TestBean.class.getDeclaredMethod("booleanCreator");
        AnnotatedWithParams creator = new AnnotatedWithParams(booleanMethod, null);
        collector.addBooleanCreator(creator, false);

        AnnotatedWithParams booleanCreator = collector._creators[CreatorCollector.C_BOOLEAN];
        assertNotNull(booleanCreator);
        assertTrue(booleanCreator.getAnnotated().equals(creator.getAnnotated()));
    }

    @Test
    public void testAddDelegatingCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method delegatingMethod = TestBeanDelegate.class.getDeclaredMethod("delegateConstructor", String.class);
        AnnotatedWithParams creator = new AnnotatedWithParams(delegatingMethod, null);
        SettableBeanProperty[] injectables = new SettableBeanProperty[] { new TestBeanProperty() };
        collector.addDelegatingCreator(creator, false, injectables);

        AnnotatedWithParams delegateCreator = collector._creators[CreatorCollector.C_DELEGATE];
        assertNotNull(delegateCreator);
        assertTrue(delegateCreator.getAnnotated().equals(creator.getAnnotated()));
        assertNotNull(collector._delegateArgs);
        assertTrue(collector._delegateArgs[0] instanceof TestBeanProperty);
    }

    @Test
    public void testAddPropertyCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method propertyMethod = TestBeanPropertyCreator.class.getDeclaredMethod("propertyConstructor", String.class, int.class);
        AnnotatedWithParams creator = new AnnotatedWithParams(propertyMethod, null);
        SettableBeanProperty[] properties = new SettableBeanProperty[] { new TestBeanProperty() };
        collector.addPropertyCreator(creator, false, properties);

        AnnotatedWithParams propertyCreator = collector._creators[CreatorCollector.C_PROPS];
        assertNotNull(propertyCreator);
        assertTrue(propertyCreator.getAnnotated().equals(creator.getAnnotated()));
        assertNotNull(collector._propertyBasedArgs);
        assertTrue(collector._propertyBasedArgs[0] instanceof TestBeanProperty);
    }

    @Test
    public void testAddArrayDelegatingCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, null);

        Method arrayDelegatingMethod = TestBeanArrayDelegate.class.getDeclaredMethod("arrayDelegateConstructor", String[].class);
        AnnotatedWithParams creator = new AnnotatedWithParams(arrayDelegatingMethod, null);
        SettableBeanProperty[] injectables = new SettableBeanProperty[] { new TestBeanProperty() };
        collector.addDelegatingCreator(creator, false, injectables);

        AnnotatedWithParams arrayDelegateCreator = collector._creators[CreatorCollector.C_ARRAY_DELEGATE];
        assertNotNull(arrayDelegateCreator);
        assertTrue(arrayDelegateCreator.getAnnotated().equals(creator.getAnnotated()));
        assertNotNull(collector._arrayDelegateArgs);
        assertTrue(collector._arrayDelegateArgs[0] instanceof TestBeanProperty);
    }

    private BeanDescription createTestBeanDescription() {
        AnnotatedClass ac = new AnnotatedClass(TestBean.class);
        BeanDescription beanDesc = BeanDescription.findStandardBeanDescription(ac, null, null);
        return beanDesc;
    }

    private DeserializationConfig createTestConfig() {
        DeserializationConfig config = new DeserializationConfig();
        config.disable(MapperFeature.AUTO_DETECT_GETTERS);
        config.disable(MapperFeature.AUTO_DETECT_SETTERS);
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        return config;
    }

    private static class TestBean {
        private TestBean() {}

        @JsonCreator
        public TestBean defaultConstructor() {}

        @JsonCreator
        public TestBean(@JsonProperty("s") String s) { this.s = s; }

        @JsonProperty
        private String s;

        @JsonCreator
        public static TestBean stringCreator(String s) { return new TestBean(s); }

        @JsonCreator
        public static TestBean intCreator(@JsonProperty("i") int i) { return new TestBean(); }

        @JsonCreator
        public static TestBean longCreator(@JsonProperty("l") long l) { return new TestBean(); }

        @JsonCreator
        public static TestBean doubleCreator(@JsonProperty("d") double d) { return new TestBean(); }

        @JsonCreator
        public static TestBean booleanCreator(@JsonProperty("b") boolean b) { return new TestBean(); }
    }

    private static class TestBeanDelegate {
        private String s;

        public TestBeanDelegate(String s) { this.s = s; }

        @JsonCreator
        public static TestBeanDelegate delegateConstructor(String s) { return new TestBeanDelegate(s); }
    }

    private static class TestBeanProperty {
        private String s;

        public TestBeanProperty() {}

        public String getS() { return s; }

        public void setS(String s) { this.s = s; }
    }

    private static class TestBeanPropertyCreator {
        private String s;
        private int i;

        public TestBeanPropertyCreator(String s, int i) {
            this.s = s;
            this.i = i;
        }

        @JsonCreator
        public static TestBeanPropertyCreator propertyConstructor(@JsonProperty("s") String s, @JsonProperty("i") int i) {
            TestBeanPropertyCreator bean = new TestBeanPropertyCreator(s, i);
            beansWithProperties.add(bean);
            return bean;
        }

        public String getS() { return s; }

        public int getI() { return i; }
    }

    private static class TestBeanArrayDelegate {
        private String[] ss;

        public TestBeanArrayDelegate(String[] ss) { this.ss = ss; }

        @JsonCreator
        public static TestBeanArrayDelegate arrayDelegateConstructor(String[] ss) { return new TestBeanArrayDelegate(ss); }
    }
}