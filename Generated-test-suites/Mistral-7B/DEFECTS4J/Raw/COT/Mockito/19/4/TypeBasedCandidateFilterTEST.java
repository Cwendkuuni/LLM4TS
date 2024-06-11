package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.Arrays.asList;

public class TypeBasedCandidateFilterTest {

    TypeBasedCandidateFilter filter;
    MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("testField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getMockCandidateFilter());
        assertEquals(0, result.getCandidates().size());
    }

    @Test
    public void testFilterCandidate_NoMatchingMock() {
        Collection<Object> mocks = asList(new String(), new Integer(1), new Object());
        Field field = getField("testField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getMockCandidateFilter());
        assertEquals(0, result.getCandidates().size());
    }

    @Test
    public void testFilterCandidate_OneMatchingMock() {
        Collection<Object> mocks = asList(new MockitoTestClass(), new String(), new Object());
        Field field = getField("testField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getMockCandidateFilter());
        assertEquals(1, result.getCandidates().size());
        assertEquals(MockitoTestClass.class, result.getCandidates().get(0).getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullCollection() {
        filter.filterCandidate(null, getField("testField"), new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(asList(new Object()), null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullInstance() {
        filter.filterCandidate(asList(new Object()), getField("testField"), null);
    }

    private Field getField(String name) {
        try {
            return TypeBasedCandidateFilter.class.getField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}