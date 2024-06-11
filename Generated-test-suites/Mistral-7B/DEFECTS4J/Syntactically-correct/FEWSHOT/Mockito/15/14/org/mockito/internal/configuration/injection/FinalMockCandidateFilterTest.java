package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.FieldSetter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.Field;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.FieldMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);
        Object matchingMock = mock(TestingClass.class);

        assertTrue(filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterFailure() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_OneMock_FieldAccessSuccess() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);
        Object matchingMock = mock(TestingClass.class);

        try {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.setAccessible(true);
            fieldSetter.set(matchingMock, 42);
        } catch (IllegalAccessException e) {
            // expected
        }

        assertTrue(filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_OneMock_FieldAccessFailure() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);

        assertFalse(filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);

        assertFalse(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_MatchingMock() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(TestingClass.class);
        Object mock2 = mock(TestingClass.class);
        Object fieldInstance = mock(TestingClass.class);
        Object matchingMock = mock(TestingClass.class);

        assertTrue(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);
        Object matchingMock = mock(TestingClass.class);

        try {
            new FieldSetter(matchingMock, field).set(fieldInstance, 42);
        } catch (IllegalAccessException e) {
            // expected
        }

        assertTrue(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessSuccess() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);
        Object matchingMock = mock(TestingClass.class);

        try {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.setAccessible(true);
            fieldSetter.set(matchingMock, 42);
        } catch (IllegalAccessException e) {
            // expected
        }

        assertTrue(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);

        assertFalse(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessFailure() {
        Field field = new Field("testField", Integer.TYPE, FieldMode.PRIVATE, null, null);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(TestingClass.class);

        assertFalse(filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance).thenInject());
    }

    static class TestingClass {
        private int testField;
    }
}