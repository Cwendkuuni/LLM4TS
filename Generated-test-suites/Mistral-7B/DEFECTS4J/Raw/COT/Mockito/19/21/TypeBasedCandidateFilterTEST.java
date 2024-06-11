package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;
import org.mockito.mock.MockMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = Arrays.asList(mock(String.class), mock(Integer.class), mock(Object.class));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_EmptyMockCollection() {
        Collection<Object> emptyMockCollection = new ArrayList<>();
        Object result = filter.filterCandidate(emptyMockCollection, field, fieldInstance);
        assertEquals(nextMock, result);
    }

    @Test
    public void testFilterCandidate_NullField() {
        Object result = filter.filterCandidate(mocks, null, fieldInstance);
        assertEquals(nextMock, result);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Object result = filter.filterCandidate(mocks, field, null);
        assertEquals(nextMock, result);
    }

    @Test
    public void testFilterCandidate_FieldTypeNotAssignableFromAnyMockType() {
        field.setType(String[].class);
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result);
    }

    @Test
    public void testFilterCandidate_FieldTypeAssignableFromOneMockType() {
        field.setType(Integer.class);
        Object mock = mocks.get(1);
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockCollection() {
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testNextFilterCandidateCall() {
        Object mock = mocks.get(1);
        when(nextMock.filterCandidate(Collections.singletonList(mock), field, fieldInstance)).thenReturn(mock);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(nextMock).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
        assertEquals(mock, result);
    }

    private <T> MockMethod<T> when(T mock) {
        return spy(mock).mock();
    }
}