package com.fasterxml.jackson.databind.deser.std; // import statements go here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the StdValueInstantiator class.
 */
public class StdValueInstantiatorTest {

    private StdValueInstantiator _instantiator;
    private DeserializationContext _context;
    private Object _delegate;

    @Before
    public void setUp() {
        _context = mock(DeserializationContext.class);
        _delegate = mock(Object.class);
        _instantiator = new StdValueInstantiator(_context, null);
    }

    @Test
    public void testGetValueTypeDesc() {
        String expectedValueTypeDesc = "UNKNOWN TYPE";
        assertEquals(expectedValueTypeDesc, _instantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        Class<?> expectedValueClass = Object.class;
        assertEquals(expectedValueClass, _instantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        boolean canCreateFromString = _instantiator.canCreateFromString();
        assertTrue(canCreateFromString);
    }

    @Test
    public void testCanCreateFromInt() {
        boolean canCreateFromInt = _instantiator.canCreateFromInt();
        assertTrue(canCreateFromInt);
    }

    @Test
    public void testCanCreateFromLong() {
        boolean canCreateFromLong = _instantiator.canCreateFromLong();
        assertTrue(canCreateFromLong);
    }

    @Test
    public void testCanCreateFromDouble() {
        boolean canCreateFromDouble = _instantiator.canCreateFromDouble();
        assertTrue(canCreateFromDouble);
    }

    @Test
    public void testCanCreateFromBoolean() {
        boolean canCreateFromBoolean = _instantiator.canCreateFromBoolean();
        assertTrue(canCreateFromBoolean);
    }

    @Test
    public void testCanCreateUsingDefault() {
        boolean canCreateUsingDefault = _instantiator.canCreateUsingDefault();
        assertFalse(canCreateUsingDefault);
    }

    @Test
    public void testCanCreateUsingDelegate() {
        boolean canCreateUsingDelegate = _instantiator.canCreateUsingDelegate();
        assertFalse(canCreateUsingDelegate);
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        boolean canCreateUsingArrayDelegate = _instantiator.canCreateUsingArrayDelegate();
        assertFalse(canCreateUsingArrayDelegate);
    }

    @Test
    public void testCanCreateFromObjectWith() {
        boolean canCreateFromObjectWith = _instantiator.canCreateFromObjectWith();
        assertFalse(canCreateFromObjectWith);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateUsingDefaultThrowsException() throws IOException {
        when(_instantiator._defaultCreator.call()).thenThrow(new IOException("Test exception"));
        _instantiator.createUsingDefault(_context);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        Object[] args = new Object[1];
        args[0] = _delegate;
        when(_instantiator._withArgsCreator.call(args)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromObjectWith(_context, args));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateUsingDelegateThrowsException() throws IOException {
        when(_instantiator._delegateCreator.call()).thenThrow(new IOException("Test exception"));
        _instantiator.createUsingDelegate(_context, _delegate);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        Object[] args = new Object[1];
        args[0] = _delegate;
        when(_instantiator._arrayDelegateCreator.call(args)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createUsingArrayDelegate(_context, _delegate));
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(_instantiator._fromStringCreator.call1("Test string")).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromString(_context, "Test string"));
    }

    @Test
    public void testCreateFromInt() throws IOException {
        when(_instantiator._fromIntCreator.call1(42)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromInt(_context, 42));
    }

    @Test
    public void testCreateFromLong() throws IOException {
        when(_instantiator._fromLongCreator.call1(42L)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromLong(_context, 42L));
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        when(_instantiator._fromDoubleCreator.call1(42.0)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromDouble(_context, 42.0));
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        boolean boolValue = true;
        when(_instantiator._fromBooleanCreator.call1(boolValue)).thenReturn(new Object());
        assertEquals(new Object(), _instantiator.createFromBoolean(_context, boolValue));
    }

    @Test
    public void testGetDelegateCreator() {
        AnnotatedWithParams expectedDelegateCreator = mock(AnnotatedWithParams.class);
        assertEquals(expectedDelegateCreator, _instantiator.getDelegateCreator());
        when(_instantiator._delegateCreator).thenReturn(expectedDelegateCreator);
    }

    @Test
    public void testGetArrayDelegateCreator() {
        AnnotatedWithParams expectedArrayDelegateCreator = mock(AnnotatedWithParams.class);
        assertEquals(expectedArrayDelegateCreator, _instantiator.getArrayDelegateCreator());
        when(_instantiator._arrayDelegateCreator).thenReturn(expectedArrayDelegateCreator);
    }

    @Test
    public void testGetDefaultCreator() {
        AnnotatedWithParams expectedDefaultCreator = mock(AnnotatedWithParams.class);
        assertEquals(expectedDefaultCreator, _instantiator.getDefaultCreator());
        when(_instantiator._defaultCreator).thenReturn(expectedDefaultCreator);
    }

    @Test
    public void testGetWithArgsCreator() {
        AnnotatedWithParams expectedWithArgsCreator = mock(AnnotatedWithParams.class);
        assertEquals(expectedWithArgsCreator, _instantiator.getWithArgsCreator());
        when(_instantiator._withArgsCreator).thenReturn(expectedWithArgsCreator);
    }

    @Test
    public void testGetIncompleteParameter() {
        AnnotatedParameter expectedIncompleteParameter = mock(AnnotatedParameter.class);
        assertEquals(expectedIncompleteParameter, _instantiator.getIncompleteParameter());
        when(_instantiator._incompleteParameter).thenReturn(expectedIncompleteParameter);
    }
}