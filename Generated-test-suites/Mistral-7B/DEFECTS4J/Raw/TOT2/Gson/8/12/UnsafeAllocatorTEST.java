package com.google.gson.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 // Test case for newInstance
 @Test
 public void testNewInstance() {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 Object result = allocator.newInstance(String.class);
 assertNotNull(result);
 }

 // Test case for create
 @Test
 public void testCreate() {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 assertNotNull(allocator);
 }

 // Test case for newInstance with null
 @Test(expected = NullPointerException.class)
 public void testNewInstanceWithNull() {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 allocator.newInstance(null);
 }

 // Test case for create with null
 @Test(expected = NullPointerException.class)
 public void testCreateWithNull() {
 UnsafeAllocator.create(null);
 }

 // Test case for newInstance with an interface
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceWithInterface() {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(String.class.getClassLoader().loadClass("java.lang.Runnable"));
 } catch (ClassNotFoundException e) {
 }
 }

 // Test case for newInstance with a class that is not instantiable
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceWithUninstantiableClass() {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(UninstantiableClass.class);
 } catch (Exception e) {
 }
 }

 // Test case for create with an inaccessible class
 @Test(expected = IllegalAccessError.class)
 public void testCreateWithInaccessibleClass() {
 try {
 UnsafeAllocator.create(InaccessibleClass.class);
 } catch (Exception e) {
 }
 }

 class UninstantiableClass {}

 class InaccessibleClass {
 private static int i;
 static {
 i = 42;
 }

 private InaccessibleClass() {
 }
 }
}

//