import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCommon.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private ErrorReporter t;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    t = new ErrorReporter() {};
  }

  // Basic test cases for all methods in the RecordType class

  @Test
  public void testRecordTypeConstructor() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClass(), RecordType.class);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType recordType3 = new RecordType(registry, properties1);

    assertTrue(recordType1.isEquivalentTo(recordType2));
    assertTrue(recordType1.isEquivalentTo(recordType3));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("c", new RecordProperty("c", registry.getStringType()));
    RecordType recordType4 = new RecordType(registry, properties3);
    assertFalse(recordType1.isEquivalentTo(recordType4));
  }

  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getImplicitPrototype().getClass(),
        JSTypeNative.ObjectType.class);
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("a", registry.getNumberType(), false, null));
    assertTrue(recordType.hasProperty("a"));
    assertEquals(recordType.getPropertyType("a"), registry.getStringType());
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("b", new RecordProperty("b", registry.getNumberType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
    assertTrue(leastSupertype.isEquivalentTo(recordType2));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", registry.getStringType()));
    properties4.put("b", new RecordProperty("b", registry.getNumberType()));
    properties4.put("c", new RecordProperty("c", registry.getBooleanType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    RecordType leastSupertype2 = recordType1.getLeastSupertype(recordType3);
    assertEquals(leastSupertype2.getClass(), RecordType.class);
    assertTrue(leastSupertype2.isEquivalentTo(recordType1));

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
    assertEquals(greatestSubtype.getClass(), RecordType.class);
    assertTrue(greatestSubtype.hasProperty("a"));
    assertTrue(greatestSubtype.hasProperty("b"));
    assertFalse(greatestSubtype.hasProperty("c"));
    assertEquals(greatestSubtype.getPropertyType("a"), registry.getStringType());
    assertEquals(greatestSubtype.getPropertyType("b"), registry.getNumberType());
  }

  // Edge cases and exception handling tests

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeConstructorWithNullRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", null);
    new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentToWithEmptyRecordType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);

    assertFalse(recordType1.isEquivalentTo(emptyRecordType));
  }

  @Test
  public void testIsEquivalentToWithIncompatibleRecordTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToWithDifferentRecordOrders() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getNumberType()));
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithFrozenRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;

    assertFalse(recordType.defineProperty("a", registry.getNumberType(), false, null));
  }

  @Test
  public void testGetLeastSupertypeWithEmptyRecordType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    JSType leastSupertype = unionTypeBuilder.build();

    assertEquals(leastSupertype.getClass(), UnionType.class);
    assertTrue(leastSupertype.isSubtype(recordType1));
    assertTrue(leastSupertype.isSubtype(emptyRecordType));
  }

  @Test
  public void testGetLeastSupertypeWithRecordTypeAndPrimitiveType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType leastSupertype = recordType.getLeastSupertype(registry.getNumberType());
    assertEquals(leastSupertype.getClass(), JSTypeNative.ObjectType.class);
  }

  @Test
  public void testGetLeastSupertypeWithRecordTypeAndEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType);
    unionTypeBuilder.addAlternate(emptyRecordType);
    JSType leastSupertype = unionTypeBuilder.build();

    assertEquals(leastSupertype.getClass(), UnionType.class);
    assertTrue(leastSupertype.isSubtype(recordType));
    assertTrue(leastSupertype.isSubtype(emptyRecordType));
  }

  @Test
  public void testGetLeastSupertypeWithRecordTypeAndAnotherRecordType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    JSType leastSupertype = unionTypeBuilder.build();

    assertEquals(leastSupertype.getClass(), UnionType.class);
    assertTrue(leastSupertype.isSubtype(recordType1));
    assertTrue(leastSupertype.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelperWithEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(emptyRecordType);
    assertEquals(greatestSubtype.getClass(), JSTypeNative.ObjectType.class);
  }

  @Test
  public void testGetGreatestSubtypeHelperWithRecordTypeAndPrimitiveType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNumberType());
    assertEquals(greatestSubtype.getClass(), JSTypeNative.ObjectType.class);
  }

  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType resolvedType = recordType.resolveInternal(t, new StaticScope<>(registry));
    assertEquals(resolvedType.getClass(), RecordType.class);
  }
}