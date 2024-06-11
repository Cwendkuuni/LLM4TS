package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Map;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testIsSubtype() {
    RecordType intType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("IntType")
        .addProperty("value", registry.getType("Int"));
    RecordType stringType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("StringType")
        .addProperty("value", registry.getType("String"));

    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordType")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"));

    assertTrue(intType.isSubtype(intType));
    assertTrue(intType.isSubtype(registry.getType("Int")));
    assertFalse(intType.isSubtype(registry.getType("String")));

    assertTrue(stringType.isSubtype(stringType));
    assertTrue(stringType.isSubtype(registry.getType("String")));
    assertFalse(stringType.isSubtype(registry.getType("Int")));

    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(registry.getType("Object")));

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeA")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"));

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeB")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"))
        .addProperty("c", registry.getType("String"));

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordType));
    assertFalse(recordTypeA.isSubtype(recordTypeB));

    RecordProperty recordProperty = new RecordProperty("c", registry.getType("Int"));
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeC")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("c", registry.getType("Int"));

    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeD")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("d", registry.getType("Int"));

    assertTrue(recordTypeC.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordType));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeC.isSubtype(recordTypeB));
    assertFalse(recordTypeC.isSubtype(recordTypeD));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType intType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("IntType")
        .addProperty("value", registry.getType("Int"));
    RecordType stringType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("StringType")
        .addProperty("value", registry.getType("String"));
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordType")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"));

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertTrue(intType.isSubtype(greatestSubtype));

    greatestSubtype = recordType.getGreatestSubtypeHelper(stringType);
    assertTrue(stringType.isSubtype(greatestSubtype));

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeA")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"));

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeB")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"))
        .addProperty("c", registry.getType("String"));

    JSType greatestSubtypeAB = recordType.getGreatestSubtypeHelper(recordTypeA);
    assertTrue(recordTypeA.isSubtype(greatestSubtypeAB));

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeC")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("c", registry.getType("Int"));

    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeD")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("d", registry.getType("Int"));

    JSType greatestSubtypeCD = recordType.getGreatestSubtypeHelper(recordTypeD);
    assertFalse(recordTypeD.isSubtype(greatestSubtypeCD));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType intType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("IntType")
        .addProperty("value", registry.getType("Int"));
    RecordType stringType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("StringType")
        .addProperty("value", registry.getType("String"));
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordType")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"));

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeA")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"));

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeB")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"))
        .addProperty("c", registry.getType("String"));

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeC")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("c", registry.getType("Int"));

    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordTypeD")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("Int"))
        .addProperty("d", registry.getType("Int"));

    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
    assertTrue(recordType.checkRecordEquivalenceHelper(intType, true));
    assertTrue(recordType.checkRecordEquivalenceHelper(stringType, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeA, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeB, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeC, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeD, false));
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordType")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"));

    RecordProperty recordProperty = new RecordProperty("c", registry.getType("Int"));
    Node propertyNode = new Node_Property();

    boolean isDefined = recordType.defineProperty("c", registry.getType("Int"),
        false, propertyNode);
    assertFalse(isDefined);

    isDefined = recordType.defineProperty("c", registry.getType("Int"),
        true, propertyNode);
    assertFalse(isDefined);

    isDefined = recordType.defineProperty("c", registry.getType("Int"),
        false, null);
    assertFalse(isDefined);

    isDefined = recordType.defineProperty("c", registry.getType("Int"),
        true, null);
    assertTrue(isDefined);

    JSType cPropertyType = recordType.getPropertyType("c");
    assertNotNull(cPropertyType);
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true)
        .setName("RecordType")
        .addProperty("a", registry.getType("Int"))
        .addProperty("b", registry.getType("String"));

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    ObjectType expectedPrototype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    assertEquals(expectedPrototype, implicitPrototype);
  }
}