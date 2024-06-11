/*
 * Comprehensive test cases for NameBasedCandidateFilter class
 */

package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter nextMockCandidateFilter;
    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Before
    public void setup() {
        nextMockCandidateFilter = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_WithMatchingNames_ReturnsMocksWithMatchingNames() throws Exception {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("name");
        Object fieldInstance = new Object();
        
        MockUtil mockUtil = mock(MockUtil.class);
        nameBasedCandidateFilter.mockUtil = mockUtil;
        
        when(mock1.getName()).thenReturn("mock1");
        when(mock2.getName()).thenReturn("mock2");
        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertEquals(mock1, result.mockNameMatches.get(0));
        assertEquals(mock2, result.mockNameMatches.get(1));
        verify(nextMockCandidateFilter).filterCandidate(result.mockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithoutMatchingNames_ReturnsMocks() throws Exception {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("name");
        Object fieldInstance = new Object();
        
        MockUtil mockUtil = mock(MockUtil.class);
        nameBasedCandidateFilter.mockUtil = mockUtil;
        
        when(mock1.getName()).thenReturn("mock1");
        when(mock2.getName()).thenReturn("mock2");
        when(mockUtil.getMockName(mock1)).thenReturn("mock3");
        when(mockUtil.getMockName(mock2)).thenReturn("mock4");

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(mocks, result.mockNameMatches);
        verify(nextMockCandidateFilter).filterCandidate(result.mockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithNullMocks_ReturnsNull() throws Exception {
        // Arrange
        Collection<Object> mocks = null;
        Field field = Object.class.getDeclaredField("name");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertSame(null, result);
        verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }

}