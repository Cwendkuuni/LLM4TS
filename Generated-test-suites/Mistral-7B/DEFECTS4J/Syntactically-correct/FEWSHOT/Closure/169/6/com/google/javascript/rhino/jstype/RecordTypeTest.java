package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("p1", registry.getStringType());
    RecordProperty recordProperty2 = new RecordProperty("p2", registry.getNumberType());

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("p1", recordProperty1);
    recordProperties.put("p2", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties, true);
    RecordType recordType2 = new RecordType(registry, recordProperties, true);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType1.isSubtype(recordType3));

    recordType3.defineProperty("p1", registry.getStringType(), false, Node.NULL);
    assertFalse(recordType1.isSubtype(recordType3));

    recordType3.defineProperty("p1", registry.getStringType(), false, Node.NULLOBJECT);
    assertFalse(recordType1.isSubtype(recordType3));

    RecordType recordType4 = new RecordType(registry, recordProperties, true);
    recordType4.setFrozen(true);
    assertFalse(recordType1.isSubtype(recordType4));

    RecordType recordType5 = new RecordType(registry, recordProperties, false);
    assertFalse(recordType1.isSubtype(recordType5));

    UnionType unionType = new UnionTypeBuilder(registry).build();
    unionType.addAlternate(recordType1);
    unionType.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    RecordType recordType6 = new RecordType(registry, recordProperties, true);
    recordType6.setFrozen(true);
    assertTrue(recordType1.isSubtype(unionType));
    assertFalse(unionType.isSubtype(recordType6));

    RecordType recordType7 = new RecordType(registry, recordProperties, true);
    recordType7.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    assertFalse(recordType1.isSubtype(recordType7));

    RecordType recordType8 = new RecordType(registry, recordProperties, true);
    RecordType recordType9 = new RecordType(registry, recordProperties, true);
    recordType9.defineProperty("p1", registry.getStringType(), false, Node.NULLOBJECT);
    JSType recordType9Prop1Type = recordType9.getPropertyType("p1");
    recordType8.defineProperty("p1", recordType9Prop1Type, false, Node.NULL);
    assertTrue(recordType1.isSubtype(recordType8));

    RecordType recordType10 = new RecordType(registry, recordProperties, true);
    recordType10.defineProperty("p1", registry.getStringType(), false, Node.UNDEFINED);
    JSType recordType10Prop1Type = recordType10.getPropertyType("p1");
    recordType9.defineProperty("p1", recordType10Prop1Type, false, Node.UNDEFINED);
    assertFalse(recordType1.isSubtype(recordType9));

    RecordType recordType11 = new RecordType(registry, recordProperties, true);
    recordType11.defineProperty("p1", registry.getNumberType(), false, Node.UNDEFINED);
    JSType recordType11Prop1Type = recordType11.getPropertyType("p1");
    recordType9.defineProperty("p1", recordType11Prop1Type, false, Node.UNDEFINED);
    assertTrue(recordType1.isSubtype(recordType9));

    RecordType recordType12 = new RecordType(registry, recordProperties, true);
    RecordProperty recordProperty13 = new RecordProperty("p3", registry.getBooleanType());
    recordType12.defineProperty("p3", recordProperty13.getType(), false, Node.UNDEFINED);
    Map<String, RecordProperty> recordProperties13 = Maps.newHashMap();
    RecordProperty recordProperty14 = new RecordProperty("p1", registry.getStringType());
    RecordProperty recordProperty15 = new RecordProperty("p2", registry.getNumberType());
    recordProperties13.put("p1", recordProperty14);
    recordProperties13.put("p2", recordProperty15);
    RecordType recordType13 = new RecordType(registry, recordProperties13, true);
    assertTrue(recordType1.isSubtype(recordType13));

    RecordType recordType14 = new RecordType(registry, recordProperties, true);
    RecordType recordType15 = new RecordType(registry, recordProperties, true);
    recordType14.defineProperty("p1", registry.getBooleanType(), false, Node.UNDEFINED);
    JSType recordType14Prop1Type = recordType14.getPropertyType("p1");
    recordType15.defineProperty("p1", recordType14Prop1Type, false, Node.UNDEFINED);
    assertFalse(recordType1.isSubtype(recordType15));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("p1", registry.getStringType());
    RecordProperty recordProperty2 = new RecordProperty("p2", registry.getNumberType());

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("p1", recordProperty1);
    recordProperties.put("p2", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties, true);
    RecordType recordType2 = new RecordType(registry, recordProperties, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType3 = new RecordType(registry, recordProperties, true);
    recordType3.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertTrue(recordType1.isSubtype(greatestSubtype));

    UnionType unionType = new UnionTypeBuilder(registry).build();
    greatestSubtype = recordType1.getGreatestSubtypeHelper(unionType);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType4 = new RecordType(registry, recordProperties, true);
    recordType4.defineProperty("p1", registry.getStringType(), false, Node.UNDEFINED);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType5 = new RecordType(registry, recordProperties, true);
    recordType5.defineProperty("p1", registry.getNumberType(), false, Node.UNDEFINED);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType6 = new RecordType(registry, recordProperties, true);
    recordType6.setFrozen(true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType6);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType7 = new RecordType(registry, recordProperties, false);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType7);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType8 = new RecordType(registry, recordProperties, true);
    RecordProperty recordProperty9 = new RecordProperty("p3", registry.getBooleanType());
    recordType8.defineProperty("p3", recordProperty9.getType(), false, Node.UNDEFINED);

    Map<String, RecordProperty> recordProperties9 = Maps.newHashMap();
    RecordProperty recordProperty10 = new RecordProperty("p1", registry.getStringType());
    RecordProperty recordProperty11 = new RecordProperty("p2", registry.getNumberType());
    recordProperties9.put("p1", recordProperty10);
    recordProperties9.put("p2", recordProperty11);
    RecordType recordType9 = new RecordType(registry, recordProperties9, true);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType9);
    assertTrue(recordType1.isSubtype(greatestSubtype));

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    for (ObjectType alt : registry.getEachReferenceTypeWithProperty("p1")) {
      JSType altPropType = alt.getPropertyType("p1");
      if (altPropType != null && !alt.isEquivalentTo(recordType1) &&
          alt.isSubtype(recordType2) &&
          registry.getStringType().isSubtype(altPropType)) {
        unionTypeBuilder.addAlternate(alt);
      }
    }
    UnionType unionType = unionTypeBuilder.build();
    greatestSubtype = recordType1.getGreatestSubtypeHelper(unionType);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    RecordType recordType10 = new RecordType(registry, recordProperties, true);
    recordType10.defineProperty("p1", registry.getNumberType(), false, Node.UNDEFINED);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType10);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("p1", registry.getStringType());
    RecordProperty recordProperty2 = new RecordProperty("p2", registry.getNumberType());

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("p1", recordProperty1);
    recordProperties1.put("p2", recordProperty2);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("p2", recordProperty2);
    recordProperties2.put("p3", new RecordProperty("p3", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, true));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    RecordType recordType3 = new RecordType(registry, recordProperties1, true);
    recordType3.setFrozen(true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));

    RecordType recordType4 = new RecordType(registry, recordProperties2, true);
    recordType4.setFrozen(true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, true));

    recordType1.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    recordType1.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    recordType2.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    recordType1.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    recordType2.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    recordType2.defineProperty("p4", registry.getBooleanType(), false, Node.UNDEFINED);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    recordType1.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    recordType2.defineProperty("p3", registry.getBooleanType(), false, Node.UNDEFINED);
    recordType2.defineProperty("p4", registry.getBooleanType(), false, Node.UNDEFINED);
    Scope scope = new Scope(new StaticScope(registry), new Symbol("x"));
    JSType typeInScope = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    typeInScope = typeInScope.resolve(new ErrorReporter(), scope);
    recordType2.defineProperty("p5", typeInScope, false, Node.UNDEFINED);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }
}