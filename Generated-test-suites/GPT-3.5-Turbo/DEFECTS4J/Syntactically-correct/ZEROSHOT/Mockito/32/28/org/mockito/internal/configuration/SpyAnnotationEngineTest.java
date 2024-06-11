import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Object instance = field.get(testClass);
                    if (instance == null) {
                        throw new MockitoException("Cannot create a @Spy for '" + field.getName() + "' field because the *instance* is missing\n" +
                                "The instance must be created *before* initMocks();\n" +
                                "Example of correct usage of @Spy:\n" +
                                "   @Spy List mock = new LinkedList();\n" +
                                "   //also, don't forget about MockitoAnnotations.initMocks();");

                    }
                    if (new MockUtil().isMock(instance)) {
                        // instance has been spied earlier
                        Mockito.reset(instance);
                    } else {
                        field.set(testClass, Mockito.spy(instance));
                    }
                    //Test mock instance
                    assertTrue(new MockUtil().isMock(instance));
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        }
    }

    //TODO duplicated elsewhere
    void assertNoAnnotations(Class annotation, Field field, Class ... undesiredAnnotations) {
        for (Class u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
            }
        }
    }
}