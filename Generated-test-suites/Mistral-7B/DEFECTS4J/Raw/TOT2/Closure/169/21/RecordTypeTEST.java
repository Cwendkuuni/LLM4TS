import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));
    recordType = new RecordType(registry, properties, true);
  }

  @Test
  public void testBasicFunctionality() {
    assertEquals("{a: number, b: string}", recordType.toString());
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testSubtyping() {
    Map<String, RecordProperty> subProperties = new HashMap<>();
    subProperties.putAll(recordType.properties);
    subProperties.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType subType = new RecordType(registry, subProperties, false);
    assertTrue(recordType.isSubtype(subType));

    // Record with same properties but different order
    Map<String, RecordProperty> reverseProperties = new HashMap<>();
    reverseProperties.putAll(recordType.properties);
    reverseProperties.put("b", reverseProperties.remove("a"));

    RecordType reverseRecord = new RecordType(registry, reverseProperties, true);
    assertTrue(recordType.isSubtype(reverseRecord));

    // Record with same properties but different types
    Map<String, RecordProperty> differentProperties = new HashMap<>();
    differentProperties.putAll(recordType.properties);
    differentProperties.put("a", new RecordProperty("a", registry.getBooleanType()));

    RecordType differentRecord = new RecordType(registry, differentProperties, true);
    assertFalse(recordType.isSubtype(differentRecord));

    // Record with extra property
    Map<String, RecordProperty> extraProperties = new HashMap<>();
    extraProperties.putAll(recordType.properties);
    extraProperties.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType extraRecord = new RecordType(registry, extraProperties, true);
    assertFalse(recordType.isSubtype(extraRecord));
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException() {
    Map<String, RecordProperty> illegalProperties = new HashMap<>();
    illegalProperties.put("a", null);
    new RecordType(registry, illegalProperties, true);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> subProperties2 = new HashMap<>();
    subProperties2.put("a", new RecordProperty("a", registry.getNumberType()));
    subProperties2.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType subType2 = new RecordType(registry, subProperties2, false);
    RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(subType2);
    assertEquals(recordType, greatestSubtype);

    Map<String, RecordProperty> subProperties3 = new HashMap<>();
    subProperties3.put("a", new RecordProperty("a", registry.getBooleanType()));

    RecordType subType3 = new RecordType(registry, subProperties3, false);
    RecordType greatestSubtype3 = recordType.getGreatestSubtypeHelper(subType3);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype3);

    Map<String, RecordProperty> subProperties4 = new HashMap<>();
    subProperties4.putAll(recordType.properties);
    subProperties4.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType subType4 = new RecordType(registry, subProperties4, false);
    RecordType greatestSubtype4 = recordType.getGreatestSubtypeHelper(subType4);
    assertEquals(subType4, greatestSubtype4);
  }

  @Test
  public void testIsSubtype() {
    // Test subtyping with RecordType
    RecordType subTypeRecord = new RecordType(registry, recordType.properties, false);
    assertTrue(recordType.isSubtype(subTypeRecord));

    // Test subtyping with ObjectType
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(recordType.isSubtype(objectType));

    // Test subtyping with UnionType
    UnionType unionType =
        new UnionType(registry, Collections.singletonList(registry.getNumberType()));
    assertFalse(recordType.isSubtype(unionType));

    // Test subtyping with IntersectionType
    IntersectionType intersectionType =
        new IntersectionType(registry, Collections.singletonList(recordType));
    assertTrue(recordType.isSubtype(intersectionType));
  }
}