import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    // Test no-arg constructor
    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(MyClass.class);
        Assert.assertNotNull(obj);
    }

    // Test with outer class instance constructor
    @Test
    public void testWithOuterClassConstructor() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        MyClass obj = instantiator.newInstance(MyClass.class);
        Assert.assertNotNull(obj);
    }

    // Test with invalid outer class instance
    @Test(expected = InstantationException.class)
    public void testInvalidOuterClassConstructor() {
        Object invalidOuterClass = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(invalidOuterClass);
        instantiator.newInstance(MyClass.class);
    }

    // Test class with no parameter-less constructor
    @Test(expected = InstantationException.class)
    public void testNoParameterLessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithConstructorParams.class);
    }

    // Test class with exception during instantiation
    @Test(expected = InstantationException.class)
    public void testExceptionDuringInstantiation() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithExceptionDuringInstantiation.class);
    }

    // Test class with private constructor
    @Test(expected = NoSuchMethodException.class)
    public void testPrivateConstructor() throws Exception {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithPrivateConstructor.class);
    }
    
    // Test class with protected constructor
    @Test
    public void testProtectedConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(ClassWithProtectedConstructor.class);
        Assert.assertNotNull(obj);
    }
    
    // Test class with package-private constructor
    @Test
    public void testPackagePrivateConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(ClassWithPackagePrivateConstructor.class);
        Assert.assertNotNull(obj);
    }

    // Test class with public constructor with params
    @Test
    public void testPublicConstructorWithParams() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(ClassWithPublicConstructorParams.class);
        Assert.assertNotNull(obj);
    }

    static class MyClass {
    }

    static class OuterClass {
    }

    static class ClassWithConstructorParams {
        public ClassWithConstructorParams(int param) {
        }
    }

    static class ClassWithExceptionDuringInstantiation {
        public ClassWithExceptionDuringInstantiation() throws Exception {
            throw new Exception("Test exception");
        }
    }

    static class ClassWithPrivateConstructor {
        private ClassWithPrivateConstructor() {
        }
    }
    
    static class ClassWithProtectedConstructor {
        protected ClassWithProtectedConstructor() {
        }
    }
    
    static class ClassWithPackagePrivateConstructor {
        ClassWithPackagePrivateConstructor() {
        }
    }
    
    static class ClassWithPublicConstructorParams {
        public ClassWithPublicConstructorParams(int param) {
        }
    }
    
}