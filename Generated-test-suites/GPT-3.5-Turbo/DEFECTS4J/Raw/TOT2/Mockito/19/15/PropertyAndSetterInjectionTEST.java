import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() {
        Field injectMocksField = SomeClass.class.getDeclaredField("mockField");
        SomeClass injectMocksFieldOwner = new SomeClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new MockCandidate());

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testInitializeInjectMocksField() {
        Field field = SomeClass.class.getDeclaredField("mockField");
        Object fieldOwner = new SomeClass();

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
        assertTrue(report != null);
    }

    @Test
    public void testInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = SomeClass.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(new MockCandidate());
        Object instance = new Object();

        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(new MockCandidate());
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<Field>();

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = SomeClass.class;
        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertTrue(orderedInstanceFields.size() > 0);
    }

}