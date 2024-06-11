package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectionPoint;
import org.mockito.internal.util.reflection.MockCreationSettings;
import org.mockito.internal.util.reflection.TypeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private InjectionPoint injectionPoint;
    private Object fieldInstance;
    private Field field;
    private FinalMockCandidateFilter filter;
    private Reporter reporter;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        injectionPoint = new InjectionPoint(new MockCreationSettings(), null, null, null, null, null);
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        reporter = mock(Reporter.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(field.getType()).thenReturn(TypeUtils.getJavaType(String.class));
        when(field.getName()).thenReturn("nameField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, filterResult);

        Object mockReturned = filterResult.thenInject();
        verify(mock).equals(mockReturned);
        verify(mock).getName();
        verifyNoInteractions(reporter);
    }

    @Test
    public void testFilterCandidateWithOneFailingPropertySetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(field.getType()).thenReturn(TypeUtils.getJavaType(String.class));
        when(field.getName()).thenReturn("nameField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        BeanPropertySetter propertySetterSpy = spy(new BeanPropertySetter(fieldInstance, field));
        doThrow(RuntimeException.class).when(propertySetterSpy).set(mock, "name");

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, filterResult);

        Object mockReturned = filterResult.thenInject();
        verifyNoInteractions(mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidateWithOneFailingFieldSetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(field.getType()).thenReturn(TypeUtils.getJavaType(String.class));
        when(field.getName()).thenReturn("nameField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        FieldSetter fieldSetterSpy = spy(new FieldSetter(fieldInstance, field));
        doThrow(RuntimeException.class).when(fieldSetterSpy).set(mock, "name");

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, filterResult);

        Object mockReturned = filterResult.thenInject();
        verifyNoInteractions(mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getType()).thenReturn(TypeUtils.getJavaType(String.class));
        when(field.getName()).thenReturn("nameField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, filterResult);

        Object mockReturned = filterResult.thenInject();
        verifyNoInteractions(mock1, mock2, reporter);
    }

    @Test
    public void testFilterCandidateWithNoMocks() {
        mocks = new ArrayList<>();

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, filterResult);

        Object mockReturned = filterResult.thenInject();
        verifyNoInteractions(fieldInstance, reporter);
        assertEquals(null, mockReturned);
    }

}