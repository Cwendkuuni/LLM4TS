package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
        assertEquals(SomeClass.class, instance.getClass());
    }
    
    @Test
    public void testConstructorInstantiatorWithNonNullOuterClassInstance() {
        OuterClass outer = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
        assertEquals(SomeClass.class, instance.getClass());
        assertEquals(outer, instance.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithInvalidOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("invalid");
        instantiator.newInstance(SomeClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithClassWithoutNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithoutNoArgConstructor.class);
    }
}

class OuterClass {}

class SomeClass {
    private final Object outerClassInstance;

    public SomeClass() {
        this.outerClassInstance = null;
    }
    
    public SomeClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }
    
    public Object getOuterClassInstance() {
        return outerClassInstance;
    }
}

class ClassWithoutNoArgConstructor {
    public ClassWithoutNoArgConstructor(Object arg) {
    }
}