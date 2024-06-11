package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordType = new RecordType(registry, ImmutableMap.of("property", new RecordProperty(null, null)));
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType otherRecordType = new RecordType(registry, ImmutableMap.of("property", new RecordProperty(null, null)));

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(recordType);
    when(registry.getNativeType(JSTypeNative.NO_TYPE)).thenReturn(recordType);

    assertEquals(true, recordType.isEquivalentTo(otherRecordType));
  }

  @Test
  public void testIsSubtype() {
    RecordType otherRecordType = new RecordType(registry, ImmutableMap.of("property", new RecordProperty(null, null)));

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(recordType);

    assertEquals(true, recordType.isSubtype(otherRecordType));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter reporter = mock(ErrorReporter.class);
    StaticScope<JSType> scope = mock(StaticScope.class);
    JSType resolvedType = mock(JSType.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(recordType);
    when(resolvedType.resolve(reporter, scope)).thenReturn(resolvedType);

    recordType.resolveInternal(reporter, scope);

    assertEquals(resolvedType, recordType.getPropertyType("property"));
  }

  // Add more test cases here...
}