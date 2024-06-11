package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceWithClass() throws Exception {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        return super.newInstance(c);
      }
    };

    Object obj = allocator.newInstance(String.class);
    assertNotNull(obj);
    assertTrue(obj instanceof String);
  }

  @Test
  public void testNewInstanceWithUnsupportedClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(Object.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    } catch (Exception e) {
      fail("Expected UnsupportedOperationException, but got " + e.getClass().getName());
    }
  }

  @Test
  public void testCreateUnsafeAllocator() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstanceWithAbstractClass() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        return super.newInstance(c);
      }
    };

    try {
      allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    } catch (Exception e) {
      fail("Expected UnsupportedOperationException, but got " + e.getClass().getName());
    }
  }

  @Test
  public void testCheckClassInstantiation() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot instantiate " + c);
      }
    };

    try {
      allocator.checkClassInstantiation(String.class);
      // no exception thrown indicates success
    } catch (UnsupportedOperationException e) {
      fail("Unexpected UnsupportedOperationException: " + e.getMessage());
    }
  }

}