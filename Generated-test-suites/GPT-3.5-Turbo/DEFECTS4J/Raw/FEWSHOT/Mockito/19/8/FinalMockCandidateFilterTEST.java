package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        final Object matchingMock = mocks.iterator().next();

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(fieldInstance, field)).thenReturn(true);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(fieldInstance, field)).thenReturn(true);

        Reporter reporter = mock(Reporter.class);

        assertEquals(matchingMock, finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject());

        verify(beanPropertySetter).set(fieldInstance, field);
        verify(fieldSetter, never()).set(fieldInstance, field);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, null);
    }

    @Test
    public void testFilterCandidateWithOneMockCannotInject() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        final Object matchingMock = mocks.iterator().next();

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(fieldInstance, field)).thenReturn(false);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(fieldInstance, field)).thenReturn(true);

        Reporter reporter = mock(Reporter.class);

        assertEquals(matchingMock, finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject());

        verify(beanPropertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, null);
    }

    @Test
    public void testFilterCandidateWithOneMockCannotSetField() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        final Object matchingMock = mocks.iterator().next();

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(fieldInstance, field)).thenReturn(false);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(fieldInstance, field)).thenReturn(false);

        Reporter reporter = mock(Reporter.class);

        assertNull(finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject());

        verify(beanPropertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verify(reporter).cannotInjectDependency(field, matchingMock, null);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }
}