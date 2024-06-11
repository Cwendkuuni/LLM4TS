
package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_withMatchingName_shouldReturnNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withNoMatch_shouldReturnNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withConflictingNames_shouldReturnNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }
}
