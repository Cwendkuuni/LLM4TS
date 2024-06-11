import org.junit.Test;
import org.mockito.internal.creation.instance.InstantationException;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        String instance = instantiator.newInstance(String.class);
        
        assertNotNull(instance);
        assertTrue(instance instanceof String);
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        InnerClass instance = instantiator.newInstance(InnerClass.class);
        
        assertNotNull(instance);
        assertTrue(instance instanceof InnerClass);
        assertSame(outerInstance, instance.getOuterInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_wrongOuterInstanceType() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(123);
        
        instantiator.newInstance(InnerClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_noParameterlessConstructor() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        instantiator.newInstance(NotParameterlessConstructorClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_exceptionInInstantiation() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        instantiator.newInstance(ExceptionThrowingClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_exceptionInInstantiation() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        
        instantiator.newInstance(ExceptionThrowingClass.class);
    }
    
    private static class OuterClass {
        
    }
    
    private static class InnerClass {
        private final OuterClass outerInstance;
        
        InnerClass(OuterClass outerInstance) {
            this.outerInstance = outerInstance;
        }
        
        OuterClass getOuterInstance() {
            return outerInstance;
        }
    }
    
    private static class NotParameterlessConstructorClass {
        NotParameterlessConstructorClass(String param) {
        
        }
    }
    
    private static class ExceptionThrowingClass {
        ExceptionThrowingClass() {
            throw new RuntimeException("Unable to instantiate");
        }
    }
}