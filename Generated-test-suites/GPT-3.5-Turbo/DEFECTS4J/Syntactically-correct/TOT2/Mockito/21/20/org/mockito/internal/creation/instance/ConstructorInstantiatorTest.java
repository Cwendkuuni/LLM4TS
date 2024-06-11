package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass instance = instantiator.newInstance(MyClass.class);
        
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        OuterClass outer = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        MyClass instance = instantiator.newInstance(MyClass.class);

        assertNotNull(instance);
        assertEquals(outer, instance.getOuter());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_invalidOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new InvalidOuterClass());
        instantiator.newInstance(MyClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(NoArgConstructorClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_missingConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(MissingConstructorClass.class);
    }

    // Additional test cases can be added here for other edge cases and error scenarios

}