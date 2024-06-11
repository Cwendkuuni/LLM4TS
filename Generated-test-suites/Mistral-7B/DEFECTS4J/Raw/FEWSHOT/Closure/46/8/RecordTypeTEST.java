package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NativeObject;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeAssert;
import com.google.javascript.rhino.testing.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;
  private JSTypeRegistry registry;
  private JSTypeNumber numberType;
  private JSTypeString stringType;
  private JSTypeBoolean booleanType;
  private JSTypeObject objectType;
  private JSTypeRecordType recordTypeA;
  private JSTypeRecordType recordTypeB;
  private JSTypeRecordType recordTypeC;
  private JSTypeRecordType recordTypeD;

  @Before
  public void setUp() {
    errorReporter = ErrorReporter.NULL;
    scope = new StaticScope<>(errorReporter);
    registry = new JSTypeRegistry();

    numberType = registry.getNumberType();
    stringType = registry.getStringType();
    booleanType = registry.getBooleanType();
    objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", numberType, Node.NULL_NODE));
    recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", numberType, Node.NULL_NODE));
    propertiesB.put("b", new RecordProperty("b", stringType, Node.NULL_NODE));
    recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", booleanType, Node.NULL_NODE));
    propertiesC.put("b", new RecordProperty("b", numberType, Node.NULL_NODE));
    recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("a", new RecordProperty("a", numberType, Node.NULL_NODE));
    propertiesD.put("b", new RecordProperty("b", booleanType, Node.NULL_NODE));
    propertiesD.put("c", new RecordProperty("c", stringType, Node.NULL_NODE));
    recordTypeD = new RecordType(registry, propertiesD);
  }

  @Test
  public void testIsEquivalentTo() {
    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeB.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeB.isEquivalentTo(recordTypeA));
    assertFalse(recordTypeA.isEquivalentTo(recordTypeC));
    assertFalse(recordTypeA.isEquivalentTo(numberType));
    assertFalse(recordTypeA.isEquivalentTo(booleanType));
    assertFalse(recordTypeA.isEquivalentTo(objectType));
  }

  @Test
  public void testGetImplicitPrototype() {
    TypeAssert.assertInstanceOf(NativeObject.class, recordTypeA.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("c", new RecordProperty("c", stringType, Node.NULL_NODE));
    JSType newRecordType = new RecordType(registry, newProperties);

    SortedMap<String, JSType> oldProperties = recordTypeA.properties;
    String propertyName = "c";
    JSType newPropertyType = newRecordType.getPropertyType(propertyName);
    Node propertyNode = newRecordType.getPropertyNode(propertyName);

    assertTrue(newRecordType.defineProperty(propertyName, newPropertyType, false, propertyNode));
    assertTrue(newRecordType.hasProperty(propertyName));
    assertEquals(newPropertyType, newRecordType.getPropertyType(propertyName));
    assertSame(propertyNode, newRecordType.getPropertyNode(propertyName));

    assertFalse(recordTypeA.defineProperty(propertyName, newPropertyType, false, propertyNode));
    assertFalse(recordTypeA.hasProperty(propertyName));
  }

  @Test
  public void testGetLeastSupertype() {
    JSType recordTypeAB = recordTypeA.getLeastSupertype(recordTypeB);
    TypeAssert.assertInstanceOf(recordTypeA.getClass(), recordTypeAB.getClass());
    assertTrue(recordTypeAB.isEquivalentTo(recordTypeA));

    JSType recordTypeAC = recordTypeA.getLeastSupertype(recordTypeC);
    JSType recordTypeAD = recordTypeA.getLeastSupertype(recordTypeD);

    TypeAssert.assertInstanceOf(recordTypeC.getClass(), recordTypeAC.getClass());
    TypeAssert.assertInstanceOf(recordTypeB.getClass(), recordTypeAC.getClass());
    TypeAssert.assertInstanceOf(recordTypeA.getClass(), recordTypeAC.getClass());
    assertTrue(recordTypeAC.isSubtype(recordTypeA));
    assertTrue(recordTypeAC.isSubtype(recordTypeB));
    assertTrue(recordTypeAC.isSubtype(recordTypeC));

    TypeAssert.assertInstanceOf(recordTypeD.getClass(), recordTypeAD.getClass());
    TypeAssert.assertInstanceOf(recordTypeA.getClass(), recordTypeAD.getClass());
    TypeAssert.assertInstanceOf(recordTypeB.getClass(), recordTypeAD.getClass());
    TypeAssert.assertInstanceOf(recordTypeC.getClass(), recordTypeAD.getClass());
    assertTrue(recordTypeAD.isSubtype(recordTypeA));
    assertTrue(recordTypeAD.isSubtype(recordTypeB));
    assertTrue(recordTypeAD.isSubtype(recordTypeC));
    assertTrue(recordTypeAD.isSubtype(recordTypeD));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType typeA = registry.getRecordType("{a: !Number, b: !String}");
    JSType typeB = registry.getRecordType("{a: !Number, b: !Boolean}");

    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(typeB);
    TypeAssert.assertInstanceOf(recordTypeA.getClass(), greatestSubtypeAB.getClass());
    assertTrue(greatestSubtypeAB.isEquivalentTo(recordTypeA));

    JSType greatestSubtypeBA = typeB.getGreatestSubtypeHelper(recordTypeA);
    TypeAssert.assertInstanceOf(typeB.getClass(), greatestSubtypeBA.getClass());
    assertTrue(greatestSubtypeBA.isEquivalentTo(typeB));

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(typeB);
    TypeAssert.assertInstanceOf(recordTypeA.getClass(), greatestSubtype.getClass());
    assertTrue(greatestSubtype.isEquivalentTo(recordTypeA));
    assertEquals(greatestSubtype, greatestSubtypeAB);
    assertEquals(greatestSubtype, greatestSubtypeBA);

    JSType noType = registry.getNativeType(JSTypeNative.NO_TYPE);
    JSType greatestSubtypeNO = recordTypeA.getGreatestSubtypeHelper(noType);
    assertNull(greatestSubtypeNO);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeB));

    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeA.isSubtype(numberType));
    assertFalse(recordTypeA.isSubtype(booleanType));
    assertFalse(recordTypeA.isSubtype(objectType));

    assertTrue(numberType.isSubtype(numberType));
    assertTrue(numberType.isSubtype(booleanType));
    assertFalse(numberType.isSubtype(stringType));
    assertTrue(booleanType.isSubtype(booleanType));
    assertFalse(booleanType.isSubtype(numberType));
    assertFalse(booleanType.isSubtype(stringType));
    assertFalse(booleanType.isSubtype(objectType));
    assertTrue(stringType.isSubtype(stringType));
    assertTrue(objectType.isSubtype(objectType));

    assertTrue(recordTypeC.isSubtype(recordTypeB));
    assertTrue(recordTypeC.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeC.isSubtype(numberType));
    assertFalse(recordTypeC.isSubtype(booleanType));
    assertFalse(recordTypeC.isSubtype(objectType));

    assertFalse(recordTypeD.isSubtype(recordTypeA));
    assertFalse(recordTypeD.isSubtype(recordTypeB));
    assertFalse(recordTypeD.isSubtype(recordTypeC));
    assertTrue(recordTypeD.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(numberType));
    assertTrue(recordTypeD.isSubtype(booleanType));
    assertTrue(recordTypeD.isSubtype(stringType));
    assertTrue(recordTypeD.isSubtype(objectType));

    JSType recordTypeE = new RecordType(registry, new HashMap<>());
    assertFalse(recordTypeE.isSubtype(recordTypeA));
    assertFalse(recordTypeE.isSubtype(recordTypeB));
    assertFalse(recordTypeE.isSubtype(recordTypeC));
    assertFalse(recordTypeE.isSubtype(recordTypeD));
    assertFalse(recordTypeE.isSubtype(numberType));
    assertFalse(recordTypeE.isSubtype(booleanType));
    assertFalse(recordTypeE.isSubtype(stringType));
    assertFalse(recordTypeE.isSubtype(objectType));
  }

  @Test
  public void testToMaybeRecordType() {
    assertTrue(recordTypeA.toMaybeRecordType() instanceof RecordType);
    assertTrue(recordTypeA.toMaybeRecordType() == recordTypeA);
  }

  @Test
  public void testIsFrozen() {
    assertFalse(recordTypeA.isFrozen());

    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("c", new RecordProperty("c", stringType, Node.NULL_NODE));
    JSType newRecordType = new RecordType(registry, newProperties);

    assertFalse(recordTypeA.isSubtype(newRecordType));
    assertFalse(newRecordType.isSubtype(recordTypeA));
  }
}