package com.google.javascript.rhino.jstype;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordType emptyRecordType;
  private RecordType nonEmptyRecordType;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    emptyRecordType = new RecordType(registry, new HashMap<>());
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    nonEmptyRecordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testIsEquivalentTo() {
    assertFalse(emptyRecordType.isEquivalentTo(null));
    assertTrue(emptyRecordType.isEquivalentTo(emptyRecordType));
    assertFalse(emptyRecordType.isEquivalentTo(nonEmptyRecordType));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), emptyRecordType.getImplicitPrototype());
    assertNotEquals(emptyRecordType.getImplicitPrototype(), nonEmptyRecordType.getImplicitPrototype());
  }
  
  @Test
  public void testDefineProperty() {
    assertFalse(emptyRecordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    assertTrue(emptyRecordType.defineProperty("b", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true, null));
  }
  
  @Test
  public void testGetLeastSupertype() {
    assertEquals(emptyRecordType, emptyRecordType.getLeastSupertype(emptyRecordType));
    assertEquals(nonEmptyRecordType, nonEmptyRecordType.getLeastSupertype(nonEmptyRecordType));
  }
  
  @Test
  public void testToMaybeRecordType() {
    assertEquals(emptyRecordType, emptyRecordType.toMaybeRecordType());
    assertEquals(nonEmptyRecordType, nonEmptyRecordType.toMaybeRecordType());
  }
  
  @Test
  public void testIsSubtype() {
    assertTrue(emptyRecordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(emptyRecordType.isSubtype(nonEmptyRecordType));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter(null);
    StaticScope<JSType> scope = new StaticScope<>();
    emptyRecordType.resolveInternal(errorReporter, scope);
    nonEmptyRecordType.resolveInternal(errorReporter, scope);
  }
}