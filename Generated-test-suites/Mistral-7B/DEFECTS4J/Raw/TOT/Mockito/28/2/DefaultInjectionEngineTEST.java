import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testInstance;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testInstance = new TestClass();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(
 TestClass.class.getDeclaredFields()
 ).stream()
 .filter(Field::isAnnotationPresent)
 .map(field -> (Field) field.getAnnotation(InjectMocks.class))
 .collect(Collectors.toSet())
 .toArray(Set::new);

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

 for (Field injectMocksField : injectMocksFields) {
 assertNotNull(injectMocksField.get(testInstance));
 }
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithException() {
 Set<Field> injectMocksFields = Arrays.asList(
 TestClass2.class.getDeclaredFields()
 ).stream()
 .filter(Field::isAnnotationPresent)
 .map(field -> (Field) field.getAnnotation(InjectMocks.class))
 .collect(Collectors.toSet())
 .toArray(Set::new);

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = TestClass3.class;
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 Object fieldInstance = mock(awaitingInjectionClazz);

 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);

 assertNotNull(awaitingInjectionClazz.getDeclaredFields()[0].get(fieldInstance));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidateWithException() {
 Class<?> awaitingInjectionClazz = TestClass4.class;
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 Object fieldInstance = spy(awaitingInjectionClazz);

 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 Class<?> awaitingInjectionClazz = TestClass5.class;
 Field[] orderedInstanceFieldsFrom = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 assertNotNull(orderedInstanceFieldsFrom[0]);
 assertNotNull(orderedInstanceFieldsFrom[1]);
 assertNotSame(orderedInstanceFieldsFrom[0], orderedInstanceFieldsFrom[1]);
 }

 @Test
 public void testFilterCandidate() {
 Class<?> awaitingInjectionClazz = TestClass6.class;
 Set<Object> mocks = new HashSet<>(Arrays.asList(mock(AnotherClass.class), mock(AnotherClass2.class)));
 Field field = awaitingInjectionClazz.getDeclaredField("fieldToTest");
 Object fieldInstance = new TestClass6();

 NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter();
 TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(nameBasedCandidateFilter);
 FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
 MockCandidateFilter mockCandidateFilter = new MockCandidateFilter(typeBasedCandidateFilter, finalMockCandidateFilter, nameBasedCandidateFilter);

 assertNotNull(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testThenInject() {
 Class<?> awaitingInjectionClazz = TestClass7.class;
 Set<Object> mocks = new HashSet<>(Arrays.asList(mock(AnotherClass.class), mock(AnotherClass2.class)));
 Object fieldInstance = new TestClass7();
 Field field = awaitingInjectionClazz.getDeclaredField("fieldToTest");

 NameBasedCandidateFilter nameBasedCandidateFilter = mock(NameBasedCandidateFilter.class);
 TypeBasedCandidateFilter typeBasedCandidateFilter = mock(TypeBasedCandidateFilter.class);
 FinalMockCandidateFilter finalMockCandidateFilter = mock(FinalMockCandidateFilter.class);
 MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
 when(nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(AnotherClass.class));
 when(typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);
 when(finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);
 when(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(AnotherClass2.class));
 when(mockCandidateFilter.thenInject(mocks, field, fieldInstance)).thenReturn(null);

 injectionEngine = new DefaultInjectionEngine();
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 assertNotNull(awaitingInjectionClazz.getDeclaredFields()[0].get(fieldInstance));
 }
}

class TestClass {
 @InjectMocks
 private AnotherClass anotherClass;
}

class TestClass2 {
 @InjectMocks
 private AnotherClass anotherClass;
 @InjectMocks
 private AnotherClass2 anotherClass2;
}

class TestClass3 {
 private AnotherClass anotherClass;
}

class TestClass4 {
 @InjectMocks
 private AnotherClass anotherClass;
 private AnotherClass anotherClassSpy;
}

class TestClass5 {
 private AnotherClass anotherClass;
 private AnotherClass2 anotherClass2;
 private AnotherClass3 anotherClass3;
}

class TestClass6 {
 @InjectMocks
 private AnotherClass anotherClass;
 private AnotherClass2 anotherClass2;
}

class TestClass7 {
 @InjectMocks
 private AnotherClass anotherClass;
}

interface AnotherClass {}

interface AnotherClass2 {}