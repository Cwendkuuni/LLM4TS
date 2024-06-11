package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    TypeBasedCandidateFilter filter;
    MockCandidateFilter nextMock;
    Collection<Object> mocks;
    Object fieldInstance;
    Object mockType;
    Field field;

    @Before
    public void setup() {
        filter = new TypeBasedCandidateFilter(new StubMockCandidateFilter()){};
        nextMock = Mockito.mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        mockType = mock(Object.class);
        fieldInstance = spy(new Object());
        field = Mockito.spy(new Field(() -> "field", int.class, fieldInstance.getClass()));
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_NextFilterCalled() {
        mocks.add(mockType);
        when(nextMock.filterCandidate(Mockito.isA(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(nextMock).filterCandidate(Mockito.eq(mocks), Mockito.eq(field), Mockito.eq(fieldInstance));
        verify(nextMock).filterCandidate(Mockito.isA(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class));
    }

    @Test
    public void testFilterCandidate_NoMockTypeMatches_NullReturned() {
        mocks.clear();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_EmptyMockList_NullReturned() {
        mocks.clear();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }
}

class StubMockCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return null;
    }
}