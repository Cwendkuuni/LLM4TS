package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.ReflectionUtils.getAccessibleField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setAccessible;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        when(field.getName()).thenReturn("testField");
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterSuccess() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterFailure() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, never()).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new BeanPropertySetter(fieldInstance, field), never()).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, never()).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), never()).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnNull() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(injecter, times(1)).thenInject();
        verify(field, never()).setAccessible(isAccessible());
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetter_invocationOnMock() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);
        when(mock.getName()).thenReturn("mockName");
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(invocation);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, times(1)).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(mock);
        verify(invocation).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return fieldInstance;
            }
        });
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterWithException() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);
        when(mock.getName()).thenReturn("mockName");
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(invocation);
        when(new BeanPropertySetter(fieldInstance, field).set(any(Object.class))).thenThrow(new RuntimeException());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, times(1)).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterWithException() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);
        when(new FieldSetter(fieldInstance, field).set(any(Object.class))).thenThrow(new RuntimeException());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, never()).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterAndFieldSetter() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(mock);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, times(1)).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterAndFieldSetterWithException() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(mock);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, never()).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), never()).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterAndFieldSetter_invocationOnMock() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(invocation);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, times(1)).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
        verify(invocation).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return fieldInstance;
            }
        });
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterAndFieldSetterWithException_invocationOnMock() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new BeanPropertySetter(fieldInstance, field).getPropertyValue(fieldInstance)).thenReturn(invocation);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, times(1)).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), never()).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
        verify(invocation).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return fieldInstance;
            }
        });
    }

    @Test
    public void testFilterCandidate_singleMock_beanPropertySetterAndFieldSetter_accessibleFalse() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).canSet(any(Object.class))).thenReturn(true);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                throw new IllegalAccessException();
            }
        }).when(field).setAccessible(anyBoolean());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock, never()).equals(fieldInstance);
        verify(field).setAccessible(isAccessible());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
        verify(new Reporter()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }
}