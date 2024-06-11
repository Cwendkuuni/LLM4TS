package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.MockCandidate;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.mock.MockMethod;
import org.mockito.internal.util.collections.CollectionUtils;
import org.mockito.internal.util.reflection.Whitebox;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mocks = newArrayList(new MockMethod(), new Object());
    }

    @Test
    public void testFilterCandidateWithMockTypeMatch() {
        Object mockTypeMatch = new MockMethod();
        Collection<Object> filteredMocks = filter.filterCandidate(mocks, mockType(Field.class), mock(Object.class));
        assertNotNull(filteredMocks);
        assertNotNull(getInternalState(filteredMocks, OngoingInjecter.class).candidate);
        assertEquals(mockTypeMatch, getInternalState(filteredMocks, OngoingInjecter.class).candidate);
    }

    @Test
    public void testFilterCandidateWithNoMockTypeMatch() {
        Collection<Object> filteredMocks = filter.filterCandidate(mocks, mock(String.class), mock(Object.class));
        assertNotNull(filteredMocks);
        assertTrue(filteredMocks.isEmpty());
    }

    @Test
    public void testFilterCandidateNextIsCalledWhenNoMockTypeMatch() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        Collection<Object> filteredMocks = filter.filterCandidate(mocks, mock(String.class), mock(Object.class));
        verify(nextMock).filterCandidate(mocks, mock(Field.class), mock(Object.class));
        assertNotNull(filteredMocks);
        assertTrue(filteredMocks.isEmpty());
    }
}