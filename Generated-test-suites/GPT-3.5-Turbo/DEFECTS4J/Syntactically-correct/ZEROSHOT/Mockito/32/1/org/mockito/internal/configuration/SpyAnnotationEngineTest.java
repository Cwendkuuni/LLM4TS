import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {
    
    private SpyAnnotationEngine spyAnnotationEngine;
    
    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }
    
    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        
        assertNull(result);
    }
    
    @Test
    public void testProcess() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        spyAnnotationEngine.process(context, testClass);
        
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object instance = field.get(testClass);
                assertNotNull(instance);
                assertTrue(new MockUtil().isMock(instance));
            }
        }
    }
    
    @Test(expected = MockitoException.class)
    public void testProcessMissingInstance() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        spyAnnotationEngine.process(context, testClass);
    }
    
    @Test
    public void testProcessResetInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTestClass.class;
        Object testClass = new SpyAnnotationEngineTestClass();
        
        spyAnnotationEngine.process(context, testClass);
        
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object instance = field.get(testClass);
                assertNotNull(instance);
                assertTrue(new MockUtil().isMock(instance));
            }
        }
        
        spyAnnotationEngine.process(context, testClass);
        
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object instance = field.get(testClass);
                assertNotNull(instance);
                assertTrue(new MockUtil().isMock(instance));
            }
        }
    }
    
    @Test(expected = MockitoException.class)
    public void testProcessIllegalAccessException() {
        Class<?> context = SpyAnnotationEngineTestClass.class;
        Object testClass = new SpyAnnotationEngineTestClass();
        
        spyAnnotationEngine.process(context, testClass);
    }
    
    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Class<SpyAnnotationEngine> annotation = Spy.class;
        
        Field field = SpyAnnotationEngineTest.class.getDeclaredField("testField");
        
        Class<Mock> undesiredAnnotation = Mock.class;
        Class<org.mockito.MockitoAnnotations.Mock> undesiredAnnotation2 = org.mockito.MockitoAnnotations.Mock.class;
        Class<Captor> undesiredAnnotation3 = Captor.class;
        
        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation, undesiredAnnotation2, undesiredAnnotation3);
        
        field = SpyAnnotationEngineTestClass.class.getDeclaredField("testField2");
        
        undesiredAnnotation = Mock.class;
        undesiredAnnotation2 = org.mockito.MockitoAnnotations.Mock.class;
        undesiredAnnotation3 = Captor.class;
        
        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation, undesiredAnnotation2, undesiredAnnotation3);
    }
    
    public class SpyAnnotationEngineTestClass {
        @Spy
        private List<String> testField;
        
        @Spy
        @Mock
        private List<String> testField2;
    }
}