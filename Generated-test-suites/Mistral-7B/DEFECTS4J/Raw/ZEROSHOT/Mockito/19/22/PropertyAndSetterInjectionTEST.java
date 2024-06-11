package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
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
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;
    private Reporter reporter;
    private Set<Object> mockCandidates;
    private Object testObject;

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = spy(new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter())));
        reporter = mock(Reporter.class);
        mockCandidates = newMockSafeHashSet(10);
        testObject = new Object() {
            @InjectMocks
            private Field fieldToInject;
        };
    }

    @Test
    public void testProcessInjection_FieldAnnotatedByInjectMocks_SetsMockByPropertySetter() throws Exception {
        Object mock = mock(Field.class);
        Field field = new Field() {
            @InjectMocks
            private Field injectMocksField;
            private Object injectMocksFieldOwner = testObject;

            @Override
            public Object get(Object obj) {
                return injectMocksField.get(testObject);
            }

            @Override
            public void set(Object obj, Object value) {
                injectMocksField.set(testObject, value);
                this.fieldToInject = value;
            }
        };
        mockCandidates.add(mock);
        FieldInitializationReport report = new FieldInitializationReport(field, mock);

        propertyAndSetterInjection.processInjection(field, testObject, mockCandidates);

        verify(mockCandidateFilter).filterCandidate(mockCandidates, field, testObject);
        verify(mockCandidateFilter).thenInject(mock);
        verify(reporter).fieldInitialisationSuccess(field, mock);
    }

    @Test
    public void testProcessInjection_FieldAnnotatedByInjectMocks_SetsMockByFieldInjection() throws Exception {
        Object mock = mock(Field.class);
        Field field = new Field() {
            @InjectMocks
            private Field injectMocksField;
            private Object injectMocksFieldOwner = testObject;

            @Override
            public Object get(Object obj) {
                return injectMocksField.get(testObject);
            }

            @Override
            public void set(Object obj, Object value) {
                injectMocksField.set(testObject, value);
            }
        };

        PropertyAndSetterInjection spyPropertyAndSetterInjection = spy(propertyAndSetterInjection);
        doReturn(null).when(spyPropertyAndSetterInjection).initializeInjectMocksField(field, testObject);
        doThrow(new MockitoException("Test Exception")).when(spyPropertyAndSetterInjection).injectMockCandidates(Field.class.getSuperclass(), mockCandidates, testObject);

        mockCandidates.add(mock);

        propertyAndSetterInjection.processInjection(field, testObject, mockCandidates);

        verify(mockCandidateFilter).filterCandidate(mockCandidates, field, testObject);
        verifyZeroInteractions(mock);
        verifyZeroInteractions(reporter);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_FieldAnnotatedByInjectMocks_ThrowsException() throws Exception {
        Object mock = mock(Field.class);
        Field field = new Field() {
            @InjectMocks
            private Field injectMocksField;
            private Object injectMocksFieldOwner = testObject;
        };
        mockCandidates.add(mock);

        doThrow(new MockitoException("Test Exception")).when(mockCandidateFilter).filterCandidate(mockCandidates, field, testObject);

        propertyAndSetterInjection.processInjection(field, testObject, mockCandidates);

        verify(reporter).cannotInitializeForInjectMocksAnnotation(field.getName(), new MockitoException("Test Exception"));
    }

    @Test
    public void testProcessInjection_FieldNotAnnotatedByInjectMocks() throws Exception {
        Object mock = mock(Field.class);
        Field field = new Field();
        mockCandidates.add(mock);

        propertyAndSetterInjection.processInjection(field, testObject, mockCandidates);

        verifyNoMoreInteractions(mockCandidateFilter);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testInjectMockCandidates_NoMockInjected() throws Exception {
        Object mock = mock(Field.class);
        Class<?> awaitingInjectionClazz = Field.class;
        Set<Object> mocks = newMockSafeHashSet(1);
        Object instance = new Object();

        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        verifyNoMoreInteractions(mockCandidateFilter);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testInjectMockCandidates_MockInjectedByType() throws Exception {
        Object mock = mock(Field.class);
        Object mockType = mock(Field.class);
        Class<?> awaitingInjectionClazz = Field.class;
        Set<Object> mocks = newMockSafeHashSet(3);
        Object instance = new Object();

        mocks.add(mockType);
        mocks.add(mock);

        doReturn(mockType).when(mockCandidateFilter).filterCandidate(mocks, null, null);

        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        verify(mockCandidateFilter).filterCandidate(mocks, null, null);
        verify(mockCandidateFilter).thenInject(mockType);
        verify(reporter).fieldInitialisationSuccess(null, mockType);
    }

    @Test
    public void testInjectMockCandidates_MockInjectedByName() throws Exception {
        Object mock1 = mock(Field.class);
        Object mock2 = mock(Field.class);
        Object mockName = mock(Field.class);
        Class<?> awaitingInjectionClazz = Field.class;
        Set<Object> mocks = newMockSafeHashSet(4);
        Object instance = new Object();

        mocks.add(mock1);
        mocks.add(mock2);

        doReturn(null).when(mockCandidateFilter).filterCandidate(mocks, null, null);
        doReturn(mockName).when(mockCandidateFilter).filterCandidate(mocks, new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, instance);

        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        verify(mockCandidateFilter).filterCandidate(mocks, null, null);
        verify(mockCandidateFilter).filterCandidate(mocks, new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, instance);
        verify(mockCandidateFilter).thenInject(mockName);
        verify(reporter).fieldInitialisationSuccess(new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, mockName);
    }

    @Test
    public void testInjectMockCandidates_MultiplePasses() throws Exception {
        Object mock1 = mock(Field.class);
        Object mock2 = mock(Field.class);
        Object mock3 = mock(Field.class);
        Object mockName = mock(Field.class);
        Class<?> awaitingInjectionClazz = Field.class;
        Set<Object> mocks = newMockSafeHashSet(6);
        Object instance = new Object();

        mocks.add(mock1);
        mocks.add(mock2);

        doReturn(mock1).when(mockCandidateFilter).filterCandidate(mocks, null, null);
        doReturn(mockName).when(mockCandidateFilter).filterCandidate(mocks, new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, instance);

        doReturn(null).when(mockCandidateFilter).filterCandidate(mocks, null, null);
        doReturn(mock3).when(mockCandidateFilter).filterCandidate(mocks, new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, instance);

        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        verify(mockCandidateFilter, times(3)).filterCandidate(mocks, null, null);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, instance);
        verify(mockCandidateFilter).thenInject(mockName);
        verify(mockCandidateFilter).thenInject(mock3);
        verify(reporter, times(2)).fieldInitialisationSuccess(new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, mockName);
        verify(reporter).fieldInitialisationSuccess(new Field() {
            @Override
            public String getName() {
                return "name";
            }
        }, mock3);
    }
}