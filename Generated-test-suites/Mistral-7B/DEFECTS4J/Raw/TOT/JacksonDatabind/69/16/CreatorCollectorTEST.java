package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedClass;
import com.fasterxml.jackson.core.base.AnnotatedMember;
import com.fasterxml.jackson.core.type.AnnotatedClassId;
import com.fasterxml.jackson.core.type.AnnotatedType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

public class CreatorCollectorTest {

    private BeanDescription _beanDescription;
    private MapperConfig<?> _config;
    private CreatorCollector _collector;

    @BeforeMethod
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        _config = mapper.getConfig();
        AnnotatedClassIntrospector introspector = mapper.getAnnotationIntrospector();
        AnnotationIntrospector valueIntrospector = mapper.getValueIntrospector();

        _beanDescription = introspector.findBeanDescription(MyClass.class, null);

        try {
            _collector = new CreatorCollector(_beanDescription, _config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDefaultCreator() throws Exception {
        assertNull(_collector.getDefaultCreator());
        AnnotatedClass defaultCreatorClass = new AnnotatedClass(MyClass.class, MyClass.class.getName());
        Method defaultConstructor = MyClass.class.getDeclaredConstructor();
        MyClass defaultInstance = new MyClass();
        AnnotatedMethod defaultConstructorMethod = AnnotatedMember.construct(defaultConstructor, defaultCreatorClass);
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(defaultConstructorMethod, null);
        _collector.setDefaultCreator(defaultCreator);
        AnnotatedWithParams result = _collector.getDefaultCreator();
        assertSame(defaultCreator, result);
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedMember stringCreatorMethod = findAnnotatedMethod("MyClass", "stringCreator");
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorMethod, null);
        _collector.addStringCreator(stringCreator, false);
        AnnotatedWithParams result = _collector.getCreatorForInput(String.class);
        assertSame(stringCreator, result);
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedMember intCreatorMethod = findAnnotatedMethod("MyClass", "intCreator");
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, null);
        _collector.addIntCreator(intCreator, false);
        AnnotatedWithParams result = _collector.getCreatorForInput(Integer.TYPE);
        assertSame(intCreator, result);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedMember longCreatorMethod = findAnnotatedMethod("MyClass", "longCreator");
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longCreatorMethod, null);
        _collector.addLongCreator(longCreator, false);
        AnnotatedWithParams result = _collector.getCreatorForInput(Long.TYPE);
        assertSame(longCreator, result);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedMember doubleCreatorMethod = findAnnotatedMethod("MyClass", "doubleCreator");
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleCreatorMethod, null);
        _collector.addDoubleCreator(doubleCreator, false);
        AnnotatedWithParams result = _collector.getCreatorForInput(Double.TYPE);
        assertSame(doubleCreator, result);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedMember booleanCreatorMethod = findAnnotatedMethod("MyClass", "booleanCreator");
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanCreatorMethod, null);
        _collector.addBooleanCreator(booleanCreator, false);
        AnnotatedWithParams result = _collector.getCreatorForInput(Boolean.TYPE);
        assertSame(booleanCreator, result);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        AnnotatedMember delegateCreatorMethod = findAnnotatedMethod("MyClass", "delegateCreator");
        SettableBeanProperty[] delegateArgs = {new SettableBeanProperty("delegateArg1", String.class),
                new SettableBeanProperty("delegateArg2", Integer.TYPE)};
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateCreatorMethod, null, delegateArgs);
        _collector.addDelegatingCreator(delegateCreator, false, delegateArgs);
        JavaType delegateType = _collector.constructValueInstantiator(_config).getValueType();
        assertEquals(MyDelegate.class, delegateType.getRawClass());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        AnnotatedMember arrayDelegateCreatorMethod = findAnnotatedMethod("MyClass", "arrayDelegateCreator");
        SettableBeanProperty[] arrayDelegateArgs = {new SettableBeanProperty("arrayDelegateArg", MyArray.class)};
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(arrayDelegateCreatorMethod, null, arrayDelegateArgs);
        _collector.addDelegatingCreator(arrayDelegateCreator, false, arrayDelegateArgs);
        JavaType arrayDelegateType = _collector.constructValueInstantiator(_config).getValueType();
        assertEquals(MyArray.class, arrayDelegateType.getRawClass());
    }

    @Test
    public void testPropertyCreator() throws Exception {
        AnnotatedMember propertyCreatorMethod = findAnnotatedMethod("MyClass", "propertyCreator");
        SettableBeanProperty[] propertyBasedArgs = {new SettableBeanProperty("propertyArg1", String.class),
                new SettableBeanProperty("propertyArg2", Integer.TYPE)};
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(propertyCreatorMethod, null, propertyBasedArgs);
        _collector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);
        Map<String, Object> map = new HashMap<>();
        map.put("propertyArg1", "value1");
        map.put("propertyArg2", Integer.valueOf(1));
        Object result = _collector.constructValueInstantiator(_config).createValue(null, _config, map);
        assertEquals(MyClass.class, result.getClass());
    }

    @Test
    public void testNullInput() throws Exception {
        assertNull(_collector.getCreatorForInput(null));
    }

    @Test
    public void testUnknownInput() throws Exception {
        assertNull(_collector.getCreatorForInput(MyOtherClass.class));
    }

    @Test
    public void testMissingCreators() throws Exception {
        _collector = new CreatorCollector(_beanDescription, _config);
        assertNull(_collector.getDefaultCreator());
        assertFalse(_collector.hasDelegatingCreator());
        assertFalse(_collector.hasPropertyBasedCreator());
        assertFalse(_collector.hasDefaultInputCreator());
    }

    @Test
    public void testDefaultInputCreator() throws Exception {
        _collector = new CreatorCollector(_beanDescription, _config);
        Object result = _collector.constructValueInstantiator(_config).createValue(null, _config, null);
        assertEquals(MyClass.class, result.getClass());
    }

    private AnnotatedMember findAnnotatedMethod(String className, String methodName) throws Exception {
        AnnotatedClassId id = new AnnotatedClassId(className, MyClass.class);
        AnnotatedClass annotatedClass = _config.getFactory().findClassAnnotations(id, _config);
        Method method = annotatedClass.getRawClass().getDeclaredMethod(methodName);
        AnnotatedMethod annotatedMethod = AnnotatedMember.construct(method, new AnnotatedClass(_config));
        return annotatedMethod;
    }

    private static class MyClass {
        public MyClass() {
        }

        public static MyClass stringCreator(String s) {
            return new MyClass();
        }

        public static MyClass intCreator(int i) {
            return new MyClass();
        }

        public static MyClass longCreator(long l) {
            return new MyClass();
        }

        public static MyClass doubleCreator(double d) {
            return new MyClass();
        }

        public static MyClass booleanCreator(boolean b) {
            return new MyClass();
        }

        public static MyClass delegateCreator(MyDelegate delegate) {
            return new MyClass();
        }

        public static MyClass[] arrayDelegateCreator(MyArray[] delegates) {
            return new MyClass[0];
        }

        public static MyClass propertyCreator(String propertyArg1, Integer propertyArg2) {
            return new MyClass();
        }
    }

    private static class MyDelegate {
    }

    private static class MyArray {
    }
}