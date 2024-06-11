import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.testutil.FieldTestUtils;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

@RunWith(JUnit4.class)
public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;

 @Test
 public void testProcessInjection_success() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then
 Assert.assertTrue(result);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_mockitoExceptionThrown_whenInitializationFails() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 Mockito.doThrow(new InvocationTargetException(null, new RuntimeException("Test Exception"))).when(mockCandidates.iterator().next()).someMethod();

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then - expected MockitoException
 }

 @Test
 public void testProcessInjection_noMockCandidatesFound() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newHashSet();

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then
 Assert.assertFalse(result);
 }

 @Test
 public void testProcessInjection_multipleMockCandidatesFound_typeMatch() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 Set<Object> expectedInjected = newHashSet(Arrays.asList(Mockito.mock(MockA.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then
 Assert.assertTrue(result);
 Assert.assertEquals(expectedInjected, propertyAndSetterInjection.getInjectedValues());
 }

 @Test
 public void testProcessInjection_multipleMockCandidatesFound_nameMatch() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 Set<Object> expectedInjected = newHashSet(Arrays.asList(Mockito.mock(MockB.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 propertyAndSetterInjection.setMockCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
 boolean result = propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then
 Assert.assertTrue(result);
 Assert.assertEquals(expectedInjected, propertyAndSetterInjection.getInjectedValues());
 }

 @Test
 public void testProcessInjection_noInjectedField() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 Set<Object> expectedInjected = newHashSet();

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates);

 // Then
 Assert.assertTrue(result);
 Assert.assertEquals(expectedInjected, propertyAndSetterInjection.getInjectedValues());
 }

 @Test
 public void testInitializeInjectMocksField_success() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 FieldInitializationReport result = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, mockOwner);

 // Then
 Assert.assertNotNull(result);
 Assert.assertNotNull(result.getFieldInstance());
 }

 @Test(expected = MockitoException.class)
 public void testInitializeInjectMocksField_initializationExceptionThrown() throws Exception {
 // Given
 Object mockOwner = Mockito.spy(new TestClass());
 Field injectMocksField = FieldTestUtils.getDeclaredField(TestClass.class, "injectMocksField");
 injectMocksField.setAccessible(true);
 Mockito.doThrow(new RuntimeException("Test Exception")).when(mockOwner).newInstance();

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 FieldInitializationReport result = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, mockOwner);

 // Then - expected MockitoException
 }

 @Test
 public void testInjectMockCandidates_success() throws Exception {
 // Given
 Object awaitingInjectionInstance = Mockito.spy(new TestClass());
 Class<?> awaitingInjectionClazz = awaitingInjectionInstance.getClass();
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, awaitingInjectionInstance);

 // Then
 Assert.assertTrue(result);
 }

 @Test
 public void testInjectMockCandidatesOnFields_success() throws Exception {
 // Given
 Object awaitingInjectionInstance = Mockito.spy(new TestClass());
 Class<?> awaitingInjectionClazz = awaitingInjectionInstance.getClass();
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, awaitingInjectionInstance, false, orderedInstanceFields);

 // Then
 Assert.assertTrue(result);
 }

 @Test
 public void testInjectMockCandidatesOnFields_multipleMockCandidatesFound_typeMatch() throws Exception {
 // Given
 Object awaitingInjectionInstance = Mockito.spy(new TestClass());
 Class<?> awaitingInjectionClazz = awaitingInjectionInstance.getClass();
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 Set<Object> expectedInjected = newHashSet(Arrays.asList(Mockito.mock(MockA.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 propertyAndSetterInjection.setMockCandidateFilter(new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter())));
 boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, awaitingInjectionInstance, false, orderedInstanceFields);

 // Then
 Assert.assertTrue(result);
 Assert.assertEquals(expectedInjected, propertyAndSetterInjection.getInjectedValues());
 }

 @Test
 public void testInjectMockCandidatesOnFields_multipleMockCandidatesFound_nameMatch() throws Exception {
 // Given
 Object awaitingInjectionInstance = Mockito.spy(new TestClass());
 Class<?> awaitingInjectionClazz = awaitingInjectionInstance.getClass();
 Set<Object> mockCandidates = newHashSet(Arrays.asList(Mockito.mock(MockA.class), Mockito.mock(MockB.class)));
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 Set<Object> expectedInjected = newHashSet(Arrays.asList(Mockito.mock(MockB.class)));

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 propertyAndSetterInjection.setMockCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
 boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, awaitingInjectionInstance, false, orderedInstanceFields);

 // Then
 Assert.assertTrue(result);
 Assert.assertEquals(expectedInjected, propertyAndSetterInjection.getInjectedValues());
 }

 @Test
 public void testInjectMockCandidatesOnFields_noMockCandidatesFound() throws Exception {
 // Given
 Object awaitingInjectionInstance = Mockito.spy(new TestClass());
 Class<?> awaitingInjectionClazz = awaitingInjectionInstance.getClass();
 Set<Object> mockCandidates = newHashSet();
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, awaitingInjectionInstance, false, orderedInstanceFields);

 // Then
 Assert.assertFalse(result);
 }
}

 class TestClass {
 @InjectMocks
 private TestDependency testDependency;
 }

 interface TestDependency {}

 class MockA implements TestDependency {}
 class MockB implements TestDependency {}