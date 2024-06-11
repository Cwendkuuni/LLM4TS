import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setUp() {
    recordType = new RecordType();
  }
  
  @Test
  public void testDefineProperty() {
    // Test when property name is valid and type is valid.
    assertTrue(recordType.defineProperty("property1", JSTypeRegistry.getType("Type1"), false, null));
    
    // Test when property name is empty.
    assertFalse(recordType.defineProperty("", JSTypeRegistry.getType("Type1"), true, null));
    
    // Test when property type is null.
    assertFalse(recordType.defineProperty("property2", null, false, null));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType typeA = new RecordType();
    typeA.defineProperty("property1", JSTypeRegistry.getType("Type1"), false, null);
    
    RecordType typeB = new RecordType();
    typeB.defineProperty("property1", JSTypeRegistry.getType("Type1"), false, null);
    
    RecordType typeC = new RecordType();
    typeC.defineProperty("property2", JSTypeRegistry.getType("Type2"), false, null);
    
    // Test when typeA has all the properties declared in typeB and their types are equal.
    assertTrue(typeA.isSubtype(typeB));
    
    // Test when typeA has all the properties declared in typeB and their types are not equal.
    assertFalse(typeA.isSubtype(typeC));
    
    // Test when typeA does not have all the properties declared in typeB.
    assertFalse(typeA.isSubtype(typeB));
  }
  
  @Test
  public void testResolveInternal() {
    // Test when all properties have been resolved.
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    assertEquals(JSTypeNative.NO_TYPE, recordType.resolveInternal(errorReporter, scope));
    
    // Test when some properties have been resolved.
    recordType.defineProperty("property1", null, false, null);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.resolveInternal(errorReporter, scope));
    
    // Test when no properties have been resolved.
    recordType = new RecordType();
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, recordType.resolveInternal(errorReporter, scope));
  }
}