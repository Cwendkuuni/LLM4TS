package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_ReturnsNull_WhenFieldInstanceIsNotNullAndMocksCollectionIsEmpty() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        Object fieldInstance = new Object();
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenFieldInstanceIsNotNull() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(Mockito.spy(Mockito.mock(String.class)), Mockito.spy(Mockito.mock(Integer.class)));
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_ReturnsSameInstance_WhenFieldInstanceIsMockAndMocksCollectionContainsInstanceOfSameType() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        String mockString = Mockito.mock(String.class);
        Object fieldInstance = mockString;
        Collection<Object> mocks = Arrays.asList(mockString, Mockito.spy(Mockito.mock(Integer.class)));
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));

        assertSame(fieldInstance, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_ReturnsDifferentInstance_WhenFieldInstanceIsMockAndMocksCollectionDoesNotContainInstanceOfSameType() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        String mockString1 = Mockito.mock(String.class);
        Object fieldInstance = mockString1;
        Collection<Object> mocks = Arrays.asList(Mockito.spy(Mockito.mock(Integer.class)), Mockito.spy(Mockito.mock(String.class)));
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));
        String mockString2 = Mockito.mock(String.class);

        assertSame(fieldInstance, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        assertNotSame(fieldInstance, filter.filterCandidate(mocks, fieldToBeInjected, mockString2));
    }

    @Test
    public void testFilterCandidate_ReturnsMockInstance_WhenFieldInstanceIsNotNullAndMatchesCandidateFilter() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        Object fieldInstance = new Object() {
            @Override
            public String toString() {
                return "mockable instance";
            }
        };
        Collection<Object> mocks = Arrays.asList(Mockito.spy(Mockito.mock(String.class)), Mockito.spy(fieldInstance));
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Mockito.verify(filter).inject(fieldToBeInjected, fieldInstance);
        Mockito.verify(result).equals(fieldInstance);
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenFieldInstanceIsNull() {
        MockCandidateFilter filter = Mockito.spy(Mockito.any());
        Collection<Object> mocks = Arrays.asList(Mockito.spy(Mockito.mock(String.class)), Mockito.spy(Mockito.mock(Integer.class)));
        Field fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }
}