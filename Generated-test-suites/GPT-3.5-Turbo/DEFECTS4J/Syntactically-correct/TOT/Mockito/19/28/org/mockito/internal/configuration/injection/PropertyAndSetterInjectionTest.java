import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection strategy;
    private Object mockObject1;
    private Object mockObject2;
    private Object mockObject3;

    @Before
    public void setUp() {
        strategy = new PropertyAndSetterInjection();
        mockObject1 = mock(Object.class);
        mockObject2 = mock(Object.class);
        mockObject3 = mock(Object.class);
    }

    @Test
    public void testProcessInjection_InjectMocksFieldInitialized_InjectsMocksAndReturnsTrue() throws Exception {
        // Arrange
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = strategy;
        Set<Object> mockCandidates = new HashSet<>(Arrays.asList(mockObject1, mockObject2, mockObject3));

        // Act
        boolean result = strategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mockCandidates, injectMocksField, injectMocksFieldOwner);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mockCandidates, injectMocksField, injectMocksFieldOwner);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mockCandidates, injectMocksField, injectMocksFieldOwner);
        verify(mockCandidateFilter).thenInject();
        assert result;
    }

    @Test
    public void testInitializeInjectMocksField_FieldInitializationSucceeds_ReturnsFieldInitializationReport() throws Exception {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = strategy;

        // Act
        FieldInitializationReport report = strategy.initializeInjectMocksField(field, fieldOwner);

        // Assert
        assert report != null;
        assert report.fieldInstance() != null;
        assert report.fieldClass() != null;
    }

    @Test(expected = Reporter.class)
    public void testInitializeInjectMocksField_FieldInitializationThrowsException_ThrowsReporterException() throws Exception {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = strategy;
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        MockitoException mockitoException = mock(MockitoException.class);
        InvocationTargetException invocationTargetException = new InvocationTargetException(mockitoException);
        when(fieldInitializer.initialize()).thenThrow(new MockitoException(invocationTargetException.getCause()));

        // Act
        strategy.initializeInjectMocksField(field, fieldOwner);
    }

    @Test
    public void testInjectMockCandidates_InjectsMocksOnFields_ReturnsTrue() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockObject1, mockObject2, mockObject3));
        Object instance = mock(PropertyAndSetterInjection.class);

        // Act
        boolean result = strategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getField("mockCandidateFilter"), instance);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getField("mockCandidateFilter"), instance);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getField("mockCandidateFilter"), instance);
        verify(mockCandidateFilter).thenInject();
        assert result;
    }

    @Test
    public void testInjectMockCandidatesOnFields_InjectsMockCandidates_ReturnsTrue() throws Exception {
        // Arrange
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockObject1, mockObject2, mockObject3));
        Object instance = mock(PropertyAndSetterInjection.class);
        List<Field> orderedInstanceFields = Arrays.asList(PropertyAndSetterInjection.class.getDeclaredFields());

        // Act
        boolean result = strategy.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getDeclaredFields()[0], instance);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getDeclaredFields()[1], instance);
        verify(mockCandidateFilter).thenInject();
        verify(mockCandidateFilter).filterCandidate(mocks, PropertyAndSetterInjection.class.getDeclaredFields()[2], instance);
        verify(mockCandidateFilter).thenInject();
        assert result;
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ReturnsOrderedFieldsList() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        // Act
        List<Field> result = strategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assert result != null;
        assert result.size() == 3;
        assert result.get(0).getName().equals("mockCandidateFilter");
        assert result.get(1).getName().equals("notFinalOrStatic");
        assert result.get(2).getName().equals("strategy");
    }

    @Test(expected = Reporter.class)
    public void testOrderedInstanceFieldsFrom_FilterThrowsException_ThrowsReporterException() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        List<Field> orderedInstanceFields = Arrays.asList(PropertyAndSetterInjection.class.getDeclaredFields());
        ListUtil.Filter<Field> notFinalOrStatic = mock(ListUtil.Filter.class);
        when(notFinalOrStatic.isOut(PropertyAndSetterInjection.class.getDeclaredFields()[0])).thenThrow(new Reporter());

        // Act
        List<Field> result = strategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}