package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.lang.reflect.Field;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;

        // Single mock case
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify that the returned OngoingInjecter is not null
        assertNotNull(result);

        // Verify that thenInject() method returns the mock object
        Object injectedMock = result.thenInject();
        assertEquals(mock, injectedMock);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;

        // Multiple mocks case
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify that the returned OngoingInjecter is not null
        assertNotNull(result);

        // Verify that thenInject() method returns null
        Object injectedMock = result.thenInject();
        assertNull(injectedMock);
    }

}