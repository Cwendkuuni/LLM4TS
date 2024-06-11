import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testNewInstance_withNoArgConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Act
        Object result = instantiator.newInstance(TestClass.class);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        // Arrange
        TestOuterClass outerInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        // Act
        Object result = instantiator.newInstance(TestInnerClass.class);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestInnerClass);
        assertEquals(outerInstance, ((TestInnerClass) result).getOuterInstance());
    }
    
    @Test(expected = InstantiationException.class)
    public void testNewInstance_withNoArgConstructor_throwInstantiationException() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Act
        instantiator.newInstance(TestClassWithException.class);
        
        // Expecting InstantiationException
    }
    
    @Test(expected = InstantiationException.class)
    public void testNewInstance_withOuterClass_throwInstantiationException() {
        // Arrange
        TestOuterClass outerInstance = new TestOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        // Act
        instantiator.newInstance(TestInnerClassWithException.class);
        
        // Expecting InstantiationException
    }
    
    private static class TestClass {
        public TestClass() {}
    }
    
    private static class TestOuterClass {}
    
    private static class TestInnerClass {
        private TestOuterClass outerInstance;
        
        public TestInnerClass(TestOuterClass outerInstance) {
            this.outerInstance = outerInstance;
        }
        
        public TestOuterClass getOuterInstance() {
            return outerInstance;
        }
    }
    
    private static class TestClassWithException {
        public TestClassWithException() throws InstantiationException {
            throw new InstantiationException();
        }
    }
    
    private static class TestInnerClassWithException {
        public TestInnerClassWithException(TestOuterClass outerInstance) throws InstantiationException {
            throw new InstantiationException();
        }
    }
}