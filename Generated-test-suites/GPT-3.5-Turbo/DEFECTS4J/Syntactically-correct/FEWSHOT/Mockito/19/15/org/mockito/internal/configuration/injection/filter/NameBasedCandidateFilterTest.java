
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
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_withMatchingName() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getClass().getDeclaredField("mock");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock)).thenReturn("mock");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), any(Field.class), any(Object.class));
        assertEquals(result, next.filterCandidate(anyList(), any(Field.class), any(Object.class)));
        assertTrue(result instanceof OngoingInjecter);
    }

    @Test
    public void testFilterCandidate_withNoMatchingName() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getClass().getDeclaredField("mock");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock)).thenReturn("");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), any(Field.class), any(Object.class));
        assertEquals(result, next.filterCandidate(anyList(), any(Field.class), any(Object.class)));
        assertTrue(result instanceof OngoingInjecter);
    }

    @Test
    public void testFilterCandidate_withConflictingNamingFields() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getClass().getDeclaredField("mock");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock1)).thenReturn("mock");
        when(mockUtil.getMockName(mock2)).thenReturn("mock");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), any(Field.class), any(Object.class));
        assertEquals(result, next.filterCandidate(anyList(), any(Field.class), any(Object.class)));
        assertTrue(result instanceof OngoingInjecter);
    }
}
