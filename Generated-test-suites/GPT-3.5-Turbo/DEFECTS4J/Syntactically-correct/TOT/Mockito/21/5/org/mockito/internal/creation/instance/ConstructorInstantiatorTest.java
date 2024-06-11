import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // typical use case
        MyClass result = instantiator.newInstance(MyClass.class);
        
        // verify that the instance is not null
        Assert.assertNotNull(result);
        
        // edge case: verify that the instance class is of type MyClass
        Assert.assertTrue(result instanceof MyClass);
    }

    @Test
    public void testWithOuterClass() {
        MyClass outerInstance = new MyClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        // typical use case
        MyClass result = instantiator.newInstance(MyClass.class);

        // verify that the instance is not null
        Assert.assertNotNull(result);

        // edge case: verify that the instance class is of type MyClass
        Assert.assertTrue(result instanceof MyClass);

        // edge case: verify that the instance has a reference to the outer instance
        Assert.assertEquals(outerInstance, result.getOuterInstance());
    }

    @Test
    public void testNoArgConstructor_Error() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        try {
            // error scenario: providing a class that does not have a parameter-less constructor
            instantiator.newInstance(MyClassWithConstructor.class);
            
            // fail if no exception is thrown
            Assert.fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            // verify that the exception message contains the correct information
            Assert.assertTrue(e.getMessage().contains("Please ensure it has parameter-less constructor."));
        }
    }

    @Test
    public void testWithOuterClass_Error() {
        MyClass outerInstance = new MyClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        try {
            // error scenario: providing a class that requires an outer class instance of incorrect type
            instantiator.newInstance(MyClassWithOuterClass.class);

            // fail if no exception is thrown
            Assert.fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            // verify that the exception message contains the correct information
            Assert.assertTrue(e.getMessage().contains("Please ensure that the outer instance has correct type"));
        }
    }

    private static class MyClass {
        public MyClass() {}
        public MyClass(MyClass outerInstance) {}
        public MyClass(MyClass outerInstance, int param) {}
        public MyClass(int param) {}
        public MyClass(String param) {}

        public MyClass getOuterInstance() {
            return null;
        }
    }

    private static class MyClassWithConstructor {
        public MyClassWithConstructor(int param) {}
    }

    private static class MyClassWithOuterClass {
        public MyClassWithOuterClass(ConstructorInstantiator outerInstance) {}
    }

}