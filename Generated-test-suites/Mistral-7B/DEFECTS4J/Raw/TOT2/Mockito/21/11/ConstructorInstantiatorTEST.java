
package org.junit.test; // Replace with the actual package name

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.TestClass;
import org.mockito.internal.creation.instance.TestClassWithNoArgConstructor;
import org.mockito.internal.creation.instance.TestClassWithNoOuterClassConstructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClass> testClass = TestClass.class;
        TestClass expectedClassInstance = new TestClass();
        TestClass actualClassInstance = constructorInstantiator.newInstance(testClass);
        Assert.assertEquals(expectedClassInstance, actualClassInstance);
    }

    @Test
    public void testWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClass> testClass = TestClass.class;
        TestClass expectedClassInstance = new TestClass(outerClassInstance);
        TestClass actualClassInstance = constructorInstantiator.withOuterClass(testClass);
        Assert.assertEquals(expectedClassInstance, actualClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_ThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
        constructorInstantiator.noArgConstructor(testClass);
    }

    @Test
    public void testParamsException_CorrectExceptionMessage() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
        Assert.assertEquals(
            "Unable to create mock instance of 'TestClassWithNoArgConstructor'.\nPlease ensure it has parameter-less constructor.",
            constructorInstantiator.paramsException(testClass, new InstantiationException("Test")).getMessage()
        );
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
        constructorInstantiator.newInstance(testClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithNoOuterClassConstructor> testClass = TestClassWithNoOuterClassConstructor.class;
        constructorInstantiator.withOuterClass(testClass);
    }
}
