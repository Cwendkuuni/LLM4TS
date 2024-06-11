package com.google.javascript.rhino.jstype;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType otherRecordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
    assertTrue(recordType.isEquivalentTo(recordType));
    assertFalse(recordType.isEquivalentTo(null));
    assertTrue(recordType.isEquivalentTo(otherRecordType));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "property";
    JSType type = new JSType();
    boolean inferred = true;
    Node propertyNode = new Node();

    assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));

    JSType propertyType = recordType.getPropertyType(propertyName);
    assertNotNull(propertyType);
    assertEquals(type, propertyType);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    assertFalse(recordType.isSubtype(null));
    assertTrue(recordType.isSubtype(that));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertNotNull(resolvedType);
  }

}