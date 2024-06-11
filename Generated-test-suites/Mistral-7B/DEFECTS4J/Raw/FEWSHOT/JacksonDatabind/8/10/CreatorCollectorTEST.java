package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClass.class, TestClass::new);
        collector.setDefaultCreator(creator);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testStringCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassStringConstructor.class, TestClassStringConstructor::new);
        collector.addStringCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testIntCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassIntConstructor.class, TestClassIntConstructor::new);
        collector.addIntCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testLongCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassLongConstructor.class, TestClassLongConstructor::new);
        collector.addLongCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassDoubleConstructor.class, TestClassDoubleConstructor::new);
        collector.addDoubleCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassBooleanConstructor.class, TestClassBooleanConstructor::new);
        collector.addBooleanCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassDelegateConstructor.class, TestClassDelegateConstructor::new);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty(null, TestClassDelegateInjectable.class, null);
        collector.addDelegatingCreator(creator, false, injectables);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test
    public void testPropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassPropertyBasedConstructor.class, TestClassPropertyBasedConstructor::new);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("prop1", TestClassProperty1.class, null);
        properties[1] = new CreatorProperty("prop2", TestClassProperty2.class, null);
        collector.addPropertyCreator(creator, false, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreatorArgument() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(TestClassPropertyBasedConstructor.class, TestClassPropertyBasedConstructor::new);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty("prop1", TestClassProperty1.class, null);
        collector.addPropertyCreator(creator, false, properties);
        collector.addPropertyCreator(creator, false, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreatorType() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creatorString = new AnnotatedWithParams(TestClassStringConstructor.class, TestClassStringConstructor::new);
        collector.addStringCreator(creatorString, false);
        AnnotatedWithParams creatorInt = new AnnotatedWithParams(TestClassIntConstructor.class, TestClassIntConstructor::new);
        collector.addIntCreator(creatorInt, false);
        collector.addIntCreator(creatorString, true); // try to add string creator as int creator
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructWithNullBeanDescription() {
        CreatorCollector collector = new CreatorCollector(null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructWithFalseCanFixAccess() {
        BeanDescription beanDesc = new BeanDescription(TestClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);
    }

    private static class TestClass {
        public TestClass() {
        }
    }

    private static class TestClassStringConstructor {
        @JsonCreator
        public TestClassStringConstructor(String s) {
            // empty constructor
        }
    }

    private static class TestClassIntConstructor {
        @JsonCreator
        public TestClassIntConstructor(int i) {
            // empty constructor
        }
    }

    private static class TestClassLongConstructor {
        @JsonCreator
        public TestClassLongConstructor(long l) {
            // empty constructor
        }
    }

    private static class TestClassDoubleConstructor {
        @JsonCreator
        public TestClassDoubleConstructor(double d) {
            // empty constructor
        }
    }

    private static class TestClassBooleanConstructor {
        @JsonCreator
        public TestClassBooleanConstructor(boolean b) {
            // empty constructor
        }
    }

    private static class TestClassDelegateConstructor {
        @JsonCreator
        public TestClassDelegateConstructor(@JsonProperty("delegate") TestClassDelegateInjectable delegate) {
            // constructor with injectable
        }
    }

    private static class TestClassDelegateInjectable {
        private Object delegate;

        @JsonProperty
        public void setDelegate(Object delegate) {
            this.delegate = delegate;
        }

        public Object getDelegate() {
            return delegate;
        }
    }

    private static class TestClassPropertyBasedConstructor {
        @JsonCreator
        public TestClassPropertyBasedConstructor(@JsonProperty("prop1") TestClassProperty1 prop1,
                                               @JsonProperty("prop2") TestClassProperty2 prop2) {
            // constructor with properties
        }
    }

    private static class TestClassProperty1 {
    }

    private static class TestClassProperty2 {
    }
}