package com.google.gson.internal;
import org.junit.Test;
import org.junit.Assert;

public class UnsafeAllocatorTest {
   @Test
   public void testUnsafeAllocatorInstance() {
      UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
      Assert.assertNotNull(unsafeAllocator);
   }

   @Test
   public void testNewInstance() throws Exception {
      UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
      String newInstance = (String) unsafeAllocator.newInstance(String.class);
      Assert.assertNotNull(newInstance);
   }
}