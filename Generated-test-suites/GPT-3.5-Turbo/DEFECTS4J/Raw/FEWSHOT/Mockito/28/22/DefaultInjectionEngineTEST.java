package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        defaultInjectionEngine.mockCandidateFilter = mockCandidateFilter;
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Field field1 = MyClass.class.getDeclaredField("field1");
        Field field2 = MyClass.class.getDeclaredField("field2");
        Field field3 = MyClass.class.getDeclaredField("field3");

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        Object mock3 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Object testClassInstance = new MyClass();

        when(mockCandidateFilter.filterCandidate(mocks, field1, testClassInstance)).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(mocks, field2, testClassInstance)).thenReturn(mock2);
        when(mockCandidateFilter.filterCandidate(mocks, field3, testClassInstance)).thenReturn(mock3);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertEquals(mock1, ((MyClass) testClassInstance).getField1());
        assertEquals(mock2, ((MyClass) testClassInstance).getField2());
        assertEquals(mock3, ((MyClass) testClassInstance).getField3());
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        Field field1 = MyClass.class.getDeclaredField("field1");
        Field field2 = MyClass.class.getDeclaredField("field2");
        Field field3 = MyClass.class.getDeclaredField("field3");

        Set<Object> mocks = new HashSet<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        Object mock3 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Object fieldInstance = new MyClass();

        when(mockCandidateFilter.filterCandidate(mocks, field1, fieldInstance)).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(mocks, field2, fieldInstance)).thenReturn(mock2);
        when(mockCandidateFilter.filterCandidate(mocks, field3, fieldInstance)).thenReturn(mock3);

        defaultInjectionEngine.injectMockCandidate(MyClass.class, mocks, fieldInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field1, fieldInstance);
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field2, fieldInstance);
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field3, fieldInstance);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Field field1 = MyClass.class.getDeclaredField("field1");
        Field field2 = MyClass.class.getDeclaredField("field2");
        Field field3 = MyClass.class.getDeclaredField("field3");

        Field[] expected = {field1, field3, field2};

        Field[] result = defaultInjectionEngine.orderedInstanceFieldsFrom(MyClass.class);

        assertArrayEquals(expected, result);
    }

    private static class MyClass {
        private Object field1;
        private Object field2;
        private Object field3;

        public Object getField1() {
            return field1;
        }

        public void setField1(Object field1) {
            this.field1 = field1;
        }

        public Object getField2() {
            return field2;
        }

        public void setField2(Object field2) {
            this.field2 = field2;
        }

        public Object getField3() {
            return field3;
        }

        public void setField3(Object field3) {
            this.field3 = field3;
        }
    }
}