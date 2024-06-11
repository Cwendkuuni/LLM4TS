import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;

    @Before
    public void setup() {
        injection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_InitializesFieldViaSetter() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithInjectMocks.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setName("injectMocksField");
        injectMocksField.setAnnotated(true);
        Object mockCandidate = Mockito.mock(ClassToInject.class);

        Mockito.doReturn(mockCandidate).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField).when(targetInstance).getDeclaredField("injectMocksField");
        Mockito.doReturn(true).when(injectMocksField).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doNothing().when(injectMocksField).setAccessible(true);
        Mockito.doReturn(mockCandidate).when(injectMocksField).get(targetInstance);
        Mockito.doReturn(new FieldInitializationReport(ClassWithInjectMocks.class, injectMocksField, targetInstance)).when(injection).initializeInjectMocksField(injectMocksField, targetInstance);
        Mockito.doReturn(mockCandidate).when(injectMocksField).getMock(ArgumentMatchers.eq(mockCandidate), ArgumentMatchers.any());

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(mockCandidate);

        // when
        boolean injectionOccurred = injection.processInjection(injectMocksField, targetInstance, mockCandidates);

        // then
        assertTrue(injectionOccurred);
        assertEquals(mockCandidate, injectMocksField.get(targetInstance));
        verify(injectMocksField).setAccessible(true);
        verify(injectMocksField, times(2)).getMock(ArgumentMatchers.eq(mockCandidate), ArgumentMatchers.any());
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_InitializesFieldViaField() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithInjectMocks.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setName("injectMocksField");
        injectMocksField.setAnnotated(true);
        Object mockCandidate = Mockito.mock(ClassToInject.class);

        Mockito.doReturn(mockCandidate).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField).when(targetInstance).getDeclaredField("injectMocksField");
        Mockito.doReturn(true).when(injectMocksField).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(true).when(injectMocksField).canRead(targetInstance);
        Mockito.doReturn(null).when(injectMocksField).get(targetInstance);
        Mockito.doReturn(new FieldInitializationReport(ClassWithInjectMocks.class, injectMocksField, targetInstance)).when(injection).initializeInjectMocksField(injectMocksField, targetInstance);
        Mockito.doReturn(mockCandidate).when(injectMocksField).getMock(ArgumentMatchers.eq(mockCandidate), ArgumentMatchers.any());
        Mockito.doReturn(new Object[]{mockCandidate}).when(injectMocksField).getValues(targetInstance);

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(mockCandidate);

        // when
        boolean injectionOccurred = injection.processInjection(injectMocksField, targetInstance, mockCandidates);

        // then
        assertTrue(injectionOccurred);
        assertEquals(mockCandidate, injectMocksField.get(targetInstance));
        verify(injectMocksField).setAccessible(true);
        verify(injectMocksField, times(1)).getMock(ArgumentMatchers.eq(mockCandidate), ArgumentMatchers.any());
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_DoesNotInitializeField() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithInjectMocks.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setName("injectMocksField");
        injectMocksField.setAnnotated(true);
        Object mockCandidate = Mockito.mock(ClassToInject.class);

        Mockito.doReturn(mockCandidate).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField).when(targetInstance).getDeclaredField("injectMocksField");
        Mockito.doReturn(true).when(injectMocksField).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(true).when(injectMocksField).canRead(targetInstance);
        Mockito.doReturn(null).when(injectMocksField).get(targetInstance);
        Mockito.doReturn(new FieldInitializationReport(ClassWithInjectMocks.class, injectMocksField, targetInstance)).when(injection).initializeInjectMocksField(injectMocksField, targetInstance);
        Mockito.doReturn(mockCandidate).when(injectMocksField).getMock(ArgumentMatchers.eq(mockCandidate), ArgumentMatchers.any());
        Mockito.doReturn(new Object[]{new Object()}).when(injectMocksField).getValues(targetInstance);

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(mockCandidate);

        // when
        boolean injectionOccurred = injection.processInjection(injectMocksField, targetInstance, mockCandidates);

        // then
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(targetInstance));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InjectMocksAnnotatedField_InitializationThrowsException() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithInjectMocks.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setName("injectMocksField");
        injectMocksField.setAnnotated(true);

        Mockito.doReturn(targetClass).when(injectMocksField).getDeclaringClass();
        Mockito.doReturn(injectMocksField).when(targetInstance).getDeclaredField("injectMocksField");
        Mockito.doReturn(true).when(injectMocksField).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(new FieldInitializationReport(ClassWithInjectMocks.class, injectMocksField, targetInstance)).when(injection).initializeInjectMocksField(injectMocksField, targetInstance);
        Mockito.doThrow(new RuntimeException("Initialization failed")).when(injectMocksField).getMock(ArgumentMatchers.any(), ArgumentMatchers.any());

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(Mockito.mock(ClassToInject.class));

        // when & then
        injection.processInjection(injectMocksField, targetInstance, mockCandidates);
    }

    @Test
    public void testProcessInjection_MultipleInjectMocksAnnotatedFields_InitializesFields() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithMultipleInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithMultipleInjectMocks.class);
        Field injectMocksField1 = Mockito.mock(Field.class);
        injectMocksField1.setName("injectMocksField1");
        injectMocksField1.setAnnotated(true);
        Object mockCandidate1 = Mockito.mock(ClassToInject.class);

        Field injectMocksField2 = Mockito.mock(Field.class);
        injectMocksField2.setName("injectMocksField2");
        injectMocksField2.setAnnotated(true);
        Object mockCandidate2 = Mockito.mock(ClassToInject.class);

        Mockito.doReturn(mockCandidate1).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField1).when(targetInstance).getDeclaredField("injectMocksField1");
        Mockito.doReturn(true).when(injectMocksField1).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(mockCandidate1).when(injectMocksField1).getMock(ArgumentMatchers.eq(mockCandidate1), ArgumentMatchers.any());

        Mockito.doReturn(mockCandidate2).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField2).when(targetInstance).getDeclaredField("injectMocksField2");
        Mockito.doReturn(true).when(injectMocksField2).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(mockCandidate2).when(injectMocksField2).getMock(ArgumentMatchers.eq(mockCandidate2), ArgumentMatchers.any());

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        // when
        boolean injectionOccurred = injection.processInjection(injectMocksField1, targetInstance, mockCandidates);
        injectionOccurred = injection.processInjection(injectMocksField2, targetInstance, mockCandidates);

        // then
        assertTrue(injectionOccurred);
        assertTrue(injectionOccurred);
        assertEquals(mockCandidate1, injectMocksField1.get(targetInstance));
        assertEquals(mockCandidate2, injectMocksField2.get(targetInstance));
    }

    @Test
    public void testProcessInjection_MultipleInjectMocksAnnotatedFields_DifferentCandidates() {
        // given
        Class<?> targetClass = Mockito.mock(ClassWithMultipleInjectMocks.class);
        Object targetInstance = Mockito.mock(ClassWithMultipleInjectMocks.class);
        Field injectMocksField1 = Mockito.mock(Field.class);
        injectMocksField1.setName("injectMocksField1");
        injectMocksField1.setAnnotated(true);
        Object mockCandidate1 = Mockito.mock(ClassToInject1.class);

        Field injectMocksField2 = Mockito.mock(Field.class);
        injectMocksField2.setName("injectMocksField2");
        injectMocksField2.setAnnotated(true);
        Object mockCandidate2 = Mockito.mock(ClassToInject2.class);

        Mockito.doReturn(mockCandidate1).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField1).when(targetInstance).getDeclaredField("injectMocksField1");
        Mockito.doReturn(true).when(injectMocksField1).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(mockCandidate1).when(injectMocksField1).getMock(ArgumentMatchers.eq(mockCandidate1), ArgumentMatchers.any());

        Mockito.doReturn(mockCandidate2).when(injection).getMockCandidates(ArgumentMatchers.eq(targetClass), ArgumentMatchers.eq(targetInstance));
        Mockito.doReturn(injectMocksField2).when(targetInstance).getDeclaredField("injectMocksField2");
        Mockito.doReturn(true).when(injectMocksField2).canSetAccessible(ArgumentMatchers.anyBoolean());
        Mockito.doReturn(mockCandidate2).when(injectMocksField2).getMock(ArgumentMatchers.eq(mockCandidate2), ArgumentMatchers.any());

        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        // when
        boolean injectionOccurred1 = injection.processInjection(injectMocksField1, targetInstance, mockCandidates);
        boolean injectionOccurred2 = injection.processInjection(injectMocksField2, targetInstance, mockCandidates);

        // then
        assertTrue(injectionOccurred1);
        assertFalse(injectionOccurred2);
        assertEquals(mockCandidate1, injectMocksField1.get(targetInstance));
        assertNull(injectMocksField2.get(targetInstance));
    }
}

class ClassWithInjectMocks {
    @InjectMocks
    private static class InnerClass {
        private SomeDependency dependency;
    }

    @InjectMocks
    private static class AnotherInnerClass {
        private AnotherDependency dependency;
    }
}

class ClassWithMultipleInjectMocks {
    @InjectMocks
    private static class InnerClass1 {
        private SomeDependency dependency1;
        // other fields
    }

    @InjectMocks
    private static class InnerClass2 {
        private AnotherDependency dependency2;
        // other fields
    }
}

class ClassToInject {
    // some implementation
}

class ClassToInject1 {
    // some implementation
}

class ClassToInject2 {
    // some implementation
}