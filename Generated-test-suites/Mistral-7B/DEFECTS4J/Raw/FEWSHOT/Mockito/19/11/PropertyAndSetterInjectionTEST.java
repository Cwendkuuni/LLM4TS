package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestableField;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private static final List<Answer<Object>> ANSWERS = new ArrayList<>();
    private static final Set<Object> MOCK_CANDIDATES = new MockSafeHashSet<>();

    private PropertyAndSetterInjection injectionStrategy;
    private FieldInitializer fieldInitializer;
    private Stubber stubber;
    private Class<?> testClass;

    @Before
    public void setup() {
        testClass = getClass();
        injectionStrategy = new PropertyAndSetterInjection();
        fieldInitializer = mock(FieldInitializer.class);
        stubber = mock(Stubber.class);
    }

    @Test
    public void testProcessInjection_NoMocksToInject() {
        Object instance = new Object();
        Set<Object> mocks = new HashSet<>();
        assertFalse(injectionStrategy.processInjection(new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null), instance, mocks));
    }

    @Test
    public void testProcessInjection_NoMockCandidate() {
        Object instance = new Object();
        Object mock = mock(Object.class);
        MOCK_CANDIDATES.add(mock);
        Set<Object> mocks = new HashSet<>(MOCK_CANDIDATES);
        Field field = new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null);
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = new FieldInitializationReport(field, fieldInstanceNeedingInjection);
        when(fieldInitializer.initialize()).thenReturn(report);

        assertFalse(injectionStrategy.processInjection(field, instance, mocks));
    }

    @Test
    public void testProcessInjection_MockCandidateByType() {
        Object instance = new Object();
        Object mock = mock(Object.class);
        MOCK_CANDIDATES.add(mock);
        Set<Object> mocks = new HashSet<>(MOCK_CANDIDATES);
        Field field = new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null);
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = new FieldInitializationReport(field, fieldInstanceNeedingInjection);
        when(fieldInitializer.initialize()).thenReturn(report);

        doAnswer(invocation -> {
            Object mockCandidate = invocation.getArgument(0);
            ANSWERS.add(invocation.getMockitoMethod().get(0).getAnswer());
            mocks.remove(mockCandidate);
            return null;
        }).when(stubber).thenAnswer(any(Answer.class)).on(any(Class.class)).andReturn(mock);

        when(field.getType()).thenReturn(Object.class);

        injectionStrategy.processInjection(field, instance, mocks);

        verify(stubber).thenAnswer(any(Answer.class)).on(eq(Object.class));
        assertEquals(1, ANSWERS.size());
    }

    @Test
    public void testProcessInjection_MockCandidateByName() {
        Object instance = new Object();
        Object mock1 = mock(Object1.class);
        Object mock2 = mock(Object2.class);
        Object mock3 = mock(Object3.class);
        MOCK_CANDIDATES.addAll(Arrays.asList(mock1, mock2, mock3));
        Set<Object> mocks = new HashSet<>(MOCK_CANDIDATES);
        Field field = new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null);
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = new FieldInitializationReport(field, fieldInstanceNeedingInjection);
        when(fieldInitializer.initialize()).thenReturn(report);

        doAnswer(invocation -> {
            Object mockCandidate = invocation.getArgument(0);
            ANSWERS.add(invocation.getMockitoMethod().get(0).getAnswer());
            mocks.remove(mockCandidate);
            return null;
        }).when(stubber).thenAnswer(any(Answer.class)).on(any(Class.class)).andReturn(mock1);

        doAnswer(invocation -> {
            Object mockCandidate = invocation.getArgument(0);
            ANSWERS.add(invocation.getMockitoMethod().get(0).getAnswer());
            mocks.remove(mockCandidate);
            return null;
        }).when(stubber).thenAnswer(any(Answer.class)).on(eq(Object2.class)).andReturn(mock2);

        when(field.getType()).thenReturn(Object1.class);

        injectionStrategy.processInjection(field, instance, mocks);

        verify(stubber, times(2)).thenAnswer(any(Answer.class));
        assertEquals(2, ANSWERS.size());
    }

    @Test
    public void testProcessInjection_NoMockCandidateInHierarchy() {
        Object instance = new Object();
        Object mock = mock(Object.class);
        MOCK_CANDIDATES.add(mock);
        Set<Object> mocks = new HashSet<>(MOCK_CANDIDATES);
        Field field = new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null);
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = new FieldInitializationReport(field, fieldInstanceNeedingInjection);
        when(fieldInitializer.initialize()).thenReturn(report);

        Class<?> subClass = getClass();
        Class<?> superClass = Object.class;

        injectionStrategy.processInjection(field, instance, mocks);

        verifyNoInteractions(stubber);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InitializationException() {
        Object instance = new Object();
        Object mock = mock(Object.class);
        MOCK_CANDIDATES.add(mock);
        Set<Object> mocks = new HashSet<>(MOCK_CANDIDATES);
        Field field = new TestableField<>(testClass, "field1", Object.class, Modifier.PRIVATE, null);
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = new FieldInitializationReport(field, fieldInstanceNeedingInjection);
        when(fieldInitializer.initialize()).thenThrow(new MockitoException("Initialization failed"));

        injectionStrategy.processInjection(field, instance, mocks);
    }
}

class Object1 {}
class Object2 {}
class Object3 {}