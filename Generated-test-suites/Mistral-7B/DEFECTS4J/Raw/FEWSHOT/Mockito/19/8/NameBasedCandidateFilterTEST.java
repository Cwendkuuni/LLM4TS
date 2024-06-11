package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = Mockito.spy(new MockUtil());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidate_SingleMock_NoConflictingFields() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = Mockito.field(Object.class, "fieldName");
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock, result);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoConflictingFields() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.field(Object.class, "fieldName");
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        if (field.getName().equals(mockUtil.getMockName(mock1).toString())) {
            assertEquals(mock1, result);
        } else {
            assertEquals(mock2, result);
        }
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ConflictingFields() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = Mockito.field(Object.class, "fieldName1");
        Object fieldInstance1 = mock(Object.class);
        Object result1 = filter.filterCandidate(mocks, field1, fieldInstance1);
        assertEquals(mock1, result1);

        Field field2 = Mockito.field(Object.class, "fieldName1");
        Object fieldInstance2 = mock(Object.class);

        InvocationOnMock invocation = Mockito.mockingDetails(filter).getInvocations().get(0);
        Answer answer = (Answer) invocation.getMethod().getAnswer();
        Object result2 = answer.call(filter, mocks, field2, fieldInstance2);
        assertEquals(mock2, result2);

        verify(next).filterCandidate(mocks, field2, fieldInstance2);
    }
}