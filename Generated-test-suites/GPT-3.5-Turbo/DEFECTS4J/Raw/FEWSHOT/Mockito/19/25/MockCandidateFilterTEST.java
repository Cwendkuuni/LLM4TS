package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new MockCandidateFilterImpl();
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithMocksNull() {
        OngoingInjecter result = filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithMocksEmpty() {
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), fieldToBeInjected, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithFieldToBeInjectedNull() {
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithFieldInstanceNull() {
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, null);
        assertNull(result);
    }
}