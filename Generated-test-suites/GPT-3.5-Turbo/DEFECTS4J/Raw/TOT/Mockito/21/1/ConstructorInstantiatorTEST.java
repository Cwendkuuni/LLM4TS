import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor_NoOuterClassInstance() {
        ConstructorInstantiator inst = new ConstructorInstantiator(null);
        MyClass obj = inst.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testNoArgConstructor_WithOuterClassInstance() {
        OuterClass outer = new OuterClass();
        ConstructorInstantiator inst = new ConstructorInstantiator(outer);
        MyClass obj = inst.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass_NoOuterClassInstance() {
        ConstructorInstantiator inst = new ConstructorInstantiator(null);
        try {
            OuterClass outer = new OuterClass();
            MyClass obj = inst.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass_WithOuterClassInstance() {
        OuterClass outer = new OuterClass();
        ConstructorInstantiator inst = new ConstructorInstantiator(outer);
        MyClass obj = inst.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass_WrongOuterClassType() {
        OuterClass outer = new OuterClassSubclass(); // OuterClassSubclass is a subclass of OuterClass
        ConstructorInstantiator inst = new ConstructorInstantiator(outer);
        try {
            MyClass obj = inst.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor_ExceptionThrown() {
        ConstructorInstantiator inst = new ConstructorInstantiator(null);
        try {
            MyClassWithException obj = inst.newInstance(MyClassWithException.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClassWithException'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}

class MyClass {
    // some class implementation
}

class MyClassWithException {
    public MyClassWithException() throws RuntimeException {
        // constructor that throws an exception
    }
}

class OuterClass {
    // some outer class implementation
}

class OuterClassSubclass extends OuterClass {
    // a subclass of OuterClass
}