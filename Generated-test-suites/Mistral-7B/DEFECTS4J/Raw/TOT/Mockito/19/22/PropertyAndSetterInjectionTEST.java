import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object targetInstance;
    private Set<Object> mockCandidates;
    private Stubber stubber;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        targetInstance = new TestClass();
        mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(mock(ClassWithMockableFields.class), mock(AnotherClass.class), mock(YetAnotherClass.class)));
        stubber = Mockito.mock(Stubber.class);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(stubber).injectMocks(targetInstance);
        injectionStrategy.setStubber(stubber);
    }

    @Test
    public void testInjectionWithSetters() {
        // Given
        Field fieldAnnotatedWithInjectMocks = getFieldAnnotatedWithInjectMocks(targetInstance.getClass());
        fieldAnnotatedWithInjectMocks.setAccessible(true);
        fieldAnnotatedWithInjectMocks.set(targetInstance, mock(DependentClass.class));

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(fieldAnnotatedWithInjectMocks, targetInstance, mockCandidates);

        // Then
        assertEquals(true, injectionOccurred);
        verify(stubber).injectMocks(targetInstance);
    }

    @Test
    public void testInjectionWithFieldAnnotation() {
        // Given
        Field fieldToBeInjected = getFieldToBeInjected(targetInstance.getClass());
        fieldToBeInjected.setAccessible(true);

        // When
        injectionStrategy.processInjection(fieldToBeInjected, targetInstance, mockCandidates);

        // Then
        ArgumentCaptor<Class<?>> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(stubber).injectMocks(classArgumentCaptor.capture(), objectArgumentCaptor.capture());
        assertEquals(DependentClass.class, classArgumentCaptor.getValue());
        assertNotNull(objectArgumentCaptor.getValue());
    }

    @Test
    public void testInjectionWithNoMockCandidates() {
        // Given
        Field fieldToBeInjected = getFieldToBeInjected(targetInstance.getClass());
        fieldToBeInjected.setAccessible(true);

        // When
        mockCandidates.clear();
        injectionStrategy.processInjection(fieldToBeInjected, targetInstance, mockCandidates);

        // Then
        // No verification is performed as we expect no injection to occur
    }

    @Test(expected = MockitoException.class)
    public void testInjectionWithFailedInitialization() {
        // Given
        Field fieldToBeInjected = getFieldToBeInjected(targetInstance.getClass());
        fieldToBeInjected.setAccessible(true);

        // When & Then
        injectionStrategy.processInjection(fieldToBeInjected, targetInstance, mockCandidates);
    }

    @Test(expected = MockitoException.class)
    public void testInjectionWithNoSettersOrFields() {
        // Given
        targetInstance = new NonInjectableClass();

        // When & Then
        injectionStrategy.processInjection(getFieldAnnotatedWithInjectMocks(targetInstance.getClass()), targetInstance, mockCandidates);
    }

    private Field getFieldAnnotatedWithInjectMocks(Class<?> clazz) {
        Field field = null;
        for (Field fieldDeclaration : clazz.getDeclaredFields()) {
            if (fieldDeclaration.isAnnotationPresent(InjectMocks.class)) {
                field = fieldDeclaration;
                break;
            }
        }
        return field;
    }

    private Field getFieldToBeInjected(Class<?> clazz) {
        for (Field fieldDeclaration : clazz.getDeclaredFields()) {
            if (fieldDeclaration.getType().isAssignableFrom(DependentClass.class)) {
                return fieldDeclaration;
            }
        }
        throw new RuntimeException("No field annotated with @InjectMocks or of type DependentClass found");
    }

    private static class TestClass {
        @InjectMocks
        private DependentClass dependentClass;
    }

    private static class DependentClass {
    }

    private static class ClassWithMockableFields {
        @InjectMocks
        private AnotherClass anotherClass;
    }

    private static class AnotherClass {
    }

    private static class NonInjectableClass {
        // No @InjectMocks or any field of type DependentClass
    }
}