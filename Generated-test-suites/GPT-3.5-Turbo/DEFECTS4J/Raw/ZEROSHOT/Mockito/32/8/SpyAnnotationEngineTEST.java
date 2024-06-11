import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }
    
    @Test
    public void testProcess() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        Annotation spyAnnotation = mock(Spy.class);
        
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.getName()).thenReturn("fieldName");
        
        when(field.get(testClass)).thenReturn(null);
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'fieldName' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
        
        Object mockedInstance = mock(Object.class);
        when(field.get(testClass)).thenReturn(mockedInstance);
        when(new MockUtil().isMock(mockedInstance)).thenReturn(true);
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            verify(mockedInstance, times(1)).reset();
        }
        
        when(new MockUtil().isMock(mockedInstance)).thenReturn(false);
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            verify(field, times(1)).set(testClass, Mockito.spy(mockedInstance));
        }
        
        when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            verify(field, never()).set(testClass, Mockito.spy(mockedInstance));
        }
    }
    
    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> annotation = Spy.class;
        
    }

}