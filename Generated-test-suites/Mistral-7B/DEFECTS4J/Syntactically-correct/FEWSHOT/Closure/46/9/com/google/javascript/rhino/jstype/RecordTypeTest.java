package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getDeclaredName(), "Object");
    assertEquals(recordType.getClassName(), "RecordType");
    assertEquals(recordType.getProperties().size(), 2);

    assertTrue(recordType.isRecordType());
    assertFalse(recordType.isPrimitiveType());
    assertFalse(recordType.isFunctionType());
    assertFalse(recordType.isPrototypeObjectType());
    assertFalse(recordType.isUnionType());
    assertFalse(recordType.isIntersectionType());

    assertEquals(recordType.getImplicitPrototype().getClass().getName(),
        "com.google.javascript.rhino.jstype.ObjectType");
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesA.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesB.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesA.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    RecordType recordTypeD = new RecordType(registry, propertiesA);
    assertTrue(recordTypeA.isSubtype(recordTypeD));
  }

  @Test
  public void testIsSubtypeWithIncompatiblePropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesA.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getStringType()));
    propertiesB.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithMissingProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesA.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesA.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesB.put("z", new RecordProperty("z", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    RecordType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);

    assertTrue(leastSupertype.isSubtype(recordTypeA));
    assertTrue(leastSupertype.isSubtype(recordTypeB));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesC.put("y", new RecordProperty("y", registry.getNumberType()));
    propertiesC.put("z", new RecordProperty("z", registry.getNumberType()));

    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertEquals(recordTypeC, leastSupertype);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
    propertiesB.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(greatestSubtype));

    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelperWithIncompatiblePropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getStringType()));
    propertiesB.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertNull(greatestSubtype);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.putType("Number", registry.getNumberType());
    registry.putType("String", registry.getStringType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getType("Number")));
    properties.put("y", new RecordProperty("y", registry.getType("String")));

    RecordType recordType = new RecordType(registry, properties);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(null, registry);

    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

    assertEquals(recordType, resolvedType);
  }
}