package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private ErrorReporter errorReporter;
  private Scope scope;
  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordType2;
  private RecordType recordType3;
  private RecordProperty recordProperty;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    scope = new Scope(null, null, null, null, null, null, null, errorReporter);
    registry = new JSTypeRegistry(null, null, null, null, null);

    recordProperty = new RecordProperty("prop", registry.getNativeType(JSTypeNative.STRING_TYPE));

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("prop", recordProperty);

    recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop", recordProperty);
    recordProperties2.put("prop2", new RecordProperty("prop2", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));

    recordType2 = new RecordType(registry, recordProperties2, true);

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("prop3", new RecordProperty("prop3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));

    recordType3 = new RecordType(registry, recordProperties3, true);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, true));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, false));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordType2));
    assertTrue(recordType.isSubtype(recordType3));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
    assertTrue(registry.isEquivalentTo(recordType, greatestSubtype));

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
    UnionType unionType = new UnionTypeBuilder(registry).build();
    unionType.addAlternate(recordType);
    unionType.addAlternate(recordType2);
    assertTrue(registry.isEquivalentTo(unionType, greatestSubtype));

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
    assertTrue(registry.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype));
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("prop", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
    assertFalse(recordType.defineProperty("prop", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    assertFalse(recordType.defineProperty("prop", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));

    assertNull(recordType.getPropertyType("prop"));

    recordType.defineProperty("prop", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null);
    JSType propType = recordType.getPropertyType("prop");
    assertTrue(registry.isEquivalentTo(registry.getNativeType(JSTypeNative.STRING_TYPE), propType));

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
    UnionType unionType = new UnionTypeBuilder(registry).build();
    unionType.addAlternate(recordType);
    unionType.addAlternate(recordType2);
    greatestSubtype = greatestSubtype.getLeastSupertype(unionType);
    JSType greatestSubtypeProp = greatestSubtype.getPropertyType("prop");
    assertTrue(registry.isEquivalentTo(registry.getNativeType(JSTypeNative.STRING_TYPE), greatestSubtypeProp));
  }
}