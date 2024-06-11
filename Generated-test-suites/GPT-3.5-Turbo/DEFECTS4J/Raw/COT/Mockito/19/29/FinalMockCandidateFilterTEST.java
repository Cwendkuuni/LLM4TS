import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    // Test filterCandidate method when mocks collection has one element
    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verifyNoInteractions(field);
        verifyNoInteractions(fieldInstance);
    }

    // Test filterCandidate method when mocks collection has more than one element
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class, times(2));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verifyNoInteractions(field);
        verifyNoInteractions(fieldInstance);
    }

    // Test thenInject method of OngoingInjecter when BeanPropertySetter succeeds
    @Test
    public void testThenInjectWithBeanPropertySetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class, times(1));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object matchingMock = mock(Object.class);

        when(mocks.iterator().next()).thenReturn(matchingMock);
        when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(true);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(matchingMock, result);
        verify(field, never()).set(any(), any());
        verifyNoInteractions(new Reporter());
    }

    // Test thenInject method of OngoingInjecter when BeanPropertySetter fails and FieldSetter succeeds
    @Test
    public void testThenInjectWithFieldSetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class, times(1));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object matchingMock = mock(Object.class);

        when(mocks.iterator().next()).thenReturn(matchingMock);
        when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(false);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(matchingMock, result);
        verify(field).set(null, matchingMock);
        verifyNoInteractions(new Reporter());
    }

    // Test thenInject method of OngoingInjecter when an exception is thrown during injection
    @Test
    public void testThenInjectWithException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class, times(1));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object matchingMock = mock(Object.class);
        RuntimeException exception = mock(RuntimeException.class);
        Reporter reporter = mock(Reporter.class);

        when(mocks.iterator().next()).thenReturn(matchingMock);
        when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenThrow(exception);
        whenNew(Reporter.class).withNoArguments().thenReturn(reporter);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verifyNoInteractions(field).set(any(), any());
        verify(reporter).cannotInjectDependency(eq(field), eq(matchingMock), eq(exception));
    }

}