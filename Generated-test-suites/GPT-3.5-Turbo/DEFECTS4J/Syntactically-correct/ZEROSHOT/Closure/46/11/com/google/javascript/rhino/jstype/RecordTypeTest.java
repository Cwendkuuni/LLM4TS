import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private RecordType recordType;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("a", registry.getGlobalType("TYPE_1"));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo_sameRecordType_true() {
    RecordType otherRecordType = new RecordType(registry, recordType.properties);
    assertTrue(recordType.isEquivalentTo(otherRecordType));
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_false() {
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("b", registry.getGlobalType("TYPE_2"));
    properties.put("a", registry.getGlobalType("TYPE_1"));
    RecordType otherRecordType = new RecordType(registry, properties);
    assertFalse(recordType.isEquivalentTo(otherRecordType));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(implicitPrototype, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testDefineProperty_notFrozen_true() {
    boolean result = recordType.defineProperty("b", registry.getGlobalType("TYPE_2"), false, null);
    assertTrue(result);
  }

  @Test
  public void testDefineProperty_frozen_false() {
    recordType.constructor(registry, recordType.toObjectType(), registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
    boolean result = recordType.defineProperty("b", registry.getGlobalType("TYPE_2"), false, null);
    assertFalse(result);
  }

  @Test
  public void testGetLeastSupertype_notRecordType_true() {
    JSType other = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    recordType.constructor(registry, recordType.toObjectType(), registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
    JSType leastSupertype = recordType.getLeastSupertype(other);
    assertEquals(leastSupertype, registry.getNativeType(JSTypeNative.NUMBER_TYPE));
  }

  @Test
  public void testGetLeastSupertype_recordType_true() {
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("b", registry.getGlobalType("TYPE_2"));
    RecordType other = new RecordType(registry, properties);
    JSType leastSupertype = recordType.getLeastSupertype(other);
    assertEquals(leastSupertype, registry.getGlobalType("RECORD_TYPE_AB"));
  }

  @Test
  public void testGetGreatestSubtypeHelper_recordType_true() {
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("b", registry.getGlobalType("TYPE_2"));
    RecordType other = new RecordType(registry, properties);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(other);
    assertEquals(greatestSubtype, registry.getGlobalType("RECORD_TYPE_A"));
  }

  @Test
  public void testIsSubtype_notRecordType_true() {
    JSType other = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    boolean result = recordType.isSubtype(other);
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_recordType_true() {
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("b", registry.getGlobalType("TYPE_2"));
    RecordType other = new RecordType(registry, properties);
    boolean result = recordType.isSubtype(other);
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_recordType_false() {
    SortedMap<String, JSType> properties = new HashMap<>();
    properties.put("b", registry.getGlobalType("TYPE_2"));
    properties.put("c", registry.getGlobalType("TYPE_3"));
    RecordType other = new RecordType(registry, properties);
    boolean result = recordType.isSubtype(other);
    assertFalse(result);
  }

  @Test
  public void testResolveInternal_true() {
    ErrorReporter errorReporter = null;
    StaticScope<JSType> scope = null;
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(resolvedType, recordType);
  }
}