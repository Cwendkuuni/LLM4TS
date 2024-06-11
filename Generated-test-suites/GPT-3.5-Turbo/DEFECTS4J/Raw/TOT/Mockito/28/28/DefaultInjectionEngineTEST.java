import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultInjectionEngineTest {

    @Mock
    private FieldInitializer fieldInitializerMock;
    @Mock
    private Field fieldMock;
    @Mock
    private Reporter reporterMock;
    @Mock
    private MockCandidateFilter mockCandidateFilterMock;

    @Test
    public void testInjectMocksOnFields() throws Exception {
        // Arrange
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(fieldMock);

        Set<Object> mocks = new HashSet<>();
        mocks.add(fieldInitializerMock);

        Object testClassInstance = new Object();

        Mockito.when(fieldMock.getName()).thenReturn("testField");

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        Mockito.verify(fieldInitializerMock, Mockito.times(1)).initialize();
        Mockito.verify(mockCandidateFilterMock, Mockito.times(1)).filterCandidate(mocks, fieldMock, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_cannotInitializeForInjectMocksAnnotation() throws Exception {
        // Arrange
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(fieldMock);

        Set<Object> mocks = new HashSet<>();
        mocks.add(fieldInitializerMock);

        Object testClassInstance = new Object();

        Mockito.when(fieldMock.getName()).thenReturn("testField");
        Mockito.doThrow(new MockitoException()).when(fieldInitializerMock).initialize();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        Mockito.verify(reporterMock, Mockito.times(1)).cannotInitializeForInjectMocksAnnotation("testField", new MockitoException());
    }

    @Test
    public void testInjectMocksOnFields_orderedInstanceFieldsFrom() {
        // Arrange
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = DefaultInjectionEngine.class;

        // Act
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertTrue(orderedFields.length > 0);
        for (int i = 0; i < orderedFields.length - 1; i++) {
            Class<?> fieldType1 = orderedFields[i].getType();
            Class<?> fieldType2 = orderedFields[i + 1].getType();
            assertTrue(fieldType1.isAssignableFrom(fieldType2));
        }
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        // Arrange
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Class<?> clazz = DefaultInjectionEngine.class;

        // Act
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(clazz);

        // Assert
        List<Field> sortedFields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        sortedFields.sort(Comparator.comparing(Field::getType));
        assertEquals(sortedFields.get(0), orderedFields[orderedFields.length - 1]);
        assertEquals(sortedFields.get(sortedFields.size() - 1), orderedFields[0]);
    }
}