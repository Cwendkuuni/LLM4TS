package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidate_WhenMockTypeMatches_ThenNextFilterIsCalled() throws Exception {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class, "mock1"), mock(Object.class, "mock2"), mock(ClassThatImplementsType.class, "mock3"));
        Field field = new Field("field", String.class, TypeBasedCandidateFilter.class);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate( Arrays.asList(mock3), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMockTypeDoesNotMatch_ThenNextFilterIsNotCalled() throws Exception {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class, "mock1"), mock(Object.class, "mock2"), mock(ClassThatDoesNotImplementType.class, "mock3"));
        Field field = new Field("field", String.class, TypeBasedCandidateFilter.class);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyZeroInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsObject_ThenAllMocksAreReturned() throws Exception {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class, "mock1"), mock(Object.class, "mock2"), mock(ClassThatImplementsType.class, "mock3"));
        Field field = new Field("field", Object.class, TypeBasedCandidateFilter.class);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}

class ClassThatImplementsType implements Type {
}

class ClassThatDoesNotImplementType {
}