package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter filter = new MockCandidateFilterImpl();

        Collection<Object> mocks = null;
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(result);
    }

    // TODO: Add more test cases to comprehensively test all possible scenarios

}