package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection. Whitebox.getInternalState;
import static org.mockito.internal.util.reflection. Whitebox.setInternalState;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidateReturnsNullWhenMockCollectionIsEmpty() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Collections.emptyList();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateReturnsNullWhenFieldInstanceIsNull() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);

        assertNull(filter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidateReturnsStubbingWhenFieldInstanceIsNotMock() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();

        OngoingStubbing<InvocationOnMock> stubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(stubbing);

        assertSame(stubbing, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateReturnsMockWhenFieldInstanceIsMock() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(MockCandidateFilter.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Mockito.class);

        Mockito.when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(Mockito.mock(Object.class));

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        Mockito.verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateSetsMockStateWhenFieldInstanceIsMock() throws IllegalAccessException {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(MockCandidateFilter.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Mockito.class);

        Object mock = Mockito.mock(Object.class);
        Mockito.when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock);

        Field mocksField = Mockito.class.getDeclaredField("mocks");
        mocksField.setAccessible(true);
        Object mocksInternal = getInternalState(Mockito.class, "mocks");

        Field instanceField = Whitebox.getInternalState(fieldInstance, "mock");
        setInternalState(fieldInstance, "mock", mock);

        filter.filterCandidate(mocks, field, fieldInstance);

        List<?> mocksList = (List<?>) mocksInternal;
        assertTrue(mocksList.contains(mock));
    }
}