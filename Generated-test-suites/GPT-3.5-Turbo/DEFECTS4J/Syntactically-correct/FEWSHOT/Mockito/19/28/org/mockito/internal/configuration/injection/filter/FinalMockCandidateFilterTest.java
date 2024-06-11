package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleCandidate() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidate_multipleCandidates() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_injectByBeanPropertySetter() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = mock;

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(any())).thenReturn(true);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenReturn(false);

        try {
            Field setterField = filter.getClass().getDeclaredField("setter");
            setterField.setAccessible(true);
            setterField.set(filter, setter);
            Field fieldSetterField = filter.getClass().getDeclaredField("fieldSetter");
            fieldSetterField.setAccessible(true);
            fieldSetterField.set(filter, fieldSetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock, result);

        verify(setter, times(1)).set(mock);
        verify(fieldSetter, never()).set(mock);
    }

    @Test
    public void testFilterCandidate_injectByFieldSetter() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = mock;

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(any())).thenReturn(false);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenReturn(true);

        try {
            Field setterField = filter.getClass().getDeclaredField("setter");
            setterField.setAccessible(true);
            setterField.set(filter, setter);
            Field fieldSetterField = filter.getClass().getDeclaredField("fieldSetter");
            fieldSetterField.setAccessible(true);
            fieldSetterField.set(filter, fieldSetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock, result);

        verify(setter, never()).set(mock);
        verify(fieldSetter, times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_injectFailure() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = mock;

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(any())).thenThrow(new RuntimeException());

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenThrow(new RuntimeException());

        try {
            Field setterField = filter.getClass().getDeclaredField("setter");
            setterField.setAccessible(true);
            setterField.set(filter, setter);
            Field fieldSetterField = filter.getClass().getDeclaredField("fieldSetter");
            fieldSetterField.setAccessible(true);
            fieldSetterField.set(filter, fieldSetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock, result);

        verify(setter, times(1)).set(mock);
        verify(fieldSetter, never()).set(mock);

        verifyNoMoreInteractions(setter, fieldSetter);
    }

    @Test
    public void testFilterCandidate_noCandidates() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNull(result);
    }
}