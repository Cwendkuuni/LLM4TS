package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getClass().getDeclaredField("mockField");
        Object fieldInstance = mock(Object.class);
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertNotNull(injecter);
        assertTrue(mocks.contains(fieldInstance));
        assertSame(fieldInstance, injecter.getInstance());
    }

}