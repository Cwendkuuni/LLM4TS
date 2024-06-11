package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertNotNull(result);
        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidate_noMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_exceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Mocking BeanPropertySetter to throw RuntimeException
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(any())).thenThrow(new RuntimeException("Exception"));

        // Mocking FieldSetter since BeanPropertySetter will throw exception
        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenReturn(true);

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertNotNull(result);
        verify(fieldSetter, times(1)).set(fieldInstance, field);
        verify(beanPropertySetter, times(1)).set(fieldInstance, field);
    }
}