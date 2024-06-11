package org.mockito.internal.creation.instance;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNoOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outer = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testWithOuterClassExceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(Integer.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e instanceof InstantationException);
        }
    }

    @Test
    public void testParamsExceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(Integer.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e instanceof InstantationException);
        }
    }

    @Test
    public void testNoArgConstructorExceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(ConstructorInstantiator.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e instanceof InstantationException);
        }
    }
}