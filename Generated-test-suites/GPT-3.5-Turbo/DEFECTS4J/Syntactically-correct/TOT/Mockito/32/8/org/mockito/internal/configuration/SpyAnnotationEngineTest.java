import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    
    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }
    
    @Test
    public void createMockFor_ShouldReturnNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        
        assertNull(result);
    }
    
    @Test
    public void process_ShouldThrowException_WhenInstanceIsNull() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'fieldName' field because the *instance* is missing\n" +
            		"The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }
    
    @Test
    public void process_ShouldResetInstance_WhenInstanceIsMock() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        MockitoAnnotations.initMocks(testClass);
        
        spyAnnotationEngine.process(context, testClass);
        
        assertNull(testClass.fieldName);
    }
    
    @Test
    public void process_ShouldSpyInstance_WhenInstanceIsNotMock() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        spyAnnotationEngine.process(context, testClass);
        
        assertNotNull(testClass.fieldName);
    }
    
    @Test
    public void assertNoAnnotations_ShouldThrowException_WhenUndesiredAnnotationPresent() {
        Class annotation = mock(Class.class);
        Field field = mock(Field.class);
        Class[] undesiredAnnotations = {Mock.class};
        
        try {
            spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
            fail("Expected Reporter to be called");
        } catch (Exception e) {
            // Verify that the Reporter was called
            verify(mock(Reporter.class)).unsupportedCombinationOfAnnotations(anyString(), anyString());
        }
    }
}