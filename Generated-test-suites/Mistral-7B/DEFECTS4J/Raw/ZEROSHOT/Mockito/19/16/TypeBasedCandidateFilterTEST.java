package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextFilter;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
        mocks = Collections3.newArrayList(mock(String.class), mock(Integer.class), mock(List.class));
    }

    @Test
    public void testFilterCandidate_ReturnsNextFilterWhenFieldTypeIsNotAssignableFromAnyMocks_TypeIsString() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(String.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_ReturnsNextFilterWhenFieldTypeIsNotAssignableFromAnyMocks_TypeIsInteger() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Integer.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_ReturnsNextFilterWhenFieldTypeIsNotAssignableFromAnyMocks_TypeIsList() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(List.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_ReturnsTypeMatchingMock_TypeIsAssignableFromOneOfMocks_TypeIsString() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(String.class);

        Object stringMock = mocks.iterator().next();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(stringMock, result);
    }

    @Test
    public void testFilterCandidate_ReturnsTypeMatchingMock_TypeIsAssignableFromOneOfMocks_TypeIsInteger() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Integer.class);

        Object integerMock = mocks.iterator().next();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(integerMock, result);
    }

    @Test
    public void testFilterCandidate_ReturnsTypeMatchingMock_TypeIsAssignableFromOneOfMocks_TypeIsList() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(List.class);

        Object listMock = mocks.iterator().next();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(listMock, result);
    }

    @Test
    public void testFilterCandidate_CallsNextFilterWhenTypeBasedMatchingFails() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(List.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(nextFilter).filterCandidate(mock(Collection.class), field, fieldInstance);
    }
}