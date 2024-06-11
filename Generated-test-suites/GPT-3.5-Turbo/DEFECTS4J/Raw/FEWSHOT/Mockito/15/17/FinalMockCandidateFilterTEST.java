package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter mockCandidateFilter;
    private Field field;
    private Object fieldInstance;
    
    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(field).set(fieldInstance, mocks.iterator().next());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(new Exception()).when(fieldSetter).set(any());

        mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}