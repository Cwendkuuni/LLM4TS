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
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Field field;
    private Object fieldOwner;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        field = mock(Field.class);
        fieldOwner = new Object();
        mockCandidates = new HashSet<>();
    }

    @Test
    public void testProcessInjection_WithInjectMocksFieldAndMockCandidates_ReturnsTrue() {
        boolean injectionOccurred = injectionStrategy.processInjection(field, fieldOwner, mockCandidates);

        assertTrue(injectionOccurred);
    }

    @Test
    public void testProcessInjection_WithInjectMocksFieldAndEmptyMockCandidates_ReturnsFalse() {
        boolean injectionOccurred = injectionStrategy.processInjection(field, fieldOwner, new HashSet<>());

        assertFalse(injectionOccurred);
    }

    @Test
    public void testInitializeInjectMocksField_WithNoExceptions_ReturnsValidReport() {
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, fieldOwner);

        assertNotNull(report);
        assertEquals(fieldOwner, report.fieldInstance());
        assertEquals(Object.class, report.fieldClass());
    }

    @Test(expected = Reporter.FieldInitialisationThrewException.class)
    public void testInitializeInjectMocksField_WithInvocationTargetException_ThrowsReporterException() {
        FieldInitializer initializer = mock(FieldInitializer.class);
        when(initializer.initialize()).thenThrow(new MockitoException(new InvocationTargetException(new Exception())));

        injectionStrategy.initializeInjectMocksField(field, fieldOwner);
    }

    @Test(expected = Reporter.CannotInitializeForInjectMocksAnnotation.class)
    public void testInitializeInjectMocksField_WithOtherException_ThrowsReporterException() {
        FieldInitializer initializer = mock(FieldInitializer.class);
        when(initializer.initialize()).thenThrow(new MockitoException(new Exception()));

        injectionStrategy.initializeInjectMocksField(field, fieldOwner);
    }

    @Test
    public void testInjectMockCandidates_WithMockCandidatesAndInstance_InjectsMocks() throws NoSuchFieldException {
        TestClass instance = new TestClass();
        mockCandidates.add("mock1");
        mockCandidates.add("mock2");

        boolean injectionOccurred = injectionStrategy.injectMockCandidates(TestClass.class, mockCandidates, instance);

        assertTrue(injectionOccurred);
        assertNull(instance.mock1);
        assertNull(instance.mock2);
    }

    @Test
    public void testInjectMockCandidatesOnFields_WithInjectedMocks_RemovesMocksAndFields() throws NoSuchFieldException {
        TestClass instance = new TestClass();
        FieldMockCandidate mock1 = new FieldMockCandidate("mock1", "field1");
        FieldMockCandidate mock2 = new FieldMockCandidate("mock2", "field2");
        Set<Object> mocks = new HashSet<>(Arrays.asList(mock1, mock2));
        List<Field> orderedInstanceFields = Arrays.asList(TestClass.class.getDeclaredFields());

        boolean injectionOccurred = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        assertTrue(injectionOccurred);
        assertFalse(mocks.contains(mock1));
        assertFalse(mocks.contains(mock2));
        assertFalse(orderedInstanceFields.contains(mock1.getField()));
        assertFalse(orderedInstanceFields.contains(mock2.getField()));
    }

    @Test
    public void testOrderedInstanceFieldsFrom_WithTestClass_ReturnsOrderedFields() {
        List<Field> orderedFields = injectionStrategy.orderedInstanceFieldsFrom(TestClass.class);

        assertNotNull(orderedFields);
        assertFalse(orderedFields.isEmpty());

        List<String> actualFieldNames = new ArrayList<>();
        for (Field field : orderedFields) {
            actualFieldNames.add(field.getName());
        }

        List<String> expectedFieldNames = Arrays.asList("privateFinalField", "packageField", "protectedField", "publicField", "privateStaticField", "packageStaticField", "protectedStaticField", "publicStaticField");

        assertEquals(expectedFieldNames, actualFieldNames);
    }

    private static class TestClass {
        private final String privateFinalField = "privateFinalFieldValue";
        String packageField = "packageFieldValue";
        protected String protectedField = "protectedFieldValue";
        public String publicField = "publicFieldValue";

        private static final String privateStaticField = "privateStaticFieldValue";
        static String packageStaticField = "packageStaticFieldValue";
        protected static String protectedStaticField = "protectedStaticFieldValue";
        public static String publicStaticField = "publicStaticFieldValue";

        private Object mock1;
        private Object mock2;

        private Field getField(String fieldName) throws NoSuchFieldException {
            return TestClass.class.getDeclaredField(fieldName);
        }
    }

    private static class FieldMockCandidate {
        private final String mockName;
        private final String fieldName;

        public FieldMockCandidate(String mockName, String fieldName) {
            this.mockName = mockName;
            this.fieldName = fieldName;
        }

        public String getMockName() {
            return mockName;
        }

        public Field getField() throws NoSuchFieldException {
            return TestClass.class.getDeclaredField(fieldName);
        }
    }
}