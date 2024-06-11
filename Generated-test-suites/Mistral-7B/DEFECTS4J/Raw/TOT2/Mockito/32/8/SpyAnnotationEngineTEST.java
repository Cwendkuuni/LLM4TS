import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SpyAnnotationEngineTest {

    @Mock
    private Annotation annotation;

    @Mock
    private Field field;

    @Mock
    private Class<?> context;

    @Mock
    private Object testClass;

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.createMockFor(annotation, field);
        verify(annotationEngine, times(1)).createMockFor(argument(), argument());
    }

    @Test
    public void testProcess() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        initMocks(this);

        when(context.getDeclaredFields()).thenReturn(new Field[0]);
        annotationEngine.process(context, testClass);
        verify(annotationEngine, times(1)).process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithNullTestClass() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, null);
    }

    @Test
    public void testProcessWithSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Field spyField = Mockito.mock(Field.class);
        when(context.getDeclaredFields()).thenReturn(new Field[]{spyField});
        when(spyField.isAnnotationPresent(Spy.class)).thenReturn(true);

        initMocks(this);

        annotationEngine.process(context, testClass);

        verify(annotationEngine, times(1)).process(context, testClass);
        verify(spyField, times(1)).set(testClass, argThat(instanceOf(org.mockito.internal.util.mock.MockitoCore.spiedInstance(any()))));
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = Mockito.mock(Field.class);

        annotationEngine.assertNoAnnotations(Annotation.class, field, Mockito.class);

        verify(annotationEngine, times(1)).assertNoAnnotations(ArgumentMatchers.any(), ArgumentMatchers.eq(field), ArgumentMatchers.array(Mockito.class));
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithAnnotationPresent() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        annotationEngine.assertNoAnnotations(Annotation.class, field, Mockito.class);
    }
}