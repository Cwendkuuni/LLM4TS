import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.collections.ListUtil.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.ReflectionUtils.newInstance;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Object testObject;
 private Field testField;
 private Field testAnotherField;
 private List<Field> testFields;
 private Object mockCandidate1;
 private Object mockCandidate2;
 private Object mockCandidate3;
 private Set<Object> mockCandidates;

 @Before
 public void setUp() throws Exception {
 injection = new PropertyAndSetterInjection();
 testObject = new TestObject();
 testField = new TestField();
 testField.setAccessible(true);
 testField.set(testObject, new Object());
 testAnotherField = new TestAnotherField();
 testAnotherField.setAccessible(true);
 testAnotherField.set(testObject, new Object());
 testFields = Arrays.asList(testField, testAnotherField);
 mockCandidate1 = new MockCandidate1();
 mockCandidate2 = new MockCandidate2();
 mockCandidate3 = new MockCandidate3();
 mockCandidates = newMockSafeHashSet(Arrays.asList(mockCandidate1, mockCandidate2, mockCandidate3));
 }

 @Test
 public void testProcessInjection() throws Exception {
 FieldInjectMocksAnnotation fieldAnnotation = new FieldInjectMocksAnnotation();
 testField.set(testObject, fieldAnnotation);
 assertFalse(injection.processInjection(testField, testObject, mockCandidates));
 assertNull(testField.get(testObject));

 FieldInjectMocksAnnotation anotherFieldAnnotation = new FieldInjectMocksAnnotation();
 testAnotherField.set(testObject, anotherFieldAnnotation);
 assertFalse(injection.processInjection(testAnotherField, testObject, mockCandidates));
 assertNull(testAnotherField.get(testObject));
 }

 @Test
 public void testInitializeInjectMocksField() throws Exception {
 Object instance = newInstance(testObject.getClass());
 testField.set(instance, new Object());
 testField.setAccessible(true);
 FieldInitializationReport report = injection.initializeInjectMocksField(testField, instance);
 assertNull(report.fieldInstance());
 }

 @Test(expected = MockitoException.class)
 public void testInitializeInjectMocksFieldWithException() throws Exception {
 Object instance = newInstance(testObject.getClass());
 testField.setAccessible(true);
 testField.set(instance, new Object());
 new FieldInitializer(instance, testField).initialize();
 }

 @Test
 public void testInjectMockCandidates() throws Exception {
 Class<?> awaitingInjectionClazz = TestClassWithFields.class;
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFieldsPass1() throws Exception {
 mockCandidates.clear();
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 testFields = Arrays.asList(testField, testAnotherField);
 boolean injectionOccurred = injection.injectMockCandidatesOnFields(mockCandidates, testObject, false, testFields);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFieldsPass2() throws Exception {
 mockCandidates.clear();
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 testFields = Arrays.asList(testField, testAnotherField);
 boolean injectionOccurred = injection.injectMockCandidatesOnFields(mockCandidates, testObject, true, testFields);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidateOnFieldWithType() throws Exception {
 mockCandidates.clear();
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 testFields = Arrays.asList(testField, testAnotherField);
 testField.set(testObject, new TestFieldWithMockCandidateType());
 testField.setAccessible(true);
 Object injected = new MockitoCandidateFilter().filterCandidate(mockCandidates, testField, testObject).thenInject();
 assertTrue(injected == mockCandidate1);
 }

 @Test
 public void testInjectMockCandidateOnFieldWithName() throws Exception {
 mockCandidates.clear();
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 testFields = Arrays.asList(testField, testAnotherField);
 testField.set(testObject, new TestFieldWithSameNameAsMockCandidate());
 testField.setAccessible(true);
 Object injected = new MockitoCandidateFilter().filterCandidate(mockCandidates, testField, testObject).thenInject();
 assertTrue(injected == mockCandidate2);
 }

 @Test
 public void testInjectMockCandidateOnFieldWithTypeAndName() throws Exception {
 mockCandidates.clear();
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);
 mockCandidates.add(mockCandidate3);
 testFields = Arrays.asList(testField, testAnotherField);
 testField.set(testObject, new TestFieldWithMockCandidateTypeAndName());
 testField.setAccessible(true);
 Object injected = new MockitoCandidateFilter().filterCandidate(mockCandidates, testField, testObject).thenInject();
 assertTrue(injected == mockCandidate3);
 }
}

class TestObject {
 @InjectMocks
 private Object field;
}

class TestClassWithFields {
 // Contains fields annotated with @InjectMocks
}

class TestField {
 private Object fieldInstance;
}

class TestFieldWithMockCandidateType extends TestField {
 // Mock candidate type
}

class TestFieldWithSameNameAsMockCandidate extends TestField {
 // Mock candidate with the same name as test field
}

class TestFieldWithMockCandidateTypeAndName extends TestField {
 // Mock candidate type and name
}

class MockCandidate1 {}

class MockCandidate2 {}

class MockCandidate3 {}

class MockitoCandidateFilter extends Mockito.internal.configuration.injection.filter.MockCandidateFilter {
}