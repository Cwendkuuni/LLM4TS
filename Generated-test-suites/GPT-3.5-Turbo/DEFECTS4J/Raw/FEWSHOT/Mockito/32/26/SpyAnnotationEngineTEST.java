
package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertNull(spyAnnotationEngine.createMockFor(null, null));
    }

    @Test
    public void testProcess() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        // test field with @Spy annotation and null instance
        Field field1 = testClass.getClass().getDeclaredField("field1");
        assertNull(field1.get(testClass));
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'field1' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }

        // test field with @Spy annotation and pre-existing mock instance
        Field field2 = testClass.getClass().getDeclaredField("field2");
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isMock(anyObject())).thenReturn(true);
        field2.set(testClass, mockUtil);
        spyAnnotationEngine.process(context, testClass);
        verify(mockUtil).isMock(anyObject());
        verify(mockUtil).reset(anyObject());

        // test field with @Spy annotation and non-mock instance
        Field field3 = testClass.getClass().getDeclaredField("field3");
        field3.set(testClass, new Object());
        spyAnnotationEngine.process(context, testClass);
        assertTrue(field3.get(testClass) instanceof Object);
        verify(mockUtil).isMock(anyObject());
        verifyNoMoreInteractions(mockUtil);
    }

    @Spy
    private Object field1;

    @Spy
    private MockUtil field2;

    @Spy
    private Object field3;

}
