import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private RecordType recordType;
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    
    properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testDefineProperty() {
    recordType.defineProperty("d", JSTypeNative.STRING_TYPE, false, null);
    assertTrue(recordType.hasProperty("d"));
    assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("d"));
  }

  @Test
  public void testDefinePropertyWithInferredFalse() {
    recordType.defineProperty("d", JSTypeNative.STRING_TYPE, true, null);
    assertFalse(recordType.hasProperty("d"));
    assertNull(recordType.getPropertyType("d"));
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType otherRecordType = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(otherRecordType));
  }
  
  @Test
  public void testIsEquivalentToDifferentProperties() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    otherProperties.put("d", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType otherRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.isEquivalentTo(otherRecordType));
  }
  
  @Test
  public void testIsEquivalentToDifferentPropertyTypes() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType otherRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.isEquivalentTo(otherRecordType));
  }
  
  @Test
  public void testIsEquivalentToDifferentPropertyNumber() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    otherProperties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    otherProperties.put("d", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType otherRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.isEquivalentTo(otherRecordType));
  }
  
  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType thatRecordType = new RecordType(registry, properties);
    assertTrue(recordType.getGreatestSubtypeHelper(thatRecordType).isEquivalentTo(recordType));
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithDifferentProperties() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType thatRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.getGreatestSubtypeHelper(thatRecordType).isEquivalentTo(recordType));
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithDifferentPropertyTypes() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    otherProperties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType thatRecordType = new RecordType(registry, otherProperties);
    assertTrue(recordType.getGreatestSubtypeHelper(thatRecordType).isEquivalentTo(recordType));
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithDifferentPropertyNumber() {
    Map<String, RecordProperty> otherProperties = new HashMap<String, RecordProperty>();
    otherProperties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    otherProperties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    otherProperties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    otherProperties.put("d", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    
    RecordType thatRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.getGreatestSubtypeHelper(thatRecordType).isEquivalentTo(recordType));
  }
  
  @Test
  public void testToMaybeRecordType() {
    assertEquals(recordType, recordType.toMaybeRecordType());
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
  }
}