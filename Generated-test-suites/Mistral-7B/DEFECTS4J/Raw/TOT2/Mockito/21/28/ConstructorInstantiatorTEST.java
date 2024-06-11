import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {
    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        String instance = instantIator.newInstance(stringClass);
        Assert.assertNull(instance);
    }

    @Test
    public void testNewInstanceWithValidClass() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(new Object());
        Class<String> stringClass = String.class;
        String instance = instantIator.newInstance(stringClass);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullClass() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(new Object());
        Class<String> stringClass = null;
        instantIator.newInstance(stringClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasConstructorWithArguments() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(new Object());
        Class<ClassWithArgs> classWithArgs = ClassWithArgs.class;
        instantIator.newInstance(classWithArgs);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatThrowsExceptionOnConstruction() {
        Class<ExceptionClass> exceptionClass = ExceptionClass.class;
        Class<ExceptionClass> exceptionClassInstance = new ConstructorInstantiator(new Object()).newInstance(exceptionClass);
    }

    @Test
    public void testWithOuterClassWithNullOuterClassInstance() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        try {
            instantIator.withOuterClass(stringClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testWithOuterClassWithClassThatHasNoArgumentConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(new Object());
        Class<String> stringClass = String.class;
        String instance = instantIator.withOuterClass(stringClass);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithClassThatHasConstructorWithArguments() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(new Object());
        Class<ClassWithArgs> classWithArgs = ClassWithArgs.class;
        instantIator.withOuterClass(classWithArgs);
    }

    @Test
    public void testParamsExceptionWithClassThatHasNoArgumentConstructor() {
        try {
            new ConstructorInstantiator(new Object()).paramsException(String.class, new InstantiationException(""));
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testParamsExceptionWithClassThatHasConstructorWithArguments() {
        try {
            new ConstructorInstantiator(new Object()).paramsException(ClassWithArgs.class, new InstantiationException(""));
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }
}