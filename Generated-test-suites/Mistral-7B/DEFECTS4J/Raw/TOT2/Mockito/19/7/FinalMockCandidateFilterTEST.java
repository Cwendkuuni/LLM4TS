package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InternalMockito;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
        field = RandomUtils.getRandomField(fieldInstance.getClass());
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mocks.iterator().next();

        OngoingInjecter candidate = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, candidate.thenInject());
        verify(mock).setField(fieldInstance);
        verify(mock).setFieldUsingReflection(fieldInstance);
        verifyPropertySetter(fieldInstance, mock, field);
        verifyFieldSetter(fieldInstance, mock, field);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter candidate = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(candidate.thenInject());
        verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testFilterCandidateWithException() {
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        OngoingInjecter candidate = filter.filterCandidate(mocks, field, fieldInstance);

        InOrder inOrder = inOrder(mocks);
        inOrder.verify(mocks.get(0)).setField(fieldInstance);
        inOrder.verify(mocks.get(1)).setFieldUsingReflection(fieldInstance);

        doThrow(new RuntimeException("Test Exception")).when(mocks).setField(fieldInstance);
        doThrow(new RuntimeException("Test Exception")).when(mocks).setFieldUsingReflection(fieldInstance);

        candidate.thenInject();

        verify(Reporter.class).cannotInjectDependency(field, mocks.get(0), Mockito.any(RuntimeException.class));
    }

    private void verifyPropertySetter(Object fieldInstance, Object mock, Field field) {
        Mockito.verify(mock).setField(fieldInstance);
    }

    private void verifyFieldSetter(Object fieldInstance, Object mock, Field field) {
        Mockito.verify(mock).setFieldUsingReflection(fieldInstance);
    }
}