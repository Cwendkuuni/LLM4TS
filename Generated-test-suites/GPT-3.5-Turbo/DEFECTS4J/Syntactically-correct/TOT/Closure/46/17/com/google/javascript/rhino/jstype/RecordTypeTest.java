import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType(null, null);
  }

  @Test
  public void testIsEquivalentTo_sameRecordType_returnsTrue() {
    RecordType other = new RecordType(null, null);
    
    boolean result = recordType.isEquivalentTo(other);
    
    assertTrue(result);
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_returnsFalse() {
    RecordType other = new RecordType(null, null);
    other.defineProperty("property", new JSType(), false, null);
    
    boolean result = recordType.isEquivalentTo(other);
    
    assertFalse(result);
  }

  @Test
  public void testGetImplicitPrototype_returnsNativeObjectType() {
    ObjectType prototype = recordType.getImplicitPrototype();
    
    assertEquals(JSTypeNative.OBJECT_TYPE, prototype.getDisplayName());
  }

  @Test
  public void testDefineProperty_notFrozenAndNotInferred_returnsTrue() {
    boolean result = recordType.defineProperty("property", new JSType(),
        false, null);
    
    assertTrue(result);
  }

  @Test
  public void testDefineProperty_frozen_returnsFalse() {
    recordType.setFrozen(true);

    boolean result = recordType.defineProperty("property", new JSType(),
        false, null);

    assertFalse(result);
  }

  @Test
  public void testGetLeastSupertype_notRecordType_returnsSuperType() {
    JSType other = new JSType();
    
    ObjectType supertype = recordType.getLeastSupertype(other);
    
    assertEquals(other, supertype);
  }

  @Test
  public void testGetLeastSupertype_recordTypeWithMatchingProperties_returnsRecordTypeWithMatchingProperties() {
    RecordType other = new RecordType(null, null);
    other.defineProperty("property", new JSType(), false, null);

    ObjectType supertype = recordType.getLeastSupertype(other);

    assertTrue(supertype instanceof RecordType);
    assertEquals(1, supertype.getProperties().size());
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_notRecordType_returnsNativeNoObjectType() {
    JSType that = new JSType();

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);

    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype.getDisplayName());
  }

  @Test
  public void testGetGreatestSubtypeHelper_recordTypeWithConflictingProperty_returnsNativeNoType() {
    RecordType that = new RecordType(null, null);
    that.defineProperty("property", new JSType(), false, null);
    recordType.defineProperty("property", new JSType(), false, null);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);

    assertEquals(JSTypeNative.NO_TYPE, greatestSubtype.getDisplayName());
  }
  
  @Test
  public void testIsSubtype_notRecordType_returnsFalse() {
    JSType that = new JSType();

    boolean result = recordType.isSubtype(that);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_nativeObjectType_returnsTrue() {
    JSType that = new JSType();
    that.setDisplayName(JSTypeNative.OBJECT_TYPE);

    boolean result = recordType.isSubtype(that);

    assertTrue(result);
  }

  @Test
  public void testIsSubtype_recordTypeWithMatchingProperties_returnsTrue() {
    RecordType that = new RecordType(null, null);
    that.defineProperty("property", new JSType(), false, null);
    recordType.defineProperty("property", new JSType(), false, null);

    boolean result = recordType.isSubtype(that);

    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_recordTypeWithDifferentProperties_returnsFalse() {
    RecordType that = new RecordType(null, null);
    that.defineProperty("property1", new JSType(), false, null);
    recordType.defineProperty("property2", new JSType(), false, null);

    boolean result = recordType.isSubtype(that);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_recordTypeWithSubtypeProperties_returnsTrue() {
    RecordType that = new RecordType(null, null);
    that.defineProperty("property", new SubType(), false, null);
    recordType.defineProperty("property", new SuperType(), false, null);

    boolean result = recordType.isSubtype(that);

    assertTrue(result);
  }

  @Test
  public void testResolveInternal_resolvesAllProperties() {
    ErrorReporter errorReporter = new ErrorReporter();
    recordType.defineProperty("property", new JSType(), false, null);

    ObjectType resolvedType = recordType.resolveInternal(errorReporter, null);

    assertEquals(1, resolvedType.getProperties().size());
  }
}