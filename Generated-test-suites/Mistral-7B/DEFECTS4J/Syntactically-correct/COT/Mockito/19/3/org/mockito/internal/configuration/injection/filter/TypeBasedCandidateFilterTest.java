package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextFilter;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testConstructor() {
        // Basic functionality test
        assertNull(filter.next);
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        // Empty collection test
        Collection<Object> mocks = Collections3.emptyList();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = mock(String.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        // No verification needed since next filter is not called
    }

    @Test
    public void testFilterCandidate_noTypeMatch() {
        // No type match test
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = mock(String.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(Collections.singletonList(mock(Object.class)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_typeMatch() {
        // Type match test
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = mock(String.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMockTypeMatches = new ArrayList<>();
        expectedMockTypeMatches.add(mock(String.class));

        verify(nextFilter).filterCandidate(expectedMockTypeMatches, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullCollection() {
        // Null collection test with exception handling
        Collection<Object> mocks = null;
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = mock(String.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullField() {
        // Null field test with exception handling
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = mock(String.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullFieldInstance() {
        // Null field instance test with exception handling
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }
}