package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertSame;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;

    @Before
    public void setup() {
        mock1 = Mockito.mock(String.class);
        mock2 = Mockito.mock(Number.class);
        fieldInstance = new Object();

        filter = new TypeBasedCandidateFilter(new NoopMockCandidateFilter()) {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return super.filterCandidate(mocks, field, fieldInstance);
            }
        };
    }

    // Test case 1: Check if the filter selects the correct mock when the field type is assignable from the mock type
    @Test
    public void testFilterCandidate_AssignableFrom() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.fieldUtils.getField(filter, "next");
        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(filter).filterCandidate(Mockito.eq(mocks), Mockito.eq(field), Mockito.eq(fieldInstance));

        Object selectedMock = filter.filterCandidate(mocks, Mockito.fieldUtils.getField(String.class, null), fieldInstance);
        assertSame(mock1, selectedMock);
    }

    // Test case 2: Check if the filter does not select the mock when the field type is not assignable from the mock type
    @Test
    public void testFilterCandidate_NotAssignableFrom() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.fieldUtils.getField(filter, "next");
        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(filter).filterCandidate(Mockito.eq(mocks), Mockito.eq(field), Mockito.eq(fieldInstance));

        Object selectedMock = filter.filterCandidate(mocks, Mockito.fieldUtils.getField(Number.class, null), fieldInstance);
        assertNull(selectedMock);
    }

    // Test case 3: Check if the next filter in the chain is called when no candidate is found in this filter
    @Test
    public void testFilterCandidate_NoMatchingMock() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.fieldUtils.getField(filter, "next");
        OngoingInjecter nextMock = Mockito.mock(OngoingInjecter.class);
        Mockito.when(nextMock.filterCandidate(Mockito.eq(mocks), Mockito.eq(field), Mockito.eq(fieldInstance)))
                .thenReturn(new OngoingInjecter() {});

        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(filter).filterCandidate(Mockito.eq(mocks), Mockito.eq(field), Mockito.eq(fieldInstance));

        Object selectedMock = filter.filterCandidate(mocks, Mockito.fieldUtils.getField(Boolean.class, null), fieldInstance);
        assertNotNull(selectedMock);
    }
}

class NoopMockCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return null;
    }
}