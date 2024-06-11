package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.*;

import java.lang.reflect.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FootprintMatcher.isAnnotatedWith;
import static org.mockito.internal.util.reflection.FootprintMatcher.isAssignableFrom;
import static org.mockito.internal.util.reflection.FootprintMatcher.isField;
import static org.mockito.internal.util.reflection.FootprintMatcher.isInstanceOf;
import static org.mockito.internal.util.reflection.FootprintMatcher.isType;
import static org.mockito.internal.util.reflection.FootprintMatcher.isVisible;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Object testObject;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        testObject = new TestClass();
    }

    @Test
    public void testProcessInjection() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, Mockito.mock(Mockito.type(String.class)));

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));
        mockCandidates.add(Mockito.mock(Number.class));

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(1)).isInstanceOf(String.class);
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verify(mockCandidates.iterator().next(), times(1)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithNoCandidates() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));
    }

    @Test
    public void testProcessInjectionWithMultipleCandidates() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));
        mockCandidates.add(Mockito.mock(Number.class));

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(String.class);
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test
    public void testProcessInjectionWithMultipleCandidatesByName() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksFieldByName");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));
        mockCandidates.add(Mockito.mock(Number.class));

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(String.class);
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(Number.class);
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());

        Field injectMocksFieldByName = testObject.getClass().getDeclaredField("injectMocksFieldByName");
        injectMocksFieldByName.setAccessible(true);
        injectMocksFieldByName.set(testObject, Mockito.mock(String.class));

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksFieldByName, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(1)).isInstanceOf(String.class);
        verify(mockCandidates.iterator().next(), times(1)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test
    public void testProcessInjectionWithNoSetter() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksFieldNoSetter");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(String.class);
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test
    public void testProcessInjectionWithFieldInjection() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));

        Field injectMocksFieldTarget = testObject.getClass().getDeclaredField("injectMocksFieldTarget");
        injectMocksFieldTarget.setAccessible(true);
        injectMocksFieldTarget.set(testObject, null);

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(injectMocksFieldTarget, times(1)).set(isInstanceOf(String.class), any());
        verifyNoMoreInteractions(injectMocksFieldTarget);
        verify(mockCandidates.iterator().next(), times(1)).isInstanceOf(String.class);
        verify(mockCandidates.iterator().next(), times(1)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithInjectionFailure() throws Exception {
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testObject, null);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(String.class));
        mockCandidates.add(Mockito.mock(Number.class));

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(String.class);
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());
        verify(mockCandidates.iterator().next(), times(0)).isInstanceOf(Number.class);
        verify(mockCandidates.iterator().next(), times(0)).isAMock();
        verifyNoMoreInteractions(mockCandidates.iterator().next());

        Field injectMocksFieldWithNoSetter = testObject.getClass().getDeclaredField("injectMocksFieldNoSetter");
        injectMocksFieldWithNoSetter.setAccessible(true);
        injectMocksFieldWithNoSetter.set(testObject, null);

        Class<?> fieldClass = testObject.getClass();
        Object fieldInstanceNeedingInjection = null;
        while (fieldClass != Object.class) {
            try {
                fieldInstanceNeedingInjection = getFieldValue(fieldClass, "injectMocksFieldNoSetter");
                break;
            } catch (IllegalAccessException e) {
                fieldClass = fieldClass.getSuperclass();
            }
        }

        assertNull(fieldInstanceNeedingInjection);

        String errorMessage = "";
        try {
            propertyAndSetterInjection.processInjection(injectMocksFieldWithNoSetter, testObject, mockCandidates);
        } catch (MockitoException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("Field <injectMocksFieldNoSetter> in class <[...]> is not accessible: is private", errorMessage);
    }

    private Object getFieldValue(Class<?> fieldClass, String fieldName) throws IllegalAccessException {
        Field field = fieldClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(null);
    }

    static class TestClass {
        @InjectMocks
        private String injectMocksField;
        private String injectMocksFieldTarget;
        @InjectMocks
        private String injectMocksFieldByName;
        @InjectMocks
        private String injectMocksFieldNoSetter;

        public TestClass() {
        }
    }
}