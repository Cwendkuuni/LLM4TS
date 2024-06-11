import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.testutils.FieldTestUtils;
import org.mockito.internal.util.reflection.testutils.ObjectTestUtils;
import org.mockito.internal.util.reflection.testutils.TypeTestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Object testObject;

 @Before
 public void setUp() {
 injection = new PropertyAndSetterInjection();
 testObject = mock(TestObject.class);
 }

 @Test
 public void testProcessInjection_InitializeInjectMocksField_Success() {
 Field injectMocksField = FieldTestUtils.getField(TestObject.class, "injectMocksField");
 Object injectMocksFieldOwner = spy(TestObject.class);
 Set<Object> mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));

 FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 assertNotNull(report);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_InitializeInjectMocksField_Exception() {
 Field injectMocksField = FieldTestUtils.getField(TestObject.class, "injectMocksField");
 Object injectMocksFieldOwner = new TestObject();
 Set<Object> mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));

 injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 }

 @Test
 public void testProcessInjection_InjectMockCandidates() {
 Class<?> awaitingInjectionClazz = TestObject.class;
 Set<Object> mocks = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));
 Object instance = new TestObject();

 boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
 assertFalse(injectionOccurred);

 injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
 assertFalse(injectionOccurred);

 injection = spy(PropertyAndSetterInjection.class);
 injection.mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
 injection.notFinalOrStatic = new ListUtil.Filter<Field>() {
 @Override
 public boolean isOut(Field object) {
 return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
 }
 };

 Field injectMocksField = FieldTestUtils.getField(TestObject.class, "injectMocksField");
 Object injectMocksFieldOwner = new TestObject();
 Set<Object> mockCandidatesSpy = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));

 FieldInitializationReport report = new FieldInitializationReport(null, injectMocksField.getType());
 report.setFieldClass(injectMocksField.getType());
 report.setFieldInstance(injectMocksFieldOwner);

 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidatesSpy);

 assertFalse(mockCandidatesSpy.contains(injectMocksFieldOwner.injectMocksField));
 assertFalse(injectMocksFieldOwner.getClass().getDeclaredField("injectMocksField").isAccessible(injectMocksFieldOwner));

 Field injectedField = FieldTestUtils.getField(Candidate1.class, "injectedField");
 Candidate1 injectedCandidate = (Candidate1) injectedField.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate);
 assertNull(injectedCandidate.injectedField);

 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidatesSpy);

 injectedCandidate = (Candidate1) injectedField.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate);
 assertNotNull(injectedCandidate.injectedField);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_InjectMockCandidates_Exception() {
 Class<?> awaitingInjectionClazz = TestObject.class;
 Set<Object> mocks = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));
 Object instance = new TestObject();

 injection.processInjection(null, null, mocks);
 }

 @Test
 public void testInjectMockCandidatesOnFields() {
 Class<?> awaitingInjectionClazz = TestObject.class;
 Set<Object> mocks = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));
 Object instance = new TestObject();
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 boolean injectionOccurred = injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
 assertFalse(injectionOccurred);

 injectionOccurred = injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
 assertFalse(injectionOccurred);

 injection = spy(PropertyAndSetterInjection.class);
 injection.mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
 injection.notFinalOrStatic = new ListUtil.Filter<Field>() {
 @Override
 public boolean isOut(Field object) {
 return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
 }
 };

 Field injectMocksField = FieldTestUtils.getField(TestObject.class, "injectMocksField");
 Object injectMocksFieldOwner = new TestObject();
 Set<Object> mockCandidatesSpy = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));

 FieldInitializationReport report = new FieldInitializationReport(null, injectMocksField.getType());
 report.setFieldClass(injectMocksField.getType());
 report.setFieldInstance(injectMocksFieldOwner);

 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidatesSpy);

 assertFalse(mockCandidatesSpy.contains(injectMocksFieldOwner.injectMocksField));
 assertFalse(injectMocksFieldOwner.getClass().getDeclaredField("injectMocksField").isAccessible(injectMocksFieldOwner));

 Field injectedField1 = FieldTestUtils.getField(Candidate1.class, "injectedField");
 Candidate1 injectedCandidate1 = (Candidate1) injectedField1.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate1);
 assertNull(injectedCandidate1.injectedField);

 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidatesSpy);

 injectedCandidate1 = (Candidate1) injectedField1.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate1);
 assertNotNull(injectedCandidate1.injectedField);

 Field injectedField2 = FieldTestUtils.getField(Candidate2.class, "injectedField");
 Candidate2 injectedCandidate2 = (Candidate2) injectedField2.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate2);
 assertNull(injectedCandidate2.injectedField);

 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidatesSpy);

 injectedCandidate2 = (Candidate2) injectedField2.get(injectMocksFieldOwner.injectMocksField);
 assertNotNull(injectedCandidate2);
 assertNotNull(injectedCandidate2.injectedField);
 }

 @Test
 public void testInjectMockCandidatesOnFields_NoCandidates() {
 Class<?> awaitingInjectionClazz = TestObject.class;
 Set<Object> mocks = Sets.newMockSafeHashSet(Arrays.asList(mock(Candidate1.class), mock(Candidate2.class)));
 Object instance = new TestObject();
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 boolean injectionOccurred = injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
 assertFalse(injectionOccurred);
 }

 private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
 List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
 declaredFields = ListUtil.filter(declaredFields, new ListUtil.Filter<Field>() {
 @Override
 public boolean isOut(Field object) {
 return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
 }
 });

 return new SuperTypesLastSorter().sort(declaredFields);
 }
}

class TestObject {
 @InjectMocks
 private Candidate1 injectMocksField;

 private static class Candidate1 {
 @Inject
 private Object injectedField;
 }

 private static class Candidate2 {
 @Inject
 private Object injectedField;
 }
}