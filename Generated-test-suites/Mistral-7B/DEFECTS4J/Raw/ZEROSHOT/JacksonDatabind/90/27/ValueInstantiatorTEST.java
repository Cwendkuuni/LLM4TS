package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

/**
 * Class that contains unit tests for ValueInstantiator class.
 */
public class ValueInstantiatorTest {

    /**
     * Test method for the getValueClass() method.
     */
    @Test
     public void testGetValueClass() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertEquals(String.class, vi.getValueClass());
     }

    /**
     * Test method for the getValueTypeDesc() method.
     */
    @Test
     public void testGetValueTypeDesc() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertEquals("java.lang.String", vi.getValueTypeDesc());
     }

    /**
     * Test method for the canInstantiate() method.
     */
    @Test
     public void testCanInstantiate() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertFalse(vi.canInstantiate());
     }

    /**
     * Test method for the canCreateUsingDefault() method.
     */
    @Test
     public void testCanCreateUsingDefault() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertFalse(vi.canCreateUsingDefault());
     }

    /**
     * Test method for the canCreateUsingDelegate() method.
     */
    @Test
     public void testCanCreateUsingDelegate() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertFalse(vi.canCreateUsingDelegate());
     }

    /**
     * Test method for the canCreateFromObjectWith() method.
     */
    @Test
     public void testCanCreateFromObjectWith() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertFalse(vi.canCreateFromObjectWith());
     }

    /**
     * Test method for the createUsingDefault() method.
     */
    @Test(expected = IOException.class)
     public void testCreateUsingDefault() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         Object obj = vi.createUsingDefault(null);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromObjectWith() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromObjectWith() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         Object[] args = new Object[0];
         Object obj = vi.createFromObjectWith(null, args);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromObjectWith(DeserializationContext, SettableBeanProperty[]) method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromObjectWithDeserializationContext() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         DeserializationContext ctx = null;
         SettableBeanProperty[] props = new SettableBeanProperty[0];
         Object obj = vi.createFromObjectWith(ctx, props);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createUsingDelegate() method.
     */
    @Test(expected = IOException.class)
     public void testCreateUsingDelegate() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         Object delegate = new Object();
         Object obj = vi.createUsingDelegate(null, delegate);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createUsingArrayDelegate() method.
     */
    @Test(expected = IOException.class)
     public void testCreateUsingArrayDelegate() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         Object delegate = new Object();
         Object obj = vi.createUsingArrayDelegate(null, delegate);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromString() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromString() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         DeserializationContext ctx = null;
         String value = "";
         Object obj = vi.createFromString(ctx, value);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromInt() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromInt() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(Integer.class);
         DeserializationContext ctx = null;
         int value = 0;
         Object obj = vi.createFromInt(ctx, value);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromLong() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromLong() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(Long.class);
         DeserializationContext ctx = null;
         long value = 0;
         Object obj = vi.createFromLong(ctx, value);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromDouble() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromDouble() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(Double.class);
         DeserializationContext ctx = null;
         double value = 0.0;
         Object obj = vi.createFromDouble(ctx, value);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the createFromBoolean() method.
     */
    @Test(expected = IOException.class)
     public void testCreateFromBoolean() throws IOException {
         ValueInstantiator vi = new ValueInstantiator.Base(Boolean.class);
         DeserializationContext ctx = null;
         boolean value = false;
         Object obj = vi.createFromBoolean(ctx, value);
         fail("Expected IOException, but got " + obj.getClass().getName());
     }

    /**
     * Test method for the getDefaultCreator() method.
     */
    @Test
     public void testGetDefaultCreator() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertNull(vi.getDefaultCreator());
     }

    /**
     * Test method for the getDelegateCreator() method.
     */
    @Test
     public void testGetDelegateCreator() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertNull(vi.getDelegateCreator());
     }

    /**
     * Test method for the getArrayDelegateCreator() method.
     */
    @Test
     public void testGetArrayDelegateCreator() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertNull(vi.getArrayDelegateCreator());
     }

    /**
     * Test method for the getWithArgsCreator() method.
     */
    @Test
     public void testGetWithArgsCreator() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertNull(vi.getWithArgsCreator());
     }

    /**
     * Test method for the getIncompleteParameter() method.
     */
    @Test
     public void testGetIncompleteParameter() {
         ValueInstantiator vi = new ValueInstantiator.Base(String.class);
         assertNull(vi.getIncompleteParameter());
     }
}