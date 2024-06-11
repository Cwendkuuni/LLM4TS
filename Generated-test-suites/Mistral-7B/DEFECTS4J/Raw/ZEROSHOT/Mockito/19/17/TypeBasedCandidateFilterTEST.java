package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_whenTypeMatches_shouldReturnFilteredMock() {
        // given
        Object fieldInstance = mock(String.class);
        Field field = Mockito.spy(Field.class);
        field.setType(String.class);
        Object mock1 = mock(String.class);
        Object mock2 = mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Stubber stubber = Mockito.stub(nextMock.class);
        Object filteredMock = mock(String.class);
        stubber.when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.eq(field), Mockito.eq(fieldInstance)))
                .thenReturn(filteredMock);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(filteredMock, result.getMock());
        stubber.verify(nextMock).filterCandidate(mockTypeMatches(mock1, mock2), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenNoTypeMatches_shouldCallNextFilter() {
        // given
        Object fieldInstance = mock(String.class);
        Field field = Mockito.spy(Field.class);
        field.setType(String.class);
        Object mock1 = mock(String.class);
        Object mock2 = mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Stubber stubber = Mockito.stub(nextMock.class);
        Object nextResult = mock(Object.class);
        stubber.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance)))
                .thenReturn(nextResult);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        // as we didn't set up any expectation for TypeBasedCandidateFilter, it will delegate the call to next filter
        // and we verify the next filter's call instead
        stubber.verify(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
        assertEquals(nextResult, result.getMock());
    }

    private Collection<Object> mockTypeMatches(Object... mocks) {
        List<Object> mockTypeMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (mock.getClass().isAssignableFrom(String.class)) {
                mockTypeMatches.add(mock);
            }
        }
        return mockTypeMatches;
    }
}