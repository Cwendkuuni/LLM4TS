package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockCandidateFilter nextMockCandidateFilter;
    private Field field;
    private Object fieldInstance;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        nextMockCandidateFilter = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
        field = getField();
        fieldInstance = new Object();
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_whenMultipleMocksWithMatchingName_thenNextFilterCalledWithMatchingMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);
        List<Object> expectedMatches = Arrays.asList(mock2, mock3);

        when(field.getName()).thenReturn(mockUtil.getMockName(mock2).toString());

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextMockCandidateFilter.filterCandidate(expectedMatches, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenOnlyOneMockOrNoMatchingMock_thenNextFilterCalledWithOriginalMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1);

        when(field.getName()).thenReturn("name");

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextMockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }

    private Field getField() {
        try {
            return NameBasedCandidateFilter.class.getDeclaredField("mocks");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}