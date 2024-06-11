package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setup() {
        filter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = SomeClass.class.getField("someField");
        Object fieldInstance = new SomeClass();

        OngoingInjecter result = mock(OngoingInjecter.class);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(result);

        OngoingInjecter output = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter).filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, output);
    }

    public class SomeClass {
        public Object someField;
    }
}