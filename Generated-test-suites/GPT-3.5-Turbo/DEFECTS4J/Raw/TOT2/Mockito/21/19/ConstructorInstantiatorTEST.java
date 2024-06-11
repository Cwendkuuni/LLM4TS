package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNoOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        ObjectTestClass obj = constructorInstantiator.newInstance(ObjectTestClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        ObjectTestClass outerInstance = new ObjectTestClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        ObjectTestClass obj = constructorInstantiator.newInstance(ObjectTestClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testNewInstanceException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        try {
            ObjectTestClass obj = constructorInstantiator.newInstance(ObjectTestClass.class);
            fail("Should have thrown InstantiationException");
        } catch (InstantiationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testWithOuterClassException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            ObjectTestClass obj = constructorInstantiator.withOuterClass(ObjectTestClass.class);
            fail("Should have thrown InstantiationException");
        } catch (InstantiationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            ObjectTestClass obj = constructorInstantiator.noArgConstructor(ObjectTestClass.class);
            fail("Should have thrown InstantiationException");
        } catch (InstantiationException e) {
            assertNotNull(e.getMessage());
        }
    }
}

class ObjectTestClass {
    // Class to be used as a test case
}