package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.MockUtil.getMockName;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockUtil;
import org.mockito.internal.util.naming.SimpleName;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object() {});
        Field field = new Field("testField", String.class, new Object() {});
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchingName() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object() {
            @Override
            public String toString() {
                return new SimpleName("mock1").toString();
            }
        });
        mocks.add(new Object() {
            @Override
            public String toString() {
                return new SimpleName("mock2").toString();
            }
        });
        Field field = new Field("testField", String.class, new Object());
        Object fieldInstance = new Object();

        List<Object> expectedMatchingMocks = new ArrayList<>();
        expectedMatchingMocks.add(mocks.get(0));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMatchingMocks, result.getCandidates());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNonMatchingNames() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object() {
            @Override
            public String toString() {
                return new SimpleName("mock1").toString();
            }
        });
        mocks.add(new Object() {
            @Override
            public String toString() {
                return new SimpleName("mock2").toString();
            }
        });
        Field field = new Field("testField", String.class, new Object());
        Object fieldInstance = new Object();

        Field conflictingField = new Field("conflictingField", String.class, new Object());
        Object conflictingFieldInstance = new Object();

        Collection<Object> conflictingMockNameMatches = new ArrayList<>();
        conflictingMockNameMatches.add(mocks.get(1));

        OngoingInjecter nextResult = nextMockFilter.filterCandidate(conflictingMockNameMatches, conflictingField, conflictingFieldInstance);

        Collection<Object> mocksWithConflictingField = new ArrayList<>();
        mocksWithConflictingField.addAll(mocks);
        mocksWithConflictingField.add(nextResult.getCandidates().get(0));

        OngoingInjecter result = filter.filterCandidate(mocksWithConflictingField, field, fieldInstance);

        assertEquals(mocks.get(0), result.getCandidates().get(0));
    }

    @Test
    public void testFilterCandidateWithNoMatchingMocks() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, new Object());
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }
}