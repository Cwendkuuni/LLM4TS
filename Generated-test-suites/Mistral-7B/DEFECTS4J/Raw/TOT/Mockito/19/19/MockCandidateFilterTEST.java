
package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Object fieldInstance;
    private Field fieldToBeInjected;
    private List<Object> mocks;

    private static final Object MOCK1 = mock(Object.class);
    private static final Object MOCK2 = mock(Object.class);

    @Before
    public void setUp() throws IllegalAccessException {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        fieldInstance = mock(Object.class);
        fieldToBeInjected = Mockito.spy(Mockito.field(MockCandidateFilter.class, "fieldToBeInjected"));
        mocks = Arrays.asList(MOCK1, MOCK2);
    }

    @Test
    public void testFilterCandidate_WhenEmptyMocksCollection_ThenNullReturned() {
        when(mockCandidateFilter.filterCandidate(Collections.emptyList(), fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        assertNull(mockCandidateFilter.filterCandidate(Collections.emptyList(), fieldToBeInjected, fieldInstance));
        verify(mockCandidateFilter).filterCandidate(Collections.emptyList(), fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenNotNullMocksCollection_ThenInjectorReturned() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        assertSame(ongoingInjecter, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenNullFieldInstance_ThenNullReturned() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null));
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test
    public void testFilterCandidate_WhenFilterCandidateCalledTwiceWithSameArguments_ThenSameInjectorReturned() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter firstInjector = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        OngoingInjecter secondInjector = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(ongoingInjecter, firstInjector);
        assertSame(ongoingInjecter, secondInjector);
        verify(mockCandidateFilter, Mockito.times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenFilterCandidateCalledWithDifferentFields_ThenDifferentInjectorsReturned() {
        Object differentFieldInstance = mock(Object.class);
        OngoingInjecter ongoingInjecter1 = mock(OngoingInjecter.class);
        OngoingInjecter ongoingInjecter2 = mock(OngoingInjecter.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter1);
        when(mockCandidateFilter.filterCandidate(mocks, Mockito.field(MockCandidateFilter.class, "anotherFieldToBeInjected"), differentFieldInstance)).thenReturn(ongoingInjecter2);

        assertNotSame(ongoingInjecter1, ongoingInjecter2);
        verify(mockCandidateFilter, Mockito.times(2)).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }
}

class MockCandidateFilterTest {
    // Previous code
}