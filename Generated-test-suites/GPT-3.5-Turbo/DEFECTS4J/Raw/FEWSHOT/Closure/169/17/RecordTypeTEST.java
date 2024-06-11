package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.ErrorReporterProvider;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, JSType> properties1 = Maps.newHashMap();
    properties1.put("property1", JSTypeNative.BOOLEAN_TYPE);
    properties1.put("property2", JSTypeNative.STRING_TYPE);
    properties1.put("property3", JSTypeNative.NUMBER_TYPE);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, JSType> properties2 = Maps.newHashMap();
    properties2.put("property1", JSTypeNative.BOOLEAN_TYPE);
    properties2.put("property2", JSTypeNative.STRING_TYPE);
    properties2.put("property3", JSTypeNative.NUMBER_TYPE);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(true, recordType1.checkRecordEquivalenceHelper(recordType2, true));

    Map<String, JSType> properties3 = Maps.newHashMap();
    properties3.put("property1", JSTypeNative.OBJECT_TYPE);
    properties3.put("property2", JSTypeNative.STRING_TYPE);
    properties3.put("property3", JSTypeNative.NUMBER_TYPE);
    RecordType recordType3 = new RecordType(registry, properties3);

    assertEquals(false, recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testIsSubtype() {
    Map<String, JSType> properties1 = Maps.newHashMap();
    properties1.put("property1", JSTypeNative.BOOLEAN_TYPE);
    properties1.put("property2", JSTypeNative.STRING_TYPE);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, JSType> properties2 = Maps.newHashMap();
    properties2.put("property1", JSTypeNative.BOOLEAN_TYPE);
    properties2.put("property2", JSTypeNative.STRING_TYPE);
    properties2.put("property3", JSTypeNative.NUMBER_TYPE);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(true, recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties);

    properties.put("property3", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType1, greatestSubtype);

    JSType greatestSubtype2 = recordType2.getGreatestSubtypeHelper(recordType1);
    assertEquals(recordType1, greatestSubtype2);
  }

  @Test
  public void testResolveInternal() {
    Map<String, JSType> properties = Maps.newHashMap();
    properties.put("property1", JSTypeNative.BOOLEAN_TYPE);
    properties.put("property2", JSTypeNative.STRING_TYPE);
    RecordType recordType = new RecordType(registry, properties);

    ErrorReporterProvider errorReporterProvider = mock(ErrorReporterProvider.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> scope = mock(StaticScope.class);
    when(registry.getErrorReporterProvider()).thenReturn(errorReporterProvider);
    when(errorReporterProvider.get()).thenReturn(errorReporter);
    when(registry.createScope(any(Node.class))).thenReturn(scope);

    recordType.resolveInternal(errorReporter, scope);

    verify(errorReporterProvider).get();
    verify(errorReporter).error(
        any(Node.class), any(), any(), any(),
        contains("Associating property with a property should not be null!"));
    verify(registry).createScope(any(Node.class));
    verify(scope, times(5)).insertReferenceName(anyString());
    verify(scope, times(5)).getSlot(anyString());
  }
}