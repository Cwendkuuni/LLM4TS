package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

public class ValueInstantiatorTest {

    private ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
    private DeserializationContext deserializationContext = mock(DeserializationContext.class);

    @Test
    public void testGetValueClass() {
        when(valueInstantiator.getValueClass()).thenReturn(String.class);
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        when(valueInstantiator.getValueTypeDesc()).thenReturn("java.lang.String");
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(true);
        when(valueInstantiator.canCreateFromString()).thenReturn(true);

        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCannotInstantiate() {
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(false);
        when(valueInstantiator.canCreateFromString()).thenReturn(false);

        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test(expected = AssertionError.class)
    public void testGetFromObjectArgumentsNull() throws IOException {
        when(valueInstantiator.getFromObjectArguments(null)).thenCallRealMethod();
        valueInstantiator.getFromObjectArguments(deserializationContext);
    }

    @Test
    public void testGetFromObjectArgumentsEmptyList() throws IOException {
        Set<SettableBeanProperty> settableBeanProperties = Collections.emptyList();
        when(valueInstantiator.getFromObjectArguments(deserializationContext)).thenReturn(settableBeanProperties.toArray(new SettableBeanProperty[0]));

        Object obj = valueInstantiator.createUsingDefault(deserializationContext);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        Object[] args = new Object[] { "test" };
        when(valueInstantiator.getFromObjectArguments(deserializationContext)).thenReturn(new SettableBeanProperty[] { new SettableBeanProperty("testProperty") });
        when(valueInstantiator.createFromObjectWith(deserializationContext, args)).thenCallRealMethod();

        Object obj = valueInstantiator.createFromObjectWith(deserializationContext, args);
        assertNotNull(obj);
    }

    @Test(expected = AssertionError.class)
    public void testCreateUsingDefaultThrowsIOException() throws IOException {
        when(valueInstantiator.getDefaultCreator()).thenThrow(new IOException());
        valueInstantiator.createUsingDefault(deserializationContext);
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(valueInstantiator.canCreateFromString()).thenReturn(true);
        when(valueInstantiator.createFromString(deserializationContext, "test")).thenCallRealMethod();

        String obj = (String) valueInstantiator.createFromString(deserializationContext, "test");
        assertEquals("test", obj);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromIntThrowsIOException() throws IOException {
        when(valueInstantiator.canCreateFromInt()).thenReturn(true);
        when(valueInstantiator.createFromInt(deserializationContext, 1)).thenThrow(new IOException());

        valueInstantiator.createFromInt(deserializationContext, 1);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        when(valueInstantiator.canCreateFromBoolean()).thenReturn(true);
        when(valueInstantiator.createFromBoolean(deserializationContext, true)).thenCallRealMethod();

        Boolean obj = (Boolean) valueInstantiator.createFromBoolean(deserializationContext, true);
        assertEquals(true, obj);
    }
}