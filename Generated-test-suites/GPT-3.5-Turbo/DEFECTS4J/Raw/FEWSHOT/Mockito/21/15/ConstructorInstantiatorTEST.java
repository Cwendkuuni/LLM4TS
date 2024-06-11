package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() {
        // Test with outerClassInstance being null
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        // case 1: Test with noArgConstructor
        assertEquals("foo", instantiator.newInstance(String.class));

        // case 2: Test with withOuterClass
        assertEquals("bar", instantiator.newInstance(Character.class));

        // case 3: Test with IllegalArgumentException
        try {
            instantiator.newInstance(Integer.class);
        } catch (InstantiationException e) {
            assertEquals("InstantiationException", e.getClass().getSimpleName());
        } catch (IllegalAccessException e) {
            assertEquals("IllegalAccessException", e.getClass().getSimpleName());
        }

        // Test with outerClassInstance not being null
        instantiator = new ConstructorInstantiator("outer");
        // case 4: Test with withOuterClass
        assertEquals("baz", instantiator.newInstance(String.class));

        // case 5: Test with IllegalArgumentException
        try {
            instantiator.newInstance(Integer.class);
        } catch (InstantiationException e) {
            assertEquals("InstantiationException", e.getClass().getSimpleName());
        } catch (IllegalAccessException e) {
            assertEquals("IllegalAccessException", e.getClass().getSimpleName());
        }
    }

}