import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        
        assertNull(result);
    }

    @Test
    public void testProcess_withNullInstance() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withMockInstance() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        MockitoAnnotations.initMocks(testClass);
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance = Mockito.mock(Object.class);
                Mockito.when(field.get(testClass)).thenReturn(instance);

                spyAnnotationEngine.process(context, testClass);

                assertTrue(new MockUtil().isMock(instance));
            }
        }
    }

    @Test
    public void testProcess_withResetInstance() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        MockitoAnnotations.initMocks(testClass);
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance = Mockito.spy(new Object());
                Mockito.when(field.get(testClass)).thenReturn(instance);

                spyAnnotationEngine.process(context, testClass);

                Mockito.verify(instance, Mockito.times(1)).reset();
            }
        }
    }

}