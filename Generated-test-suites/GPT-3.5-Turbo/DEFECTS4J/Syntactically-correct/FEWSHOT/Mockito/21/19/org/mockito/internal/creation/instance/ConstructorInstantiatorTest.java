package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    @Test
    public void testWithOuterClass() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertEquals(outerClassInstance, myClass.getOuterClassInstance());
    }

    @Test
    public void testWithInvalidOuterClassType() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            constructorInstantiator.newInstance(InvalidClass.class);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithInvalidNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            constructorInstantiator.newInstance(InvalidConstructorClass.class);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidConstructorClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}

class MyClass {
    private OuterClass outerClassInstance;

    public MyClass(OuterClass outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public OuterClass getOuterClassInstance() {
        return outerClassInstance;
    }
}

class OuterClass {}

class InvalidClass {}

class InvalidConstructorClass {
    public InvalidConstructorClass(int i) {}
}