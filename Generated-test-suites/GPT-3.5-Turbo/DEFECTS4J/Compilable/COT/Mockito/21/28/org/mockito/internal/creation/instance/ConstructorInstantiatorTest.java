package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        SampleClass obj = instantiator.newInstance(SampleClass.class);
        assertNotNull(obj);
        assertEquals(outerClassInstance, obj.getOuterClassInstance());
    }

    @Test
    public void testNewInstanceWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        SampleClass obj = instantiator.newInstance(SampleClass.class);
        assertNotNull(obj);
        assertNull(obj.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithInvalidOuterClass() {
        Object incorrectOuterClassInstance = new Integer(10);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(incorrectOuterClassInstance);
        instantiator.newInstance(SampleClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithMissingNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithConstructorParams.class);
    }
}

class SampleClass {
    private Object outerClassInstance;

    public SampleClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public Object getOuterClassInstance() {
        return outerClassInstance;
    }
}

class ClassWithConstructorParams {
    public ClassWithConstructorParams(String param) {
    }
}
