import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.errors.TypeError;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getNameForType(RecordType.class), recordType.getName());
    assertEquals(2, recordType.getNumberOfProperties());

    assertEquals("a", recordType.getPropertyName(0));
    assertEquals("b", recordType.getPropertyName(1));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("b"));

    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(recordType, recordType2);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("c", new RecordProperty("c", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentTypes() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getBooleanType()));

    RecordType recordType3 = new RecordType(registry, properties3);

    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testIsSubtypeWithUnionType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(registry.getNumberType());
    unionType.addAlternate(registry.getStringType());

    RecordType recordType2 = new RecordType(registry, properties);
    recordType2.setPropertyType("a", unionType);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithNullRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    assertFalse(recordType1.isSubtype(null));
  }

  @Test
  public void testIsSubtypeWithObjectType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testIsSubtypeWithEmptyRecordType() {
    RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    Map<String, RecordProperty> properties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(emptyRecordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithSameRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    assertTrue(recordType1.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeWithRecordTypeWithFrozenProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);
    recordType1.freeze();

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithRecordTypeWithAdditionalProperty() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));
    properties2.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithRecordTypeWithConflictingProperty() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties2.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertTrue(leastSupertype instanceof RecordType);
    assertEquals(registry.getNameForType(RecordType.class), leastSupertype.getName());
    assertEquals(2, ((RecordType) leastSupertype).getNumberOfProperties());
    assertEquals("a", ((RecordType) leastSupertype).getPropertyName(0));
    assertEquals(registry.getBooleanType(), ((RecordType) leastSupertype).getPropertyType("a"));
    assertEquals("b", ((RecordType) leastSupertype).getPropertyName(1));
    assertEquals(registry.getStringType(), ((RecordType) leastSupertype).getPropertyType("b"));
  }

  @Test
  public void testGetLeastSupertypeWithEmptyRecordTypes() {
    RecordType emptyRecordType1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType emptyRecordType2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType leastSupertype = emptyRecordType1.getLeastSupertype(emptyRecordType2);

    assertEquals(emptyRecordType1, leastSupertype);
  }

  @Test
  public void testGetLeastSupertypeWithSameRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType1);

    assertEquals(recordType1, leastSupertype);
  }

  @Test
  public void testGetLeastSupertypeWithNullRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    try {
      recordType.getLeastSupertype(null);
      fail();
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testGetLeastSupertypeWithRecordTypeWithFrozenProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);
    recordType1.freeze();

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    try {
      recordType1.getLeastSupertype(recordType2);
      fail();
    } catch (TypeError e) {
      // expected
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType2.getGreatestSubtype(recordType1);

    assertTrue(greatestSubtype instanceof RecordType);
    assertEquals(registry.getNameForType(RecordType.class), greatestSubtype.getName());
    assertEquals(1, ((RecordType) greatestSubtype).getNumberOfProperties());
    assertEquals("a", ((RecordType) greatestSubtype).getPropertyName(0));
    assertEquals(registry.getNumberType(), ((RecordType) greatestSubtype).getPropertyType("a"));
  }

  @Test
  public void testGetGreatestSubtypeWithEmptyRecordTypes() {
    RecordType emptyRecordType1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType emptyRecordType2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType greatestSubtype = emptyRecordType1.getGreatestSubtype(emptyRecordType2);

    assertEquals(emptyRecordType1, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithSameRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType1);

    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithNullRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    try {
      recordType.getGreatestSubtype(null);
      fail();
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeWithFrozenProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);
    recordType1.freeze();

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    try {
      recordType1.getGreatestSubtype(recordType2);
      fail();
    } catch (TypeError e) {
      // expected
    }
  }

  @Test
  public void testResolve() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    Node propNode = new Node(Node.IDENTIFIER, "a");

    JSType resolvedType = recordType.resolve(null, null);
    assertEquals(registry.getNumberType(), resolvedType);

    try {
      recordType.resolve(null, null, new ErrorReporter() {
        @Override
        public void error(int line, int lineNumber, String message) {
          fail();
        }
      });
      fail();
    } catch (Error e) {
      // expected
    }
  }
}