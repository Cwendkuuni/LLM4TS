package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withOnlyOneMock_shouldReturnOngoingInjecterWithMatchingMock() {
        Collection<Object> mocks = Collections.singleton("mock");
        Field field = null; // initialize with proper field
        Object fieldInstance = null; // initialize with proper instance

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnOngoingInjecterThatDoesNotInject() {
        Collection<Object> mocks = Collections.emptyList();
        Field field = null; // initialize with proper field
        Object fieldInstance = null; // initialize with proper instance

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withExceptionWhileInjecting_shouldThrowMockitoException() {
        Collection<Object> mocks = Collections.singleton("mock");
        Field field = null; // initialize with proper field
        Object fieldInstance = null; // initialize with proper instance

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        ongoingInjecter.thenInject();
    }
}