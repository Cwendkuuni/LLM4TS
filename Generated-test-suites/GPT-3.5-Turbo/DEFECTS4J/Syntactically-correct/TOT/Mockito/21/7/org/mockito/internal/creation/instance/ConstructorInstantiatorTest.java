import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_outerClassInstanceIsNull_noArgConstructorCalled() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        
        Object result = constructorInstantiator.newInstance(String.class);
        
        assertEquals(String.class, result.getClass());
        verifyZeroInteractions(outerClassInstance);
    }
    
    @Test
    public void testNewInstance_outerClassInstanceIsNotNull_withOuterClassCalled() {
        OuterClassInstance outerClassInstanceMock = mock(OuterClassInstance.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstanceMock);
        
        Object result = constructorInstantiator.newInstance(Integer.class);
        
        assertEquals(Integer.class, result.getClass());
        verify(outerClassInstanceMock).getClass();
    }
    
    @Test
    public void testWithOuterClass_validOuterClass_outerClassInstanceUsedInConstructor() {
        OuterClassInstance outerClassInstanceMock = mock(OuterClassInstance.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstanceMock);
        
        Object result = constructorInstantiator.withOuterClass(Double.class);
        
        assertEquals(Double.class, result.getClass());
        verify(outerClassInstanceMock).getClass();
        verifyNoMoreInteractions(outerClassInstanceMock);
    }
    
    @Test(expected = InstantationException.class)
    public void testWithOuterClass_invalidOuterClass_exceptionThrown() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(123);
        
        constructorInstantiator.withOuterClass(Character.class);
    }
    
    @Test
    public void testNoArgConstructor_parameterLessClass_noArgConstructorCalled() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        
        Object result = constructorInstantiator.noArgConstructor(Float.class);
        
        assertEquals(Float.class, result.getClass());
    }
    
    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_classWithParameters_exceptionThrown() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        
        constructorInstantiator.noArgConstructor(Boolean.class);
    }
    
}