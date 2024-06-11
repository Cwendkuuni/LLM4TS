
package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mockCollection;
    private Object fieldInstance;
    private Field fieldToBeInjected;

    @Before
    public void setup() {
        mockCollection = mock(Collection.class);
        fieldInstance = mock(Object.class);
        fieldToBeInjected = mock(Field.class);
        filter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate_ValidScenario() {
        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(filter).filterCandidate(mockCollection, fieldToBeInjected, fieldInstance);

        filter.filterCandidate(mockCollection, fieldToBeInjected, fieldInstance);

        verify(filter).filterCandidate(Mockito.eq(mockCollection), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        filter.filterCandidate(Collections.emptyList(), fieldToBeInjected, fieldInstance);
        verify(filter).filterCandidate(Mockito.eq(Collections.emptyList()), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mockCollection, null, fieldInstance);
        verify(filter).filterCandidate(Mockito.eq(mockCollection), Mockito.eq(null), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        filter.filterCandidate(mockCollection, fieldToBeInjected, null);
        verify(filter).filterCandidate(Mockito.eq(mockCollection), Mockito.eq(fieldToBeInjected), Mockito.eq(null));
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mockCollection.addAll(Arrays.asList(mock1, mock2));

        filter.filterCandidate(mockCollection, fieldToBeInjected, fieldInstance);

        verify(filter).filterCandidate(Mockito.eq(mockCollection), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }
}
