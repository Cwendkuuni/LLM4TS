package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

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
        this.filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        this.mocks = new ArrayList<>();
        this.fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        this.fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_BasicScenario() {
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);
                        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
                        return ongoingInjecter;
                    }
                });

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(result, Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullCollection() {
        filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullInstance() {
        filter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        Collection<Object> emptyMocks = new ArrayList<>();
        OngoingInjecter result = filter.filterCandidate(emptyMocks, fieldToBeInjected, fieldInstance);
        assertSame(result, Mockito.verifyZeroInteractions(filter));
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(result, Mockito.verifyZeroInteractions(mock1, mock2));
    }
}