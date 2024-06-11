import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        
        assertNull(result);
    }
    
    @Test
    public void testProcess_withSpyAnnotation() throws Exception {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field = context.getDeclaredField("instance");
        field.setAccessible(true);

        Spy spyAnnotation = mock(Spy.class);
        when(spyAnnotation.value()).thenReturn("instance");

        field.set(testClass, null);

        spyAnnotationEngine.process(context, testClass);

        assertNotNull(field.get(testClass));
        assertTrue(field.get(testClass) instanceof List);
        assertEquals(new LinkedList(), field.get(testClass));
    }

    @Test
    public void testProcess_withSpyAnnotationAndExistingMockInstance() throws Exception {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field = context.getDeclaredField("instance");
        field.setAccessible(true);

        List<String> instance = new LinkedList<>();
        field.set(testClass, instance);

        Spy spyAnnotation = mock(Spy.class);
        when(spyAnnotation.value()).thenReturn("instance");

        spyAnnotationEngine.process(context, testClass);

        assertSame(instance, field.get(testClass));
        verifyZeroInteractions(instance);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withSpyAnnotationAndMissingInstance() throws Exception {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field = context.getDeclaredField("instance");
        field.setAccessible(true);

        Spy spyAnnotation = mock(Spy.class);
        when(spyAnnotation.value()).thenReturn("instance");

        spyAnnotationEngine.process(context, testClass);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testProcess_withMockitoAnnotationsMockAnnotation() throws Exception {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field = context.getDeclaredField("mockitoAnnotationsMock");
        field.setAccessible(true);

        field.set(testClass, null);

        spyAnnotationEngine.process(context, testClass);

        assertNotNull(field.get(testClass));
        assertTrue(field.get(testClass) instanceof List);
        assertEquals(new LinkedList(), field.get(testClass));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testProcess_withMockitoAnnotationsSpyAnnotation() throws Exception {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field = context.getDeclaredField("mockitoAnnotationsSpy");
        field.setAccessible(true);

        field.set(testClass, null);

        spyAnnotationEngine.process(context, testClass);

        assertNotNull(field.get(testClass));
        assertTrue(field.get(testClass) instanceof List);
        assertEquals(new LinkedList(), field.get(testClass));
    }

    @Test
    public void testAssertNoAnnotations_withNoUndesiredAnnotations() throws Exception {
        Class<?> annotation = Mock.class;
        Field field = mock(Field.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);

        verifyNoInteractions(field);
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withUndesiredAnnotation() throws Exception {
        Class<?> annotation = Spy.class;
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withMultipleUndesiredAnnotations() throws Exception {
        Class<?> annotation = Spy.class;
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);
    }
    
    @SuppressWarnings("deprecation")
    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withMockitoAnnotationsMockAnnotation() throws Exception {
        Class<?> annotation = Spy.class;
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);
    }
    
    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withCapturingAnnotation() throws Exception {
        Class<?> annotation = Spy.class;
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);
    }
}