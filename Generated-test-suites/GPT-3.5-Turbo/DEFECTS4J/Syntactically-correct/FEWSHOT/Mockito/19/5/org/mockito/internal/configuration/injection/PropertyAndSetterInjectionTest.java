package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() {
        // mock injectMocksField
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = new Object();
        // mock mockCandidates
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        // mock fieldHierarchyInjectionOccurred
        boolean fieldHierarchyInjectionOccurred = false;

        // mock initializeInjectMocksField
        FieldInitializationReport report = mock(FieldInitializationReport.class);
        when(report.fieldClass()).thenReturn(Object.class);
        when(report.fieldInstance()).thenReturn(new Object());
        try {
            FieldInitializer initializer = mock(FieldInitializer.class);
            whenNew(FieldInitializer.class).withArguments(injectMocksFieldOwner, injectMocksField).thenReturn(initializer);
            when(initializer.initialize()).thenReturn(report);
        } catch (Exception e) {
            fail("An exception occurred during mocking");
        }

        // mock orderedInstanceFieldsFrom
        List<Field> orderedInstanceFields = Arrays.asList(new Field[]{mock(Field.class)});
        SuperTypesLastSorter sorter = mock(SuperTypesLastSorter.class);
        when(sorter.sort(any(List.class))).thenReturn(orderedInstanceFields);
        try {
            whenNew(SuperTypesLastSorter.class).withNoArguments().thenReturn(sorter);
        } catch (Exception e) {
            fail("An exception occurred during mocking");
        }

        // mock injectMockCandidates
        boolean injectionOccurred = false;
        boolean injectionOccurred1 = false;
        boolean injectionOccurred2 = false;
        Set<Object> mocks = Sets.newMockSafeHashSet();
        Object instance = new Object();
        ListUtil.Filter<Field> notFinalOrStatic = mock(ListUtil.Filter.class);
        when(notFinalOrStatic.isOut(any(Field.class))).thenReturn(false);
        try {
            FieldCandidateInjector fieldCandidateInjector = mock(FieldCandidateInjector.class);
            whenNew(FieldCandidateInjector.class).withArguments(mockCandidates, instance, notFinalOrStatic).thenReturn(fieldCandidateInjector);
            when(fieldCandidateInjector.orderedInstanceFieldsFrom(Object.class)).thenReturn(orderedInstanceFields);
            when(fieldCandidateInjector.injectCandidate(mocks, orderedInstanceFields)).thenReturn(injectionOccurred1, injectionOccurred2);
            when(fieldCandidateInjector.injectCandidate(mocks, orderedInstanceFields)).thenReturn(injectionOccurred2);
            when(fieldCandidateInjector.injectCandidate(mocks, orderedInstanceFields)).thenReturn(false);
        } catch (Exception e) {
            fail("An exception occurred during mocking");
        }

        // test processInjection
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // assert statements
        assertTrue(result);
        verifyNew(FieldInitializer.class).withArguments(injectMocksFieldOwner, injectMocksField);
        verify(report).fieldClass();
        verify(report).fieldInstance();
        verify(sorter).sort(any(List.class));
        verify(notFinalOrStatic, times(orderedInstanceFields.size() * 2)).isOut(any(Field.class));
        verifyNew(FieldCandidateInjector.class, times(2)).withArguments(mockCandidates, instance, notFinalOrStatic);
        verify(fieldCandidateInjector, times(2)).orderedInstanceFieldsFrom(Object.class);
        verify(fieldCandidateInjector, times(3)).injectCandidate(mocks, orderedInstanceFields);
    }
}