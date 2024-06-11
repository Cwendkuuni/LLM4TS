import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Spy
    private List<String> spyList = new LinkedList<>();

    @Test
    public void testCreateMockFor_returnsNull() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertNotNull(spyAnnotationEngine.createMockFor(Spy.class, spyList.getClass().getDeclaredFields()[0]));
    }
    
    @Test
    public void testProcess_withNonNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyList = mock(List.class);
        spyAnnotationEngine.process(SpyAnnotationEngine.class, this);
        verify(spyList, times(1)).clear();
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(SpyAnnotationEngine.class, this);
    }
    
    @Test
    public void testProcess_withExistingSpy() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyList = Mockito.spy(spyList);
        spyList.add("1");
        spyAnnotationEngine.process(SpyAnnotationEngine.class, this);
        verify(spyList, times(1)).clear();
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_withIllegalAccessException() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = Mockito.mock(Field.class);
        when(field.isAccessible()).thenReturn(false);
        when(field.getName()).thenReturn("testField");
        when(field.get(anyObject())).thenThrow(new IllegalAccessException());
        spyAnnotationEngine.process(SpyAnnotationEngine.class, this);
    }
    
    @Test
    public void testAssertNoAnnotations_withNoUndesiredAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(anyObject())).thenReturn(false);
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
    }
    
    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withUndesiredAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(anyObject())).thenReturn(true);
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
    }
}