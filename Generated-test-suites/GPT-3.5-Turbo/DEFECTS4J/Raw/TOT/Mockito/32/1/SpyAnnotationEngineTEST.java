import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {

    @Test
    public void createMockFor_annotationIsNull_returnsNull() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getMockField();
        Object mock = spyAnnotationEngine.createMockFor(null, field);
        Assert.assertNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void createMockFor_fieldIsNull_throwsMockitoException() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.createMockFor(Mockito.mock(Annotation.class), null);
    }

    @Test
    public void process_containsSpyAnnotatedFields_createsSpiesSuccessfully() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = getTestClass();
        Object testClass = getTestClassInstance();
        
        spyAnnotationEngine.process(context, testClass);
        
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance = field.get(testClass);
                Assert.assertTrue(new MockUtil().isMock(instance));
            }
        }
    }

    @Test
    public void process_containsSpyAnnotatedFields_instanceIsNull_throwsMockitoException() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = getTestClass();
        Object testClass = new Object();
        
        try {
            spyAnnotationEngine.process(context, testClass);
            Assert.fail("Should throw an exception");
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'fieldName' field because the *instance* is missing\n" +
                                "The instance must be created *before* initMocks();\n" +
                                "Example of correct usage of @Spy:\n" +
                                "   @Spy List mock = new LinkedList();\n" +
                                "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void process_containsSpyAnnotatedFields_instanceIsSpy_resetsInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = getTestClass();
        Object testClass = getTestClassInstance();
        
        spyAnnotationEngine.process(context, testClass);
        
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance = field.get(testClass);
                Mockito.verify(instance, Mockito.times(1)).reset();
            }
        }
    }

    @Test
    public void process_containsSpyAnnotatedFields_instanceIsNotSpy_createsSpy() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = getTestClass();
        Object testClass = getTestClassInstance();
        
        spyAnnotationEngine.process(context, testClass);
        
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance = field.get(testClass);
                Assert.assertTrue(new MockUtil().isMock(instance));
            }
        }
    }

    @Test
    public void assertNoAnnotations_fieldContainsUndesiredAnnotations_throwsReporterException() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getUndesiredAnnotationField();
        
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            Assert.fail("Should throw an exception");
        } catch (ReporterException e) {
            Assert.assertEquals("Unsupported combination of annotations: 'Spy' cannot be used with 'Mock' or 'Mock' or 'Captor'", e.getMessage());
        }
    }

    private Field getMockField() {
        try {
            return this.getClass().getDeclaredField("mockField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> getTestClass() {
        return this.getClass();
    }

    private Object getTestClassInstance() {
        return new SpyAnnotationEngineTest();
    }

    private Field getUndesiredAnnotationField() {
        try {
            return this.getClass().getDeclaredField("undesiredAnnotationField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Spy
    private List<Object> mockField;

    @Spy
    @Mock
    private List<Object> undesiredAnnotationField;

}