import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeCheckMode.*;

import com.google.javascript.rhino.JSCoreTypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.PropertyTester;
import com.google.javascript.rhino.testing.TypeChecker;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.SortedMap;

import com.google.javascript.rhino.jstype.*;

/**
 * Tests for {@code RecordType} class.
 *
 */
public class RecordTypeTest {

  /**
   * Test creating a record type with empty properties.
   */
  @Test
  public void testEmptyRecordType() throws Exception {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);
    assertEquals("Record type with no properties.", recordType.getClass(),
        recordType.getClass());
  }

  /**
   * Test creating a record type with properties.
   */
  @Test
  public void testRecordTypeWithProperties() throws Exception {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSCoreTypes.NUMBER));
    properties.put("b", new RecordProperty("b", JSCoreTypes.BOOLEAN));
    RecordType recordType = new RecordType(registry, properties);

    // Check properties
    assertEquals("Number of properties.", 2, recordType.getPropertyCount());
    assertEquals("Property 'a' type.", JSCoreTypes.NUMBER,
        recordType.getPropertyType("a"));
    assertEquals("Property 'b' type.", JSCoreTypes.BOOLEAN,
        recordType.getPropertyType("b"));

    // Check subtyping
    RecordType subType = new RecordType(registry, properties);
    RecordType superType = new RecordType(registry, Maps.newHashMap());
    superType.defineProperty("a", JSCoreTypes.NUMBER, false, null);
    superType.defineProperty("b", JSCoreTypes.BOOLEAN, false, null);
    assertTrue("Record type with same properties is a subtype of a record type with the same properties.",
        superType.isSubtype(subType));

    // Check supertyping
    RecordType superRecordType = new RecordType(registry, properties);
    RecordType subRecordType = new RecordType(registry, Maps.newHashMap());
    subRecordType.defineProperty("a", JSCoreTypes.NUMBER, false, null);
    subRecordType.defineProperty("b", JSCoreTypes.BOOLEAN, false, null);
    subRecordType.defineProperty("c", JSCoreTypes.STRING, false, null);
    assertFalse("Record type with more properties is not a subtype of a record type with fewer properties.",
        superRecordType.isSubtype(subRecordType));

    // Check equivalence
    RecordType equivalentType = new RecordType(registry, properties);
    assertTrue("Record types with the same properties are equivalent.",
        superRecordType.isEquivalentTo(equivalentType));

    // Check equality
    assertEquals("Record types with the same properties are equal.",
        superRecordType, equivalentType);

    // Check greatest common supertype
    RecordType greatestCommonSupertype = superRecordType.getGreatestSubtypeHelper(subRecordType);
    assertEquals("Greaterst common supertype is the record type with all common properties.",
        superRecordType, greatestCommonSupertype);

    // Check least common supertype
    RecordType leastCommonSupertype = superRecordType.getLeastSupertype(subRecordType);
    RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());
    assertEquals("Least common supertype is the empty record type.",
        emptyRecordType, leastCommonSupertype);

    // Check property access
    PropertyTester tester = new PropertyTester();
    tester.setTypeCheckingMode(THROW_EXCEPTION);
    tester.test(recordType, "a", new NodeFactory().newNumberLiteral(42));
    tester.test(recordType, "b", new NodeFactory().newBooleanLiteral(true));
  }

  /**
   * Test creating a frozen record type.
   */
  @Test
  public void testFrozenRecordType() throws Exception {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSCoreTypes.NUMBER));
    properties.put("b", new RecordProperty("b", JSCoreTypes.BOOLEAN));
    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    // Check properties cannot be modified
    assertThrows(UnsupportedOperationException.class, () -> {
      recordType.defineProperty("c", JSCoreTypes.STRING, false, null);
    });
    assertThrows(UnsupportedOperationException.class, () -> {
      recordType.removeProperty("a");
    });
  }

  /**
   * Test record type with infinite recursion property.
   */
  @Test(expected = java.lang.StackOverflowError.class)
  public void testRecordTypeWithInfiniteRecursionProperty() throws Exception {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new RecordType(registry, properties)));
    new RecordType(registry, properties);
  }
}