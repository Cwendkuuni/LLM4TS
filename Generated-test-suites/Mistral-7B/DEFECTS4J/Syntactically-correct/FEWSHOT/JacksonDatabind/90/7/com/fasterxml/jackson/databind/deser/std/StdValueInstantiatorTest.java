package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    @Test
    public void testDefaultCreator() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNull(instantiator.getDefaultCreator());

        instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        AnnotatedMethod defaultCreator = Mockito.mock(AnnotatedMethod.class);
        instantiator.configureFromObjectSettings(defaultCreator, null, null, null, null, null);
        assertEquals(defaultCreator, instantiator.getDefaultCreator());
    }

    @Test
    public void testDelegateCreator() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertNull(instantiator.getDelegateCreator());

        instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        AnnotatedMethod delegateCreator = Mockito.mock(AnnotatedMethod.class);
        instantiator.configureFromObjectSettings(null, delegateCreator, null, null, null, null);
        assertEquals(delegateCreator, instantiator.getDelegateCreator());
    }

    @Test
    public void testCreateUsingDefault() throws IOException, JsonProcessingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        AnnotatedMethod defaultCreator = Mockito.mock(AnnotatedMethod.class);
        instantiator.configureFromObjectSettings(defaultCreator, null, null, null, null, null);

        Object result = instantiator.createUsingDefault(Mockito.mock(DeserializationContext.class));
        assertNotNull(result);
        Mockito.verify(defaultCreator).call();
    }

    @Test
    public void testCreateFromObjectWith() throws IOException, JsonProcessingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        AnnotatedMethod withArgsCreator = Mockito.mock(AnnotatedMethod.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[1];
        instantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);

        Object[] args = new Object[]{Mockito.mock(Object.class)};
        Object result = instantiator.createFromObjectWith(Mockito.mock(DeserializationContext.class), args);
        assertNotNull(result);
        Mockito.verify(withArgsCreator).call(args);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException, JsonProcessingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        AnnotatedMethod delegateCreator = Mockito.mock(AnnotatedMethod.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[1];
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);

        Object delegate = Mockito.mock(Object.class);
        Object result = instantiator.createUsingDelegate(Mockito.mock(DeserializationContext.class), delegate);
        assertNotNull(result);
        Mockito.verify(delegateCreator).call(delegate);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException, JsonProcessingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(int[].class));
        AnnotatedMethod arrayDelegateCreator = Mockito.mock(AnnotatedMethod.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[1];
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.configureFromArraySettings(arrayDelegateCreator, null, arrayDelegateArguments);

        Object arrayDelegate = Mockito.mock(Object.class);
        Object result = instantiator.createUsingArrayDelegate(Mockito.mock(DeserializationContext.class), arrayDelegate);
        assertNotNull(result);
        Mockito.verify(arrayDelegateCreator).call(arrayDelegate);
    }

    @Test
    public void testCanCreateFromString() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        assertTrue(instantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(int.class));
        assertTrue(instantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(long.class));
        assertTrue(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(double.class));
        assertTrue(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(boolean.class));
        assertTrue(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCannotCreateUsingDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCannotCreateUsingArrayDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(int.class));
        assertFalse(instantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testGetDelegateType() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        assertEquals(null, instantiator.getDelegateType(null));
    }

    @Test
    public void testGetArrayDelegateType() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(int[].class));
        assertEquals(null, instantiator.getArrayDelegateType(null));
    }

    @Test
    public void testGetFromObjectArguments() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, new JavaType(String.class));
        SettableBeanProperty[] result = instantiator.getFromObjectArguments(null);
        assertNull(result);
    }
}