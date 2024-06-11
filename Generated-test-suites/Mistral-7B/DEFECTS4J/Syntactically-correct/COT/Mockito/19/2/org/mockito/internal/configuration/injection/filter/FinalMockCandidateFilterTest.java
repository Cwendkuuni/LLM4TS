package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Object mockObject;
    private Reporter reporter;

    @Before
    public void init() {
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = MockitoStubs.field(Object.class, "fieldName");
        mockObject = mock(Object.class);
        reporter = mock(Reporter.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mockObject);
        when(new BeanPropertySetter(fieldInstance, field).set(mockObject)).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        verify(mockObject).then(invocationOnMock -> invocationOnMock.answer(fieldInstance));
        verifyZeroInteractions(reporter);
        assertNotNull(injecter);
        assertEquals(mockObject, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetterFailure() {
        when(mocks.iterator().next()).thenReturn(mockObject);
        when(new BeanPropertySetter(fieldInstance, field).set(mockObject)).thenThrow(new RuntimeException());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        verify(reporter).cannotInjectDependency(isA(Field.class), isA(Object.class), isA(RuntimeException.class));
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mockObject);
        when(new FieldSetter(fieldInstance, field).set(mockObject)).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        verify(mockObject).then(invocationOnMock -> invocationOnMock.answer(fieldInstance));
        verifyZeroInteractions(reporter);
        assertNotNull(injecter);
        assertEquals(mockObject, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        when(mocks.iterator().next()).thenReturn(mockObject);
        when(new FieldSetter(fieldInstance, field).set(mockObject)).thenThrow(new RuntimeException());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        verify(reporter).cannotInjectDependency(isA(Field.class), isA(Object.class), isA(RuntimeException.class));
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        when(mocks.size()).thenReturn(2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertEquals(null, injecter.thenInject());
    }
}