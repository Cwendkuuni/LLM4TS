
import static org.mockito.Mockito.*;
import org.junit.Test;

public class PropertyAndSetterInjectionTest {
  
  //Test processInjection method
  
  @Test
  public void testProcessInjection_InjectMocksFieldInitialized_FieldInstanceInjected() {
    // prepare
    Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    Object injectMocksFieldOwner = new Object();
    Set<Object> mockCandidates = new HashSet<>();
    mockCandidates.add(mock(Object.class));
    
    // act
    boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    
    // assert
    assertTrue(result);
  }
  
  @Test(expected = NullPointerException.class)
  public void testProcessInjection_InjectMocksFieldNotInitialized_ExceptionThrown() {
    // prepare
    Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    Object injectMocksFieldOwner = new Object();
    Set<Object> mockCandidates = new HashSet<>();
    
    // act
    propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
  }
  
  // Test initializeInjectMocksField method
  
  @Test
  public void testInitializeInjectMocksField_FieldInitialized_ReportReturned() throws NoSuchFieldException {
    // prepare
    Field field = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
    Object fieldOwner = new Object();
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
    
    // assert
    assertNotNull(report);
    assertNotNull(report.fieldClass());
    assertNotNull(report.fieldInstance());
  }
  
  @Test(expected = NullPointerException.class)
  public void testInitializeInjectMocksField_FieldNotInitialized_ExceptionThrown() throws NoSuchFieldException {
    // prepare
    Field field = null;
    Object fieldOwner = new Object();
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
  }
  
  // Test injectMockCandidates method
  
  @Test
  public void testInjectMockCandidates_InjectMocksFieldInstanceInjected_MocksUpdated() {
    // prepare
    Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
    Set<Object> mocks = new HashSet<>();
    mocks.add(mock(Object.class));
    Object instance = new Object();
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    
    // assert
    assertTrue(result);
    assertTrue(mocks.isEmpty());
  }
  
  @Test
  public void testInjectMockCandidates_InjectMocksFieldInstanceNotInjected_MocksNotUpdated() {
    // prepare
    Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
    Set<Object> mocks = new HashSet<>();
    mocks.add(mock(Object.class));
    Object instance = null;
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    
    // assert
    assertFalse(result);
    assertFalse(mocks.isEmpty());
  }
  
  // Test injectMockCandidatesOnFields method
  
  @Test
  public void testInjectMockCandidatesOnFields_MocksInjectedOnFields_MocksUpdated() throws NoSuchFieldException {
    // prepare
    Set<Object> mocks = new HashSet<>();
    mocks.add(mock(Object.class));
    Object instance = new Object();
    boolean injectionOccurred = false;
    List<Field> orderedInstanceFields = new ArrayList<>();
    Field field1 = instance.getClass().getDeclaredField("field1");
    orderedInstanceFields.add(field1);
    Field field2 = instance.getClass().getDeclaredField("field2");
    orderedInstanceFields.add(field2);
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
    
    // assert
    assertTrue(result);
    assertTrue(mocks.isEmpty());
    assertFalse(orderedInstanceFields.contains(field1));
    assertFalse(orderedInstanceFields.contains(field2));
  }
  
  @Test
  public void testInjectMockCandidatesOnFields_MocksNotInjectedOnFields_MocksNotUpdated() throws NoSuchFieldException {
    // prepare
    Set<Object> mocks = new HashSet<>();
    mocks.add(mock(Object.class));
    Object instance = new Object();
    boolean injectionOccurred = false;
    List<Field> orderedInstanceFields = new ArrayList<>();
    Field field1 = instance.getClass().getDeclaredField("field1");
    orderedInstanceFields.add(field1);
    Field field2 = instance.getClass().getDeclaredField("field2");
    orderedInstanceFields.add(field2);
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
    
    // assert
    assertFalse(result);
    assertFalse(mocks.isEmpty());
    assertTrue(orderedInstanceFields.contains(field1));
    assertTrue(orderedInstanceFields.contains(field2));
  }
  
  // Test orderedInstanceFieldsFrom method
  
  @Test
  public void testOrderedInstanceFieldsFrom_OrderedFieldsReturned() {
    // prepare
    Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
    PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
    
    // act
    List<Field> orderedFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    
    // assert
    assertEquals(awaitingInjectionClazz.getDeclaredFields().length, orderedFields.size());
    for (int i = 0; i < orderedFields.size() - 1; i++) {
      Field field1 = orderedFields.get(i);
      Field field2 = orderedFields.get(i + 1);
      assertTrue(field1.getName().compareTo(field2.getName()) < 0);
    }
  }
}

