package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setup() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        // Given
        Collection<Object> mocks = null;

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, mock(Field.class), mock(Object.class));

        // Then
        assertSame(mocks, result.getMock());
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        // Given
        Collection<Object> mocks = Collections.emptyList();

        // When
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = mock(Field.class);
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(fieldInstance, result.getMock());
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        // Given
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock);
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = mock(Field.class);

        when(fieldToBeInjected.getType()).thenReturn(mock.getClass());

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(mock, result.getMock());
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock1);
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = mock(Field.class);

        when(fieldToBeInjected.getType()).thenReturn(mock2.getClass());

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(mock1, result.getMock());
    }

    @Test
    public void testFilterCandidate_FieldOfDifferentType() {
        // Given
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock);
        Object fieldInstance = mock(Number.class);
        Field fieldToBeInjected = mock(Field.class);

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(fieldInstance, result.getMock());
    }

    @Test
    public void testFilterCandidate_FieldIsStatic() {
        // Given
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock);
        Object fieldInstance = mock(Object.class);
        Field fieldToBeInjected = mock(Field.class);

        when(fieldToBeInjected.isStatic()).thenReturn(true);

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(fieldInstance, result.getMock());
    }
}