package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_withNullArg_shouldReturnNoArgConstructor() {
        // given
        Class<TestClass> cls = TestClass.class;

        // when
        TestClass result = constructorInstantiator.newInstance(cls);

        // then
        assertNotNull(result);
    }

    @Test
    public void testNewInstance_withNonNullArg_shouldReturnWithOuterClass() {
        // given
        constructorInstantiator = new ConstructorInstantiator(new OuterClass());
        Class<TestClass> cls = TestClass.class;

        // when
        TestClass result = constructorInstantiator.newInstance(cls);

        // then
        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_withInvalidOuterClass_shouldThrowException() {
        // given
        constructorInstantiator = new ConstructorInstantiator(new InnerClass());
        Class<TestClass> cls = TestClass.class;

        // when
        constructorInstantiator.newInstance(cls);

        // then
        // exception
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_withInvalidClass_shouldThrowException() {
        // given
        Class<InvalidClass> cls = InvalidClass.class;

        // when
        constructorInstantiator.noArgConstructor(cls);

        // then
        // exception
    }
}

class TestClass {
    // dummy test class
}

class OuterClass {
    // dummy outer class
}

class InnerClass {
    // dummy inner class
}

class InvalidClass {
    // class with no parameter-less constructor
}