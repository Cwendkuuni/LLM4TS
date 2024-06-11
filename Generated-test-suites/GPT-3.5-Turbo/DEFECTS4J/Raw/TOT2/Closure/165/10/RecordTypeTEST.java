import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
  
  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, properties);
    
    assertTrue(recordType.defineProperty("property1", JSType.STRING_TYPE, false, null));
    assertFalse(recordType.defineProperty("property1", JSType.NUMBER_TYPE, false, null));
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSType.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertFalse(recordType1.isEquivalentTo(null));
    assertFalse(recordType1.isEquivalentTo(JSType.STRING_TYPE));
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSType.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertTrue(recordType2.isSubtype(recordType1));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(registry, properties);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSType.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(JSType.STRING_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(null));
    assertEquals(recordType2, recordType1.getGreatestSubtypeHelper(recordType2));
  }
  
  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(registry, properties);
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
  }
}