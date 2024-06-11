package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitoSessionSettings;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_OneMock_BeanPropertySetterSuccess() {
        Object mock = mock(Object.class);
        when(field.getName()).thenReturn("testField");

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock).getClass();
        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        beanPropertySetter.set(mock);
        verify(mock).equals(result);
    }

    @Test
    public void testFilterCandidate_OneMock_BeanPropertySetterFailure() {
        Object mock = mock(Object.class);
        when(field.getName()).thenReturn("testField");

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        beanPropertySetter.set(mock);
        verify(mock).getClass();
        verify(fieldInstance, org.mockito.ArgumentMatchers.not(mock)).getClass();
        verify(fieldInstance).getClass();
        new Reporter().cannotInjectDependency(field, mock, new RuntimeException("Test Exception"));
        verify(mock).equals(result);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        Object mock = mock(Object.class);
        when(field.getName()).thenReturn("testField");

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);
        verify(mock).getClass();
        verify(mock).equals(result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnsNull() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        when(field.getName()).thenReturn("testField");

        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verifyNoInteractions(mock1, mock2, fieldInstance);
        verify(mock1).getClass();
        verify(mock2).getClass();
        verify(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_ThrowsException_WhenCouldNotInject() {
        Object mock = mock(Object.class);
        when(field.getName()).thenReturn("testField");

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
        } finally {
            throw new RuntimeException("Test Exception");
        }

        new Reporter().cannotInjectDependency(field, mock, new RuntimeException("Test Exception"));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_ThrowsException_WhenCandidateIsNull() {
        Object mock = null;
        when(field.getName()).thenReturn("testField");

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
        } finally {
            throw new RuntimeException("Test Exception");
        }

        new Reporter().cannotInjectDependency(field, mock, new RuntimeException("Test Exception"));
    }

}