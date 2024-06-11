
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeBasedCandidateFilterTest {

    @Mock
    private MockCandidateFilter next;

    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = this.getClass().getDeclaredField("mock1");

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, null);

        verify(next).filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidateNoMocks() throws Exception {
        List<Object> mocks = new ArrayList<>();
        Field field = this.getClass().getDeclaredField("mock1");

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, null);

        verify(next).filterCandidate(mocks, field, null);
    }
}
