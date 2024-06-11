import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Stubber stubber;

    @Before
    public void setup() {
        stubber = Mockito.stub(Stubber.class);
        injection = new PropertyAndSetterInjection() {
            @Override
            MockCandidateFilter getMockCandidateFilter() {
                return stubber.createMock(MockCandidateFilter.class);
            }
        };
    }

    @Test
    public void testProcessInjection_initializesFieldWithNoSetterOrFieldAnnotation() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field field = new Field(Object.class, "field") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        // When
        boolean injectionOccurred = injection.processInjection(field, owner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertNull(field.get(owner));
        verifyZeroInteractions(mockCandidates);

        // And verify that the field is initialized using a no-arg constructor
        Object initializedField = field.get(owner);
        verify(mockCandidates.iterator()).next().equals(initializedField);
    }

    @Test
    public void testProcessInjection_initializesFieldWithSetter() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        injectMocksField.set(owner, mock(Class.class));

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertSame(mock(Class.class), injectMocksField.get(owner));
        verifyZeroInteractions(mockCandidates);
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcessInjection_throwsIllegalAccessExceptionWhenAccessingPrivateField() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};

        Set<Object> mockCandidates = new HashSet<>();

        // When
        injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then: expected exception
    }

    @Test
    public void testProcessInjection_doesNotInjectFieldIfNoMockCandidateIsFound() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(owner));
        verifyZeroInteractions(mockCandidates);
    }

    @Test
    public void testProcessInjection_injectsFieldWithMockCandidateByType() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        Stubber mockCandidateFilter = stubber.createMock(MockCandidateFilter.class);
        mockCandidateFilter.when(any(Set.class), eq(injectMocksField), any(Object.class)).thenReturn(mockCandidates.iterator().next());
        injection.setMockCandidateFilter(mockCandidateFilter);

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertSame(mock(Class.class), injectMocksField.get(owner));
        verify(mockCandidateFilter).filterCandidate(any(Set.class), eq(injectMocksField), any(Object.class));
    }

    @Test
    public void testProcessInjection_injectsFieldWithMockCandidateByName() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Field("alias", String.class));

        Stubber mockCandidateFilter = stubber.createMock(MockCandidateFilter.class);
        mockCandidateFilter.when(any(Set.class), eq(injectMocksField), any(Object.class)).thenReturn(null);
        mockCandidateFilter.when(any(Set.class), eq(injectMocksField), any(Object.class)).thenReturn(mockCandidates.iterator().next());
        injection.setMockCandidateFilter(mockCandidateFilter);

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertSame(new Field("alias", String.class), injectMocksField.get(owner));
        verify(mockCandidateFilter).filterCandidate(any(Set.class), eq(injectMocksField), any(Object.class)).twice();
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_throwsExceptionWhenInitializationFails() throws Exception {
        // Given
        Object owner = mock(Object.class);
        Field injectMocksField = new Field(Object.class, "injectMocksField") {{
            setAccessible(true);
            setModifiers(Modifier.PRIVATE);
        }};
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(Class.class));

        Stubber mockCandidateFilter = stubber.createMock(MockCandidateFilter.class);
        mockCandidateFilter.when(any(Set.class), eq(injectMocksField), any(Object.class)).thenReturn(mockCandidates.iterator().next());
        injection.setMockCandidateFilter(mockCandidateFilter);

        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        ArgumentCaptor<Throwable> exceptionArgumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        stubber.doThrow(new InvocationTargetException(new MockitoException("Exception message"), null)).when(fieldInitializer).initialize();
        injection.setFieldInitializer(fieldInitializer);

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, owner, mockCandidates);

        // Then: expected exception
    }

    @Test
    public void testInjectMockCandidates_injectsMockCandidateOnFieldIfAvailable() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Set<Object> mocks = new HashSet<>();

        Stubber mockCandidateFilter = stubber.createMock(MockCandidateFilter.class);
        mockCandidateFilter.when(any(Set.class), any(Class.class), any(Set.class), any(Object.class)).thenReturn(mock(Field.class));
        injection.setMockCandidateFilter(mockCandidateFilter);

        // When
        boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        verify(mockCandidateFilter).filterCandidate(any(Set.class), eq(awaitingInjectionClazz), any(Set.class), any(Object.class));

        // And verify that the mock is injected into the field
        Field field = Whitebox.getInternalState(injection, "lastInjectedField");
        Object injectedMock = field.get(instance);
        verify(mockCandidateFilter).filterCandidate(any(Set.class), eq(field), any(Object.class));
        assertEquals(injectedMock, field.get(instance));
    }

    @Test
    public void testInjectMockCandidates_doesNotInjectMockCandidateOnFieldIfNotAvailable() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Set<Object> mocks = new HashSet<>();

        Stubber mockCandidateFilter = stubber.createMock(MockCandidateFilter.class);
        mockCandidateFilter.when(any(Set.class), any(Class.class), any(Set.class), any(Object.class)).thenReturn(null);
        injection.setMockCandidateFilter(mockCandidateFilter);

        // When
        boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
        verifyZeroInteractions(mockCandidateFilter);
    }
}