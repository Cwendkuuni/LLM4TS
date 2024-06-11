package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link StdValueInstantiator} class.
 */
public class StdValueInstantiatorTest {

    private DeserializationConfig config;
    private ObjectMapper objectMapper;
    private StdValueInstantiator stdValueInstantiator;

    @Before
    public void setup() throws Exception {
        config = new DeserializationConfig();
        objectMapper = new ObjectMapper(config);

        stdValueInstantiator = new StdValueInstantiator(config, String.class);
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        AnnotatedClass ac = new AnnotatedClass(String.class);
        AnnotatedConstructor acConstructor = ac.findConstructor();
        AnnotatedWithParams acDefaultCreator = acDefaultConstructor(ac, acConstructor);
        stdValueInstantiator.configureFromObjectSettings(acDefaultCreator, null, null, null, null, null);

        Object result = stdValueInstantiator.createUsingDefault(Mockito.mock(DeserializationContext.class));
        assertEquals("", result);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        String className = "com.example.MyClass";
        Class myClass = Class.forName(className);

        AnnotatedClass myClassAnnotatedClass = new AnnotatedClass(myClass);
        AnnotatedConstructor myClassConstructor = myClassAnnotatedClass.findConstructor();
        AnnotatedWithParams myClassCreator = acCreator(myClassConstructor);
        SettableBeanProperty[] myClassArguments = new SettableBeanProperty[]{new SettableBeanProperty()};

        AnnotatedMethod stringFromObjectMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromObjectMethod.getDeclaringClass()).thenReturn(String.class);
        when(stringFromObjectMethod.getName()).thenReturn("fromObject");
        AnnotatedWithParams stringFromObjectCreator = acCreator(stringFromObjectMethod);

        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, myClassCreator, myClassArguments);

        Object[] args = new Object[]{Mockito.mock(Object.class)};
        Object result = stdValueInstantiator.createFromObjectWith(
                Mockito.mock(DeserializationContext.class),
                args);

        ArgumentCaptor<MyClass> myClassArgumentCaptor = ArgumentCaptor.forClass(MyClass.class);
        verify(myClassCreator).call(myClassArgumentCaptor.capture());
        MyClass myClassInstance = myClassArgumentCaptor.getValue();
        assertEquals("expected", myClassInstance.getValue());
        assertEquals(args[0], myClassInstance.getArgument());

        assertEquals(myClassInstance, result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        final String className = "com.example.MyDelegateClass";
        final Class myDelegateClass = Class.forName(className);

        AnnotatedClass myDelegateClassAnnotatedClass = new AnnotatedClass(myDelegateClass);
        AnnotatedConstructor myDelegateClassConstructor = myDelegateClassAnnotatedClass.findConstructor();
        AnnotatedWithParams myDelegateClassCreator = acCreator(myDelegateClassConstructor);
        SettableBeanProperty[] myDelegateClassArguments = new SettableBeanProperty[]{new SettableBeanProperty()};

        stdValueInstantiator.configureFromObjectSettings(null, myDelegateClassCreator, null, myDelegateClassArguments, null, null);

        Object myDelegateInstance = new Object() {}; // any instance of MyDelegateClass
        Object result = stdValueInstantiator.createUsingDelegate(
                Mockito.mock(DeserializationContext.class),
                myDelegateInstance
        );

        assertEquals(myDelegateInstance, result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        final String className = "com.example.MyArrayDelegateClass";
        final Class myArrayDelegateClass = Class.forName(className);
        JavaType javaType = objectMapper.getTypeFactory().constructTypeArrayType(double[].class);

        AnnotatedClass myArrayDelegateClassAnnotatedClass = new AnnotatedClass(myArrayDelegateClass);
        AnnotatedConstructor myArrayDelegateClassConstructor = myArrayDelegateClassAnnotatedClass.findConstructor();
        AnnotatedWithParams myArrayDelegateCreator = acCreator(myArrayDelegateClassConstructor, javaType);
        SettableBeanProperty[] myArrayDelegateArguments = new SettableBeanProperty[]{new SettableBeanProperty()};

        stdValueInstantiator.configureFromArraySettings(myArrayDelegateCreator, javaType, myArrayDelegateArguments);

        double[] result = (double[]) stdValueInstantiator.createUsingArrayDelegate(
                Mockito.mock(DeserializationContext.class),
                Mockito.mock(Object.class)
        );

        assertEquals(new double[]{0.0}, result);
    }

    @Test
    public void testCreateFromString() throws Exception {
        AnnotatedMethod stringFromStringMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromStringMethod.getDeclaringClass()).thenReturn(String.class);
        when(stringFromStringMethod.getName()).thenReturn("fromString");
        AnnotatedWithParams stringFromStringCreator = acCreator(stringFromStringMethod);

        stdValueInstantiator.configureFromStringCreator(stringFromStringCreator);

        String result = (String) stdValueInstantiator.createFromString(
                Mockito.mock(DeserializationContext.class),
                "expected"
        );

        assertEquals("expected", result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        AnnotatedMethod stringFromIntMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromIntMethod.getDeclaringClass()).thenReturn(Integer.class);
        when(stringFromIntMethod.getName()).thenReturn("fromInt");
        AnnotatedWithParams stringFromIntCreator = acCreator(stringFromIntMethod);

        stdValueInstantiator.configureFromIntCreator(stringFromIntCreator);

        int value = 42;
        Object result = stdValueInstantiator.createFromInt(
                Mockito.mock(DeserializationContext.class),
                value
        );

        assertEquals(value, result);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        AnnotatedMethod stringFromLongMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromLongMethod.getDeclaringClass()).thenReturn(Long.class);
        when(stringFromLongMethod.getName()).thenReturn("fromLong");
        AnnotatedWithParams stringFromLongCreator = acCreator(stringFromLongMethod);

        stdValueInstantiator.configureFromLongCreator(stringFromLongCreator);

        long value = 42L;
        Object result = stdValueInstantiator.createFromLong(
                Mockito.mock(DeserializationContext.class),
                value
        );

        assertEquals(value, result);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        AnnotatedMethod stringFromDoubleMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromDoubleMethod.getDeclaringClass()).thenReturn(Double.class);
        when(stringFromDoubleMethod.getName()).thenReturn("fromDouble");
        AnnotatedWithParams stringFromDoubleCreator = acCreator(stringFromDoubleMethod);

        stdValueInstantiator.configureFromDoubleCreator(stringFromDoubleCreator);

        double value = 42.0;
        Object result = stdValueInstantiator.createFromDouble(
                Mockito.mock(DeserializationContext.class),
                value
        );

        assertEquals(value, result);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        AnnotatedMethod stringFromBooleanMethod = Mockito.mock(AnnotatedMethod.class);
        when(stringFromBooleanMethod.getDeclaringClass()).thenReturn(Boolean.class);
        when(stringFromBooleanMethod.getName()).thenReturn("fromBoolean");
        AnnotatedWithParams stringFromBooleanCreator = acCreator(stringFromBooleanMethod);

        stdValueInstantiator.configureFromBooleanCreator(stringFromBooleanCreator);

        boolean value = true;
        Object result = stdValueInstantiator.createFromBoolean(
                Mockito.mock(DeserializationContext.class),
                value
        );

        assertEquals(value, result);
    }

    @Test
    public void testConfigureIncompleteParameter() throws Exception {
        AnnotatedParameter incompleteParameter = new AnnotatedParameter();
        stdValueInstantiator.configureIncompleteParameter(incompleteParameter);

        assertSame(incompleteParameter, stdValueInstantiator._incompleteParameter);
    }

    private AnnotatedWithParams acCreator(AnnotatedMethod method) {
        return acCreator(method, null);
    }

    private AnnotatedWithParams acCreator(AnnotatedConstructor constructor) {
        return acCreator(constructor, null);
    }

    private AnnotatedWithParams acCreator(AnnotatedMethod method, JavaType type) {
        return new AnnotatedWithParams(
                new AnnotatedMethod(method),
                new AnnotatedConstructor(constructor),
                new AnnotatedClass(method.getDeclaringClass()),
                type,
                new TypeDeserializer[0],
                null
        );
    }

    private AnnotatedWithParams acCreator(AnnotatedConstructor constructor) {
        return new AnnotatedWithParams(
                null,
                constructor,
                new AnnotatedClass(constructor.getDeclaringClass()),
                null,
                new TypeDeserializer[0],
                null
        );
    }

    private AnnotatedWithParams acDefaultConstructor(AnnotatedClass annotatedClass, AnnotatedConstructor constructor) {
        return new AnnotatedWithParams(
                null,
                constructor,
                annotatedClass,
                null,
                null,
                null
        );
    }
}