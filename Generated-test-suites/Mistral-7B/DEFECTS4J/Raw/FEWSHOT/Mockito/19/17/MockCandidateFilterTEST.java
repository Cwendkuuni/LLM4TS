package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.FieldAccessUtil.getField;
import static org.mockito.internal.util.reflection.FieldAccessUtil.setField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;

@RunWith(MockitoJUnitRunner.class)
public class MockCandidateFilterTest {

    @SuppressWarnings("unchecked")
    @MockitoMock
    private MockCandidateFilter filter;

    @Test
    public void testFilterCandidate_nullCollection() {
        assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        assertNull(filter.filterCandidate(Collections.emptyList(), null, null));
    }

    @Test
    public void testFilterCandidate_nullField() {
        assertNull(filter.filterCandidate(Arrays.asList(new Object(){}), null, null));
    }

    @Test
    public void testFilterCandidate_nullFieldInstance() {
        assertNull(filter.filterCandidate(Arrays.asList(new Object(){}), getField(new Object(){}, "fieldToBeInjected"), null));
    }

    @Test
    public void testFilterCandidate_emptyList() {
        assertNull(filter.filterCandidate(Arrays.asList(), getField(new Object(){}, "fieldToBeInjected"), new Object(){}));
    }

    @Test
    public void testFilterCandidate_filterReturnsNull() {
        Object mock = new Object();
        Object fieldInstance = new Object();
        Object fieldToBeInjected = getField(new Object(){}, "fieldToBeInjected");
        Object filteredCandidate = filter.filterCandidate(Arrays.asList(mock), fieldToBeInjected, fieldInstance);
        assertNull(filteredCandidate);

        setField(mock, "mockedValue", new Object());

        Object filteredCandidate2 = filter.filterCandidate(Arrays.asList(mock), fieldToBeInjected, fieldInstance);
        assertNull(filteredCandidate2);
    }

    @Test
    public void testFilterCandidate_filterReturnsMock() {
        Object mock = new Object();
        Object fieldInstance = new Object();
        Object fieldToBeInjected = getField(new Object(){}, "fieldToBeInjected");
        Object filteredCandidate = filter.filterCandidate(Arrays.asList(new MockitoMock(MockitoMock.Mode.STRICT), mock), fieldToBeInjected, fieldInstance);
        assertNull(filteredCandidate);

        setField(mock, "mockedValue", new Object());

        Object filteredCandidate2 = filter.filterCandidate(Arrays.asList(new MockitoMock(MockitoMock.Mode.STRICT), mock), fieldToBeInjected, fieldInstance);
        assertEquals(mock, filteredCandidate2);
    }

}