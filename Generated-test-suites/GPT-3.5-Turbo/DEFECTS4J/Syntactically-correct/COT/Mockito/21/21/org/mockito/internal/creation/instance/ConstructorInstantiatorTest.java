
package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);
        assertEquals(outerClassInstance, constructorInstantiator.outerClassInstance);
    }

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewInstance_withOuterClass_successful() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InnerClass result = constructorInstantiator.newInstance(InnerClass.class);
        assertNotNull(result);
        assertEquals(outerClassInstance, result.outerInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_withOuterClass_invalidOuterInstanceType() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(InnerClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_withOuterClass_invalidConstructor() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(NoParameterConstructorClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_withOuterClass_outerClassNoArgConstructorNotAvailable() {
        NoParameterConstructorClass outerClassInstance = new NoParameterConstructorClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(InnerClass.class);
    }

    @Test
    public void testNewInstance_withOuterClassAndNullClass() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = constructorInstantiator.newInstance(null);
        assertNull(result);
    }

    private class OuterClass {}

    private class InnerClass {
        private final OuterClass outerInstance;

        public InnerClass(OuterClass outerInstance) {
            this.outerInstance = outerInstance;
        }
    }

    private class NoParameterConstructorClass {
        public NoParameterConstructorClass(int param) {
            // Some code
        }
    }

}
