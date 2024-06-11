import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Object testObject;
 private List<Field> fields;
 private Set<Object> mockCandidates;

 @Before
 public void setUp() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 testObject = mock(TestClass.class);
 fields = Arrays.asList(TestClass.class.getDeclaredFields());
 mockCandidates = new HashSet<>();
 mockCandidates.add(mock(Dependency1.class));
 mockCandidates.add(mock(Dependency2.class));
 }

 @Test
 public void testProcessInjection() {
 Field injectMocksField = FieldUtil.getDeclaredField(TestClass.class, "injectMocksField");
 Object injectMocksFieldOwner = spy(testObject);
 FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 assertNotNull(report);
 assertFalse(injectionOccurred);
 assertNull(FieldUtil.get(injectMocksField, injectMocksFieldOwner));
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithOneMock() {
 Field injectMocksField = FieldUtil.getDeclaredField(TestClass.class, "injectMocksField");
 Object injectMocksFieldOwner = spy(testObject);
 FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 assertNotNull(report);
 assertTrue(injectionOccurred);
 assertNotNull(FieldUtil.get(injectMocksField, injectMocksFieldOwner));
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithMultipleMocks() {
 Field injectMocksField = FieldUtil.getDeclaredField(TestClass.class, "injectMocksField");
 Object injectMocksFieldOwner = spy(testObject);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));
 mockCandidates.add(mock(Dependency2.class));
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 assertNotNull(injectMocksFieldOwner);
 assertFalse(injectionOccurred);
 assertNull(FieldUtil.get(injectMocksField, injectMocksFieldOwner));
 }

 @Test(expected = InvocationTargetException.class)
 public void testProcessInjectionWithThrowingInitializer() {
 Field injectMocksField = FieldUtil.getDeclaredField(TestClassWithThrowingInitializer.class, "injectMocksField");
 Object injectMocksFieldOwner = spy(new TestClassWithThrowingInitializer());
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 assertNotNull(injectMocksFieldOwner);
 assertFalse(injectionOccurred);
 assertNull(FieldUtil.get(injectMocksField, injectMocksFieldOwner));
 }

 @Test
 public void testInjectMockCandidates() {
 Class<?> awaitingInjectionClazz = DependencyContainer.class;
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject);

 assertFalse(injectionOccurred);
 assertNull(testObject);
 }

 @Test
 public void testInjectMockCandidatesWithOneMock() {
 Class<?> awaitingInjectionClazz = DependencyContainer.class;
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));

 assertFalse(injectionOccurred);
 assertNotNull(testObject);
 assertNotNull(FieldUtil.getMock(Dependency1.class, testObject));
 }

 @Test
 public void testInjectMockCandidatesWithMultipleMocks() {
 Class<?> awaitingInjectionClazz = DependencyContainer.class;
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));
 mockCandidates.add(mock(Dependency2.class));

 assertFalse(injectionOccurred);
 assertNotNull(testObject);
 assertNotNull(FieldUtil.getMock(Dependency1.class, testObject));
 assertNotNull(FieldUtil.getMock(Dependency2.class, testObject));
 }

 @Test
 public void testInjectMockCandidatesWithNoMocks() {
 Class<?> awaitingInjectionClazz = DependencyContainer.class;
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, Collections.emptySet(), testObject);

 assertFalse(injectionOccurred);
 assertNotNull(testObject);
 }

 @Test
 public void testInjectMockCandidatesOnFields() {
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(DependencyContainer.class);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);

 assertFalse(injectionOccurred);
 assertNull(testObject);
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithOneMock() {
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(DependencyContainer.class);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));

 assertFalse(injectionOccurred);
 assertNotNull(testObject);
 assertNotNull(FieldUtil.getMock(Dependency1.class, testObject));
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithMultipleMocks() {
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(DependencyContainer.class);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(mock(Dependency1.class));
 mockCandidates.add(mock(Dependency2.class));

 assertFalse(injectionOccurred);
 assertNotNull(testObject);
 assertNotNull(FieldUtil.getMock(Dependency1.class, testObject));
 assertNotNull(FieldUtil.getMock(Dependency2.class, testObject));
 }
}

class TestClass {
 @InjectMocks
 private Dependency1 dependency1;
 private Dependency2 dependency2;
}

class TestClassWithThrowingInitializer {
 @InjectMocks
 private Dependency dependency;

 public TestClassWithThrowingInitializer() throws Exception {
 throw new RuntimeException("Initialization failed");
 }
}

class DependencyContainer {
 // Some class with fields to be injected
}

class Dependency1 {}

class Dependency2 {}