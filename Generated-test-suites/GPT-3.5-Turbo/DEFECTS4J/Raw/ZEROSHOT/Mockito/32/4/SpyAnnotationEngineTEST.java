import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {
    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockForNull() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        Assert.assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testProcessFieldIsAccessible() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(null);
        spyAnnotationEngine.process(context, testClass);
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(field, Mockito.times(0)).set(testClass, null);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
    }

    @Test
    public void testProcessInstanceIsNull() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(null);
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                            "The instance must be created *before* initMocks();\n" +
                            "Example of correct usage of @Spy:\n" +
                            "   @Spy List mock = new LinkedList();\n" +
                            "   //also, don't forget about MockitoAnnotations.initMocks();",
                    e.getMessage());
        }
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(field, Mockito.times(0)).set(testClass, null);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
    }

    @Test
    public void testProcessInstanceIsMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAccessible()).thenReturn(true);
        List<String> instance = Mockito.mock(List.class);
        Mockito.when(field.get(testClass)).thenReturn(instance);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        Mockito.when(mockUtil.isMock(instance)).thenReturn(true);
        spyAnnotationEngine.process(context, testClass);
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(mockUtil, Mockito.times(1)).isMock(instance);
        Mockito.verify(Mockito, Mockito.times(1)).reset(instance);
        Mockito.verify(field, Mockito.times(0)).set(testClass, null);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
    }

    @Test
    public void testProcessSpyInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAccessible()).thenReturn(true);
        List<String> instance = new LinkedList<>();
        Mockito.when(field.get(testClass)).thenReturn(instance);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        Mockito.when(mockUtil.isMock(instance)).thenReturn(false);
        spyAnnotationEngine.process(context, testClass);
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(mockUtil, Mockito.times(1)).isMock(instance);
        Mockito.verify(Mockito, Mockito.times(1)).spy(instance);
        Mockito.verify(field, Mockito.times(1)).set(testClass, instance);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
    }

    @Test
    public void testAssertNoAnnotations() throws IllegalAccessException {
        Class annotation = Spy.class;
        Field field = Mockito.mock(Field.class);
        Class[] undesiredAnnotations = new Class[]{Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class};
        Mockito.when(field.isAnnotationPresent(Mockito.any(Class.class))).thenReturn(true);
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).unsupportedCombinationOfAnnotations(Mockito.anyString(), Mockito.anyString());
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        //TODO: invoke assertNoAnnotations method
        Mockito.verify(field, Mockito.times(1)).isAnnotationPresent(Mockito.any(Class.class));
        Mockito.verify(reporter, Mockito.times(3)).unsupportedCombinationOfAnnotations(Mockito.anyString(), Mockito.anyString());
    }
}