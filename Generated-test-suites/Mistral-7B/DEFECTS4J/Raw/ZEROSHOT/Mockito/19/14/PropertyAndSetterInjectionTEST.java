package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    }

    @Test
    public void testProcessInjection_success() throws Exception {
        Class targetClass = Mockito.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        assertTrue(propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates));

        verify(targetFieldOwner, times(1)).setMocks(mockCandidates.iterator().next());
        verify(targetField).setAccessible(true);
    }

    @Test
    public void testProcessInjection_noInjectedField() throws Exception {
        Class targetClass = Mockito.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();

        assertFalse(propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates));

        verifyNoInteractions(targetFieldOwner);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_initializationFailed() throws Exception {
        Class targetClass = Mockito.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();

        doThrow(new InstantiationException("Test error"))
                .when(mockCandidateFilter)
                .filterCandidate(mockCandidates, anyField(), anyObject());

        propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates);
    }

    @Test
    public void testProcessInjection_noMockCandidateFound() throws Exception {
        Class targetClass = Mockito.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();

        mockCandidateFilter = spy(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mockCandidates, anyField(), anyObject())).thenReturn(null);

        assertFalse(propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates));

        verifyNoInteractions(targetFieldOwner);
    }

    @Test
    public void testProcessInjection_noSetterFound() throws Exception {
        Class targetClass = TestTargetClass.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        List<Field> orderedInstanceFields = Arrays.asList(targetClass.getDeclaredFields());
        when(mockCandidateFilter.filterCandidate(mockCandidates, anyField(), anyObject()))
                .thenReturn(orderedInstanceFields.get(0));

        assertFalse(propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates));

        verifyNoInteractions(targetFieldOwner);
    }

    @Test
    public void testProcessInjection_setterNotFoundButFieldInjected() throws Exception {
        Class targetClass = TestTargetClass.class;
        Field targetField = targetClass.getDeclaredField("mocks");
        Object targetFieldOwner = mock(targetClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        List<Field> orderedInstanceFields = Arrays.asList(targetClass.getDeclaredFields());
        when(mockCandidateFilter.filterCandidate(mockCandidates, anyField(), anyObject()))
                .thenReturn(orderedInstanceFields.get(0));

        doReturn(true).when(propertyAndSetterInjection).injectField(targetFieldOwner, orderedInstanceFields.get(0));

        assertTrue(propertyAndSetterInjection.processInjection(targetField, targetFieldOwner, mockCandidates));

        verify(targetFieldOwner, times(1)).setMocks(mockCandidates.iterator().next());
        verify(targetField).setAccessible(true);
    }

    @Test
    public void testInjectMockCandidatesOnFields_success() throws Exception {
        Class testClass = TestClassWithFields.class;
        Object testInstance = mock(testClass);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Class.class));

        List<Field> orderedInstanceFields = Arrays.asList(testClass.getDeclaredFields());

        when(mockCandidateFilter.filterCandidate(mocks, anyField(), anyObject())).thenReturn(orderedInstanceFields.get(0));

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, testInstance, false, orderedInstanceFields));

        verify(testInstance).setField1(mockCandidates.iterator().next());
        verify(testInstance).setField2(mockCandidates.iterator().next());
        verify(testInstance).setField3(mockCandidates.iterator().next());
    }

    @Test
    public void testInjectMockCandidatesOnFields_noInjectionOccurred() throws Exception {
        Class testClass = TestClassWithFields.class;
        Object testInstance = mock(testClass);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Class.class));

        List<Field> orderedInstanceFields = Arrays.asList(testClass.getDeclaredFields());

        when(mockCandidateFilter.filterCandidate(mocks, anyField(), anyObject())).thenReturn(orderedInstanceFields.get(0));
        when(propertyAndSetterInjection.injectField(testInstance, orderedInstanceFields.get(0))).thenReturn(false);

        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, testInstance, false, orderedInstanceFields));

        verifyNoInteractions(testInstance);
    }

    private static class TestTargetClass {
        @InjectMocks
        private Set<Object> mocks;
    }

    private static class TestClassWithFields {
        private Set<Object> mocks;

        @InjectMocks
        private void setMocks(Set<Object> mocks) {
            this.mocks = mocks;
        }

        private Field field1;
        private Field field2;
        private Field field3;

        @InjectMocks
        private void setField1(Set<Object> mocks) {
            this.field1 = field1;
        }

        @InjectMocks
        private void setField2(Set<Object> mocks) {
            this.field2 = field2;
        }

        @InjectMocks
        private void setField3(Set<Object> mocks) {
            this.field3 = field3;
        }
    }
}