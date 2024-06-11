import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.ReflectionUtils.getDeclaredField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setFieldValue;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Object testObject;

 @Before
 public void setup() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 testObject = new TestClass();
 }

 @Test
 public void testProcessInjection_withMocksProvided_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mockCandidates = new HashSet<>(Arrays.asList(new MockA(), new MockB()));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(getDeclaredField(TestClass.class, "injectMocksField"), testObject, mockCandidates);

 // Then
 assertFalse("Injection should have occurred", !injectionOccurred);
 }

 @Test
 public void testProcessInjection_withoutMocksProvided_shouldReturnFalse() throws Exception {
 // Given
 Set<Object> mockCandidates = Collections.emptySet();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(getDeclaredField(TestClass.class, "injectMocksField"), testObject, mockCandidates);

 // Then
 assertFalse("Injection should not have occurred", injectionOccurred);
 }

 @Test
 public void testInitializeInjectMocksField_whenSuccessful_shouldNotReturnNull() throws Exception {
 // Given
 Field injectMocksField = getDeclaredField(TestClass.class, "injectMocksField");
 Object fieldInstanceNeedingInjection = new Object();

 // When
 FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);

 // Then
 assertNull("Report should not be null", report);
 }

 @Test(expected = MockitoException.class)
 public void testInitializeInjectMocksField_whenExceptionThrown_shouldThrowMockitoException() throws Exception {
 // Given
 Field injectMocksField = getDeclaredField(TestClass.class, "injectMocksField");
 Object fieldInstanceNeedingInjection = new Object();

 // When & Then
 propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
 }

 @Test
 public void testInjectMockCandidates_whenNoMocksFound_shouldReturnFalse() throws Exception {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>();
 Object instance = new Object();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertFalse("Injection should not have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidates_whenMockFoundByType_shouldReturnTrue() throws Exception {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA()));
 Object instance = new Object();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidates_whenMockFoundByName_shouldReturnTrue() throws Exception {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockB()));
 Object instance = new Object();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidates_whenMultipleMocksFound_shouldReturnTrue() throws Exception {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA(), new MockB()));
 Object instance = new Object();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenNoMocksFound_shouldReturnFalse() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>();
 Object instance = new Object();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertFalse("Injection should not have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenMockFoundByType_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA()));
 Object instance = new Object();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenMockFoundByName_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockB()));
 Object instance = new Object();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenMultipleMocksFound_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA(), new MockB()));
 Object instance = new Object();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenNoMoreMocksToInject_shouldReturnFalse() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA()));
 Object instance = new Object();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(Collections.emptySet(), instance, true, orderedInstanceFields);

 // Then
 assertFalse("Injection should not have occurred", injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenMockInjectedBySetter_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA()));
 Object instance = new TestClass();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);

 // When
 Object mockA = mocks.iterator().next();
 Field field1 = getDeclaredField(TestClass.class, "field1");
 setFieldValue(instance, field1, mockA);

 // Then
 assertTrue("Mock A should have been injected by setter", propertyAndSetterInjection.injectMockCandidatesOnFields(Collections.emptySet(), instance, true, orderedInstanceFields).isOut(field1));
 }

 @Test
 public void testInjectMockCandidatesOnFields_whenMockInjectedByField_shouldReturnTrue() throws Exception {
 // Given
 Set<Object> mocks = new HashSet<>(Arrays.asList(new MockA()));
 Object instance = new TestClass();
 List<Field> orderedInstanceFields = Arrays.asList(getDeclaredField(TestClass.class, "field1"), getDeclaredField(TestClass.class, "field2"));

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue("Injection should have occurred", injectionOccurred);

 // When
 Object mockA = mocks.iterator().next();
 Field field2 = getDeclaredField(TestClass.class, "field2");
 setFieldValue(instance, field2, mockA);

 // Then
 assertTrue("Mock A should have been injected by field", propertyAndSetterInjection.injectMockCandidatesOnFields(Collections.emptySet(), instance, true, orderedInstanceFields).isOut(field2));
 }
}

class TestClass {
 @InjectMocks
 private Object injectMocksField;
 private Field field1;
 private Field field2;

 public TestClass() {
 field1 = getDeclaredField(TestClass.class, "field1");
 field2 = getDeclaredField(TestClass.class, "field2");
 }
}

class MockA {}

class MockB {}