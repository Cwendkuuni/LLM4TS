
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(Object.class);
        assertNull(result);
    }
    
    @Test
    public void testConstructorInstantiatorWithNonNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Object result = instantiator.newInstance(Object.class);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testConstructorInstantiatorWithNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(ClassWithNoArgConstructor.class);
        assertNotNull(result);
        assertTrue(result instanceof ClassWithNoArgConstructor);
    }
    
    @Test
    public void testConstructorInstantiatorWithArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        Object result = instantiator.newInstance(ClassWithArgConstructor.class);
        assertNotNull(result);
        assertTrue(result instanceof ClassWithArgConstructor);
    }
    
    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithNoArgConstructor_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithoutNoArgConstructor.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithArgConstructor_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithoutArgConstructor.class);
    }
    
    private static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor() {
        }
    }
    
    private static class ClassWithArgConstructor {
        private OuterClass outerClass;
        
        public ClassWithArgConstructor(OuterClass outerClass) {
            this.outerClass = outerClass;
        }
    }
    
    private static class ClassWithoutNoArgConstructor {
        public ClassWithoutNoArgConstructor(int arg) {
        }
    }
    
    private static class ClassWithoutArgConstructor {
    }
    
    private static class OuterClass {
    }
}
