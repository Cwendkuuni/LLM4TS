import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.reflect.FieldUtil;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotationAndField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        assertNull(annotationEngine.createMockFor(null, null));
    }

    @Test
    public void testProcessWithNoSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        annotationEngine.process(context, testClass);
        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcessWithSpyAnnotationAndMissingInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);
        annotationEngine.process(context, testClass);

        try {
            annotationEngine.createMockFor(new Spy(), field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testProcessWithSpyAnnotationAndMockInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);
        field.set(testClass, instance);
        annotationEngine.process(context, testClass);

        Object spiedInstance = annotationEngine.createMockFor(new Spy(), field);
        verify(instance).getClass();
        verify(instance).toString();
        verifyNoMoreInteractions(instance);
        verify(spiedInstance).getClass();
        verify(spiedInstance).toString();
    }

    @Test
    public void testProcessWithSpyAnnotationAndAlreadySpyedInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);
        field.set(testClass, instance);
        Mockito.reset(instance);
        annotationEngine.process(context, testClass);
        Mockito.verifyReset(instance);

        Object spiedInstance = annotationEngine.createMockFor(new Spy(), field);
        verify(instance).getClass();
        verify(instance).toString();
        verifyNoMoreInteractions(instance);
        verify(spiedInstance).getClass();
        verify(spiedInstance).toString();
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcessWithSpyAnnotationAndInaccessibleField() throws IllegalAccessException {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = CustomAccessibleContext.class;
        Object testClass = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);
        annotationEngine.process(context, testClass);

        annotationEngine.createMockFor(new Spy(), field);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithSpyAnnotationAndAnnotationPresentMultipleTimes() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);

        field.setAccessible(true);
        field.set(testClass, mock(Object.class));

        annotationEngine.process(context, testClass);
        annotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithSpyAnnotationAndMixedAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);

        field.setAccessible(true);
        field.set(testClass, mock(Object.class));

        annotationEngine.process(context, testClass);

        Spy spyAnnotation = new Spy();
        Mock annotation = new MockitoAnnotations.Mock();
        Captor captorAnnotation = new Captor();

        field.setAnnotation(new SpyAnnotation[]{spyAnnotation, annotation, captorAnnotation});
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithSpyAnnotationAndCaptorAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = mock(Object.class);
        Field field = FieldUtil.getField(context, "testField", Object.class);

        field.setAccessible(true);
        field.set(testClass, mock(Object.class));

        annotationEngine.process(context, testClass);

        Spy spyAnnotation = new Spy();
        Captor captorAnnotation = new Captor();

        field.setAnnotation(new SpyAnnotation[]{spyAnnotation, captorAnnotation});
    }

    static class CustomAccessibleContext extends AccessControlContext {
        @Override
        public AccessControlContext getContextClass() {
            return null;
        }

        @Override
        public boolean checkPermission(int perm, Object res) {
            return true;
        }

        @Override
        public void checkPermission(int perm, Object res) throws SecurityException {
            if (perm == RuntimePermission.ACCESS_DECLARED_ANNOTATIONS) {
                throw new SecurityException("Not allowed to access annotations");
            }
            super.checkPermission(perm, res);
        }

        @Override
        public PermissionCollection getPermisions(Class clazz) {
            return null;
        }
    }
}