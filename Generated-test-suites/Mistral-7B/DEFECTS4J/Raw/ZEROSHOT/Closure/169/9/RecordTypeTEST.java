import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @SuppressWarnings("unchecked")
  private static <T> T newRecordType(JSTypeRegistry registry, String[] propertyNames,
      Class<? extends JSType>[] propertyTypes) {
    Map<String, RecordProperty> properties = new HashMap<>();
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      JSType propertyType = null;
      try {
        propertyType = registry.getNewInstance(propertyTypes[i]);
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      }
      properties.put(propertyName, new RecordProperty(propertyName, propertyType));
    }
    return (T) new RecordType(registry, properties, true);
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type1 = newRecordType(registry, new String[]{"a", "b"},
        new Class<?>[]{Integer.class, Float.class});
    RecordType type2 = newRecordType(registry, new String[]{"b", "a"},
        new Class<?>[]{Float.class, Integer.class});
    RecordType type3 = newRecordType(registry, new String[]{"a", "b", "c"},
        new Class<?>[]{Integer.class, Float.class, String.class});

    // type1 and type2 are not equivalent since their property orders are different
    assertFalse(type1.checkRecordEquivalenceHelper(type2, true));

    // type1 and type3 are not equivalent since they have different properties
    assertFalse(type1.checkRecordEquivalenceHelper(type3, true));

    // type1 and type1 are equivalent since they have the same properties
    assertTrue(type1.checkRecordEquivalenceHelper(type1, true));

    // type2 and type2 are equivalent since they have the same properties
    assertTrue(type2.checkRecordEquivalenceHelper(type2, true));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type1 = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});
    RecordType type2 = newRecordType(registry, new String[]{"a", "b"},
        new Class<?>[]{Integer.class, String.class});
    RecordType type3 = newRecordType(registry, new String[]{"b"},
        new Class<?>[]{String.class});
    RecordType type4 = newRecordType(registry, new String[]{},
        new Class<?>[]{Object.class});
    RecordType type5 = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{String.class});

    // type1 is a subtype of type2 since all properties of type2 are
    // a subtype of the corresponding properties in type1
    assertTrue(type1.isSubtype(type2));

    // type2 is not a subtype of type3 since type3 has an additional property
    assertFalse(type2.isSubtype(type3));

    // type3 is not a subtype of type1 since it lacks the required property "a"
    assertFalse(type3.isSubtype(type1));

    // type4 is a subtype of any record type since the empty record is a supertype
    // of any record type
    assertTrue(type4.isSubtype(type1));
    assertTrue(type4.isSubtype(type2));
    assertTrue(type4.isSubtype(type3));

    // type5 is not a subtype of type1 since it has a different property type
    assertFalse(type5.isSubtype(type1));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});
    RecordType recordType2 = newRecordType(registry, new String[]{"b"},
        new Class<?>[]{Float.class});
    RecordType recordType3 = newRecordType(registry, new String[]{"a", "b"},
        new Class<?>[]{Integer.class, Float.class});

    // type A is a subtype of type B, and type B is a subtype of type C,
    // so the greatest subtype of type A and type C is type B
    assertEquals(recordType2, recordType1.getGreatestSubtype(recordType3));
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});

    // Define a new inferred property
    recordType.defineProperty("b", registry.getNewInstance(String.class),
        false, Node.createIntLiteral(0));

    // Check that the new property exists
    assertTrue(recordType.hasProperty("b"));
    assertEquals(registry.getNewInstance(String.class),
        recordType.getPropertyType("b"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});

    // Freeze the record type
    recordType.setFrozen(true);

    // Attempt to define a new property
    recordType.defineProperty("b", registry.getNewInstance(String.class),
        false, Node.createIntLiteral(0));

    // An exception should be thrown since the record type is frozen
    fail("Expected IllegalStateException");
  }

  @Test
  public void testRecordTypeSetFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});

    // Check that the record type is unfrozen by default
    assertFalse(recordType.isFrozen());

    // Freeze the record type
    recordType.setFrozen(true);
    assertTrue(recordType.isFrozen());

    // Attempt to unfroze the record type
    recordType.setFrozen(false);
    assertFalse(recordType.isFrozen());

    // Refreeze the record type
    recordType.setFrozen(true);
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testRecordTypeUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = newRecordType(registry, new String[]{"a"},
        new Class<?>[]{Integer.class});
    RecordType recordType2 = newRecordType(registry, new String[]{"b"},
        new Class<?>[]{Float.class});
    RecordType recordType3 = newRecordType(registry, new String[]{"a", "b"},
        new Class<?>[]{Integer.class, Float.class});

    // Create a union type that includes type1 and type2
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    JSType unionType = unionTypeBuilder.build();

    // Check that the union type is a supertype of both type1 and type2
    assertTrue(recordType1.isSubtype(unionType));
    assertTrue(recordType2.isSubtype(unionType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = newRecordType(registry, new String[]{},
        new Class<?>[]{Object.class});

    // Check that a record type is a subtype of Object.TYPE
    assertTrue(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
  }
}