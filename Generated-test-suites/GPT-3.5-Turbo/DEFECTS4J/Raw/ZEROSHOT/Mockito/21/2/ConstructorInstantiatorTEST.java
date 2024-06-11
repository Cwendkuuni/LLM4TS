import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(TestClass.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }

    @Test
    public void testWithOuterClass() {
        TestOuterClass outerInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object instance = instantiator.newInstance(TestClass.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
        TestClass testInstance = (TestClass) instance;
        assertEquals(outerInstance, testInstance.getOuterInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(TestClassWithConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() {
        TestOuterClass outerInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.newInstance(TestClassWithConstructor.class);
    }

    private static class TestClass {
        private TestOuterClass outerInstance;

        public TestOuterClass getOuterInstance() {
            return outerInstance;
        }

        public TestClass(TestOuterClass outerInstance) {
            this.outerInstance = outerInstance;
        }

        public TestClass() {
            // no-args constructor
        }
    }

    private static class TestClassWithConstructor {
        public TestClassWithConstructor() {
            throw new RuntimeException("Constructor exception");
        }
    }

    private static class TestOuterClass {
        // empty outer class
    }
}