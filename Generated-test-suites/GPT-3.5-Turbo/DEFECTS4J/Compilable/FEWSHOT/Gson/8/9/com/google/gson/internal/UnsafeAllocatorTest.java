
package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            assertNotNull(unsafeAllocator);

            // Test instantiation of different classes
            DummyClass dummyClass = unsafeAllocator.newInstance(DummyClass.class);
            assertNotNull(dummyClass);

            AnotherDummyClass anotherDummyClass = unsafeAllocator.newInstance(AnotherDummyClass.class);
            assertNotNull(anotherDummyClass);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}

class DummyClass {
}

class AnotherDummyClass {
}
