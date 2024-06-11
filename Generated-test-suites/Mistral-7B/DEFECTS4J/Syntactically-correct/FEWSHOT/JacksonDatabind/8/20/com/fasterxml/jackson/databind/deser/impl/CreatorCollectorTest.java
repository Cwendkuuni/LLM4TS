package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValuePropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private static BeanDescription createTestBeanDescription() {
        return new BeanDescription( TestClass.class );
    }

    private static CreatorCollector createTestCollector(BeanDescription beanDesc, boolean canFixAccess) {
        return new CreatorCollector(beanDesc, canFixAccess);
    }

    @Test
    public void testCreateDefaultCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method defaultMethod = TestClass.class.getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(defaultMethod, null);

        collector.setDefaultCreator(defaultCreator);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        assertNotNull(inst);
    }

    @Test
    public void testCreateStringCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method stringMethod = TestClass.class.getDeclaredMethod("stringConstructor");
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringMethod, null);

        collector.addStringCreator(stringCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromStringCreator(collector._creators[CreatorCollector.C_STRING]);

        assertEquals("foobar", inst.createFromString("foobar"));
    }

    @Test
    public void testCreateIntCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method intMethod = TestClass.class.getDeclaredMethod("intConstructor");
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intMethod, null);

        collector.addIntCreator(intCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromIntCreator(collector._creators[CreatorCollector.C_INT]);

        assertEquals(42, inst.createFromInt(42));
    }

    @Test
    public void testCreateLongCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method longMethod = TestClass.class.getDeclaredMethod("longConstructor");
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longMethod, null);

        collector.addLongCreator(longCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromLongCreator(collector._creators[CreatorCollector.C_LONG]);

        assertEquals(42L, inst.createFromLong(42L));
    }

    @Test
    public void testCreateDoubleCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method doubleMethod = TestClass.class.getDeclaredMethod("doubleConstructor");
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleMethod, null);

        collector.addDoubleCreator(doubleCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromDoubleCreator(collector._creators[CreatorCollector.C_DOUBLE]);

        assertEquals(3.14, inst.createFromNumber(3.14), 0.001);
    }

    @Test
    public void testCreateBooleanCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method booleanMethod = TestClass.class.getDeclaredMethod("booleanConstructor");
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanMethod, null);

        collector.addBooleanCreator(booleanCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromBooleanCreator(collector._creators[CreatorCollector.C_BOOLEAN]);

        assertTrue(inst.createFromBoolean(true));
    }

    @Test
    public void testCreateDelegateCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Class<?> delegateClass = TestDelegate.class;
        Method delegateMethod = delegateClass.getDeclaredMethod("delegateConstructor");
        AnnotatedClass delegateAnnotatedClass = new AnnotatedClass(delegateClass);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateMethod, new AnnotatedMember(delegateAnnotatedClass, delegateMethod));

        CreatorProperty[] delegateArgs = new CreatorProperty[] {
                new CreatorProperty("arg1", TestType1.class),
                new CreatorProperty("arg2", TestType2.class),
                new CreatorProperty("arg3", TestType3.class)
        };

        collector.addDelegatingCreator(delegateCreator, true, delegateArgs);

        JavaType delegateType = TypeBindings.constructType(TestDelegate.class, TestType1.class, TestType2.class, TestType3.class);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromDelegateCreator(collector._creators[CreatorCollector.C_DELEGATE]);
        inst.configureFromDelegateArguments(collector._delegateArgs);

        assertEquals(new TestDelegate(new TestType1(), new TestType2(), new TestType3()), inst.createUsingDefault(null));
    }

    @Test
    public void testCreatePropertyBasedCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Class<?> testClass = TestClassWithProperties.class;
        AnnotatedClass annotatedClass = new AnnotatedClass(testClass);
        BeanPropertyDefinition propertyDefinition1 = annotatedClass.findPropertyDefinition("property1");
        BeanPropertyDefinition propertyDefinition2 = annotatedClass.findPropertyDefinition("property2");

        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(null, propertyDefinition1, propertyDefinition2);

        CreatorProperty[] propertyBasedArgs = new CreatorProperty[] {
                new CreatorProperty("property1", TestType1.class),
                new CreatorProperty("property2", TestType2.class)
        };

        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromPropertyBasedCreator(collector._creators[CreatorCollector.C_PROPS]);
        inst.configureFromPropertyBasedArguments(collector._propertyBasedArgs);

        TestClassWithProperties instance = inst.createUsingDefault(null);

        assertEquals(new TestType1(), instance.getProperty1());
        assertEquals(new TestType2(), instance.getProperty2());
    }

    @Test
    public void testCreateValueInstantiatorWithMultipleCreators() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method intMethod = TestClass.class.getDeclaredMethod("intConstructor");
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intMethod, null);

        Method stringMethod = TestClass.class.getDeclaredMethod("stringConstructor");
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringMethod, null);

        collector.addIntCreator(intCreator, true);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator inst = collector.constructValueInstantiator(null);

        inst.configureFromIntCreator(collector._creators[CreatorCollector.C_INT]);
        inst.configureFromStringCreator(collector._creators[CreatorCollector.C_STRING]);

        assertNull(inst.createUsingDefault(null));

        assertEquals("foobar", inst.createFromString("foobar"));
        assertEquals(42, inst.createFromInt(42));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInstantiatorWithDuplicateCreators() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Method intMethod = TestClass.class.getDeclaredMethod("intConstructor");
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intMethod, null);

        Method stringMethod = TestClass.class.getDeclaredMethod("stringConstructor");
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringMethod, null);

        collector.addIntCreator(intCreator, true);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true); // duplicate

        try {
            ValueInstantiator inst = collector.constructValueInstantiator(null);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Unexpected exception type: " + e.getClass().getName(), e);
        }

        // this should fail due to duplicated string creator
        collector.addStringCreator(stringCreator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInstantiatorWithDuplicateProperties() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = createTestCollector(beanDesc, true);

        Class<?> testClass = TestClassWithProperties.class;
        AnnotatedClass annotatedClass = new AnnotatedClass(testClass);
        BeanPropertyDefinition propertyDefinition1 = annotatedClass.findPropertyDefinition("property1");
        BeanPropertyDefinition propertyDefinition2 = annotatedClass.findPropertyDefinition("property2");

        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(null, propertyDefinition1, propertyDefinition2);

        CreatorProperty[] propertyBasedArgs = new CreatorProperty[] {
                new CreatorProperty("property1", TestType1.class),
                new CreatorProperty("property1", TestType1.class) // duplicate
        };

        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);

        try {
            ValueInstantiator inst = collector.constructValueInstantiator(null);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Unexpected exception type: " + e.getClass().getName(), e);
        }

        // this should fail due to duplicated property creator
        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
    }

    static class TestClass {
        public TestClass() {}

        public TestClass(String s) {}

        public TestClass(int i) {}

        public TestClass(double d) {}

        public TestClass(boolean b) {}

        public TestClass(TestDelegate delegate) {}

        public static TestClass defaultConstructor() { return new TestClass(); }

        public static TestClass intConstructor(int i) { return new TestClass(i); }

        public static TestClass longConstructor(long l) { return new TestClass((int) l); }

        public static TestClass doubleConstructor(double d) { return new TestClass((float) d); }

        public static TestClass booleanConstructor(boolean b) { return new TestClass(b); }

        public static TestClass stringConstructor(String s) { return new TestClass(s); }
    }

    static class TestDelegate {
        private TestType1 arg1;
        private TestType2 arg2;
        private TestType3 arg3;

        public TestDelegate(TestType1 arg1, TestType2 arg2, TestType3 arg3) {
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        public void delegateConstructor(TestType1 arg1, TestType2 arg2, TestType3 arg3) {}
    }

    static class TestClassWithProperties {
        private TestType1 property1;
        private TestType2 property2;

        public TestClassWithProperties() {}

        public TestClassWithProperties(TestType1 property1, TestType2 property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public TestType1 getProperty1() { return property1; }

        public TestType2 getProperty2() { return property2; }

        @JacksonStdImpl
        public static TestClassWithProperties createFromValues(TestType1 property1, TestType2 property2) {
            return new TestClassWithProperties(property1, property2);
        }
    }

    static class TestType1 {}

    static class TestType2 {}

    static class TestType3 {}
}