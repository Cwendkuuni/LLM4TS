
package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertEquals("", result);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator("outerInstance");
        String result = constructorInstantiator.newInstance(String.class);
        assertEquals("outerInstance", result);
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator("outerInstance");
        String result = constructorInstantiator.withOuterClass(String.class);
        assertEquals("outerInstance", result);
    }

    @Test
    public void testParamsException() {
        try {
            ConstructorInstantiator.paramsException(String.class, new Exception("Test Exception"));
            fail("Expected InstantationException was not thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor() {
        String result = ConstructorInstantiator.noArgConstructor(String.class);
        assertEquals("", result);
    }

}
