package org.mockito.internal.configuration.injection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.junit.Before;
import org.junit.Test;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(ongoingInjecter).thenInject();
        verify(fieldInstance, times(1)).set(field, mocks.iterator().next());
    }
    
    @Test
    public void testFilterCandidateNoMocks() throws Exception {
        Collection<Object> mocks = Collections.emptyList();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verify(ongoingInjecter).thenInject();
        verify(fieldInstance, never()).set(field, any());
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidateException() throws Exception {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();
        
        when(fieldInstance.set(field, mocks.iterator().next())).thenThrow(new Exception());
                
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}