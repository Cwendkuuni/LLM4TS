package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;

    @Before
    public void setup() {
        next = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter(mocks, field, fieldInstance);
            }
        };
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndMatchingName_shouldReturnFilteredMocks() {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        mocks.add(new Object());

        Field field = new Field();
        field.setName("mock1");

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
        assertEquals(1, result.getMocks().size());
        assertEquals("mock1", result.getMocks().get(0));
        assertEquals(field, result.getField());
        assertNull(result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndNoMatchingName_shouldReturnAllMocks() {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        mocks.add(new Object());

        Field field = new Field();
        field.setName("mock4");

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
        assertEquals(3, result.getMocks().size());
        assertEquals(mocks, result.getMocks());
        assertEquals(field, result.getField());
        assertNull(result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withSingleMockAndMatchingName_shouldReturnFilteredMock() {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());

        Field field = new Field();
        field.setName("mock1");

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
        assertEquals(1, result.getMocks().size());
        assertEquals("mock1", result.getMocks().get(0));
        assertEquals(field, result.getField());
        assertNull(result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withSingleMockAndNoMatchingName_shouldReturnAllMocks() {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());

        Field field = new Field();
        field.setName("mock2");

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
        assertEquals(1, result.getMocks().size());
        assertEquals(mocks, result.getMocks());
        assertEquals(field, result.getField());
        assertNull(result.getFieldInstance());
    }
}