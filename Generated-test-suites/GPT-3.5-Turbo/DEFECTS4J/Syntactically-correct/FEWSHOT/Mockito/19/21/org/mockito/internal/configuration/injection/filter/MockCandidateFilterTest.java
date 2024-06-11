package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(
                Collection<Object> mocks,
                Field fieldToBeInjected,
                Object fieldInstance
            ) {
                // insert implementation here
            }
        };

        // Test 1
        Collection<Object> mocks1 = new ArrayList<Object>();
        Field fieldToBeInjected1 = null;
        Object fieldInstance1 = null;
        OngoingInjecter result1 = filter.filterCandidate(mocks1, fieldToBeInjected1, fieldInstance1);
        assertNotNull(result1);

        // Test 2
        Collection<Object> mocks2 = new ArrayList<Object>();
        mocks2.add(new Object());
        Field fieldToBeInjected2 = null;
        Object fieldInstance2 = null;
        OngoingInjecter result2 = filter.filterCandidate(mocks2, fieldToBeInjected2, fieldInstance2);
        assertNotNull(result2);

        // Test 3
        Collection<Object> mocks3 = new ArrayList<Object>();
        mocks3.add(new Object());
        Field fieldToBeInjected3 = new Field();
        Object fieldInstance3 = null;
        OngoingInjecter result3 = filter.filterCandidate(mocks3, fieldToBeInjected3, fieldInstance3);
        assertNotNull(result3);

        // Test 4
        Collection<Object> mocks4 = new ArrayList<Object>();
        mocks4.add(new Object());
        Field fieldToBeInjected4 = new Field();
        Object fieldInstance4 = new Object();
        OngoingInjecter result4 = filter.filterCandidate(mocks4, fieldToBeInjected4, fieldInstance4);
        assertNotNull(result4);
    }

}