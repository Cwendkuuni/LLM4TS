package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
  private static final RecordTypeRegistry REGISTRY = mock(RecordTypeRegistry.class);
  private JSTypeRegistry typeRegistry;
  private RecordType recordType;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    typeRegistry = new JSTypeRegistry();
    typeRegistry.setRecordTypeRegistry(REGISTRY);
    recordType = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))));
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))));
    RecordType recordType2 = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))
            .put("b", new RecordProperty("b", JSType.INT_TYPE))));

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, false));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))));

    ObjectType objectType = typeRegistry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    RecordType recordType2 = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))
            .put("b", new RecordProperty("b", JSType.INT_TYPE))));
    RecordType recordType3 = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("b", new RecordProperty("b", JSType.INT_TYPE))));

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(objectType));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", new RecordProperty("a", JSType.INT_TYPE))));

    JSType type = JSType.STRING_TYPE;
    Node node = mock(Node.class);
    assertTrue(recordType.defineProperty("a", type, false, node));
    assertEquals(type, recordType.getPropertyType("a"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullRecordProperty() {
    RecordType recordType = new RecordType(typeRegistry,
        Maps.newHashMap(Maps.newHashMap()
            .put("a", null)));
  }
}

class RecordTypeRegistry extends JSTypeRegistry {
  @Override
  RecordTypeBuilder newRecordTypeBuilder() {
    throw new UnsupportedOperationException();
  }
}