package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Invocations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.lang.reflect.AccessibleObject.SETTER;
import static java.lang.reflect.Modifier.IS_PRIVATE;
import static java.lang.reflect.Modifier.IS_STATIC;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Reporter reporter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Object matchingMock;

    @Before
    public void setUp() {
        reporter = mock(Reporter.class);
        finalMockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = new FieldType(Object.class, "fieldName", fieldInstance).getField();
        field.setAccessible(true);
        when(mocks.iterator().next()).thenReturn(matchingMock = mock(Object.class));
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        // Given

        // When
        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(matchingMock).setFieldName(matchingMock);
        verify(matchingMock, Invocations.atLeastOnce()).setFieldName(fieldInstance);
        verify(reporter, MockMaker.never()).cannotInjectDependency(MockitoConfiguration.FIELD, matchingMock, any(Throwable.class));
        assertNotNull(ongoingInjecter.thenInject());
        assertEquals(matchingMock, ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(reporter, MockMaker.never()).cannotInjectDependency(MockitoConfiguration.FIELD, matchingMock, any(Throwable.class));
        assertNull(ongoingInjecter.thenInject());
    }

    @Test(expected = AssertionError.class)
    public void testFilterCandidateWithException() {
        // Given
        when(matchingMock.setFieldName(any())).thenThrow(new RuntimeException("Exception message"));

        // When
        finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // An AssertionError should be thrown as the test method expects
    }

    @Test
    public void testFilterCandidateWithPrivateStaticField() {
        // Given
        Object clazz = mock(Object.class);
        Field privateStaticField = clazz.getClass().getDeclaredField("PRIVATE_STATIC_FIELD");
        privateStaticField.setAccessible(true);
        privateStaticField.set(clazz, mock(Object.class));
        when(matchingMock.getClass()).thenReturn(clazz.getClass());

        // When
        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, privateStaticField, fieldInstance);

        // Then
        verify(reporter, MockMaker.never()).cannotInjectDependency(MockitoConfiguration.FIELD, matchingMock, any(Throwable.class));
        assertNull(ongoingInjecter.thenInject());
    }
}