import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.Captor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.mockito.Mockito;


public class SpyAnnotationEngineTest {

    @SpyAnnotationEngine
    private SpyAnnotationEngine spyAnnotationEngine;

    @Spy
    private List<String> spyList;

    @Mock
    private List<String> mockList;

    @Captor
    private List<String> captorList;

    @Test
    public void testCreateMockFor() {
        Annotation annotation = new Annotation() {};
        Field field = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        // Test that createMockFor returns null
        // Edge case: null field
        assert result == null;
    }

    @Test
    public void testProcess() {
        Class<?> context = this.getClass();
        Object testClass = this;

        // Test process method
        // Case: No @Spy annotated fields
        spyAnnotationEngine.process(context, testClass);

        // Case: @Spy annotated field with instance already spied
        spyList = Mockito.spy(new LinkedList<>());
        spyAnnotationEngine.process(context, testClass);

        // Case: @Spy annotated field with no instance
        MockitoException exception = assertThrows(MockitoException.class, () -> { spyAnnotationEngine.process(context, testClass); });
        assert exception.getMessage().contains("Cannot create a @Spy for");

        // Case: @Spy annotated field with non-mock instance
        mockList = new LinkedList<>();
        exception = assertThrows(MockitoException.class, () -> { spyAnnotationEngine.process(context, testClass); });
        assert exception.getMessage().contains("Problems initiating spied field");

        // Case: @Spy annotated field with @Mock or @Captor annotation
        exception = assertThrows(Reporter.class, () -> { spyAnnotationEngine.process(context, testClass); });
        assert exception.getMessage().contains("unsupportedCombinationOfAnnotations");
    }

    @Test
    public void testAssertNoAnnotations() {
        Class annotation = Spy.class;
        Field field = null;
        Class[] undesiredAnnotations = {Mock.class, MockitoAnnotations.Mock.class, Captor.class};

        // Test assertNoAnnotations method
        // Case: No undesired annotations
        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);

        // Case: Field annotated with undesired annotation
        field = SpyAnnotationEngineTest.class.getDeclaredField("mockList");
        MockitoException exception = assertThrows(Reporter.class, () -> { spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations); });
        assert exception.getMessage().contains("unsupportedCombinationOfAnnotations");
    }
}