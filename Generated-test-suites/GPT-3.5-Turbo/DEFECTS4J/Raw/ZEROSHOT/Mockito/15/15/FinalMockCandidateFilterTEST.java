package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();

        assertEquals(true, result);
        verifyNoMoreInteractions(mocks);
        verify(field).getName();
        verify(fieldInstance, times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();

        assertEquals(false, result);
        verifyNoMoreInteractions(mocks);
        verify(field, never()).getName();
        verifyZeroInteractions(fieldInstance);
    }

}