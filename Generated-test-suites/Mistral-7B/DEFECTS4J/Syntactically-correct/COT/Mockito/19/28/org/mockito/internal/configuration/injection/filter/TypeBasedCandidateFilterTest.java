package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verifyZeroInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_NoTypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new ArrayList<>());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(field.getType()).thenReturn(String.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verifyZeroInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_TypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new ArrayList<>());
        mocks.add(Mockito.mock(List.class));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(field.getType()).thenReturn(List.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(Mockito.mock(List.class), result);
        verify(nextMock).filterCandidate(mocks, eq(field), eq(fieldInstance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field nullField = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, nullField, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object nullFieldInstance = null;

        filter.filterCandidate(mocks, field, nullFieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullMockCollection() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_NextFilterThrowsException() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(List.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(nextMock.filterCandidate(mocks, eq(field), eq(fieldInstance))).thenThrow(new MockitoException());

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail();
        } catch (MockitoException e) {
            // expected
        }

        verify(nextMock).filterCandidate(mocks, eq(field), eq(fieldInstance));
    }
}