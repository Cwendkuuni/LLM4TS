import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithArgConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> intType = int[].class;
        int[] intArray = constructorInstantiator.newInstance(intType);
        assertNotNull(intArray);
        assertEquals(int[].class, intArray.getClass());
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<?> intType = int[].class;
        int[] intArray = constructorInstantiator.newInstance(intType);
        assertNotNull(intArray);
        assertEquals(int[].class, intArray.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testParamsExceptionWithInvalidClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> invalidClass = String[].class;
        try {
            constructorInstantiator.newInstance(invalidClass);
        } catch (Exception e) {
            constructorInstantiator.paramsException(invalidClass, e);
            fail("Expected InstantiationException");
        }
    }

    @Test(expected = InstantiationException.class)
    public void testParamsExceptionWithNoArgConstructorException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> intType = int[].class;
        try {
            constructorInstantiator.noArgConstructor(intType);
        } catch (Exception e) {
            constructorInstantiator.paramsException(intType, e);
            fail("Expected InstantiationException");
        }
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestOuterClass> testOuterClass = TestOuterClass.class;
        TestOuterClass testOuterClassInstance = constructorInstantiator.newInstance(testOuterClass);
        assertNotNull(testOuterClassInstance);
        assertEquals(TestOuterClass.class, testOuterClassInstance.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterClassNoArgConstructorException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestOuterClass> testOuterClass = TestOuterClass.class;
        try {
            constructorInstantiator.withOuterClass(testOuterClass);
        } catch (Exception e) {
            constructorInstantiator.paramsException(testOuterClass, e);
            fail("Expected InstantiationException");
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<TestInnerClass> testInnerClass = TestInnerClass.class;
        TestInnerClass testInnerClassInstance = constructorInstantiator.newInstance(testInnerClass);
        assertNotNull(testInnerClassInstance);
        assertEquals(TestInnerClass.class, testInnerClassInstance.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassAndNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<TestInnerClass> testInnerClass = TestInnerClass.class;
        try {
            constructorInstantiator.noArgConstructor(testInnerClass);
        } catch (Exception e) {
            constructorInstantiator.paramsException(testInnerClass, e);
            fail("Expected InstantiationException");
        }
    }

    // Test other methods with similar test cases

    static class TestInnerClass {
        // Empty class
    }

    static class TestOuterClass {
        // Empty class
    }
}

//