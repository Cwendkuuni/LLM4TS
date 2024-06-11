import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_ParameterClassHasNoArgConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerInstance);
        Class<TestClass> testClass = TestClass.class;
        TestClass instance = instantiation.newInstance(testClass);
        assertTrue(instance instanceof TestClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ParameterClassHasArgumentConstructor_NoOuterInstance() {
        Class<TestClassWithArgs> testClass = TestClassWithArgs.class;
        ConstructorInstantiator instantiation = new ConstructorInstantiator(null);
        try {
            instantiation.newInstance(testClass);
        } catch (Exception e) {
            assertInstanceOf(InstantiationException.class, e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ParameterClassHasArgumentConstructor_InvalidOuterInstanceType() {
        Object outerInstance = new String();
        Class<TestClassWithArgs> testClass = TestClassWithArgs.class;
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerInstance);
        try {
            instantiation.newInstance(testClass);
        } catch (Exception e) {
            assertInstanceOf(InstantiationException.class, e);
        }
    }

    // Edge cases and exception handling for other methods can be added here.
}