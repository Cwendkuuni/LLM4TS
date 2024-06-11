import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass() {
        AnotherClass outerClassInstance = new AnotherClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
        assertEquals(outerClassInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(SomeClass.class);
    }

    private class SomeClass {
        private final Object outerClassInstance;

        SomeClass() {
            this.outerClassInstance = null;
        }

        SomeClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private class AnotherClass {
    }

}