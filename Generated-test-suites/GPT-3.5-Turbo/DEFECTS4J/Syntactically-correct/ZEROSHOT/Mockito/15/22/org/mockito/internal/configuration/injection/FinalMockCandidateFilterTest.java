package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(fieldInstance).set(any());
    }

    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verifyZeroInteractions(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        doThrow(Exception.class).when(fieldInstance).set(any());

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}