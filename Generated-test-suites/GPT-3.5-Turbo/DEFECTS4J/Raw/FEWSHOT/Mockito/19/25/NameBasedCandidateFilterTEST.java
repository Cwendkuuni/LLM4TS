package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Before
    public void setUp() {
        nameBasedCandidateFilter = new NameBasedCandidateFilter(null);
    }

    @Test
    public void testFilterCandidate_withMockNameMatches_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_withoutMockNameMatches_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_withConflictingNaming_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_withNullMocks_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = null;
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }
}