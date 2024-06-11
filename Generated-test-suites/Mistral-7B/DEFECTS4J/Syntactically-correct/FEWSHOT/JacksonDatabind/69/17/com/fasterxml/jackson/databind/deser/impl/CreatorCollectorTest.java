package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        assertEquals(beanDesc, collector._beanDesc);
        assertFalse(collector.hasDefaultCreator());
        assertFalse(collector.hasDelegatingCreator());
        assertFalse(collector.hasPropertyBasedCreator());
    }

    @Test
    public void testCreatorCollectorDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        JavaType type = JsonTypeFactory.valueType(DummyBean.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(new ConstructorMethod(DummyBean.class), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.setDefaultCreator(constructor);
        assertTrue(collector.hasDefaultCreator());
        assertEquals(constructor, collector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testCreatorCollectorStringCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams method = new AnnotatedWithParams(new MethodNameMethod(DummyBean.class, "fromString"), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addStringCreator(method, false);
        assertTrue(collector.hasDelegatingCreator());
        assertEquals(method, collector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testCreatorCollectorIntCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams method = new AnnotatedWithParams(new MethodNameMethod(DummyBean.class, "fromInt"), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addIntCreator(method, false);
        assertTrue(collector.hasIntCreator());
        assertEquals(method, collector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testCreatorCollectorLongCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams method = new AnnotatedWithParams(new MethodNameMethod(DummyBean.class, "fromLong"), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addLongCreator(method, false);
        assertTrue(collector.hasLongCreator());
        assertEquals(method, collector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testCreatorCollectorDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams method = new AnnotatedWithParams(new MethodNameMethod(DummyBean.class, "fromDouble"), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addDoubleCreator(method, false);
        assertTrue(collector.hasDoubleCreator());
        assertEquals(method, collector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testCreatorCollectorBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams method = new AnnotatedWithParams(new MethodNameMethod(DummyBean.class, "fromBoolean"), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addBooleanCreator(method, false);
        assertTrue(collector.hasBooleanCreator());
        assertEquals(method, collector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testCreatorCollectorDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBean.class), null, null);
        AnnotatedWithParams constructor = new AnnotatedWithParams(new ConstructorMethod(DummyDelegate.class), null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        SettableBeanProperty property = new SettableBeanProperty(DummyBean.class, "delegate", null, null, null, true, null);
        injectables[0] = new CreatorProperty(property);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addDelegatingCreator(constructor, false, injectables);
        assertTrue(collector.hasDelegatingCreator());
        assertEquals(constructor, collector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables.length, collector._delegateArgs.length);
        assertEquals(injectables[0], collector._delegateArgs[0]);
    }

    @Test
    public void testCreatorCollectorArrayDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBeanArray.class), null, null);
        AnnotatedWithParams constructor = new AnnotatedWithParams(new ConstructorMethod(DummyDelegateArray.class), null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        SettableBeanProperty property = new SettableBeanProperty(DummyBeanArray.class, "delegate", null, null, null, true, null);
        injectables[0] = new CreatorProperty(property);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addDelegatingCreator(constructor, false, injectables);
        assertTrue(collector.hasArrayDelegateCreator());
        assertEquals(constructor, collector._creators[CreatorCollector.C_ARRAY_DELEGATE]);
        assertEquals(injectables.length, collector._arrayDelegateArgs.length);
        assertEquals(injectables[0], collector._arrayDelegateArgs[0]);
    }

    @Test
    public void testCreatorCollectorPropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBeanWithProperties.class), null, null);
        AnnotatedWithParams constructor = new AnnotatedWithParams(new ConstructorMethod(DummyBeanWithProperties.class), null);
        AnnotatedParameter parameter = new AnnotatedParameter(DummyBeanWithProperties.class.getDeclaredField("value1"), null, false, false);
        CreatorProperty[] properties = new CreatorProperty[2];
        SettableBeanProperty property1 = new SettableBeanProperty(DummyBeanWithProperties.class, "value1", null, null, parameter, true, null);
        SettableBeanProperty property2 = new SettableBeanProperty(DummyBeanWithProperties.class, "value2", null, null, null, true, null);
        properties[0] = new CreatorProperty(property1);
        properties[1] = new CreatorProperty(property2);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addPropertyCreator(constructor, false, properties);
        assertTrue(collector.hasPropertyBasedCreator());
        assertEquals(constructor, collector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties.length, collector._propertyBasedArgs.length);
        assertEquals(properties[0], collector._propertyBasedArgs[0]);
        assertEquals(properties[1], collector._propertyBasedArgs[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicatePropertyCreator() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBeanWithDuplicateProperties.class), null, null);
        AnnotatedWithParams constructor = new AnnotatedWithParams(new ConstructorMethod(DummyBeanWithDuplicateProperties.class), null);
        AnnotatedParameter parameter = new AnnotatedParameter(DummyBeanWithDuplicateProperties.class.getDeclaredField("value"), null, false, false);
        CreatorProperty[] properties = new CreatorProperty[2];
        SettableBeanProperty property1 = new SettableBeanProperty(DummyBeanWithDuplicateProperties.class, "value1", null, null, parameter, true, null);
        SettableBeanProperty property2 = new SettableBeanProperty(DummyBeanWithDuplicateProperties.class, "value2", null, null, parameter, true, null);
        properties[0] = new CreatorProperty(property1);
        properties[1] = new CreatorProperty(property2);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addPropertyCreator(constructor, false, properties);
    }

    @Test
    public void testCreatorCollectorIncompleteParameter() {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(DummyBeanWithIncompleteParameter.class), null, null);
        AnnotatedParameter parameter = new AnnotatedParameter(DummyBeanWithIncompleteParameter.class.getDeclaredField("incomplete"), null, false, false);
        CreatorCollector collector = new CreatorCollector(beanDesc, null);
        collector.addIncompleteParameter(parameter);
        assertEquals(parameter, collector._incompleteParameter);
    }

    private static class DummyBean {
        public DummyBean() {
        }

        @JsonCreator
        public static DummyBean fromString(String str) {
            return new DummyBean();
        }

        @JsonCreator
        public static DummyBean fromInt(int i) {
            return new DummyBean();
        }

        @JsonCreator
        public static DummyBean fromLong(long l) {
            return new DummyBean();
        }

        @JsonCreator
        public static DummyBean fromDouble(double d) {
            return new DummyBean();
        }

        @JsonCreator
        public static DummyBean fromBoolean(boolean b) {
            return new DummyBean();
        }

        @JsonCreator
        public static DummyBean fromDelegate(DummyDelegate delegate) {
            return new DummyBean();
        }

        @JsonProperty
        public DummyDelegate delegate;
    }

    private static class DummyBeanArray {
        public DummyBean[] value;

        @JsonCreator
        public static DummyBeanArray fromDelegate(DummyDelegateArray delegate) {
            return new DummyBeanArray();
        }

        @JsonProperty
        public DummyDelegate delegate;
    }

    private static class DummyBeanWithProperties {
        public int value1;
        public String value2;

        @JsonCreator
        public static DummyBeanWithProperties constructor(int value1, String value2) {
            DummyBeanWithProperties result = new DummyBeanWithProperties();
            result.value1 = value1;
            result.value2 = value2;
            return result;
        }
    }

    private static class DummyBeanWithDuplicateProperties {
        public String value;

        @JsonCreator
        public static DummyBeanWithDuplicateProperties constructor(String value1, String value2) {
            DummyBeanWithDuplicateProperties result = new DummyBeanWithDuplicateProperties();
            result.value = value1;
            result.value = value2;
        }
    }

    private static class DummyBeanWithIncompleteParameter {
        public String incomplete;
    }

    private static class DummyDelegate {
        // no-args constructor or factory method
    }

    private static class DummyDelegateArray {
        DummyDelegate[] delegates;

        @JsonCreator
        public static DummyDelegateArray constructor(@JsonProperty("delegates") DummyDelegate[] delegates) {
            DummyDelegateArray result = new DummyDelegateArray();
            result.delegates = delegates;
            return result;
        }
    }
}