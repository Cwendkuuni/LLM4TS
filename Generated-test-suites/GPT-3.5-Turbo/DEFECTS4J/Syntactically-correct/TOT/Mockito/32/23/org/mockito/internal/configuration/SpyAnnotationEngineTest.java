import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

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
    public void testProcess_SpyFieldHasNullInstance_ThrowMockitoException() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = getField("instance");
        field.setAccessible(true);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'instance' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_SpyFieldIsAlreadyMock_ResetMockInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        List<String> mockedList = Mockito.mock(ArrayList.class);
        Field field = getField("mockedList");
        field.setAccessible(true);
        field.set(testClass, mockedList);

        spyAnnotationEngine.process(context, testClass);

        verify(mockedList, times(1)).clear();
    }

    @Test
    public void testProcess_SpyFieldIsNotMock_CreateAndSetSpyInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        LinkedList<String> list = new LinkedList<>();
        Field field = getField("list");
        field.setAccessible(true);
        field.set(testClass, list);

        spyAnnotationEngine.process(context, testClass);

        assertTrue(Mockito.mockingDetails(list).isSpy());
    }

    private Field getField(String fieldName) {
        try {
            return SpyAnnotationEngine.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}