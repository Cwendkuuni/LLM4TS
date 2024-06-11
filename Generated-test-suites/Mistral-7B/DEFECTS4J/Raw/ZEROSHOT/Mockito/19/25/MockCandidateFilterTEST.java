package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invoke.InjectingAnswer;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertSame;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = Mockito.mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_WithEmptyMocksCollection() {
        Answer<OngoingInjecter> answer = new InjectingAnswer<>();
        Mockito.doAnswer(answer).when(filter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(answer.get(), result);
    }

    @Test
    public void testFilterCandidate_WithNonEmptyMocksCollection() {
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);

        Answer<OngoingInjecter> answer = Mockito.mock(Answer.class);
        Mockito.doAnswer(answer).when(filter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(answer).answer(Mockito.eq(mock), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        assertSame(answer, result);
    }

}