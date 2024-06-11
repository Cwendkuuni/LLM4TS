package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockitoImpl;
import org.mockito.internal.configuration.MockInjector;
import org.mockito.internal.configuration.injection.FieldAnnotations;
import org.mockito.internal.configuration.injection.InjectingFields;
import org.mockito.internal.mock.MockConstructor;
import org.mockito.internal.util.reflection.Whitebox;

public class MockCandidateFilterTest {

    @Test
    public void testNullFilterCandidate() {
        MockitoImpl mockito = Mockito.mock(MockitoImpl.class);
        Collection<Object> mocks = Collections.emptyList();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(MockCandidateFilter.class.cast(null).filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testEmptyMocksCollection() {
        MockitoImpl mockito = Mockito.mock(MockitoImpl.class);
        Collection<Object> mocks = Collections.emptyList();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockCandidateFilter mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);

        MockInjector mockInjector = Whitebox.getInternalState(mockito, MockInjector.class);
        InjectingFields injectingFields = Whitebox.getInternalState(mockInjector, InjectingFields.class);
        injectingFields.registerFilter(mockCandidateFilter);

        MockCandidateFilter filterCandidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(filterCandidate);
    }

    @Test
    public void testFilterCandidateWithNoAnnotation() {
        MockitoImpl mockito = Mockito.mock(MockitoImpl.class);
        Collection<Object> mocks = Mockito.mock(List.class);
        MockConstructor mockConstructor = Mockito.mock(MockConstructor.class);
        FieldAnnotations fieldAnnotations = Mockito.mock(FieldAnnotations.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockInjector mockInjector = Whitebox.getInternalState(mockito, MockInjector.class);
        InjectingFields injectingFields = Whitebox.getInternalState(mockInjector, InjectingFields.class);
        injectingFields.registerFilter(Mockito.mock(MockCandidateFilter.class));

        MockCandidateFilter mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        Whitebox.setInternalState(fieldAnnotations, mockCandidateFilter, "mockCandidateFilter");
        Whitebox.setInternalState(field, fieldInstance);
        Whitebox.setInternalState(mockConstructor, fieldAnnotations);
        Whitebox.setInternalState(mocks, Collections.singletonList(mockConstructor));

        MockCandidateFilter filterCandidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(filterCandidate);
    }

    @Test
    public void testFilterCandidateWithAnnotation() {
        MockitoImpl mockito = Mockito.mock(MockitoImpl.class);
        Collection<Object> mocks = Mockito.mock(List.class);
        MockConstructor mockConstructor = Mockito.mock(MockConstructor.class);
        FieldAnnotations fieldAnnotations = Mockito.mock(FieldAnnotations.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockInjector mockInjector = Whitebox.getInternalState(mockito, MockInjector.class);
        InjectingFields injectingFields = Whitebox.getInternalState(mockInjector, InjectingFields.class);
        injectingFields.registerFilter(Mockito.mock(MockCandidateFilter.class));

        MockCandidateFilter mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        Whitebox.setInternalState(fieldAnnotations, mockCandidateFilter, "mockCandidateFilter");
        Whitebox.setInternalState(field, fieldInstance);
        Whitebox.setInternalState(mockConstructor, fieldAnnotations);
        Whitebox.setInternalState(mocks, Collections.singletonList(mockConstructor));
        Whitebox.setInternalState(fieldAnnotations, "mockAnnotation", new Object());

        MockCandidateFilter filterCandidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockCandidateFilter, Mockito.times(1)).filterCandidate(mocks, field, fieldInstance);
        Mockito.verifyNoMoreInteractions(mockCandidateFilter);

        assertNull(filterCandidate);
    }

    @Test
    public void testFilterCandidateWithFilterReturnValue() {
        MockitoImpl mockito = Mockito.mock(MockitoImpl.class);
        Collection<Object> mocks = Mockito.mock(List.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Object filterCandidate = Mockito.mock(Object.class);

        MockInjector mockInjector = Whitebox.getInternalState(mockito, MockInjector.class);
        InjectingFields injectingFields = Whitebox.getInternalState(mockInjector, InjectingFields.class);
        injectingFields.registerFilter(Mockito.mock(MockCandidateFilter.class));

        MockCandidateFilter mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);

        MockCandidateFilter filterCandidateResult = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Mockito.verify(mockCandidateFilter, Mockito.times(1)).filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockCandidateFilter, Mockito.atLeastOnce()).filterCandidate(mocks, field, fieldInstance);
        Mockito.verify(mockCandidateFilter, Mockito.never()).filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class));

        Mockito.verifyZeroInteractions(mockito);

        Mockito.when(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(filterCandidateResult);
        Mockito.when(mockCandidateFilter.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class))).thenReturn(filterCandidate);

        MockCandidateFilter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockCandidateFilter, Mockito.times(1)).filterCandidate(mocks, field, fieldInstance);
        Mockito.verify(mockCandidateFilter, Mockito.times(1)).filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class));

        Mockito.verify(mockCandidateFilter, Mockito.atLeastOnce()).filterCandidate(mocks, field, fieldInstance);
        Mockito.verify(mockCandidateFilter, Mockito.atLeastOnce()).filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class));

        Mockito.assertEquals(filterCandidateResult, result);
    }

}