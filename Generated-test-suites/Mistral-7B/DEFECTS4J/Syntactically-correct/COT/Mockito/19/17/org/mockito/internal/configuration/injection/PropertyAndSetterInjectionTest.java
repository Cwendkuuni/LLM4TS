import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidate;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.util.FieldAccessor;
import org.mockito.internal.util.reflection.util.FieldUtil;
import org.mockito.internal.util.reflection.util.ReflectionUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Class<DummyClass> dummyClass;
 private Object dummyInstance;
 private List<MockCandidate> mockCandidates;
 private Set<Object> mocks;
 private Field fieldToBeInjected;
 private Field fieldToInject;
 private Object mock1;
 private Object mock2;
 private Object realInstance;

 @Before
 public void setUp() {
 injection = new PropertyAndSetterInjection();
 dummyClass = DummyClass.class;
 dummyInstance = new DummyClass();
 mockCandidates = new ArrayList<>();
 mock1 = mock(DummyClassA.class);
 mock2 = mock(DummyClassB.class);
 mocks = newMockSafeHashSet(mock1, mock2);
 fieldToBeInjected = mock(Field.class);
 fieldToInject = mock(Field.class);
 realInstance = new DummyRealClass();
 doReturn(realInstance).when(dummyInstance).newInstance();
 }

 @Test
 public void testProcessInjection_noMockCandidates() {
 assertFalse(injection.processInjection(fieldToBeInjected, dummyInstance, mocks));
 }

 @Test
 public void testProcessInjection_oneMockCandidateByType() {
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 assertEquals(mock1, injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test
 public void testProcessInjection_oneMockCandidateByName() {
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock1);
 assertEquals(mock1, injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_failedInitialization() {
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(new FieldInitializer(dummyInstance, fieldToInject).initialize()).thenThrow(new InvocationTargetException(new RuntimeException("Initialization failed")));
 assertNull(injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test
 public void testProcessInjection_multipleMockCandidatesByType() {
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock1);
 assertEquals(mock1, injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test
 public void testProcessInjection_multipleMockCandidatesByName() {
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock2);
 assertEquals(mock2, injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test
 public void testProcessInjection_processNextClass() {
 Class<DummySubClass> subClass = DummySubClass.class;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(subClass);
 when(fieldToInject.getType()).thenReturn(subClass);
 doReturn(new DummySubClass()).when(dummyInstance).newInstance();
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock1);
 injection.injectMockCandidates(subClass, mocks, dummyInstance);
 assertEquals(mock1, injection.processInjection(fieldToBeInjected, dummyInstance, mocks).thenReturn());
 }

 @Test
 public void testInjectMockCandidatesOnFields_noMockCandidates() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 injectionOccurred(false);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_oneMockCandidateByType() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 Answer answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) {
 injectionOccurred(true);
 return mock1;
 }
 };
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock1);
 when(fieldToInject.get(dummyInstance)).thenAnswer(answer);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertEquals(1, mocks.size());
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_oneMockCandidateByName() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock2);
 when(fieldToInject.getName()).thenReturn("fieldName");
 Answer answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) {
 injectionOccurred(true);
 return mock2;
 }
 };
 when(fieldToInject.get(dummyInstance)).thenAnswer(answer);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertEquals(1, mocks.size());
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_noMockCandidatesForField() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 injectionOccurred(false);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_multipleMockCandidatesByType() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock1);
 Answer answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) {
 injectionOccurred(true);
 return mock1;
 }
 };
 when(fieldToInject.get(dummyInstance)).thenAnswer(answer);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertEquals(1, mocks.size());
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_multipleMockCandidatesByName() {
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(dummyClass);
 List<Field> fieldsToTest = new ArrayList<>();
 fieldsToTest.add(fieldToBeInjected);
 fieldsToTest.addAll(orderedInstanceFields);
 mockCandidates.clear();
 mockCandidates.add(new MockCandidate(mock1, fieldToInject));
 mockCandidates.add(new MockCandidate(mock2, fieldToInject));
 when(fieldToInject.getType()).thenReturn(DummyClassA.class);
 when(mockCandidateFilter.filterCandidate(eq(mocks), eq(fieldToInject), eq(dummyInstance))).thenReturn(mock2);
 when(fieldToInject.getName()).thenReturn("fieldName");
 Answer answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) {
 injectionOccurred(true);
 return mock2;
 }
 };
 when(fieldToInject.get(dummyInstance)).thenAnswer(answer);
 injection.injectMockCandidatesOnFields(mocks, dummyInstance, injectionOccurred, fieldsToTest);
 assertEquals(1, mocks.size());
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInitializeInjectMocksField_success() {
 Field field = mock(Field.class);
 Object instance = mock(Object.class);
 when(field.getName()).thenReturn("fieldName");
 when(field.getType()).thenReturn(DummyClassA.class);
 when(dummyInstance.newInstance()).thenReturn(new DummyClassA());
 FieldInitializationReport report = new FieldInitializer(dummyInstance, field).initialize();
 assertSame(DummyClassA.class, report.fieldClass());
 assertSame(new DummyClassA(), report.fieldInstance());
 }

 @Test(expected = InvocationTargetException.class)
 public void testInitializeInjectMocksField_exception() {
 Field field = mock(Field.class);
 Object instance = mock(Object.class);
 when(field.getName()).thenReturn("fieldName");
 when(field.getType()).thenReturn(DummyClassA.class);
 when(dummyInstance.newInstance()).thenReturn(new DummyClassA());
 when(new FieldInitializer(dummyInstance, field).initialize()).thenThrow(new RuntimeException("Initialization failed"));
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 List<Field> fields = new ArrayList<>();
 fields.add(new Field() {{ setAccessible(true); setName("field3"); setType(DummyClassC.class); }});
 fields.add(new Field() {{ setAccessible(true); setName("field1"); setType(DummyClassA.class); }});
 fields.add(new Field() {{ setAccessible(true); setName("field2"); setType(DummyClassB.class); }});
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(dummyClass);
 List<Field> expectedOrderedInstanceFields = asList(
         new Field() {{ setAccessible(true); setName("field1"); setType(DummyClassA.class); }},
         new Field() {{ setAccessible(true); setName("field2"); setType(DummyClassB.class); }},
         new Field() {{ setAccessible(true); setName("field3"); setType(DummyClassC.class); }}
 );
 assertEquals(expectedOrderedInstanceFields, orderedInstanceFields);
 }
}