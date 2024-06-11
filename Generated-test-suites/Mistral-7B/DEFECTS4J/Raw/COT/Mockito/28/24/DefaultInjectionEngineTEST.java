package org.mockito.internal.configuration.DefaultInjectionEngine; // Change the package name according to your project structure

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(PowerMockRunner.class)
public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;

    @Before
    public void setup() {
        mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass());

        Field injectMocksField1 = TestClass.class.getDeclaredField("field1");
        Field injectMocksField2 = TestClass.class.getDeclaredField("field2");
        assertNotNull(injectMocksField1.get(new TestClass()));
        assertNotNull(injectMocksField2.get(new TestClass()));
    }

    @Test
    public void testFailureWhenFieldInitilizationFails() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        Field injectMocksField = testClass.getDeclaredField("fieldWithInitFailure");
        injectMocksField.setAccessible(true);
        assertThrows(MockitoException.class, () -> injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass()));
    }

    @Test
    public void testInjectMockWithTypeBasedCandidate() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        PowerMockito.mockStatic(TypeBasedCandidateFilter.class);
        PowerMockito.when(TypeBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        PowerMockito.mockStatic(NameBasedCandidateFilter.class);
        PowerMockito.when(NameBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        PowerMockito.mockStatic(FinalMockCandidateFilter.class);
        PowerMockito.when(FinalMockCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass());
        Field injectMocksField = testClass.getDeclaredField("fieldWithTypeBasedCandidate");
        assertNotNull(injectMocksField.get(new TestClass()));
    }

    @Test
    public void testInjectMockWithNameBasedCandidate() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        PowerMockito.mockStatic(TypeBasedCandidateFilter.class);
        PowerMockito.mockStatic(NameBasedCandidateFilter.class);
        PowerMockito.when(NameBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        PowerMockito.when(TypeBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        PowerMockito.mockStatic(FinalMockCandidateFilter.class);
        PowerMockito.when(FinalMockCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass());
        Field injectMocksField = testClass.getDeclaredField("fieldWithNameBasedCandidate");
        assertNotNull(injectMocksField.get(new TestClass()));
    }

    @Test
    public void testInjectMockWithFinalField() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        PowerMockito.mockStatic(TypeBasedCandidateFilter.class);
        PowerMockito.mockStatic(NameBasedCandidateFilter.class);
        PowerMockito.mockStatic(FinalMockCandidateFilter.class);
        PowerMockito.when(NameBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        PowerMockito.when(TypeBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        PowerMockito.when(FinalMockCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass());
        Field injectMocksField = testClass.getDeclaredField("fieldWithFinalModifer");
        assertNotNull(injectMocksField.get(new TestClass()));
    }

    @Test
    public void testInjectMockWithMultipleCandidates() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        PowerMockito.mockStatic(TypeBasedCandidateFilter.class);
        PowerMockito.mockStatic(NameBasedCandidateFilter.class);
        PowerMockito.mockStatic(FinalMockCandidateFilter.class);
        PowerMockito.when(TypeBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .inOrder()
                .thenReturn(mock(MockCandidateFilter.class))
                .thenReturn(mock(MockCandidateFilter.class));
        PowerMockito.when(NameBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        PowerMockito.when(FinalMockCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, new TestClass());
        Field injectMocksField1 = testClass.getDeclaredField("field1");
        assertNotNull(injectMocksField1.get(new TestClass()));
        Field injectMocksField2 = testClass.getDeclaredField("field2");
        assertNotNull(injectMocksField2.get(new TestClass()));
    }

    @Test
    public void testInjectMockWithNullInstance() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        injectionEngine.injectMocksOnFields(injectMocksFields, null, new TestClass());
        Field injectMocksField = testClass.getDeclaredField("field1");
        assertNull(injectMocksField.get(new TestClass()));
    }

    @Test
    public void testInjectMockWithMockAlreadyInjected() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());
        PowerMockito.mockStatic(TypeBasedCandidateFilter.class);
        PowerMockito.mockStatic(NameBasedCandidateFilter.class);
        PowerMockito.mockStatic(FinalMockCandidateFilter.class);
        PowerMockito.when(TypeBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        PowerMockito.when(NameBasedCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        PowerMockito.when(FinalMockCandidateFilter.filterCandidate(Mockito.anySet(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        Set<Object> mocksCopy = new HashSet<Object>(mocks);
        mocksCopy.add(mock(Object.class));
        injectionEngine.injectMocksOnFields(injectMocksFields, mocksCopy, new TestClass());
        Field injectMocksField = testClass.getDeclaredField("field1");
        Object mock1 = injectMocksField.get(new TestClass());
        Object mock2 = mocksCopy.iterator().next();
        assertEquals(mock1, mock2);
    }

    public static class TestClass {
        @InjectMocks
        private Object field1;
        @InjectMocks
        private Object field2;
        @InjectMocks
        private Object fieldWithInitFailure;
        @InjectMocks
        private Object fieldWithTypeBasedCandidate;
        @InjectMocks
        private Object fieldWithNameBasedCandidate;
        @InjectMocks
        private Object fieldWithFinalModifier;
        @InjectMocks
        private Object field1WithMultipleCandidates;
    }
}