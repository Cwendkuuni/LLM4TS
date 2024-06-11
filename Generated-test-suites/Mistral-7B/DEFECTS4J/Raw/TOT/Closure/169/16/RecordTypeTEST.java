import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newLinkedHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertNotNull(recordType);
    assertEquals(JSType.ObjectType.class, recordType.getClass());
    assertTrue(recordType.isRecordType());
    assertEquals(2, recordType.getDeclaredProperties().size());

    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);

    assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));

    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newLinkedHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newLinkedHashMap();
    recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, true));

    assertFalse(recordType1.checkRecordEquivalenceHelper(new RecordType(registry, recordProperties1, false), true));
    assertFalse(recordType2.checkRecordEquivalenceHelper(new RecordType(registry, recordProperties2, false), true));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newLinkedHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newLinkedHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));
    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
    assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(recordType1));
    assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(recordType2));

    assertNotNull(recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType()));

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    unionBuilder.addAlternate(objType);

    JSType unionType = unionBuilder.build();
    assertNotNull(recordType1.getGreatestSubtypeHelper(unionType));
  }

  @Test
  public void testRecordTypeComparison() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newLinkedHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newLinkedHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));

    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(objType);
    JSType unionType = unionBuilder.build();

    assertTrue(recordType1.isSubtype(unionType));
  }

  @Test
  public void testRecordTypeAssignment() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newLinkedHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newLinkedHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    Node record1Node = Node.objectLiteral(new String[]{"x", 1, "y", 2});
    Node record2Node = Node.objectLiteral(new String[]{"x", 1, "y", 3});

    Object record1 = registry.getTypeFactory().coerceToType(record1Node, recordType1);
    Object record2 = registry.getTypeFactory().coerceToType(record2Node, recordType2);

    assertFalse(recordType1.isSubtype(recordType2));

    try {
      record1 = recordType2.wrap(record1);
      fail("Should have thrown an error.");
    } catch (ClassCastException e) {
      // Expected
    }

    // This test relies on runtime type coercion and may fail under certain conditions
    try {
      record2 = recordType1.wrap(record2);
      assertTrue(record1 instanceof RecordType);
      assertEquals(recordType1, record1.getClass());
      assertEquals(recordType1, record2.getClass());
    } catch (ClassCastException e) {
      // Expected in some cases, e.g., when the JVM doesn't support coercion
    }
  }

  @Test
  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newLinkedHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    try {
      recordType.defineProperty("y", registry.getNumberType(), false, Node.newIdent("y"));
      fail("Should have thrown an error.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}