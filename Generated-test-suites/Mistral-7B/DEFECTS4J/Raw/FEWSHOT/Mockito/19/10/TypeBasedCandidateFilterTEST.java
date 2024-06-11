package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMocks_thenReturnFilteredMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Number.class);
        Object mock3 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = new FieldStub();

        Object fieldInstance = new ObjectStub();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1.getClass(), result.getClass());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromAnyMock_thenReturnNull() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Object mock3 = mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = new FieldStub();

        Object fieldInstance = new ObjectStub();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsFilteredMock_thenReturnThatMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Object mock3 = mock(String.class);
        Object mock4 = mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        mocks.add(mock4);

        Field field = new FieldStub();

        Object fieldInstance = new ObjectStub();

        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(mock1);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result);
    }

    private static class FieldStub {
        public Class<?> getType() {
            return String.class;
        }
    }

    private static class ObjectStub {
    }
}