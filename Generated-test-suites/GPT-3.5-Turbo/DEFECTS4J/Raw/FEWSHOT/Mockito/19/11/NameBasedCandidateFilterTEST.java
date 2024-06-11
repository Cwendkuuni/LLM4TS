
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter mockNextFilter;

    @Before
    public void setUp() {
        mockNextFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };
        filter = new NameBasedCandidateFilter(mockNextFilter);
    }

    @Test
    public void testFilterCandidate_withMatchingFieldNames() throws NoSuchFieldException {
        // Given
        Object mock1 = new Object();
        Object mock2 = new Object();
        Object mock3 = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new Object();

        // When
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, ongoingInjecter.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withNoMatchingFieldNames() throws NoSuchFieldException {
        // Given
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new Object();

        // When
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mockNextFilter, ongoingInjecter.getMockCandidateFilter());
    }
}
