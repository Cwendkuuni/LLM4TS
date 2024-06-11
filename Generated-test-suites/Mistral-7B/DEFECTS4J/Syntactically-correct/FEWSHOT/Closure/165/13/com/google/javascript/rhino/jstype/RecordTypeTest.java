package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test empty record type
    RecordType emptyRecord = new RecordType(registry, Maps.newHashMap());
    assertTrue(emptyRecord.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test record with one property
    Map<String, RecordProperty> propertyMap = Maps.newHashMap();
    propertyMap.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    RecordType record1 = new RecordType(registry, propertyMap);

    // Test subtype relationship
    RecordType record2 = new RecordType(registry, propertyMap);
    RecordType record3 = new RecordType(registry, Maps.newHashMap());
    record3.defineProperty("prop1", registry.getStringType(), true, null);
    RecordType record4 = new RecordType(registry, propertyMap);
    record4.defineProperty("prop1", registry.getIntType(), true, null);

    assertTrue(record1.isSubtype(record2));
    assertFalse(record1.isSubtype(record3));
    assertFalse(record1.isSubtype(record4));

    // Test transitivity
    RecordType record5 = new RecordType(registry, Maps.newHashMap());
    record5.defineProperty("prop1", record1.getType(), true, null);
    assertTrue(record3.isSubtype(record5));
    assertTrue(record1.isSubtype(record5));

    // Test greatest subtype
    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record3);
    assertTrue(record3.isSubtype(greatestSubtype));
    assertTrue(record1.isSubtype(greatestSubtype));

    // Test UnionType as greatest subtype
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    unionTypeBuilder.addAlternate(objType);
    unionTypeBuilder.addAlternate(record1);
    JSType unionType = unionTypeBuilder.build();
    JSType greatestSubtypeUnion = record1.getGreatestSubtypeHelper(record3);
    assertTrue(unionType.isEquivalentTo(greatestSubtypeUnion));
  }
}