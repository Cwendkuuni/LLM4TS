package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class StdValueInstantiatorTest {

    @Test
    public void testgetDefaultCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNull(instantiator.getDefaultCreator());

        instantiator = new StdValueInstantiator(null, new JavaClassType(String.class));
        AnnotatedWithParams defaultCreator = instantiator.getDefaultCreator();
        assertNotNull(defaultCreator);
        assertEquals(String.class, defaultCreator.getParameterType());
        assertEquals(setValueClassConstructor0, defaultCreator.getCallerClass());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNull(instantiator.getDelegateCreator());

        JavaType delegateType = new JavaClassType(Integer.class);
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromDelegateSettings(new AnnotatedWithParams(setValueClassConstructor1, new AnnotatedClass(Integer.class)),
                delegateType, new SettableBeanProperty[]{new ArrayNode()});
        AnnotatedWithParams delegateCreator = instantiator.getDelegateCreator();
        assertNotNull(delegateCreator);
        assertEquals(Integer.class, delegateCreator.getParameterType());
        assertEquals(setValueClassConstructor1, delegateCreator.getCallerClass());
    }

    @Test
    public void testWithArgsCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNull(instantiator.getWithArgsCreator());

        AnnotatedClass valueClass = new AnnotatedClass(MyValueClass.class);
        AnnotatedWithParams withArgsCreator = new AnnotatedWithParams(setValueClassConstructor2, valueClass);
        ValueInstantiator valueInstantiator = new StdValueInstantiator(null, null);
        valueInstantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, new SettableBeanProperty[]{new ArrayNode()});
        AnnotatedWithParams result = valueInstantiator.getWithArgsCreator();
        assertNotNull(result);
        assertEquals(MyValueClass.class, result.getParameterType());
        assertEquals(setValueClassConstructor2, result.getCallerClass());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        AnnotatedWithParams withArgsCreator = new AnnotatedWithParams(setValueClassConstructor2, new AnnotatedClass(MyValueClass.class));
        instantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, new SettableBeanProperty[]{new ArrayNode()});
        Object result = instantiator.createFromObjectWith(new DeserializationContext(new ObjectMapper()), new Object[]{new ObjectNode()});
        assertInstanceOf(MyValueClass.class, result);
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        Object result = instantiator.createUsingDefault(new DeserializationContext(new ObjectMapper()));
        assertInstanceOf(Object.class, result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        JavaType delegateType = new JavaClassType(Integer.class);
        instantiator.configureFromDelegateSettings(new AnnotatedWithParams(setValueClassConstructor1, new AnnotatedClass(Integer.class)),
                delegateType, new SettableBeanProperty[]{new ArrayNode()});
        Object result = instantiator.createUsingDelegate(new DeserializationContext(new ObjectMapper()), new Object(1));
        assertInstanceOf(Integer.class, result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        JavaType arrayDelegateType = new ArrayType(new JavaClassType(String.class), 1);
        instantiator.configureFromArraySettings(new AnnotatedWithParams(setValueClassArrayConstructor, new AnnotatedClass(String[].class)),
                arrayDelegateType, new SettableBeanProperty[]{new ArrayNode()});
        Object result = instantiator.createUsingArrayDelegate(new DeserializationContext(new ObjectMapper()), new Object(new String[]{"foo"}));
        assertInstanceOf(String[].class, result.getClass());
        assertArrayEquals(new String[]{"foo"}, (String[]) result);
    }

    @Test
    public void testCreateFromString() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(setValueClassFromStringConstructor, new AnnotatedClass(MyStringValueClass.class)));
        Object result = instantiator.createFromString(new DeserializationContext(new ObjectMapper()), "foo");
        assertInstanceOf(MyStringValueClass.class, result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureFromIntCreator(new AnnotatedWithParams(setValueClassFromIntConstructor, new AnnotatedClass(MyIntValueClass.class)));
        Object result = instantiator.createFromInt(new DeserializationContext(new ObjectMapper()), 1);
        assertInstanceOf(MyIntValueClass.class, result);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureFromLongCreator(new AnnotatedWithParams(setValueClassFromLongConstructor, new AnnotatedClass(MyLongValueClass.class)));
        Object result = instantiator.createFromLong(new DeserializationContext(new ObjectMapper()), 1L);
        assertInstanceOf(MyLongValueClass.class, result);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureFromDoubleCreator(new AnnotatedWithParams(setValueClassFromDoubleConstructor, new AnnotatedClass(MyDoubleValueClass.class)));
        Object result = instantiator.createFromDouble(new DeserializationContext(new ObjectMapper()), 1.0);
        assertInstanceOf(MyDoubleValueClass.class, result);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureFromBooleanCreator(new AnnotatedWithParams(setValueClassFromBooleanConstructor, new AnnotatedClass(MyBooleanValueClass.class)));
        Object result = instantiator.createFromBoolean(new DeserializationContext(new ObjectMapper()), true);
        assertInstanceOf(MyBooleanValueClass.class, result);
    }

    @Test
    public void testConfigureIncompleteParameter() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        instantiator = new StdValueInstantiator(instantiator);
        instantiator.configureIncompleteParameter(new AnnotatedParameter(setValueClassFromIntConstructor, 0));
        AnnotatedParameter result = instantiator.getIncompleteParameter();
        assertSame(setValueClassFromIntConstructor.getParameter(0), result);
    }

    private static Class<?> getValueClass() {
        return Object.class;
    }

    private static JavaConstructor<?> setValueClassConstructor0 = new JavassistClassGenerator() {
        @Override
        public JavaConstructor<?> create() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
            return new JavaConstructor<>(Object.class, new Object[0]);
        }
    }.generateConstructor();

    private static JavaConstructor<?> setValueClassConstructor1 = new JavassistClassGenerator() {
        @Override
        public JavaConstructor<?> create() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
            return new JavaConstructor<>(Object.class, Integer.TYPE);
        }
    }.generateConstructor();

    private static JavaConstructor<?> setValueClassConstructor2 = new JavassistClassGenerator() {
        @Override
        public JavaConstructor<?> create() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
            return new JavaConstructor<>(MyValueClass.class, new Object[0]);
        }
    }.generateConstructor();

    private static JavaConstructor<?> setValueClassArrayConstructor = new JavassistClassGenerator() {
        @Override
        public JavaConstructor<?> create() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
            return new JavaConstructor<>(String[].class, new Object[0]);
        }
    }.generateConstructor();

    private static JsonDeserializer.ValueInstantiator valueClassFromStringConstructor = new StdValueInstantiator.StdValueInstantiatorFromString();

    private static JsonDeserializer.ValueInstantiator valueClassFromIntConstructor = new StdValueInstantiator.StdValueInstantiatorFromInt();

    private static JsonDeserializer.ValueInstantiator valueClassFromLongConstructor = new StdValueInstantiator.StdValueInstantiatorFromLong();

    private static JsonDeserializer.ValueInstantiator valueClassFromDoubleConstructor = new StdValueInstantiator.StdValueInstantiatorFromDouble();

    private static JsonDeserializer.ValueInstantiator valueClassFromBooleanConstructor = new StdValueInstantiator.StdValueInstantiatorFromBoolean();

    private static class MyStringValueClass {
        // empty class
    }

    private static class MyIntValueClass {
        // empty class
    }

    private static class MyLongValueClass {
        // empty class
    }

    private static class MyDoubleValueClass {
        // empty class
    }

    private static class MyBooleanValueClass {
        // empty class
    }
}