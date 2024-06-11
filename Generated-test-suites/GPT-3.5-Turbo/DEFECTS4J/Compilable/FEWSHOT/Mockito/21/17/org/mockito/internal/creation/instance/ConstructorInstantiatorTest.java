package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_withNullOuterClassInstance() {
        // Given
        Class<TestClass> cls = TestClass.class;

        // When
        TestClass result = constructorInstantiator.newInstance(cls);

        // Then
        assertNull(result.getOuterClassInstance());
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance() {
        // Given
        TestOuterClass outerClassInstance = new TestOuterClass();
        Class<TestClass> cls = TestClass.class;
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        // When
        TestClass result = constructorInstantiator.newInstance(cls);

        // Then
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withInvalidOuterClassInstance() {
        // Given
        String outerClassInstance = "invalid";
        Class<TestClass> cls = TestClass.class;
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        // When
        constructorInstantiator.newInstance(cls);

        // Then
        // Expect InstantationException to be thrown
    }

    private static class TestOuterClass {
    }

    private static class TestClass {
        private final Object outerClassInstance;

        public TestClass() {
            this.outerClassInstance = null;
        }

        public TestClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
}