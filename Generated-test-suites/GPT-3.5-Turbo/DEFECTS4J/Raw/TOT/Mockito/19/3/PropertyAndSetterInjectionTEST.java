import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Field injectMocksField;
    private Object injectMocksFieldOwner;
    private Set<Object> mockCandidates;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        injectMocksField = mock(Field.class);
        injectMocksFieldOwner = new Object();
        mockCandidates = new HashSet<>();
    }

    @Test
    public void testProcessInjection_WithInitializedField_SuccessfullyInjectsMocks() {
        // Arrange
        Class<?> fieldClass = SomeClass.class;
        Object fieldInstanceNeedingInjection = new SomeClass();
        FieldInitializationReport report = new FieldInitializationReport(fieldClass, fieldInstanceNeedingInjection);
        when(injectionStrategy.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner)).thenReturn(report);
        when(injectionStrategy.injectMockCandidates(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection)).thenReturn(true);

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(result);
        verify(injectionStrategy).initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        verify(injectionStrategy).injectMockCandidates(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection);
    }

    @Test
    public void testProcessInjection_WithUninitializedField_ThrowsException() {
        // Arrange
        Class<?> fieldClass = SomeClass.class;
        Object fieldInstanceNeedingInjection = null;
        FieldInitializationReport report = new FieldInitializationReport(fieldClass, fieldInstanceNeedingInjection);
        when(injectionStrategy.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner)).thenReturn(report);
        when(injectionStrategy.injectMockCandidates(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection)).thenReturn(false);
        Reporter reporter = mock(Reporter.class);
        doThrow(new MockitoException("Cannot initialize field")).when(reporter).cannotInitializeForInjectMocksAnnotation(eq("fieldName"), any(MockitoException.class));
        whenNew(Reporter.class).withNoArguments().thenReturn(reporter);

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertFalse(result);
        verify(injectionStrategy).initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        verify(injectionStrategy).injectMockCandidates(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection);
        verify(reporter).cannotInitializeForInjectMocksAnnotation("fieldName", new MockitoException("Cannot initialize field"));
    }

    @Test
    public void testInitializeInjectMocksField_WithNoException_ReturnsFieldInitializationReport() throws NoSuchFieldException {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldOwner = new SomeClass();
        FieldInitializationReport expectedReport = new FieldInitializationReport(SomeClass.class, fieldOwner);
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        whenNew(FieldInitializer.class).withArguments(fieldOwner, field).thenReturn(fieldInitializer);
        when(fieldInitializer.initialize()).thenReturn(expectedReport);

        // Act
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, fieldOwner);

        // Assert
        assertEquals(expectedReport, report);
        verifyNew(FieldInitializer.class).withArguments(fieldOwner, field);
        verify(fieldInitializer).initialize();
    }

    @Test
    public void testInitializeInjectMocksField_WithInvocationTargetException_ThrowsException() throws NoSuchFieldException {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldOwner = new SomeClass();
        FieldInitializationReport expectedReport = new FieldInitializationReport(SomeClass.class, fieldOwner);
        InvocationTargetException exception = new InvocationTargetException(new NullPointerException("Field is null"));
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        whenNew(FieldInitializer.class).withArguments(fieldOwner, field).thenReturn(fieldInitializer);
        when(fieldInitializer.initialize()).thenThrow(new MockitoException(exception));
        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).fieldInitialisationThrewException(field, exception);
        whenNew(Reporter.class).withNoArguments().thenReturn(reporter);

        // Act
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, fieldOwner);

        // Assert
        assertEquals(expectedReport, report);
        verifyNew(FieldInitializer.class).withArguments(fieldOwner, field);
        verify(fieldInitializer).initialize();
        verify(reporter).fieldInitialisationThrewException(field, exception.getCause());
    }

    @Test
    public void testInjectMockCandidates_WithMockCandidates_SuccessfullyInjectsMocks() {
        // Arrange
        Class<?> awaitingInjectionClazz = SomeClass.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new SomeClass();
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(SomeClass.class.getDeclaredField("field1"));
        orderedInstanceFields.add(SomeClass.class.getDeclaredField("field2"));
        when(injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz)).thenReturn(orderedInstanceFields);
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        Object mockCandidate1 = mock(SomeClass.class);
        Object mockCandidate2 = mock(SomeClass.class);
        when(mockCandidateFilter.filterCandidate(mocks, orderedInstanceFields.get(0), instance)).thenReturn(mockCandidate1);
        when(mockCandidateFilter.filterCandidate(mocks, orderedInstanceFields.get(1), instance)).thenReturn(mockCandidate2);
        when(mockCandidateFilter.thenInject()).thenReturn(mockCandidate1, mockCandidate2);

        // Act
        boolean result = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Assert
        assertTrue(result);
        verify(injectionStrategy).orderedInstanceFieldsFrom(awaitingInjectionClazz);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, orderedInstanceFields.get(0), instance);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, orderedInstanceFields.get(1), instance);
        verify(mockCandidateFilter, times(2)).thenInject();
        verify(mocks, times(2)).remove(any());
        assertEquals(0, orderedInstanceFields.size());
    }

    @Test
    public void testInjectMockCandidatesOnFields_WithInjectedMock_SuccessfullyInjectsMocks() throws NoSuchFieldException {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("field1");
        Object injectedMock = mock(SomeClass.class);
        when(injectionStrategy.mockCandidateFilter.filterCandidate(mockCandidates, field, injectMocksFieldOwner)).thenReturn(injectionStrategy.mockCandidateFilter);
        when(injectionStrategy.mockCandidateFilter.thenInject()).thenReturn(injectedMock);

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, injectMocksFieldOwner, false, Collections.singletonList(field));

        // Assert
        assertTrue(result);
        verify(injectionStrategy.mockCandidateFilter).filterCandidate(mockCandidates, field, injectMocksFieldOwner);
        verify(injectionStrategy.mockCandidateFilter).thenInject();
        verify(mockCandidates).remove(injectedMock);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_WithClassContainingFields_ReturnsOrderedFieldsList() {
        // Arrange
        Class<?> awaitingInjectionClazz = SomeClass.class;
        List<Field> expectedOrderedFields = Arrays.asList(
                SomeClass.class.getDeclaredField("field1"),
                SomeClass.class.getDeclaredField("field2"),
                SomeSuperClass.class.getDeclaredField("superField")
        );

        // Act
        List<Field> orderedFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertEquals(expectedOrderedFields, orderedFields);
    }

}

class SomeSuperClass {
    public int superField;
}

class SomeClass extends SomeSuperClass {
    public int field1;
    public int field2;
}