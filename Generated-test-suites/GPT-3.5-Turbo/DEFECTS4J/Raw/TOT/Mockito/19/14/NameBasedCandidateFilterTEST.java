import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(any())).thenReturn("mock");
    }

    @Test
    public void testFilterCandidate_WithMatchingName_ReturnsNextFilter() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = fieldInstance.getClass().getField("field");

        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockUtil, times(2)).getMockName(any());
        verify(next).filterCandidate(anyListOf(Object.class), eq(field), eq(fieldInstance));
        assertSame(next, result);
    }

    @Test
    public void testFilterCandidate_WithNoMatchingName_ReturnsNextFilter() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = fieldInstance.getClass().getField("field");

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockUtil, times(2)).getMockName(any());
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertSame(next, result);
    }

    @Test
    public void testFilterCandidate_WithSingleMock_ReturnsNextFilter() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = fieldInstance.getClass().getField("field");

        when(mockUtil.getMockName(mock1)).thenReturn("mock");

        mocks.add(mock1);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockUtil).getMockName(mock1);
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertSame(next, result);
    }

    @Test
    public void testFilterCandidate_WithNoMocks_ReturnsNextFilter() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = fieldInstance.getClass().getField("field");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockUtil, never()).getMockName(any());
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertSame(next, result);
    }

    @Test
    public void testFilterCandidate_WithConflict_ReturnsNextFilter() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = fieldInstance.getClass().getField("field");

        when(mockUtil.getMockName(mock1)).thenReturn("mock");
        when(mockUtil.getMockName(mock2)).thenReturn("mock");

        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockUtil, times(2)).getMockName(any());
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertSame(next, result);
    }
}