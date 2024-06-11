package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullCollection() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class); // a mock field
        Object fieldInstance = spy(new Object());

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class); // a mock field
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NullField() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class); // a mock field
        Object fieldInstance = null;

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_FilterCandidateIsReturned() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Mock mock1 = mock(Object.class);
        mocks.add(mock1);
        Field fieldToBeInjected = new Field("testField", Object.class); // a mock field
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_FilterCandidateIsNotReturned() {
        // Given
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Mock mock1 = mock(Object.class);
        Mock mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field fieldToBeInjected = new Field("testField", Object.class); // a mock field
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
    }
}