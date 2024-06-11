import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Mock
    private Field field;

    @Mock
    private Object testClass;

    @Mock
    private Reporter reporter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spyAnnotationEngine = spy(new SpyAnnotationEngine());
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        List<Object> instanceList = new LinkedList<>();
        instanceList.add(null);  // adding null instance
        instanceList.add(Mockito.mock(List.class));  // adding a mocked instance
        instanceList.add(Mockito.spy(new LinkedList<>()));  // adding a spied instance

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isMock(anyObject())).thenReturn(true);

        Field[] fields = new Field[instanceList.size()];
        fields = instanceList.toArray(fields);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        doCallRealMethod().when(spyAnnotationEngine).process(any(Class.class), anyObject());
        doCallRealMethod().when(spyAnnotationEngine).assertNoAnnotations(any(Class.class), any(Field.class), any(Class[].class));

        spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);

        verify(field, times(3)).isAnnotationPresent(Spy.class);
        verifyStatic(atLeastOnce());
        MockitoAnnotations.initMocks(testClass);
        verify(mockUtil, times(1)).isMock(anyObject());
        verify(field, times(1)).set(testClass, instanceList.get(0));
        verify(field, times(1)).set(testClass, instanceList.get(1));
        verify(Mockito, times(1)).reset(instanceList.get(2));
        verify(field, times(1)).set(testClass, instanceList.get(2));

        assertEquals(true, true);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("field");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class,
                MockitoAnnotations.Mock.class, Captor.class);

        verifyZeroInteractions(reporter);
    }
}