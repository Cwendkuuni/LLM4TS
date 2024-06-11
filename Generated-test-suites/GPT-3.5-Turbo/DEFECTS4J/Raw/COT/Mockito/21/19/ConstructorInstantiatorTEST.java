import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {
    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        MyClassWithParameterizedConstructor instance = instantiator.newInstance(MyClassWithParameterizedConstructor.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClassWithParameterlessConstructor instance = instantiator.newInstance(MyClassWithParameterlessConstructor.class);
        assertNotNull(instance);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithOuterClassException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(MyClassWithoutParameterlessConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithoutOuterClassException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(MyClassWithoutParameterlessConstructor.class);
    }
}

class MyClassWithParameterizedConstructor {
    public MyClassWithParameterizedConstructor(Object outerClassInstance) {
        // constructor code
    }
}

class MyClassWithParameterlessConstructor {
    public MyClassWithParameterlessConstructor() {
        // constructor code
    }
}

class MyClassWithoutParameterlessConstructor {
    public MyClassWithoutParameterlessConstructor(Object arg) {
        // constructor code
    }
}