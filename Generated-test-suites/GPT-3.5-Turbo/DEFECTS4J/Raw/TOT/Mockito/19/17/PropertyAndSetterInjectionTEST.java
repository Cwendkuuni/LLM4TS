
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    }

    @Test
    public void testProcessInjection_InjectMocksFieldIsNull_ReturnsFalse() {
        Field injectMocksField = null;
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldOwnerIsNull_ReturnsFalse() {
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = null;
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_NoMockCandidates_ReturnsFalse() {
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldInitializationThrowsMockitoException_ReturnsFalse() {
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        FieldInitializationReport fieldInitializationReport = mock(FieldInitializationReport.class);
        MockitoException mockitoException = mock(MockitoException.class);
        Throwable cause = mock(Throwable.class);

        when(fieldInitializationReport.fieldClass()).thenReturn(Object.class);
        when(fieldInitializationReport.fieldInstance()).thenReturn(new Object());
        when(mockitoException.getCause()).thenReturn(cause);

        whenNew(FieldInitializer.class).withArguments(injectMocksFieldOwner, injectMocksField).thenReturn(mock(FieldInitializer.class));
        when(mock(FieldInitializer.class).initialize()).thenThrow(mockitoException);

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldInitializationThrowsInvocationTargetException_ReturnsFalse() {
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        FieldInitializationReport fieldInitializationReport = mock(FieldInitializationReport.class);
        MockitoException mockitoException = mock(MockitoException.class);
        InvocationTargetException invocationTargetException = mock(InvocationTargetException.class);
        Throwable cause = mock(Throwable.class);

        when(fieldInitializationReport.fieldClass()).thenReturn(Object.class);
        when(fieldInitializationReport.fieldInstance()).thenReturn(new Object());

        whenNew(FieldInitializer.class).withArguments(injectMocksFieldOwner, injectMocksField).thenReturn(mock(FieldInitializer.class));
        when(mock(FieldInitializer.class).initialize()).thenThrow(mockitoException);
        when(mockitoException.getCause()).thenReturn(invocationTargetException);
        when(invocationTargetException.getCause()).thenReturn(cause);

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldHasNoInjectedFields_ReturnsFalse() throws Exception {
        Field injectMocksField = propertyAndSetterInjection.getClass().getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = propertyAndSetterInjection;
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldHasInjectedFields_ReturnsTrue() throws Exception {
        Field injectMocksField = propertyAndSetterInjection.getClass().getDeclaredField("notFinalOrStatic");
        Object injectMocksFieldOwner = propertyAndSetterInjection;
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertTrue(result);
    }

    @Test
    public void testInitializeInjectMocksField_FieldInitializerThrowsMockitoException_CallsReporter() {
        Field field = mock(Field.class);

        whenNew(FieldInitializer.class).withArguments(propertyAndSetterInjection, field).thenReturn(mock(FieldInitializer.class));
        when(mock(FieldInitializer.class).initialize()).thenThrow(MockitoException.class);

        try {
            propertyAndSetterInjection.initializeInjectMocksField(field, propertyAndSetterInjection);
            Assert.fail("Should throw MockitoException");
        } catch (MockitoException e) {
            verifyNew(Reporter.class).withNoArguments();
        }
    }

    @Test
    public void testInitializeInjectMocksField_FieldInitializerThrowsMockitoExceptionAndCauseIsInvocationTargetException_CallsReporter() {
        Field field = mock(Field.class);
        MockitoException mockitoException = mock(MockitoException.class);
        InvocationTargetException invocationTargetException = mock(InvocationTargetException.class);

        when(mockitoException.getCause()).thenReturn(invocationTargetException);
        whenNew(FieldInitializer.class).withArguments(propertyAndSetterInjection, field).thenReturn(mock(FieldInitializer.class));
        when(mock(FieldInitializer.class).initialize()).thenThrow(mockitoException);

        try {
            propertyAndSetterInjection.initializeInjectMocksField(field, propertyAndSetterInjection);
            Assert.fail("Should throw MockitoException");
        } catch (MockitoException e) {
            verifyNew(Reporter.class).withNoArguments();
        }
    }

    @Test
    public void testInjectMockCandidates_ClassHasNoDeclaredFields_DoesNotInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();

        boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        Assert.assertFalse(result);
    }

    @Test
    public void testInjectMockCandidates_ClassHasNoMockCandidates_ReturnsFalse() {
        Class<?> awaitingInjectionClazz = propertyAndSetterInjection.getClass();
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();

        boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        Assert.assertFalse(result);
    }

    @Test
    public void testInjectMockCandidates_ClassHasInjectedFields_ReturnsTrue() {
        Class<?> awaitingInjectionClazz = propertyAndSetterInjection.getClass();
        Set<Object> mocks = new HashSet<>();
        Object instance = propertyAndSetterInjection;

        boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        Assert.assertTrue(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoMockCandidates_ReturnsFalse() {
        Set<Object> mocks = new HashSet<>();
        Object instance = propertyAndSetterInjection;
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = Arrays.asList(propertyAndSetterInjection.getClass().getDeclaredFields());

        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        Assert.assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockCandidateFilterReturnsNull_ReturnsFalse() {
        Set<Object> mocks = new HashSet<>();
        Object instance = propertyAndSetterInjection;
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = Arrays.asList(propertyAndSetterInjection.getClass().getDeclaredFields());
        Field field = mock(Field.class);

        when(mockCandidateFilter.filterCandidate(mocks, field, instance)).thenReturn(null);

        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        Assert.assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockCandidateFilterReturnsInjectedMock_ReturnsTrue() {
        Set<Object> mocks = new HashSet<>();
        Object instance = propertyAndSetterInjection;
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = Arrays.asList(propertyAndSetterInjection.getClass().getDeclaredFields());
        Field field = mock(Field.class);
        Object mock = mock(Object.class);

        when(mockCandidateFilter.filterCandidate(mocks, field, instance)).thenReturn(mock);

        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ClassHasNoDeclaredFields_ReturnsEmptyList() {
        Class<?> awaitingInjectionClazz = Object.class;

        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ClassHasDeclaredFinalAndStaticFields_ReturnsEmptyList() {
        Class<?> awaitingInjectionClazz = propertyAndSetterInjection.getClass();

        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ClassHasDeclaredInstanceFields_ReturnsSortedList() {
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        Assert.assertEquals(3, result.size());
        Assert.assertEquals("mockCandidateFilter", result.get(0).getName());
        Assert.assertEquals("notFinalOrStatic", result.get(1).getName());
        Assert.assertEquals("propertyAndSetterInjection", result.get(2).getName());
    }
}
