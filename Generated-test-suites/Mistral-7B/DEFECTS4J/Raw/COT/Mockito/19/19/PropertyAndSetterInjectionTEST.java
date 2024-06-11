import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.tests.TestFields;
import org.mockito.junit.Runners;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private TestFields testFields;

 @Before
 public void setUp() {
 injection = new PropertyAndSetterInjection();
 testFields = new TestFields();
 }

 @Test
 public void testProcessInjection_FieldAnnotatedWithInjectMocks_InitializesFieldWithInjectedMocks() throws IllegalAccessException, InstantiationException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());
 mockCandidates.add(new AnotherDummyMock());

 FieldInjectedField field = testFields.getClass().getDeclaredField("fieldAnnotatedWithInjectMocks");
 field.setAccessible(true);

 FieldInitializationReport report = injection.initializeInjectMocksField(field, injectMocksFieldOwner);
 Assert.assertNotNull(report);

 Assert.assertNotNull(testFields.getFieldAnnotatedWithInjectMocks());
 Assert.assertEquals(1, mockCandidates.size());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_FieldAnnotatedWithInjectMocks_ThrowsExceptionOnInitializationFailure() throws IllegalAccessException, InstantiationException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());
 mockCandidates.add(new AnotherDummyMock());

 FieldInjectedField field = testFields.getClass().getDeclaredField("fieldAnnotatedWithInjectMocks");
 field.setAccessible(true);

 // Simulate a failure during initialization
 FieldInitializationReport report = new FieldInitializationReport() {{
 setFieldClass(testFields.getClass());
 setFieldInstance(new Object());
 }};

 try {
 injection.initializeInjectMocksField(field, injectMocksFieldOwner);
 } catch (MockitoException e) {
 // Swallow the MockitoException and assert on the underlying cause
 Assert.assertNotNull(report.getCause());
 }
 }

 @Test
 public void testProcessInjection_PassesThroughIfNoSettersOrFieldsAnnotatedWithInjectMocks() {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();

 Field noInjectMocksField = testFields.getClass().getDeclaredField("noInjectMocksField");
 noInjectMocksField.setAccessible(true);

 Assert.assertFalse(injection.processInjection(noInjectMocksField, injectMocksFieldOwner, mockCandidates));
 }

 @Test
 public void testProcessInjection_InjectsMockUsingSetterIfAvailable() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());

 Field fieldToInject = testFields.getClass().getDeclaredField("fieldToInjectUsingSetter");
 fieldToInject.setAccessible(true);
 TestDependency testDependency = new TestDependency();
 testDependency.setName("testDependency");
 testFields.setFieldToInjectUsingSetter(testDependency);

 FieldInitializationReport report = new FieldInitializationReport() {{
 setFieldClass(testFields.getClass());
 setFieldInstance(injectMocksFieldOwner);
 }};

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.processInjection(fieldToInject, injectMocksFieldOwner, mockCandidates));
 Assert.assertSame(testDependency, testFields.getFieldToInjectUsingSetter());
 }

 @Test
 public void testProcessInjection_InjectsMockUsingFieldIfNoSetterAvailable() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());

 Field fieldToInject = testFields.getClass().getDeclaredField("fieldToInjectUsingField");
 fieldToInject.setAccessible(true);

 // Make sure no mock is injected initially
 Assert.assertNull(testFields.getFieldToInjectUsingField());

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.processInjection(fieldToInject, injectMocksFieldOwner, mockCandidates));
 Assert.assertSame(new DummyMock(), testFields.getFieldToInjectUsingField());
 }

 @Test
 public void testProcessInjection_SkipsFieldIfAlreadyInjected() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());
 mockCandidates.add(new AnotherDummyMock());

 Field alreadyInjectedField = testFields.getClass().getDeclaredField("alreadyInjectedField");
 alreadyInjectedField.setAccessible(true);
 TestDependency alreadyInjectedDependency = new TestDependency();
 alreadyInjectedDependency.setName("alreadyInjectedDependency");
 testFields.setAlreadyInjectedField(alreadyInjectedDependency);

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.processInjection(alreadyInjectedField, injectMocksFieldOwner, mockCandidates));
 Assert.assertSame(alreadyInjectedDependency, testFields.getAlreadyInjectedField());
 Assert.assertFalse(injection.processInjection(alreadyInjectedField, injectMocksFieldOwner, mockCandidates));
 }

 @Test
 public void testProcessInjection_SkipsFieldIfNoSettersOrFieldsAvailable() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();

 Field nonInjectableField = testFields.getClass().getDeclaredField("nonInjectableField");
 nonInjectableField.setAccessible(true);

 Assert.assertFalse(injection.processInjection(nonInjectableField, injectMocksFieldOwner, mockCandidates));
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcessInjection_ThrowsExceptionIfAccessingPrivateFieldFails() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();

 Field privateFieldToInject = testFields.getClass().getDeclaredField("privateFieldToInject");
 privateFieldToInject.setAccessible(true);

 Assert.assertThrows(IllegalAccessException.class, () -> injection.processInjection(privateFieldToInject, injectMocksFieldOwner, mockCandidates));
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessInjection_ThrowsExceptionIfNoInjectMocksAnnotatedFields() {
 TestFields testFields = new TestFieldsNoInjectMocksAnnotatedFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();

 Assert.assertThrows(IllegalStateException.class, () -> injection.processInjection(testFields.getNonExistentField(), injectMocksFieldOwner, mockCandidates));
 }

 @Test
 public void testInjectMockCandidates_InjectsMockCandidateUsingType() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 MockDependency mockDependency = new MockDependency();
 mockDependency.setName("mockDependency");
 mockCandidates.add(mockDependency);

 Class<?> awaitingInjectionClazz = TestDependency.class;
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 Assert.assertSame(mockDependency, testFields.getInjectedMockUsingType());
 }

 @Test
 public void testInjectMockCandidates_InjectsMockCandidateUsingName() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 MockDependency mockDependency1 = new MockDependency();
 mockDependency1.setName("mockDependency1");
 MockDependency mockDependency2 = new MockDependency();
 mockDependency2.setName("mockDependency2");
 mockCandidates.add(mockDependency1);
 mockCandidates.add(mockDependency2);

 Class<?> awaitingInjectionClazz = TestDependency.class;
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertEquals(2, mockCandidates.size());
 Assert.assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 Assert.assertSame(mockDependency1, testFields.getInjectedMockUsingName());
 }

 @Test
 public void testInjectMockCandidates_SkipsMockCandidateIfMoreThanOneFound() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 MockDependency mockDependency1 = new MockDependency();
 mockDependency1.setName("mockDependency1");
 MockDependency mockDependency2 = new MockDependency();
 mockDependency2.setName("mockDependency2");
 mockCandidates.add(mockDependency1);
 mockCandidates.add(mockDependency2);

 Class<?> awaitingInjectionClazz = TestDependency.class;
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertEquals(2, mockCandidates.size());
 Assert.assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 }

 @Test(expected = IllegalStateException.class)
 public void testInjectMockCandidates_ThrowsExceptionIfNoMockCandidatesFound() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 Class<?> awaitingInjectionClazz = TestDependency.class;
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 Assert.assertFalse(testFields.isMockInjected());
 }

 @Test
 public void testInjectMockCandidates_RemovesInjectedMockFromMocksToBeInjected() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 MockDependency mockDependency = new MockDependency();
 mockDependency.setName("mockDependency");
 mockCandidates.add(mockDependency);

 Class<?> awaitingInjectionClazz = TestDependency.class;
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 Assert.assertEquals(0, mockCandidates.size());
 }

 @Test
 public void testInjectMockCandidates_RemovesInjectedFieldFromListOfClassFields() throws IllegalAccessException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 MockDependency mockDependency = new MockDependency();
 mockDependency.setName("mockDependency");
 mockCandidates.add(mockDependency);

 Field fieldToInject = testFields.getClass().getDeclaredField("fieldToInjectUsingField");
 fieldToInject.setAccessible(true);

 Class<?> awaitingInjectionClazz = testFields.getClass();
 List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

 Assert.assertEquals(1, mockCandidates.size());
 Assert.assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner));
 Assert.assertFalse(testFields.hasField("fieldToInjectUsingField"));
 }

 @Test(expected = InvocationTargetException.class)
 public void testInitializeInjectMocksField_ThrowsExceptionOnInitializationFailure() throws IllegalAccessException, InstantiationException {
 TestFields testFields = new TestFields();
 Object injectMocksFieldOwner = testFields;
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new DummyMock());

 Field fieldToInject = testFields.getClass().getDeclaredField("fieldToInjectUsingField");
 fieldToInject.setAccessible(true);

 try {
 injection.initializeInjectMocksField(fieldToInject, injectMocksFieldOwner);
 } catch (InvocationTargetException e) {
 // Swallow the InvocationTargetException and assert on the underlying cause
 Assert.assertNotNull(e.getCause());
 }
 }
}

class DummyMock {
}

class AnotherDummyMock {
}

class TestDependency {
 @Inject public TestDependency() {
 }

 @Override
 public String toString() {
 return "TestDependency";
 }
}

class TestFields {
 @InjectMocks
 @TestSubject
 private TestSubject testSubject;
 private TestDependency fieldAnnotatedWithInjectMocks;
 private TestDependency fieldToInjectUsingSetter;
 private TestDependency fieldToInjectUsingField;
 private TestDependency alreadyInjectedField;
 private TestDependency nonInjectableField;
 private boolean mockInjected = false;

 public TestDependency getFieldAnnotatedWithInjectMocks() {
 return fieldAnnotatedWithInjectMocks;
 }

 public TestDependency getFieldToInjectUsingSetter() {
 return fieldToInjectUsingSetter;
 }

 public TestDependency getFieldToInjectUsingField() {
 return fieldToInjectUsingField;
 }

 public TestDependency getAlreadyInjectedField() {
 return alreadyInjectedField;
 }

 public TestDependency getNonExistentField() {
 throw new IllegalFieldException();
 }

 public boolean isMockInjected() {
 return mockInjected;
 }

 public void setFieldToInjectUsingSetter(TestDependency testDependency) {
 this.fieldToInjectUsingSetter = testDependency;
 mockInjected = true;
 }

 public void setFieldToInjectUsingField(TestDependency testDependency) {
 this.fieldToInjectUsingField = testDependency;
 mockInjected = true;
 }

 public void setAlreadyInjectedField(TestDependency testDependency) {
 this.alreadyInjectedField = testDependency;
 mockInjected = true;
 }

 public void setNonExistentField(TestDependency testDependency) {
 // Cannot set a non-existent field
 }
}

class TestFieldsNoInjectMocksAnnotatedFields {
 @TestSubject
 private TestSubject testSubject;

 public void setNonExistentField(TestDependency testDependency) {
 // Cannot set a non-existent field
 }
}