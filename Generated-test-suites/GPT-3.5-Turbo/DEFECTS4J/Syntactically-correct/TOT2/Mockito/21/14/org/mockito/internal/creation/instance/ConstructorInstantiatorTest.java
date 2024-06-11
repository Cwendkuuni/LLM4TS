
package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String result = instantiator.newInstance(String.class);
        assertNull(result);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object result = instantiator.newInstance(Object.class);
        assertNotNull(result);
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.withOuterClass(Object.class);
        assertNull(result);
    }

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.noArgConstructor(Object.class);
        assertNotNull(result);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.withOuterClass(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.noArgConstructor(String.class);
    }

}
