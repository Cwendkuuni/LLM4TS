import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() throws NoSuchFieldException {
        SpyAnnotationEngine spyEngine = new SpyAnnotationEngine();
        Field field = SpyAnnotationEngineTest.class.getDeclaredField("mockField");
        Object result = spyEngine.createMockFor(null, field);
        assertNull(result);
    }

    @Test
    public void testProcess() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyEngine = new SpyAnnotationEngine();
        Object testClass = new SpyAnnotationEngineTest();
        Field field = SpyAnnotationEngineTest.class.getDeclaredField("mockField");
        field.setAccessible(true);
        field.set(testClass, null);
        spyEngine.process(SpyAnnotationEngineTest.class, testClass);
        assertNull(field.get(testClass));

        Field mockField = SpyAnnotationEngineTest.class.getDeclaredField("mockField");
        mockField.setAccessible(true);
        mockField.set(testClass, mock(List.class));

        spyEngine.process(SpyAnnotationEngineTest.class, testClass);
        assertTrue(Mockito.mockingDetails(mockField.get(testClass)).isMock());
    }

    private List<String> mockField;
}