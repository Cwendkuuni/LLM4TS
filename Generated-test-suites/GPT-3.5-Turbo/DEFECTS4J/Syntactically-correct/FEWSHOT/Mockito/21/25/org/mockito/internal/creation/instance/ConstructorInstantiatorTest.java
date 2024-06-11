package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {
  
    @Test
    public void testConstructorInstantiator_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String error = "Unable to create mock instance of '"
                    + TestClass.class.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";
      
        try {
            instantiator.newInstance(TestClass.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals(error, e.getMessage());
        }
    }
  
    @Test
    public void testConstructorInstantiator_withOuterClass() {
        TestClass outer = new TestClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        String error = "Unable to create mock instance of '"
                    + TestClass.class.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
      
        try {
            instantiator.newInstance(TestClass.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals(error, e.getMessage());
        }
    }

    private static class TestClass {
        // Empty class for testing purposes
    }
}