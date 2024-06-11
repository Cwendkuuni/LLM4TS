package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.lang.reflect.Collection;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_whenFieldIsNotNull_thenReturnOngoingInjecter() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assert result != null;
    }

    @Test
    public void testFilterCandidate_whenFieldIsNull_thenReturnNull() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Field fieldToBeInjected = null;
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(null);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsAnnotatedWithMockCandidat_thenReturnOngoingInjecter() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        getInternalState(fieldInstance, "mockito_mockCandidates").set(Arrays.asList(new MockCandidateAnnotation()));

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assert result != null;
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsNotAnnotatedWithMockCandidate_thenReturnNull() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(null);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}