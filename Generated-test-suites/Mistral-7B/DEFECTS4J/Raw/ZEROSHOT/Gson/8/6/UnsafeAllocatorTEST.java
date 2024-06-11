package com.google.gson.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Test
 public void testGetInstance() throws Exception {
 allocator = UnsafeAllocator.create();

 // Test with Object
 Object obj = allocator.newInstance(Object.class);
 assertNotNull(obj);
 assertInstanceOf(Object.class, obj);

 // Test with custom class
 @SuppressWarnings("unused")
 static class TestClass {
 // Empty class for testing
 }

 TestClass testClass = allocator.newInstance(TestClass.class);
 assertNotNull(testClass);
 assertInstanceOf(TestClass.class, testClass);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationException() throws Exception {
 allocator = UnsafeAllocator.create();
 // Test with interface
 assertInstanceOf(UnsupportedOperationException.class,
 allocator.newInstance(String.class));

 // Test with abstract class
 @SuppressWarnings("unused")
 static abstract class AbstractClass {
 // Abstract class for testing
 }

 assertInstanceOf(UnsupportedOperationException.class,
 allocator.newInstance(AbstractClass.class));
 }

 @Test
 public void testReflectionException() throws Exception {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(UnsafeAllocator.class);
 } catch (InvocationTargetException e) {
 throw e.getCause();
 } catch (UndeclaredThrowableException e) {
 throw e.getThrowable();
 }

 assertInstanceOf(RuntimeException.class, e);
 }

 @Test
 public void testNullPointerException() throws Exception {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(null);
 } catch (Exception e) {
 assertInstanceOf(NullPointerException.class, e);
 }
 }

 @Test
 public void testIllegalArgumentException() throws Exception {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(new Exception("test").getClass());
 } catch (Exception e) {
 assertInstanceOf(IllegalArgumentException.class, e);
 }
 }
}