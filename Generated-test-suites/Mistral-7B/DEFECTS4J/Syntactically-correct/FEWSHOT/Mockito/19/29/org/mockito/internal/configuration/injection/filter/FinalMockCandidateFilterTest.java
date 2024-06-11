package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithOnePropertySetterSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(fieldInstance, mock)).thenReturn(true);
        when(field.getClass()).thenReturn(String.class);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(propertySetter).set(fieldInstance, mock);
    }

    @Test
    public void testFilterCandidateWithOnePropertySetterFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(fieldInstance, mock)).thenThrow(new RuntimeException());
        when(field.getClass()).thenReturn(String.class);

        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
            fail();
        } catch (RuntimeException e) {
            // expected
        }

        verify(new Reporter()).cannotInjectDependency(field, mock, e.getCause());
    }

    @Test
    public void testFilterCandidateWithOneFieldSetterSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(fieldInstance, mock)).thenReturn(null);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(fieldSetter).set(fieldInstance, mock);
    }

    @Test
    public void testFilterCandidateWithMoreThanOneMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(ongoingInjecter.thenInject());
    }
}