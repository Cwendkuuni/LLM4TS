package com.example; // replace with your test package

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
    // 

    @org.junit.Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(StdValueInstantiator.class, instantiator.getClass());
    }

    @org.junit.Test
    public void testSetDefaultCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method defaultCreatorMethod = new TestBean().getClass().getDeclaredMethod("defaultConstructor");
        creatorCollector.setDefaultCreator(createAnnotatedWithParams(defaultCreatorMethod));
        AnnotatedWithParams defaultCreator = creatorCollector.getDefaultCreator();
        assertNotNull(defaultCreator);
    }

    @org.junit.Test
    public void testAddStringCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method stringCreatorMethod = new TestBean().getClass().getDeclaredMethod("stringCreator", String.class);
        creatorCollector.addStringCreator(createAnnotatedWithParams(stringCreatorMethod), false);
        AnnotatedWithParams stringCreator = creatorCollector.getCreator(CreatorCollector.C_STRING);
        assertNotNull(stringCreator);
    }

    @org.junit.Test
    public void testAddIntCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method intCreatorMethod = new TestBean().getClass().getDeclaredMethod("intCreator", int.class);
        creatorCollector.addIntCreator(createAnnotatedWithParams(intCreatorMethod), false);
        AnnotatedWithParams intCreator = creatorCollector.getCreator(CreatorCollector.C_INT);
        assertNotNull(intCreator);
    }

    @org.junit.Test
    public void testAddLongCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method longCreatorMethod = new TestBean().getClass().getDeclaredMethod("longCreator", long.class);
        creatorCollector.addLongCreator(createAnnotatedWithParams(longCreatorMethod), false);
        AnnotatedWithParams longCreator = creatorCollector.getCreator(CreatorCollector.C_LONG);
        assertNotNull(longCreator);
    }

    @org.junit.Test
    public void testAddDoubleCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method doubleCreatorMethod = new TestBean().getClass().getDeclaredMethod("doubleCreator", double.class);
        creatorCollector.addDoubleCreator(createAnnotatedWithParams(doubleCreatorMethod), false);
        AnnotatedWithParams doubleCreator = creatorCollector.getCreator(CreatorCollector.C_DOUBLE);
        assertNotNull(doubleCreator);
    }

    @org.junit.Test
    public void testAddBooleanCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method booleanCreatorMethod = new TestBean().getClass().getDeclaredMethod("booleanCreator", boolean.class);
        creatorCollector.addBooleanCreator(createAnnotatedWithParams(booleanCreatorMethod), false);
        AnnotatedWithParams booleanCreator = creatorCollector.getCreator(CreatorCollector.C_BOOLEAN);
        assertNotNull(booleanCreator);
    }

    @org.junit.Test
    public void testAddDelegatingCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method delegateCreatorMethod = new TestBeanDelegate().getClass().getDeclaredMethod("delegateConstructor", TestBean.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { createSettableBeanProperty("field1") };
        creatorCollector.addDelegatingCreator(createAnnotatedWithParams(delegateCreatorMethod), false, delegateArgs);
        AnnotatedWithParams delegateCreator = creatorCollector.getCreator(CreatorCollector.C_DELEGATE);
        assertNotNull(delegateCreator);
    }

    @org.junit.Test
    public void testAddPropertyCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method propertyCreatorMethod = new TestBeanProperty.TestBeanPropertyBuilder().buildClass().getDeclaredMethod("propertyCreator", String.class, int.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] { createSettableBeanProperty("field1"), createSettableBeanProperty("field2") };
        creatorCollector.addPropertyCreator(createAnnotatedWithParams(propertyCreatorMethod), false, propertyBasedArgs);
        AnnotatedWithParams propertyCreator = creatorCollector.getCreator(CreatorCollector.C_PROPS);
        assertNotNull(propertyCreator);
    }

    @org.junit.Test
    public void testGetCreatorWithInvalidIndex() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        assertNull(creatorCollector.getCreator(CreatorCollector.C_INVALID));
    }

    @org.junit.Test
    public void testConstructValueInstantiatorWithNonStandardCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        AnnotatedWithParams stringCreator = createStringCreatorWithNonStandardName(beanDesc);
        creatorCollector.addStringCreator(stringCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
        instantiator.configureFromStringCreator(stringCreator);
        assertFalse(instantiator.hasDefaultCreator());
        assertFalse(instantiator.hasDelegatingCreator());
        assertFalse(instantiator.hasPropertyBasedCreator());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method defaultCreatorMethod = new TestBean().getClass().getDeclaredMethod("defaultConstructor");
        creatorCollector.setDefaultCreator(createAnnotatedWithParams(defaultCreatorMethod));
        Method stringCreatorMethod = new TestBean().getClass().getDeclaredMethod("stringCreator", String.class);
        creatorCollector.addStringCreator(createAnnotatedWithParams(stringCreatorMethod), false);
        creatorCollector.addStringCreator(createAnnotatedWithParams(defaultCreatorMethod), true);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        Method propertyCreatorMethod1 = new TestBeanProperty.TestBeanPropertyBuilder().buildClass().getDeclaredMethod("propertyCreator1", String.class, int.class);
        SettableBeanProperty[] propertyBasedArgs1 = new SettableBeanProperty[] { createSettableBeanProperty("field1"), createSettableBeanProperty("field2") };
        creatorCollector.addPropertyCreator(createAnnotatedWithParams(propertyCreatorMethod1), false, propertyBasedArgs1);
        Method propertyCreatorMethod2 = new TestBeanProperty.TestBeanPropertyBuilder().buildClass().getDeclaredMethod("propertyCreator2", String.class, int.class);
        SettableBeanProperty[] propertyBasedArgs2 = new SettableBeanProperty[] { createSettableBeanProperty("field1"), createSettableBeanProperty("field3") };
        creatorCollector.addPropertyCreator(createAnnotatedWithParams(propertyCreatorMethod2), false, propertyBasedArgs2);
    }

    private BeanDescription createTestBeanDescription() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructType(TestBean.class);
    }

    private AnnotatedWithParams createAnnotatedWithParams(Method method) {
        return new AnnotatedWithParams(new AnnotatedMethod(method), new JsonCreator(), Collections.emptyList(), null);
    }

    private AnnotatedWithParams createAnnotatedWithParams(Method method, Object... constructorArgs) throws Exception {
        Object[] args = Arrays.copyOf(constructorArgs, constructorArgs.length + 1);
        args[constructorArgs.length] = createSettableBeanProperty("field1");
        return new AnnotatedWithParams(new AnnotatedMethod(method), new JsonCreator(), Arrays.asList(new AnnotatedParameter(method, 0, null, null, null, null, args)), null);
    }

    private AnnotatedWithParams createStringCreatorWithNonStandardName(BeanDescription beanDesc) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Method method = mapper.getFactory().findCreator(beanDesc.getType(), new MapperConfig.StandardConfig().withFeature(MapperFeature.AUTO_DETECT_CREATOR), null);
        return createAnnotatedWithParams(method);
    }

    private SettableBeanProperty createSettableBeanProperty(String name) {
        return new SettableBeanProperty.Std(new AnnotatedField("field1"), null, null);
    }

    private static class TestBean {
        public TestBean() {}

        @JsonCreator
        public TestBean(String value) {}

        @JsonCreator
        public TestBean(int value) {}

        @JsonCreator
        public TestBean(long value) {}

        @JsonCreator
        public TestBean(double value) {}

        @JsonCreator
        public TestBean(boolean value) {}

        @JsonCreator
        public TestBean(TestBeanDelegate delegate) {}

        @JsonCreator
        public TestBean(String field1, int field2) {}

        private static class TestBeanDelegate {
            public TestBeanDelegate(TestBean bean) {}

            @JsonCreator
            public TestBeanDelegate() {}

            public TestBean delegateConstructor(TestBean bean) {}
        }
    }
}

// 