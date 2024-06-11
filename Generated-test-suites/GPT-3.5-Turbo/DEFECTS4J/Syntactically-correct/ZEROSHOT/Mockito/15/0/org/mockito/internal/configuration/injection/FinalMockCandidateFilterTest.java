package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verifyNoMoreInteractions(ongoingInjecter);
        verifyNew(FieldSetter.class).withArguments(fieldInstance, field);
        verify(field, never()).getName();
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verifyNoMoreInteractions(ongoingInjecter);
        verify(field, never()).getName();
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        Object matchingMock = mock(Object.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenThrow(new Exception("MockitoException"));

        mocks.add(matchingMock);
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }

}