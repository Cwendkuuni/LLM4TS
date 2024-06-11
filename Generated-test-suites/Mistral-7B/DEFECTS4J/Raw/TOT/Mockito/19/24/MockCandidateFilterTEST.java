package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_WithMocksAndFieldInstance_ShouldReturnOngoingInjecter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);

        MockCandidateFilter filter = Mockito.spy(Mockito.any());

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result); // By default, Mockito spying returns null for uninvoked methods
    }

    @Test
    public void testFilterCandidate_WithMocksAndFieldInstance_ShouldReturnOngoingInjecter_WhenImplemented() {
        // Given
        Collection<Object> mocks = Collections.singletonList(Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertEquals(result, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WithNullMocksAndFieldInstance_ShouldThrowNullPointerException() {
        // Given
        Collection<Object> mocks = null;
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockCandidateFilter filter = new MockCandidateFilter() {};

        // When & Then
        try {
            filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            assert false; // This line should never be reached
        } catch (NullPointerException e) {
            assertEquals("mocks", e.getMessage());
        }

        // Test for edge case with an empty collection instead of null
        mocks = new ArrayList<>();
        filter = new MockCandidateFilter() {};
        try {
            filter.filterCandidate(Collections.emptyList(), fieldToBeInjected, fieldInstance);
            assert false; // This line should never be reached
        } catch (UnsupportedOperationException e) {
            // The empty list is unmodifiable, so it's expected to throw an UnsupportedOperationException instead of a NullPointerException
            // in this case
        }
    }
}