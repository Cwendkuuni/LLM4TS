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

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterCandidateReturned() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);

        Mockito.when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return ongoingInjecter;
            }
        });

        assertSame(ongoingInjecter, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_ThrowsException() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new RuntimeException());

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

}