import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.collections.Sets;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.mockito.internal.util.reflection.ReflectionUtils.getFieldValue;
import static org.mockito.internal.util.reflection.ReflectionUtils.setFieldValue;
import static org.junit.Assert.*;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Object objectUnderTest;

 @Before
 public void setUp() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 objectUnderTest = new TestClass();
 }

 @Test
 public void testProcessInjection() throws Exception {
 Field injectMocksField = objectUnderTest.getClass().getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new TestMock1());
 mockCandidates.add(new TestMock2());

 FieldInitializationReport report = new FieldInitializer(objectUnderTest, injectMocksField).initialize();
 assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates));
 assertNotNull(getFieldValue(objectUnderTest, "injectedMock1"));
 assertNull(getFieldValue(objectUnderTest, "injectedMock2"));
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithException() throws Exception {
 Field injectMocksField = objectUnderTest.getClass().getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(new TestMock1());
 mockCandidates.add(new TestMock2());

 FieldInitializationReport report = new FieldInitializer(objectUnderTest, injectMocksField).initialize();
 report.setException(new IllegalStateException("Test Exception"));
 propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates);
 fail("No exception was thrown");
 }

 @Test
 public void testInitializeInjectMocksField() throws Exception {
 Field injectMocksField = objectUnderTest.getClass().getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 Object fieldInstanceNeedingInjection = getFieldValue(objectUnderTest, "injectMocksField");
 Class<?> fieldClass = injectMocksField.getType();

 FieldInitializationReport report = new FieldInitializationReport();
 report.setFieldClass(fieldClass);
 report.setFieldInstance(fieldInstanceNeedingInjection);

 FieldInitializer fieldInitializer = new FieldInitializer(objectUnderTest, injectMocksField);
 report = fieldInitializer.initialize();
 assertNotNull(report);
 }

 @Test
 public void testInjectMockCandidates() throws Exception {
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(new TestMock1());
 mocks.add(new TestMock2());
 Object instance = new TestClass();

 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
 assertTrue(injectionOccurred);
 assertNotNull(getFieldValue(instance, "injectedMock1"));
 assertNull(getFieldValue(instance, "injectedMock2"));
 }

 @Test
 public void testInjectMockCandidatesOnFields() throws Exception {
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(new TestMock1());
 mocks.add(new TestMock2());
 Object instance = new TestClass();
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
 assertTrue(injectionOccurred);
 assertNotNull(getFieldValue(instance, "injectedMock1"));
 assertNull(getFieldValue(instance, "injectedMock2"));
 }

 @Test
 public void testInjectMockCandidatesOnFieldsPass2() throws Exception {
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(new TestMock1());
 mocks.add(new TestMock2());
 Object instance = new TestClass();
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, true, orderedInstanceFields);
 assertTrue(injectionOccurred);
 assertNotNull(getFieldValue(instance, "injectedMock1"));
 assertNull(getFieldValue(instance, "injectedMock2"));
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithNoInjectedMock() throws Exception {
 Class<?> awaitingInjectionClazz = TestClass.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(new TestMock1());
 mocks.add(new TestMock2());
 Object instance = new TestClass();
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
 assertNull(getFieldValue(instance, "injectedMock1"));
 assertNull(getFieldValue(instance, "injectedMock2"));
 }

 private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
 List<Field> fields = new ArrayList<>();
 for (Field field : awaitingInjectionClazz.getDeclaredFields()) {
 if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
 fields.add(field);
 }
 }
 return new SuperTypesLastSorter().sort(fields);
 }

 static class TestClass {
 @InjectMocks
 private Object injectMocksField;
 private Object injectedMock1;
 private Object injectedMock2;
 }

 static class TestMock1 {}
 static class TestMock2 {}
}