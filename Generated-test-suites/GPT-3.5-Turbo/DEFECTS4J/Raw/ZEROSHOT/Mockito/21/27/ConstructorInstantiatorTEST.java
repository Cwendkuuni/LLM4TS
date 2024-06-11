package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);

        assertNotNull(myClass);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        outerClassInstance = new OuterClass();

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InnerClass innerClass = constructorInstantiator.newInstance(InnerClass.class);

        assertNotNull(innerClass);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_paramsException() {
        outerClassInstance = new OuterClass();

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(InvalidClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(InvalidClass.class);
    }
}

class MyClass {
}

class OuterClass {
}

class InnerClass {
}

class InvalidClass {
}