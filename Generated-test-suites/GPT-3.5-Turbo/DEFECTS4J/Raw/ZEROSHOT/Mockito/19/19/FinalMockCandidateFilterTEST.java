import org.junit.Before;
import org.junit.Test;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_WithOneMock_ReturnsOngoingInjecterWithMatchingMock() {
        Collection<Object> mocks = new HashSet<>();
        MockCandidate candidate = new MockCandidate();

        mocks.add(candidate);

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object injected = ongoingInjecter.thenInject();

        assertSame(candidate, injected);
    }

    @Test
    public void testFilterCandidate_WithMultipleMocks_ReturnsOngoingInjecterWithNull() {
        Collection<Object> mocks = new HashSet<>();
        mocks.add(new MockCandidate());
        mocks.add(new MockCandidate());

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object injected = ongoingInjecter.thenInject();

        assertNull(injected);
    }

    @Test
    public void testFilterCandidate_WithMatchingMock_CallsBeanPropertySetter() {
        Collection<Object> mocks = new HashSet<>();
        MockCandidate candidate = new MockCandidate();
        mocks.add(candidate);

        Field field = null;
        Object fieldInstance = new Object();

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(any())).thenReturn(true);

        FieldSetter mockFieldSetter = mock(FieldSetter.class);
        when(mockFieldSetter.set(any())).thenReturn(false);

        FinalMockCandidateFilter spyFilter = spy(filter);
        doReturn(mockSetter).when(spyFilter).createBeanPropertySetter(any(), any());
        doReturn(mockFieldSetter).when(spyFilter).createFieldSetter(any(), any());

        OngoingInjecter ongoingInjecter = spyFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object injected = ongoingInjecter.thenInject();

        assertSame(candidate, injected);

        verify(mockSetter, times(1)).set(fieldInstance);
        verify(mockFieldSetter, never()).set(any());
        verify(spyFilter, never()).createFieldSetter(any(), any());
        verify(spyFilter, times(1)).createBeanPropertySetter(any(), any());
    }

    @Test
    public void testFilterCandidate_WithMatchingMock_CallsFieldSetter() {
        Collection<Object> mocks = new HashSet<>();
        MockCandidate candidate = new MockCandidate();
        mocks.add(candidate);

        Field field = null;
        Object fieldInstance = new Object();

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(any())).thenReturn(false);

        FieldSetter mockFieldSetter = mock(FieldSetter.class);
        when(mockFieldSetter.set(any())).thenReturn(true);

        FinalMockCandidateFilter spyFilter = spy(filter);
        doReturn(mockSetter).when(spyFilter).createBeanPropertySetter(any(), any());
        doReturn(mockFieldSetter).when(spyFilter).createFieldSetter(any(), any());

        OngoingInjecter ongoingInjecter = spyFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object injected = ongoingInjecter.thenInject();

        assertSame(candidate, injected);

        verify(mockSetter, times(1)).set(fieldInstance);
        verify(mockFieldSetter, times(1)).set(fieldInstance);
        verify(spyFilter, times(1)).createFieldSetter(any(), any());
        verify(spyFilter, times(1)).createBeanPropertySetter(any(), any());
    }

    @Test
    public void testFilterCandidate_WithMatchingMock_ReportsException() {
        Collection<Object> mocks = new HashSet<>();
        MockCandidate candidate = new MockCandidate();
        mocks.add(candidate);

        Field field = null;
        Object fieldInstance = new Object();

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(any())).thenThrow(new RuntimeException("Inject failed"));

        FieldSetter mockFieldSetter = mock(FieldSetter.class);
        when(mockFieldSetter.set(any())).thenReturn(false);

        FinalMockCandidateFilter spyFilter = spy(filter);
        doReturn(mockSetter).when(spyFilter).createBeanPropertySetter(any(), any());
        doReturn(mockFieldSetter).when(spyFilter).createFieldSetter(any(), any());

        OngoingInjecter ongoingInjecter = spyFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object injected = ongoingInjecter.thenInject();

        assertSame(candidate, injected);

        verify(mockSetter, times(1)).set(any());
        verify(mockFieldSetter, never()).set(any());
        verify(spyFilter, never()).createFieldSetter(any(), any());
        verify(spyFilter, times(1)).createBeanPropertySetter(any(), any());
    }

    private class MockCandidate {
    }
}