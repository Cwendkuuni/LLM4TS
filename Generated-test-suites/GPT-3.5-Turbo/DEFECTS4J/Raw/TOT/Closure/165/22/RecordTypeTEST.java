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
    // Typical use case - defining a property with a type and inferred as false
    boolean result = recordType.defineProperty("property", JSTypeRegistry.getPrimitiveType("int"), false, null);
    
    assertTrue(result);
    assertTrue(recordType.hasProperty("property"));
    assertEquals(JSTypeRegistry.getPrimitiveType("int"), recordType.getPropertyType("property"));
  }
  
  @Test
  public void testDefineProperty_InferrdTrue() {
    // Typical use case - defining a property with a type and inferred as true
    boolean result = recordType.defineProperty("property", JSTypeRegistry.getPrimitiveType("boolean"), true, null);
    
    assertFalse(result);
    assertTrue(recordType.hasProperty("property"));
    assertEquals(JSTypeRegistry.getPrimitiveType("boolean"), recordType.getPropertyType("property"));
  }
  
  @Test
  public void testDefineProperty_NullPropertyNode() {
    // Error scenario - defining a property with a null property node
    boolean result = recordType.defineProperty("property", JSTypeRegistry.getPrimitiveType("string"), false, null);
    
    assertTrue(result);
    assertTrue(recordType.hasProperty("property"));
    assertEquals(JSTypeRegistry.getPrimitiveType("string"), recordType.getPropertyType("property"));
  }
  
  @Test
  public void testDefineProperty_FrozenRecordType() {
    // Error scenario - defining a property for a frozen record type
    recordType.setFrozen(true);
    
    boolean result = recordType.defineProperty("property", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    assertFalse(result);
    assertFalse(recordType.hasProperty("property"));
    assertNull(recordType.getPropertyType("property"));
  }
  
  @Test
  public void testIsSubtype_SameProperties() {
    // Edge case - record type with the same properties
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property1", JSTypeRegistry.getPrimitiveType("int"), false, null);
    otherRecordType.defineProperty("property2", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    boolean result = recordType.isSubtype(otherRecordType);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_SubsetOfProperties() {
    // Edge case - record type is a subset of properties of the other record type
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property1", JSTypeRegistry.getPrimitiveType("int"), false, null);
    otherRecordType.defineProperty("property2", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    boolean result = otherRecordType.isSubtype(recordType);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtype_SubtypeOfProperties() {
    // Edge case - record type is a subtype of properties of the other record type
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property1", JSTypeRegistry.getPrimitiveType("int"), false, null);
    
    boolean result = recordType.isSubtype(otherRecordType);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtype_DifferentProperties() {
    // Edge case - record type has different properties than the other record type
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property3", JSTypeRegistry.getPrimitiveType("int"), false, null);
    otherRecordType.defineProperty("property4", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    boolean result = recordType.isSubtype(otherRecordType);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtype_OtherTypeIsObject() {
    // Edge case - the other type is OBJECT_TYPE (top of the record types)
    ObjectType objectType = new ObjectType();
    
    boolean result = recordType.isSubtype(objectType);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEquivalentTo_EquivalentType() {
    // Typical use case - record types are equivalent
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property1", JSTypeRegistry.getPrimitiveType("int"), false, null);
    otherRecordType.defineProperty("property2", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    boolean result = recordType.isEquivalentTo(otherRecordType);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEquivalentTo_DifferentProperties() {
    // Typical use case - record types have different properties
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property3", JSTypeRegistry.getPrimitiveType("int"), false, null);
    otherRecordType.defineProperty("property4", JSTypeRegistry.getPrimitiveType("boolean"), false, null);
    
    boolean result = recordType.isEquivalentTo(otherRecordType);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsEquivalentTo_DifferentType() {
    // Typical use case - record types are not equivalent
    ObjectType objectType = new ObjectType();
    
    boolean result = recordType.isEquivalentTo(objectType);
    
    assertFalse(result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_BothTypeAreRecordTypes() {
    // Typical use case - both types are record types
    RecordType otherRecordType = new RecordType();
    otherRecordType.defineProperty("property1", JSTypeRegistry.getPrimitiveType("int"), false, null);
    
    JSType result = recordType.getGreatestSubtypeHelper(otherRecordType);
    
    assertTrue(result.isRecordType());
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_OtherTypeIsObject() {
    // Typical use case - the other type is OBJECT_TYPE (top of record types)
    ObjectType objectType = new ObjectType();
    
    JSType result = recordType.getGreatestSubtypeHelper(objectType);
    
    assertTrue(result.isRecordType());
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_OtherTypeIsNoObjectType() {
    // Error scenario - the other type is NO_OBJECT_TYPE (bottom of record types)
    ObjectType noObjectType = new ObjectType();
    noObjectType.setName("NO_OBJECT_TYPE");
    
    JSType result = recordType.getGreatestSubtypeHelper(noObjectType);
    
    assertFalse(result.isRecordType());
  }
}