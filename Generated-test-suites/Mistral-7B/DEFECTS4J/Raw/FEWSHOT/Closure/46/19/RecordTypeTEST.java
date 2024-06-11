package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.PrototypeObjectType;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int column) {}

      @Override
      public void warning(String message, int line, int column) {}
    };
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordProperties2.put("property2", new RecordProperty("property2", new IntType(registry)));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentNumberOfProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordProperties2.put("property2", new RecordProperty("property2", new IntType(registry)));
    recordProperties2.put("property3", new RecordProperty("property3", new IntType(registry)));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentPropertyTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("property1", new RecordProperty("property1", new StringType(registry)));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesB.put("property2", new RecordProperty("property2", new IntType(registry)));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsNotSubtype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", new StringType(registry)));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesB.put("property2", new RecordProperty("property2", new StringType(registry)));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesC.put("property2", new RecordProperty("property2", new IntType(registry)));
    recordPropertiesC.put("property3", new RecordProperty("property3", new StringType(registry)));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    JSType leastSupertypeAB = recordTypeA.getLeastSupertype(recordTypeB);
    JSType leastSupertypeABC = recordTypeA.getLeastSupertype(recordTypeC);

    assertTrue(leastSupertypeAB.isEquivalentTo(leastSupertypeABC));
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesB.put("property2", new RecordProperty("property2", new IntType(registry)));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesC.put("property2", new RecordProperty("property2", new IntType(registry)));
    recordPropertiesC.put("property3", new RecordProperty("property3", new StringType(registry)));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    JSType greatestSubtypeAB = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    JSType greatestSubtypeAC = recordTypeC.getGreatestSubtypeHelper(recordTypeA);

    assertTrue(greatestSubtypeAB.isEquivalentTo(greatestSubtypeAC));
  }

  @Test
  public void testGetGreatestSubtypeWithDifferentPropertyTypes() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", new StringType(registry)));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("property1", new RecordProperty("property1", new IntType(registry)));
    recordPropertiesC.put("property2", new RecordProperty("property2", new StringType(registry)));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    JSType greatestSubtypeAB = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    JSType greatestSubtypeAC = recordTypeC.getGreatestSubtypeHelper(recordTypeA);

    assertFalse(greatestSubtypeAB.isEquivalentTo(greatestSubtypeAC));
  }

  @Test
  public void testIsFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testDefinePropertyFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", new IntType(registry)));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertFalse(recordType.defineProperty("property2", new IntType(registry), false, null));
  }
}