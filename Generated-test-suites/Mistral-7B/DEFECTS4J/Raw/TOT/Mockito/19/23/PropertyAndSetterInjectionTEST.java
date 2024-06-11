import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object mockObject;
    private Object mockCandidate1;
    private Object mockCandidate2;
    private Object mockCandidate3;
    private Object fieldOwner;
    private Object fieldInstanceNeedingInjection;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockObject = mock(Object.class);
        mockCandidate1 = mock(ClassA.class);
        mockCandidate2 = mock(ClassB.class);
        mockCandidate3 = mock(ClassC.class);
        fieldOwner = new Object() {
            @InjectMocks
            private Object injectMocksField;
            private ClassA fieldA;
            private ClassB fieldB;
            private ClassC fieldC;
        };
        fieldInstanceNeedingInjection = new Object();
    }

    @Test
    public void testInjectMocksField() throws Exception {
        // Given
        List<Object> mockCandidates = new ArrayList<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);
        mockCandidates.add(mockCandidate3);

        // When
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(fieldOwner.getClass().getDeclaredField("injectMocksField"), fieldOwner);
        boolean injectionOccurred = injectionStrategy.processInjection(fieldOwner.getClass().getDeclaredField("injectMocksField"), fieldOwner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertNull(fieldOwner.injectMocksField); // should be null before injection
        assertNotNull(fieldOwner.injectMocksField); // should not be null after injection
        verify(mockCandidate1).equals(fieldOwner.injectMocksField); // first mock candidate should be injected
        verify(mockCandidate2).isInstance(fieldOwner.injectMocksField); // second mock candidate should be a subtype of injected field
        verify(mockCandidate3).isInstance(fieldOwner.injectMocksField.getClass().getSuperclass()); // third mock candidate should be a supertype of injected field
    }

    @Test
    public void testInjectMocksFieldNoMockCandidates() throws Exception {
        // Given
        List<Object> mockCandidates = Collections.emptyList();
        Set<Object> mocks = new HashSet<>(mockCandidates);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(fieldOwner.getClass().getDeclaredField("injectMocksField"), fieldOwner, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksFieldNoSetters() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = fieldOwner.getClass();
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = fieldInstanceNeedingInjection;

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksFieldWithStaticMockCandidate() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = fieldOwner.getClass();
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = fieldInstanceNeedingInjection;

        // When
        mockCandidate3.getClass().getDeclaredFields()[0].setAccessible(true);
        mockCandidate3.getClass().getDeclaredFields()[0].set(mockCandidate3, true);
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksFieldWithFinalMockCandidate() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = fieldOwner.getClass();
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = fieldInstanceNeedingInjection;

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksFieldWithMockCandidateByName() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = ClassB.class;
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = new Object() {
            @InjectMocks
            private Object injectMocksField;
            @Inject private ClassB fieldB;
        };

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        assertNotNull(instance.injectMocksField); // should be null before injection
        assertNotNull(instance.fieldB); // should not be null after injection
        verify(mockCandidate2).equals(instance.fieldB); // second mock candidate should be injected by name
    }

    @Test
    public void testInjectMocksFieldWithMultipleMockCandidatesByName() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = ClassB.class;
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = new Object() {
            @InjectMocks
            private Object injectMocksField;
            @Inject private ClassB fieldB;
        };

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        assertNotNull(instance.injectMocksField); // should be null before injection
        assertNotNull(instance.fieldB); // should not be null after injection
        verify(mockCandidate2).equals(instance.fieldB); // second mock candidate should be injected by name

        // Mockito should only inject one mock candidate per field
        verify(mockCandidate1, never()).equals(instance.fieldB);
    }

    @Test
    public void testInjectMocksFieldWithMultipleMockCandidatesByType() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = ClassC.class;
        Set<Object> mocks = new HashSet<>(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
        Object instance = new Object() {
            @InjectMocks
            private Object injectMocksField;
            @Inject private ClassC fieldC;
        };

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        assertNotNull(instance.injectMocksField); // should be null before injection
        assertNotNull(instance.fieldC); // should not be null after injection
        verify(mockCandidate3).equals(instance.fieldC); // third mock candidate should be injected by type
    }
}