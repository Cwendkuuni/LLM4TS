package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordType2;
  private RecordType recordType3;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    registry = mock(JSTypeRegistry.class);
    recordType = new RecordType(registry, Maps.newHashMap(), true);
    recordType2 = new RecordType(registry, Maps.newHashMap(), true);
    recordType3 = new RecordType(registry, Maps.newHashMap(), true);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        new ObjectType(registry, null, null) {
          @Override
          public boolean isEquivalentTo(ObjectType that) {
            return true;
          }
        });
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));
    properties.put("b", new RecordProperty("b", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>(properties);

    // Both record types have the same properties, so they should be equivalent
    assertThat(recordType.checkRecordEquivalenceHelper(recordType2, false)).isTrue();

    // Add some extra properties to record type 2
    properties.put("c", new RecordProperty("c", new IntType(registry)));
    recordType2.properties = new TreeMap<>(properties);

    // Record types are not equivalent since record type 1 does not have property "c"
    assertThat(recordType.checkRecordEquivalenceHelper(recordType2, false)).isFalse();

    // Allow unknown properties and check for equivalence
    assertThat(recordType.checkRecordEquivalenceHelper(recordType2, true)).isTrue();
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>(properties);

    // Both record types have the same properties, so they have the same greatest subtype
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
    assertNull(greatestSubtype);

    // Record type 2 has an additional property, so the greatest subtype cannot be the empty record
    properties.put("b", new RecordProperty("b", new IntType(registry)));
    recordType2.properties = new TreeMap<>(properties);

    // The greatest subtype is the empty record since neither record has a common supertype
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
    assertNull(greatestSubtype);

    // Make record type 2 a subtype of record type 1
    recordType.properties.put("b", new RecordProperty("b", new IntType(registry)));
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);

    // The greatest subtype is the record type with all common properties
    JSType expectedGreatestSubtype = new RecordTypeBuilder(registry)
        .addProperty("a", new IntType(registry), null)
        .addProperty("b", new IntType(registry), null)
        .build();
    assertThat(greatestSubtype).isEqualTo(expectedGreatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>(properties);

    // Record type 1 is a subtype of record type 1
    assertTrue(recordType.isSubtype(recordType));

    // Record type 1 is a subtype of the empty record since every record is a subtype of the empty record
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Record type 1 is not a subtype of record type 2 since they have different properties
    assertFalse(recordType.isSubtype(recordType2));

    // Record type 1 is a subtype of itself
    assertTrue(recordType.isSubtype(recordType));

    // Record type 2 is a subtype of record type 1 since it has all the properties of record type 1
    assertTrue(recordType2.isSubtype(recordType));

    // Record type 1 is a subtype of a record type with the same properties but different order
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", new IntType(registry)));
    properties2.put("a", new RecordProperty("a", new IntType(registry)));
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    recordType3.properties = new TreeMap<>(properties2);

    assertTrue(recordType.isSubtype(recordType3));

    // Record type 1 is a subtype of a record type with an additional property
    properties2.put("c", new RecordProperty("c", new IntType(registry)));
    recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    recordType3.properties = new TreeMap<>(properties2);

    assertFalse(recordType.isSubtype(recordType3));
  }

  @Test
  public void testIsSubtypeWithInferredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>();

    // Record type 1 is not a subtype of record type 2 since it misses the property "b"
    assertFalse(recordType.isSubtype(recordType2));

    // Record type 1 is a subtype of record type 2 if we add the missing property "b" to record type 2
    properties.put("b", new RecordProperty("b", new IntType(registry)));
    recordType2.properties = new TreeMap<>(properties);

    assertTrue(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithInferredPropertyOfDifferentType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>();

    // Record type 1 is not a subtype of record type 2 if the inferred property has a different type
    properties.put("b", new RecordProperty("b", new StringType(registry)));
    recordType2.properties = new TreeMap<>(properties);

    assertFalse(recordType.isSubtype(recordType2));

    // Record type 1 is a subtype of record type 2 if the inferred property has a compatible type
    properties.put("b", new RecordProperty("b", new ArrayType(registry, new IntType(registry))));
    recordType2.properties = new TreeMap<>(properties);

    assertTrue(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithInferredPropertyWithUnknownType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>();

    // Record type 1 is a subtype of record type 2 if the inferred property has an unknown type
    properties.put("b", new RecordProperty("b", new UnknownType(registry)));
    recordType2.properties = new TreeMap<>(properties);

    assertTrue(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithInferredPropertyWithInvariantType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", new IntType(registry)));

    recordType.properties = new TreeMap<>(properties);
    recordType2.properties = new TreeMap<>();

    // Record type 1 is not a subtype of record type 2 if the inferred property has a non-invariant type
    properties.put("b", new RecordProperty("b", new IntType(registry)) {
      @Override
      public boolean isInvariant(JSType type) {
        return false;
      }
    });
    recordType2.properties = new TreeMap<>(properties);

    assertFalse(recordType.isSubtype(recordType2));

    // Record type 1 is a subtype of record type 2 if the inferred property has an invariant type
    properties.put("b", new RecordProperty("b", new IntType(registry)) {
      @Override
      public boolean isInvariant(JSType type) {
        return true;
      }
    });
    recordType2.properties = new TreeMap<>(properties);

    assertTrue(recordType.isSubtype(recordType2));
  }
}