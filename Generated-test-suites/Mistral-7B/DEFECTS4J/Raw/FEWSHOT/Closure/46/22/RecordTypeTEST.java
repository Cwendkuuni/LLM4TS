package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordTypeBuilder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private RecordType recordTypeA;
  private RecordType recordTypeB;
  private RecordType recordTypeC;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    registry = mock(JSTypeRegistry.class);

    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    recordPropertiesA.put("b", new RecordProperty("b", JSType.getStringType()));
    recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", JSType.getIntType()));
    recordPropertiesB.put("c", new RecordProperty("c", JSType.getIntType()));
    recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("a", new RecordProperty("a", JSType.getStringType()));
    recordPropertiesC.put("b", new RecordProperty("b", JSType.getIntType()));
    recordTypeC = new RecordType(registry, recordPropertiesC);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        new PrototypeObjectType(registry, null, null));
  }

  @Test
  public void testIsEquivalentTo() {
    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    assertFalse(recordTypeA.isEquivalentTo(JSTypeNative.OBJECT_TYPE));
    assertFalse(recordTypeA.isEquivalentTo(JSTypeNative.NO_TYPE));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(JSTypeNative.OBJECT_TYPE, recordTypeA.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSType typeD = JSType.getUnknownType();
    RecordProperty recordPropertyD = new RecordProperty("d", typeD);

    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
    recordTypeD.defineProperty("d", typeD, false, null);

    SortedMap<String, JSType> propertiesCopy = new TreeMap<>(recordTypeA.properties);
    recordTypeA.defineProperty("d", typeD, false, null);

    assertTrue(recordTypeA.hasProperty("d"));
    assertEquals(typeD, recordTypeA.getPropertyType("d"));
    assertEquals(JSTypeNative.NO_NODE, recordTypeA.getPropertyNode("d"));
    assertEquals(recordTypeD, recordTypeA.toMaybeRecordType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType recordTypeADest = JSType.getUnknownType();
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getLeastSupertype(JSTypeNative.NO_TYPE))
        .thenReturn(recordTypeADest);

    JSType leastSupertypeAB = recordTypeA.getLeastSupertype(recordTypeB);
    assertEquals(recordTypeA, leastSupertypeAB);

    JSType leastSupertypeAC = recordTypeA.getLeastSupertype(recordTypeC);
    assertEquals(recordTypeA, leastSupertypeAC);

    JSType leastSupertypeBC = recordTypeB.getLeastSupertype(recordTypeC);
    assertEquals(recordTypeB, leastSupertypeBC);

    JSType leastSupertypeBD = recordTypeB.getLeastSupertype(recordTypeD);
    assertEquals(recordTypeB, leastSupertypeBD);

    JSType leastSupertypeABCD =
        recordTypeA.getLeastSupertype(recordTypeB, recordTypeC, recordTypeD);
    assertEquals(recordTypeA, leastSupertypeABCD);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType recordTypeE = JSType.getUnknownType();
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(JSTypeNative.NO_TYPE))
        .thenReturn(recordTypeE);

    JSType greatestSubtypeNone = JSTypeNative.NO_TYPE;
    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeA, greatestSubtypeAB);

    JSType greatestSubtypeAC = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeA, greatestSubtypeAC);

    JSType greatestSubtypeBD = recordTypeB.getGreatestSubtypeHelper(recordTypeD);
    assertEquals(recordTypeB, greatestSubtypeBD);

    JSType greatestSubtypeABCD =
        recordTypeA.getGreatestSubtypeHelper(recordTypeB, recordTypeC, recordTypeD);
    assertEquals(recordTypeA, greatestSubtypeABCD);

    JSType greatestSubtypeABDE =
        recordTypeA.getGreatestSubtypeHelper(recordTypeB, recordTypeC, recordTypeD, recordTypeE);
    assertEquals(recordTypeA, greatestSubtypeABDE);

    JSType greatestSubtypeABDE_with_conflict =
        new RecordTypeBuilder(registry)
            .addProperty("a", JSType.getIntType(), null)
            .addProperty("b", JSType.getIntType(), null)
            .addProperty("d", JSType.getIntType(), null)
            .addProperty("e", JSType.getIntType(), null)
            .build();

    JSType greatestSubtypeABDE_conflict =
        recordTypeA.getGreatestSubtypeHelper(greatestSubtypeABDE_with_conflict);
    assertEquals(JSTypeNative.NO_TYPE, greatestSubtypeABDE_conflict);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeA.isSubtype(JSTypeNative.OBJECT_TYPE));
    assertFalse(recordTypeA.isSubtype(JSTypeNative.NO_TYPE));

    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeB.isSubtype(recordTypeC));
    assertTrue(recordTypeB.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(JSTypeNative.OBJECT_TYPE));
    assertFalse(recordTypeB.isSubtype(JSTypeNative.NO_TYPE));

    assertFalse(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeC.isSubtype(recordTypeB));
    assertTrue(recordTypeC.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(JSTypeNative.OBJECT_TYPE));
    assertFalse(recordTypeC.isSubtype(JSTypeNative.NO_TYPE));

    assertTrue(recordTypeA.isSubtype(recordTypeADest));
    assertTrue(recordTypeB.isSubtype(recordTypeADest));
    assertTrue(recordTypeC.isSubtype(recordTypeADest));
    assertTrue(recordTypeD.isSubtype(recordTypeADest));
  }

  @Test
  public void testResolveInternal() {
    JSType typeF = new UnionTypeBuilder(registry).build();
    JSType typeG = JSType.getUnknownType();

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).resolve(
            errorReporter, null))
        .thenReturn(JSTypeNative.OBJECT_TYPE);

    when(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).resolve(
            errorReporter, null))
        .thenReturn(JSTypeNative.NO_OBJECT_TYPE);

    when(registry.getNativeType(JSTypeNative.NO_TYPE).resolve(
            errorReporter, null))
        .thenReturn(JSTypeNative.NO_TYPE);

    when(registry.getIntType().resolve(errorReporter, null))
        .thenReturn(JSTypeNative.getIntType());

    when(registry.getStringType().resolve(errorReporter, null))
        .thenReturn(JSTypeNative.getStringType());

    when(registry.getUnknownType().resolve(errorReporter, null))
        .thenReturn(typeG);

    when(registry.getUnionTypeBuilder().build())
        .thenReturn(typeF);

    RecordType recordTypeH = new RecordType(registry, Maps.newHashMap());
    recordTypeH.resolveInternal(errorReporter, null);

    assertEquals(JSTypeNative.OBJECT_TYPE, recordTypeH.getImplicitPrototype());

    Map<String, JSType> propertiesCopy = new TreeMap<>(recordTypeA.properties);
    propertiesCopy.get("a").setValue(typeF);
    propertiesCopy.get("b").setValue(typeG);

    RecordType recordTypeI = new RecordType(registry, propertiesCopy);
    recordTypeI.resolveInternal(errorReporter, null);

    assertEquals(JSTypeNative.OBJECT_TYPE, recordTypeI.getImplicitPrototype());

    assertEquals(typeF, recordTypeI.getPropertyType("a"));
    assertEquals(typeG, recordTypeI.getPropertyType("b"));
  }
}