import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    // Test for newInstance method with null outerClassInstance
    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    // Test for newInstance method with non-null outerClassInstance
    @Test
    public void testNewInstanceWithNonNullOuterClassInstance() {
        OuterClass outerObj = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerObj);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    // Test for withOuterClass method with valid parameters
    @Test
    public void testWithOuterClassSuccess() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new OuterClass());
        MyClass myClass = constructorInstantiator.withOuterClass(MyClass.class);
        assertNotNull(myClass);
    }

    // Test for withOuterClass method with invalid parameters (edge case)
    @Test
    public void testWithOuterClassInvalidParameters() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new OtherClass());
        try {
            MyClass myClass = constructorInstantiator.withOuterClass(MyClass.class);
            fail("Expected InstantationException was not thrown");
        } catch (InstantationException e) {
            // Exception should be caught
        }
    }

    // Test for noArgConstructor method
    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.noArgConstructor(MyClass.class);
        assertNotNull(myClass);
    }

    // Test for noArgConstructor method when constructor throws exception (error scenario)
    @Test
    public void testNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            OtherClass otherClass = constructorInstantiator.noArgConstructor(OtherClass.class);
            fail("Expected InstantationException was not thrown");
        } catch (InstantationException e) {
            // Exception should be caught
        }
    }

    // Test for paramsException method
    @Test
    public void testParamsException() {
        InstantationException instantationException = ConstructorInstantiator.paramsException(MyClass.class, new Exception("Test"));
        assertNotNull(instantationException);
    }
}