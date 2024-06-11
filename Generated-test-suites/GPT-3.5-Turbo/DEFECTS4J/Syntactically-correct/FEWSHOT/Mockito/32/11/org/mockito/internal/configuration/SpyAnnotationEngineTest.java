package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
        SpyAnnotationEngine spyAnnotationEngine = spy(new SpyAnnotationEngine());
        MockUtil mockUtil = mock(MockUtil.class);
        Field field = SpyAnnotationEngine.class.getDeclaredField("fields");
        field.setAccessible(true);
        field.set(spyAnnotationEngine, new Field[] {field});

        when(mockUtil.isMock(any())).thenReturn(true);
        
        spyAnnotationEngine.process(SpyAnnotationEngine.class, null);
        
        verify(spyAnnotationEngine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), any(), eq(Captor.class));
        verify(field).setAccessible(eq(true));
        verify(field, times(2)).get(any());

        verify(mockUtil).isMock(any());
        verify(mockUtil).reset(any());

        verify(field).set(eq(null), any());
        verify(field).setAccessible(eq(false));

    }

    @Test(expected = MockitoException.class)
    public void testProcessIllegalAccessException() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = spy(new SpyAnnotationEngine());
        MockUtil mockUtil = mock(MockUtil.class);
        Field field = SpyAnnotationEngine.class.getDeclaredField("fields");
        field.setAccessible(true);
        field.set(spyAnnotationEngine, new Field[] {field});

        when(mockUtil.isMock(any())).thenReturn(false);
        
        spyAnnotationEngine.process(SpyAnnotationEngine.class, null);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = SpyAnnotationEngine.class.getDeclaredField("fields");
        field.setAccessible(true);
        field.set(spyAnnotationEngine, new Field[] {field});
        
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

    @Test(expected = AssertionError.class)
    public void testAssertNoAnnotationsWithAnnotation() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = SpyAnnotationEngine.class.getDeclaredField("fields");
        field.setAccessible(true);
        field.set(spyAnnotationEngine, new Field[] {field});
        
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Spy.class);
    }
}