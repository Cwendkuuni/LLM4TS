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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(new Field() {});
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateIsCalled_thenOngoingInjecterIsReturned() {
        // Given
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        // When
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateIsCalledTwice_thenSameOngoingInjecterIsReturned() {
        // Given
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        // When
        OngoingInjecter result1 = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        OngoingInjecter result2 = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertSame(ongoingInjecter, result1);
        assertSame(ongoingInjecter, result2);
    }

}