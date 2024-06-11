package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertTrue(inst instanceof StdValueInstantiator);
        assertNull(collector.getDefaultCreator());
        assertEquals(0, collector.getCandidateCreatorCount());
        assertFalse(collector.hasNonDefaultCreator());
    }

    @Test
    public void testCreatorCollectorCustomConstructors() {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest1()));
        collector.setDefaultCreator(creator1);
        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClass.class, new Class[]{int.class, int.class}), false);
        collector.addLongCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClass.class, new Class[]{long.class}), false);
        collector.addDoubleCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClass.class, new Class[]{double.class}), false);
        collector.addBooleanCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClass.class, new Class[]{boolean.class}), false);
        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest2("strArg"))), true);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertTrue(inst instanceof StdValueInstantiator);
        assertEquals(creator1, collector.getDefaultCreator());
        assertEquals(6, collector.getCandidateCreatorCount());
        assertTrue(collector.hasNonDefaultCreator());
    }

    @Test
    public void testCreatorCollectorCustomConstructorsWithExplicitAnnotation() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithExplicitAnnotations.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest1WithAnnotation()));
        collector.setDefaultCreator(creator1);
        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructorWithAnnotation(), MyClassWithExplicitAnnotations.class, new Class[]{int.class, int.class}), true);
        collector.addLongCreator(new AnnotatedWithParams(new JavaBeanConstructorWithAnnotation(), MyClassWithExplicitAnnotations.class, new Class[]{long.class}), true);
        collector.addDoubleCreator(new AnnotatedWithParams(new JavaBeanConstructorWithAnnotation(), MyClassWithExplicitAnnotations.class, new Class[]{double.class}), true);
        collector.addBooleanCreator(new AnnotatedWithParams(new JavaBeanConstructorWithAnnotation(), MyClassWithExplicitAnnotations.class, new Class[]{boolean.class}), true);
        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotationWithAnnotation(), new MethodCreator(new MethodTest2WithAnnotation("strArg"))), true);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertTrue(inst instanceof StdValueInstantiator);
        assertEquals(creator1, collector.getDefaultCreator());
        assertEquals(6, collector.getCandidateCreatorCount());
        assertTrue(collector.hasNonDefaultCreator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateCreator() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDuplicateCreator.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithDuplicateCreator.class, new Class[]{int.class, int.class}), false);
        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithDuplicateCreator.class, new Class[]{int.class, int.class}), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateStringCreator() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDuplicateStringCreator.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest2("strArg"))), false);
        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest2("strArg"))), false);
    }

    @Test
    public void testCreatorCollectorInjectableValues() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithInjectableValues.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest1WithAnnotation()));
        collector.setDefaultCreator(creator1);
        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithInjectableValues.class, new Class[]{int.class, int.class}), false);
        collector.addLongCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithInjectableValues.class, new Class[]{long.class}), false);
        collector.addDoubleCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithInjectableValues.class, new Class[]{double.class}), false);
        collector.addBooleanCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithInjectableValues.class, new Class[]{boolean.class}), false);
        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest2WithAnnotation("strArg"))), true);
        collector.addDelegatingCreator(new AnnotatedWithParams(new JavaBeanConstructorWithAnnotation(), MyClassWithInjectableValues.class, new Class[0]), true, new CreatorProperty[]{new CreatorProperty("injectable1", new Class[]{String.class}), new CreatorProperty("injectable2", new TypeReference<List<Integer>>(){})});

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertTrue(inst instanceof StdValueInstantiator);
        assertEquals(creator1, collector.getDefaultCreator());
        assertEquals(7, collector.getCandidateCreatorCount());
        assertTrue(collector.hasNonDefaultCreator());
    }

    @Test
    public void testCreatorCollectorPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithPropertyCreators.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        collector.addIntCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithPropertyCreators.class, new Class[]{int.class, int.class}), false);
        collector.addLongCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithPropertyCreators.class, new Class[]{long.class}), false);
        collector.addDoubleCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithPropertyCreators.class, new Class[]{double.class}), false);
        collector.addBooleanCreator(new AnnotatedWithParams(new JavaBeanConstructor(), MyClassWithPropertyCreators.class, new Class[]{boolean.class}), false);
        collector.addStringCreator(new AnnotatedWithParams(new MethodCreatorAnnotation(), new MethodCreator(new MethodTest2("strArg"))), true);
        collector.addPropertyCreator(new AnnotatedWithParams(new PropertyCreatorAnnotation(), new PropertyCreator(new PropertyTest1("prop1"))), true, new CreatorProperty[]{new CreatorProperty("prop1", new TypeReference<String>(){})});
        collector.addPropertyCreator(new AnnotatedWithParams(new PropertyCreatorAnnotation(), new PropertyCreator(new PropertyTest2("prop2"))), true, new CreatorProperty[]{new CreatorProperty("prop2", new TypeReference<List<Integer>>(){})});

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        assertTrue(inst instanceof StdValueInstantiator);
        assertEquals(0, collector.getDefaultCreatorIndex());
        assertEquals(7, collector.getCandidateCreatorCount());
        assertTrue(collector.hasNonDefaultCreator());
    }

    // Helper classes

    static class MethodCreator<T> {
        private final T _method;

        public MethodCreator(T method) { _method = method; }

        public T getMethod() { return _method; }
    }

    static class MethodTest1 {
        public MyClass instantiate(int a, int b) { return new MyClass(a, b); }

        public static Method getMethod() throws NoSuchMethodException {
            return MethodTest1.class.getMethod("instantiate", int[].class);
        }
    }

    static class MethodTest1WithAnnotation extends MethodTest1 {
        @JavaBeanMethod
        public MyClass instantiate(int a, int b) { return new MyClass(a, b); }
    }

    static class MethodTest2 {
        public MyClass instantiate(String strArg) { return new MyClass(strArg); }

        public static Method getMethod() throws NoSuchMethodException {
            return MethodTest2.class.getMethod("instantiate", String.class);
        }
    }

    static class MethodTest2WithAnnotation {
        @JavaBeanMethod
        public MyClass instantiate(String strArg) { return new MyClass(strArg); }
    }

    static class MyClass {
        private final int _a;
        private final int _b;
        private final String _strArg;

        public MyClass(int a, int b) { _a = a; _b = b; }

        public MyClass(String strArg) { _strArg = strArg; }

        @Override
        public String toString() {
            return "MyClass{" +
                    "_a=" + _a +
                    ", _b=" + _b +
                    ", _strArg='" + _strArg + '\'' +
                    '}';
        }
    }

    static class MyClassWithDuplicateCreator {
        @java.beans.ConstructorProperties({"a", "b"})
        public MyClassWithDuplicateCreator(int a, int b) { }

        @java.beans.ConstructorProperties({"a", "b"})
        public MyClassWithDuplicateCreator(int a, int b) { }
    }

    static class MyClassWithDuplicateStringCreator {
        @java.beans.ConstructorProperties({"strArg"})
        public MyClassWithDuplicateStringCreator(String strArg) { }

        @java.beans.ConstructorProperties({"strArg"})
        public MyClassWithDuplicateStringCreator(String strArg) { }
    }

    static class MyClassWithExplicitAnnotations {
        @JavaBeanConstructor
        @JavaBeanProperty(name = "injectable1")
        public MyClassWithExplicitAnnotations(int a, int b, @JsonProperty("injectable2") List<Integer> list) { }

        @JsonProperty("injectable1")
        private String _injectable1;
        @JsonProperty("injectable2")
        private List<Integer> _injectable2;
    }

    static class MyClassWithInjectableValues {
        private int _a;
        private int _b;
        @Inject private String _injectable1;
        @Inject private List<Integer> _injectable2;

        @JavaBeanConstructor
        public MyClassWithInjectableValues(int a, int b) { _a = a; _b = b; }
    }

    static class MyClassWithPropertyCreators {
        private int _a;
        private int _b;
        private String _strArg;
        private String _prop1;
        private List<Integer> _prop2;

        @JavaBeanConstructor
        public MyClassWithPropertyCreators(int a, int b) { _a = a; _b = b; }

        @JsonProperty("prop1")
        public void setProp1(String value) { _prop1 = value; }

        @JsonProperty("prop2")
        public void setProp2(List<Integer> value) { _prop2 = value; }
    }

    static class CreatorProperty {
        private final String _name;
        private final Class<?> _type;

        public CreatorProperty(String name, Class<?> type) {
            _name = name;
            _type = type;
        }

        public String getName() { return _name; }

        public Class<?> getType() { return _type; }
    }

    static class PropertyCreator<T> {
        private final T _property;

        public PropertyCreator(T property) { _property = property; }

        public T getProperty() { return _property; }
    }

    static class PropertyTest1 {
        public void setProp1(String value) {/*...*/}

        public static Property<String> getProperty() throws NoSuchFieldException, IllegalAccessException {
            return PropertyTest1.class.getDeclaredField("prop1").getAnnotation(Property.class).get();
        }
    }

    static class PropertyTest2 {
        public void setProp2(List<Integer> value) {/*...*/}

        public static Property<List<Integer>> getProperty() throws NoSuchFieldException, IllegalAccessException {
            return PropertyTest2.class.getDeclaredField("prop2").getAnnotation(Property.class).get();
        }
    }
}