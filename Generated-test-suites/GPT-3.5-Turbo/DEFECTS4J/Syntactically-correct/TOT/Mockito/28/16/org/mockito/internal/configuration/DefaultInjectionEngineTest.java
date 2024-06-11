
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Mock
    private FieldInitializer fieldInitializerMock;

    @Mock
    private Reporter reporterMock;

    @Spy
    private DefaultInjectionEngine injectionEngineSpy;

    @Test
    public void injectMocksOnFields_ShouldInitializeField_IfFieldInitializerDoesNotThrowException() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = this;

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(fieldInitializerMock, times(1)).initialize();
    }

    @Test
    public void injectMocksOnFields_ShouldCallReporter_IfFieldInitializerThrowsException() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = this;

        doThrow(new MockitoException("Cannot initialize field")).when(fieldInitializerMock).initialize();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(reporterMock, times(1)).cannotInitializeForInjectMocksAnnotation(any(String.class), any(MockitoException.class));
    }

    @Test
    public void injectMocksOnFields_ShouldInjectMocksForFieldsInClassHierarchy() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = this;

        Field[] declaredFields = getClass().getDeclaredFields();
        Arrays.sort(declaredFields, Comparator.comparing(Field::getType));

        Field[] orderedFields = injectionEngineSpy.orderedInstanceFieldsFrom(getClass());

        assertArrayEquals(declaredFields, orderedFields);
    }

    @Test
    public void injectMocksOnFields_ShouldInjectMockCandidateForEachFieldInClassHierarchy() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = this;

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(injectionEngineSpy, times(3)).injectMockCandidate(any(Class.class), any(Set.class), any(Object.class));
    }

    @Test
    public void injectMockCandidate_ShouldNotInjectMockCandidate_IfFilterReturnsEmptySet() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = this;

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(injectionEngineSpy, never()).injectMockCandidate(any(Class.class), any(Set.class), any(Object.class));
    }

    @Test
    public void injectMockCandidate_ShouldInjectMockCandidate_IfFilterReturnsNonEmptySet() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field = getClass().getDeclaredField("injectionEngineSpy");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = this;

        when(injectionEngineSpy.orderedInstanceFieldsFrom(any(Class.class)))
                .thenReturn(new Field[] {field});

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(injectionEngineSpy, times(1)).injectMockCandidate(any(Class.class), any(Set.class), any(Object.class));
    }

    @Test
    public void orderedInstanceFieldsFrom_ShouldReturnFieldsOrderedBySupertypesLast() throws Exception {
        MockitoAnnotations.initMocks(this);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field[] declaredFields = getClass().getDeclaredFields();
        Arrays.sort(declaredFields, injectionEngineSpy.supertypesLast);

        Field[] orderedFields = injectionEngineSpy.orderedInstanceFieldsFrom(getClass());

        assertArrayEquals(declaredFields, orderedFields);
    }
}

