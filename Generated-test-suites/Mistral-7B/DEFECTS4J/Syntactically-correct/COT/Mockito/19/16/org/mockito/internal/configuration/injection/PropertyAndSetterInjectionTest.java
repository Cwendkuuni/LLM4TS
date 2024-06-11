import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

 @Test
 public void testProcessInjection_InjectMocksFieldIsInitialized() {
 Field injectMocksField = getField("injectMocksField", MyClassWithInjectMocks.class);
 Object injectMocksFieldOwner = new MyClassWithInjectMocks();
 Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(new MockClass1(), new MockClass2()));
 FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

 // Check if the field is initialized
 Assert.assertNotNull(report.getFieldInstance());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_InitializationThrowsException() {
 Field injectMocksField = getField("injectMocksField", MyClassWithInjectMocks.class);
 Object injectMocksFieldOwner = new MyClassWithInjectMocks();
 Set<Object> mockCandidates = newHashSetWithSize(1);
 mockCandidates.add(new MockClass1());
 propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 }

 @Test
 public void testProcessInjection_NoMockCandidates() {
 Field injectMocksField = getField("injectMocksField", MyClassWithNoMockCandidates.class);
 Object injectMocksFieldOwner = new MyClassWithNoMockCandidates();
 Set<Object> mockCandidates = new HashSet<>();
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessInjection_MultipleMockCandidates() {
 Field injectMocksField = getField("injectMocksField", MyClassWithMultipleMockCandidates.class);
 Object injectMocksFieldOwner = new MyClassWithMultipleMockCandidates();
 Set<Object> mockCandidates = newHashSetWithSize(2);
 mockCandidates.add(new MockClass1());
 mockCandidates.add(new MockClass2());
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_MockCandidateFoundByType() {
 Field injectMocksField = getField("injectMocksField", MyClassWithTypeBasedMockCandidate.class);
 Object injectMocksFieldOwner = new MyClassWithTypeBasedMockCandidate();
 Set<Object> mockCandidates = newHashSetWithSize(1);
 mockCandidates.add(new MockClass1());
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_MockCandidateFoundByName() {
 Field injectMocksField = getField("injectMocksField_mockName", MyClassWithNameBasedMockCandidate.class);
 Object injectMocksFieldOwner = new MyClassWithNameBasedMockCandidate();
 Set<Object> mockCandidates = newHashSetWithSize(1);
 mockCandidates.add(new MockClassWithName("mockName"));
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_NoMockCandidatesInSuperclass() {
 Field injectMocksField = getField("injectMocksField", MyClassWithNoMockCandidatesInSuperclass.class);
 Object injectMocksFieldOwner = new MyClassWithNoMockCandidatesInSuperclass();
 Set<Object> mockCandidates = new HashSet<>();
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessInjection_MultipleMockCandidatesInSuperclass() {
 Field injectMocksField = getField("injectMocksField", MyClassWithMultipleMockCandidatesInSuperclass.class);
 Object injectMocksFieldOwner = new MyClassWithMultipleMockCandidatesInSuperclass();
 Set<Object> mockCandidates = newHashSetWithSize(2);
 mockCandidates.add(new MockClass1());
 mockCandidates.add(new MockClass2());
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_MockCandidateInjectedByPropertySetter() {
 Field injectMocksField = getField("injectMocksField", MyClassWithMockCandidateInjectedByPropertySetter.class);
 Object injectMocksFieldOwner = new MyClassWithMockCandidateInjectedByPropertySetter();
 Set<Object> mockCandidates = newHashSetWithSize(1);
 mockCandidates.add(new MockClass1());
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_MockCandidateInjectedByField() {
 Field injectMocksField = getField("injectMocksField", MyClassWithMockCandidateInjectedByField.class);
 Object injectMocksFieldOwner = new MyClassWithMockCandidateInjectedByField();
 Set<Object> mockCandidates = newHashSetWithSize(1);
 mockCandidates.add(new MockClass1());
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 private Field getField(String fieldName, Class<?> clazz) {
 try {
 return clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private HashSet<Object> newHashSetWithSize(int size) {
 return new HashSet<>(size);
 }

}