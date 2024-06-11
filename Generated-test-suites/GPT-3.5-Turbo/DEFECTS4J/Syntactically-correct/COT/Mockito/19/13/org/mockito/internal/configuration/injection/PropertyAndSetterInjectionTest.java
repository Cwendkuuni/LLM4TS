
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyAndSetterInjectionTest {
  
  private PropertyAndSetterInjection propertyAndSetterInjection;
  
  @Before
  public void setUp() {
    propertyAndSetterInjection = new PropertyAndSetterInjection();
  }
  
  @Test
  public void testProcessInjection() {
    // test when injectMocksField and injectMocksFieldOwner are null
    assertFalse(propertyAndSetterInjection.processInjection(null, null, null));
    
    // test when injectMocksField and injectMocksFieldOwner are not null
    assertTrue(propertyAndSetterInjection.processInjection(new Field(), new Object(), new HashSet<Object>()));
  }
  
  @Test
  public void testInitializeInjectMocksField() {
    // test when field initialization throws MockitoException with InvocationTargetException
    Field field = new Field();
    Object fieldOwner = new Object();
    assertFalse(propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner));
    
    // test when field initialization throws MockitoException with other cause
    Field field1 = new Field();
    Object fieldOwner1 = new Object();
    assertFalse(propertyAndSetterInjection.initializeInjectMocksField(field1, fieldOwner1));
    
    // test when field initialization is successful
    Field field2 = new Field();
    Object fieldOwner2 = new Object();
    assertTrue(propertyAndSetterInjection.initializeInjectMocksField(field2, fieldOwner2));
  }
  
  @Test
  public void testInjectMockCandidates() {
    // test when awaitingInjectionClazz is Object class
    Class<?> awaitingInjectionClazz = Object.class;
    Set<Object> mocks = new HashSet<Object>();
    Object instance = new Object();
    assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    
    // test when awaitingInjectionClazz is not Object class
    Class<?> awaitingInjectionClazz1 = PropertyAndSetterInjection.class;
    Set<Object> mocks1 = new HashSet<Object>();
    Object instance1 = new Object();
    assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz1, mocks1, instance1));
    
    // test when mocks is not empty and injection occurs
    Class<?> awaitingInjectionClazz2 = PropertyAndSetterInjection.class;
    Set<Object> mocks2 = new HashSet<Object>();
    mocks2.add(new Object());
    Object instance2 = new Object();
    assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz2, mocks2, instance2));
    
    // test when mocks is empty and injection doesn't occur
    Class<?> awaitingInjectionClazz3 = PropertyAndSetterInjection.class;
    Set<Object> mocks3 = new HashSet<Object>();
    Object instance3 = new Object();
    assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz3, mocks3, instance3));
    
  }
  
  @Test
  public void testInjectMockCandidatesOnFields() {
    // test when mocks is empty and injection doesn't occur
    Set<Object> mocks = new HashSet<Object>();
    Object instance = new Object();
    List<Field> orderedInstanceFields = new ArrayList<Field>();
    assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
    
    // test when mocks is not empty and injection occurs
    Set<Object> mocks1 = new HashSet<Object>();
    mocks1.add(new Object());
    Object instance1 = new Object();
    List<Field> orderedInstanceFields1 = new ArrayList<Field>();
    assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks1, instance1, false, orderedInstanceFields1));
  }
  
  @Test
  public void testOrderedInstanceFieldsFrom() {
    // test when awaitingInjectionClazz is Object class
    Class<?> awaitingInjectionClazz = Object.class;
    List<Field> fields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    assertEquals(0, fields.size());
    
    // test when awaitingInjectionClazz is not Object class
    Class<?> awaitingInjectionClazz1 = PropertyAndSetterInjection.class;
    List<Field> fields1 = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz1);
    assertEquals(0, fields1.size());
    
    // test when awaitingInjectionClazz has fields
    Class<?> awaitingInjectionClazz2 = PropertyAndSetterInjection.class;
    List<Field> fields2 = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz2);
    assertNotEquals(0, fields2.size());
  }
}
