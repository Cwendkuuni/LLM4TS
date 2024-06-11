
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        DummyClass dummy = instantiator.newInstance(DummyClass.class);
        assertNotNull(dummy);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        DummyClass dummy = instantiator.newInstance(DummyClass.class);
        assertNotNull(dummy);
        assertEquals(OuterClass.class, dummy.getOuterClass().getClass());
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        Constructor<DummyClass> constructor = instantiator.getWithOuterClassConstructor(DummyClass.class);
        assertNotNull(constructor);
        assertEquals(OuterClass.class, constructor.getParameterTypes()[0]);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        instantiator.newInstance(InvalidClass.class);
    }

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        DummyClass dummy = instantiator.newInstance(DummyClass.class);
        assertNotNull(dummy);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(InvalidClass.class);
    }

    private static class DummyClass {
        private OuterClass outerClass;

        public OuterClass getOuterClass() {
            return outerClass;
        }
    }

    private static class OuterClass {
    }

    private static class InvalidClass {
        public InvalidClass(Object obj) {
        }
    }
}
