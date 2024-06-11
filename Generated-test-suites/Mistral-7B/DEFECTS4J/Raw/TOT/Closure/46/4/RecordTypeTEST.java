import com.google.javascript.jscomp.junit.testrunner.JSTestCase;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.Type;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest extends JSTestCase {

  private JSTypeRegistry registry;

  @Override
  public void setUp() {
    super.setUp();
    registry = new JSTypeRegistry(new BuiltInTypeRegistry());
  }

  @Test
  public void testCreateRecordType() {
    // Create a simple record type.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("RecordType<{name: java.lang.String, age: java.lang.Integer}>",
        recordType.getDisplayName());

    // Test property access.
    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);

    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));

    // Test property constraints.
    RecordType recordTypeWithUnmatchedType =
        new RecordType(registry, properties);
    RecordType recordTypeWithMismatchedType =
        new RecordType(registry, properties);
    recordTypeWithMismatchedType.defineProperty("name", new IntType(1), false, null);

    assertFalse(recordTypeWithUnmatchedType.isSubtype(recordTypeWithMismatchedType));
  }

  @Test
  public void testGetLeastSupertype() {
    // Create some simple record types.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", Integer.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", String.class));
    propertiesB.put("y", new RecordProperty("y", String.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test getLeastSupertype with empty record types.
    assertEquals(recordTypeA, recordTypeA.getLeastSupertype(recordTypeA));
    assertEquals(recordTypeB, recordTypeB.getLeastSupertype(recordTypeB));

    // Test getLeastSupertype with record types with no common properties.
    assertEquals(recordTypeA, recordTypeA.getLeastSupertype(recordTypeB));
    assertEquals(recordTypeB, recordTypeB.getLeastSupertype(recordTypeA));

    // Test getLeastSupertype with record types with common properties.
    RecordType recordTypeC = new RecordType(registry, propertiesB);
    UnionType leastSupertype = recordTypeA.getLeastSupertype(recordTypeC);
    assertTrue(leastSupertype.isUnionType());
    assertEquals(2, leastSupertype.getAlternates().size());
    assertTrue(leastSupertype.getAlternates().contains(recordTypeA));
    assertTrue(leastSupertype.getAlternates().contains(recordTypeC));

    // Test getLeastSupertype with record types with conflicting properties.
    RecordType recordTypeD = new RecordType(registry, Maps.newLinkedHashMap());
    recordTypeD.defineProperty("x", new IntType(1), false, null);
    RecordType recordTypeE = new RecordType(registry, propertiesB);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
        recordTypeA.getLeastSupertype(recordTypeD));
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
        recordTypeD.getLeastSupertype(recordTypeA));
  }

  @Test
  public void testGetGreatestSubtype() {
    // Create some simple record types.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", Integer.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", String.class));
    propertiesB.put("y", new RecordProperty("y", String.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test getGreatestSubtype with empty record types.
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeB));

    // Test getGreatestSubtype with record types with no common properties.
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeA));

    // Test getGreatestSubtype with record types with common properties.
    RecordType recordTypeC = new RecordType(registry, propertiesB);
    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeC);
    assertEquals(recordTypeA, greatestSubtype);

    // Test getGreatestSubtype with record types with conflicting properties.
    RecordType recordTypeD = new RecordType(registry, Maps.newLinkedHashMap());
    recordTypeD.defineProperty("x", new IntType(1), false, null);
    RecordType recordTypeE = new RecordType(registry, propertiesB);
    assertEquals(recordTypeB, recordTypeE.getGreatestSubtype(recordTypeD));
    assertEquals(recordTypeD, recordTypeD.getGreatestSubtype(recordTypeE));
  }

  @Test
  public void testIsEquivalentTo() {
    // Create some simple record types.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", Integer.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", String.class));
    propertiesB.put("y", new RecordProperty("y", String.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test isEquivalentTo with empty record types.
    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue(recordTypeB.isEquivalentTo(recordTypeB));

    // Test isEquivalentTo with record types with no common properties.
    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    assertFalse(recordTypeB.isEquivalentTo(recordTypeA));

    // Test isEquivalentTo with record types with common properties.
    RecordType recordTypeC = new RecordType(registry, propertiesB);
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    assertTrue(recordTypeC.isEquivalentTo(recordTypeA));

    // Test isEquivalentTo with record types with conflicting properties.
    RecordType recordTypeD = new RecordType(registry, Maps.newLinkedHashMap());
    recordTypeD.defineProperty("x", new IntType(1), false, null);
    RecordType recordTypeE = new RecordType(registry, propertiesB);
    assertFalse(recordTypeA.isEquivalentTo(recordTypeD));
    assertFalse(recordTypeD.isEquivalentTo(recordTypeA));
  }
}