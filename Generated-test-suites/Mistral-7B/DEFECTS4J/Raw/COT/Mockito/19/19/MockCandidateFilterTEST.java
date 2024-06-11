package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MockCandidateFilterTest {

    public interface MockCandidateFilterUnderTest extends MockCandidateFilter {
        OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance);
    }

    @Test
    public void testFilterCandidate_NullMocks_ThrowsIllegalArgumentException() {
        MockCandidateFilterUnderTest filter = Mockito.spy(Mockito.mock(MockCandidateFilterUnderTest.class));
        assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(null, Mockito.mock(Field.class), Mockito.mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected_ThrowsIllegalArgumentException() {
        MockCandidateFilterUnderTest filter = Mockito.spy(Mockito.mock(MockCandidateFilterUnderTest.class));
        assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(new ArrayList<>(), null, Mockito.mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance_ThrowsIllegalArgumentException() {
        MockCandidateFilterUnderTest filter = Mockito.spy(Mockito.mock(MockCandidateFilterUnderTest.class));
        assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(new ArrayList<>(), Mockito.mock(Field.class), null));
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsEmptyCollection() {
        MockCandidateFilterUnderTest filter = Mockito.spy(Mockito.mock(MockCandidateFilterUnderTest.class));
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verifyZeroInteractions(result);
        verifyNoInteractions(fieldInstance);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MocksContainsFilteredObject_ReturnsFilteredObject() {
        MockCandidateFilterUnderTest filter = Mockito.spy(Mockito.mock(MockCandidateFilterUnderTest.class));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class, Strictness.LENIENT));
        Object fieldInstance = Mockito.mock(Object.class);
        Object filteredObject = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);

        doAnswer((InvocationOnMock invocation) -> filteredObject)
                .when(filter)
                .filterCandidate(any(Collection.class), any(Field.class), any(Object.class));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(result).inject(filteredObject);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

}