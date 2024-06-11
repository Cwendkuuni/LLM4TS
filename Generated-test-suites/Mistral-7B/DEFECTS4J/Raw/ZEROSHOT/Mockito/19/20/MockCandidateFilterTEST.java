package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private FieldSetter fieldSetter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldSetter = new FieldSetter();
    }

    @Test
    public void testFilterCandidate_whenFieldIsAccessible_thenFilterCandidateIsCalled() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));

        when(fieldToBeInjected.isAccessible()).thenReturn(true);
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(null);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotAccessible_thenDoNothing() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));

        when(fieldToBeInjected.isAccessible()).thenReturn(false);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Nothing to verify as filterCandidate should do nothing when field is not accessible
    }

    @Test
    public void testFilterCandidate_whenFieldIsInjectedWithMockedObject_thenMockedObjectIsReturned() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Object expectedMockedObject = mock(Object.class);
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));

        when(fieldToBeInjected.isAccessible()).thenReturn(true);
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(null);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(expectedMockedObject);

        Object actualReturnedObject = fieldSetter.setField(fieldInstance, fieldToBeInjected, mockCandidateFilter);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance).equals(actualReturnedObject);
    }

    @Test
    public void testFilterCandidate_whenFieldIsInjectedWithRealObject_thenRealObjectIsReturned() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));

        when(fieldToBeInjected.isAccessible()).thenReturn(true);
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(null);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance;
            }
        });

        Object actualReturnedObject = fieldSetter.setField(fieldInstance, fieldToBeInjected, mockCandidateFilter);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance).equals(actualReturnedObject);
    }
}