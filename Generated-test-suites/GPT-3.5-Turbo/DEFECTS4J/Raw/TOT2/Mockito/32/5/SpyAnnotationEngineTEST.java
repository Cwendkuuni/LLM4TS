import org.junit.Test;
import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_NullInstance() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        // When
        Object result = spyAnnotationEngine.createMockFor(null, null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    public void testProcess_NullTestClass() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        // When
        try {
            spyAnnotationEngine.process(null, null);
        } catch (Exception e) {
            // Then
            fail("Should not throw an exception for a null test class");
        }
    }
    
    @Test
    public void testProcess_EmptyFields() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        
        // When
        spyAnnotationEngine.process(context, null);
        
        // Then
        // No assertions since there are no annotated fields
    }
    
    @Test
    public void testProcess_SpyField() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClass testClass = new TestClass();
        
        // When
        spyAnnotationEngine.process(testClass.getClass(), testClass);
        
        // Then
        assertNotNull(testClass.spyList);
    }
    
    class TestClass {
        @Spy
        List<String> spyList = new ArrayList<>();
    }
}