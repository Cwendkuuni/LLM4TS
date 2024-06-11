package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private OngoingInjecter ongoingInjecterMock;

    @Before
    public void setUp() {
        ongoingInjecterMock = Mockito.mock(OngoingInjecter.class);
        filter = Mockito.mock(MockCandidateFilter.class);
        Whitebox.setInternalState(filter, "ongoingInjecter", ongoingInjecterMock);
    }

    @Test
    public void testFilterCandidateNullCollection() {
        Collection<Object> mocks = null;
        InjectableField fieldToBeInjected = null;
        Object fieldInstance = new Object();
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        InjectableField fieldToBeInjected = new InjectableField(new FieldTestClass(), "field1");
        Object fieldInstance = null;
        Mockito.doReturn(fieldToBeInjected).when(filter).getInjectableField(fieldInstance);
        OngoingMock ongoingMock = Mockito.mock(OngoingMock.class);
        Mockito.when(ongoingInjecterMock.inject(fieldToBeInjected)).thenReturn(ongoingMock);
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingMock);
        assertSame(ongoingMock, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateFieldInstanceNotInjectable() {
        Collection<Object> mocks = new ArrayList<>();
        InjectableField fieldToBeInjected = new InjectableField(new FieldTestClass(), "field1");
        Object fieldInstance = new Object();
        Mockito.doReturn(fieldToBeInjected).when(filter).getInjectableField(fieldInstance);
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateInjectableFieldInjected() {
        Collection<Object> mocks = new ArrayList<>();
        InjectableField fieldToBeInjected = new InjectableField(new FieldTestClass(), "field1");
        Object fieldInstance = new Object();
        Mockito.doReturn(fieldToBeInjected).when(filter).getInjectableField(fieldInstance);
        OngoingMock ongoingMock = Mockito.mock(OngoingMock.class);
        Mockito.when(ongoingInjecterMock.inject(fieldToBeInjected)).thenReturn(ongoingMock);
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingMock);
        assertSame(ongoingMock, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testGetInjectableFieldNullFieldInstance() {
        Object fieldInstance = null;
        assertNull(filter.getInjectableField(fieldInstance));
    }

    @Test
    public void testGetInjectableFieldNonInjectableField() {
        Object fieldInstance = new Object();
        Field field = new FieldTestClass().getClass().getDeclaredField("nonInjectableField");
        assertNull(filter.getInjectableField(fieldInstance));
    }

    @Test
    public void testGetInjectableFieldInjectableField() {
        Object fieldInstance = new FieldTestClass();
        Field injectableField = new FieldTestClass().getClass().getDeclaredField("field1");
        Mockito.when(filter.getField(fieldInstance, "field1")).thenReturn(injectableField);
        assertSame(injectableField, filter.getInjectableField(fieldInstance));
    }

    private static class FieldTestClass {
        private int field1;
        private int nonInjectableField;

        public int getField1() {
            return field1;
        }

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public int getNonInjectableField() {
            return nonInjectableField;
        }

        public Field getField(Object instance, String fieldName) {
            try {
                return instance.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class OngoingMock {

    }
}