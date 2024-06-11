package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.UnknownType;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private static final ErrorReporter errorReporter = mock(ErrorReporter.class);
  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordType2;
  private RecordProperty recordProperty;
  private RecordProperty recordProperty2;
  private Map<String, RecordProperty> recordProperties;
  private Map<String, RecordProperty> recordProperties2;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordProperty = new RecordProperty("a", new IntType(registry));
    recordProperty2 = new RecordProperty("b", new NumberType(registry));
    recordProperties = Maps.newLinkedHashMap();
    recordProperties.put("a", recordProperty);
    recordProperties.put("b", recordProperty2);
    recordType = new RecordType(registry, recordProperties, true);

    recordProperty2 = new RecordProperty("b", new IntType(registry));
    recordProperties2 = Maps.newLinkedHashMap();
    recordProperties2.put("a", recordProperty);
    recordProperties2.put("b", recordProperty2);
    recordType2 = new RecordType(registry, recordProperties2, true);
  }

  @Test
  public void testIsSynthetic() {
    assertThat(recordType.isSynthetic()).isFalse();
    assertThat(recordType2.isSynthetic()).isFalse();
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    assertThat(recordType.checkRecordEquivalenceHelper(recordType, true)).isTrue();
    assertThat(recordType.checkRecordEquivalenceHelper(recordType2, true)).isFalse();
    assertThat(recordType2.checkRecordEquivalenceHelper(recordType, true)).isFalse();
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertThat(recordType.getImplicitPrototype()).isEqualTo(expected);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "c";
    JSType propertyType = new IntType(registry);
    Node propertyNode = mock(Node.class);

    recordType.defineProperty(propertyName, propertyType, false, propertyNode);

    Map<String, JSType> properties = recordType.properties;
    SortedMap<String, JSType> expectedProperties = new TreeMap<>();
    expectedProperties.put("a", recordProperty.getType());
    expectedProperties.put("b", recordProperty2.getType());
    expectedProperties.put("c", propertyType);

    assertThat(properties).isEqualTo(expectedProperties);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new RecordType(registry, Maps.newLinkedHashMap(), true);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertThat( greatestSubtype ).isInstanceOf(UnknownType.class);

    JSType thatRecordType = new RecordType(registry, recordProperties, true);
    greatestSubtype = recordType.getGreatestSubtypeHelper(thatRecordType);
    assertThat( greatestSubtype ).isEqualTo(recordType2);

    JSType thatRecordType2 = new RecordType(registry, recordProperties2, true);
    greatestSubtype = recordType2.getGreatestSubtypeHelper(thatRecordType2);
    assertThat( greatestSubtype ).isEqualTo(recordType);
  }

  @Test
  public void testIsSubtype() {
    JSType that = new RecordType(registry, recordProperties, true);
    JSType thatRecordType = new RecordType(registry, recordProperties2, true);
    JSType recordType3 = new RecordType(registry, recordProperties, true);
    JSType recordType4 = new RecordType(registry, recordProperties2, true);

    JSType thatTypeA = new RecordType(registry, Maps.newLinkedHashMap(), true);
    JSType thatTypeB = new RecordType(registry, Maps.newLinkedHashMap(), true);
    JSType thatTypeC = new IntType(registry);

    assertThat( recordType.isSubtype(recordType) ).isTrue();
    assertThat( recordType.isSubtype(that) ).isTrue();
    // RecordType is a subtype of ObjectType
    assertThat( recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)) ).isTrue();
    // ObjectType is not a subtype of RecordType
    assertThat( registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType) ).isFalse();
    // RecordType is not a subtype of primitive types
    assertThat( recordType.isSubtype(new BoolType(registry)) ).isFalse();
    assertThat( recordType.isSubtype(new IntType(registry)) ).isFalse();
    assertThat( recordType.isSubtype(new StringType(registry)) ).isFalse();
    // RecordType1 is a subtype of RecordType2 if RecordType2 has all properties of RecordType1
    assertThat( recordType.isSubtype(thatRecordType) ).isTrue();
    // RecordType1 is a subtype of RecordType2 if the property types are equivalent
    assertThat( recordType.isSubtype(thatTypeA) ).isTrue();
    // RecordType1 is not a subtype of RecordType2 if the property types are not equivalent
    assertThat( recordType.isSubtype(thatTypeB) ).isFalse();
    // RecordType1 is a subtype of RecordType3 if RecordType1 and RecordType3 have the same properties
    assertThat( recordType3.isSubtype(recordType) ).isTrue();
    // RecordType1 is not a subtype of RecordType2 if RecordType1 and RecordType2 have different properties
    assertThat( recordType.isSubtype(recordType4) ).isFalse();
    // RecordType1 is not a subtype of RecordType2 if RecordType2 has additional properties
    assertThat( recordType.isSubtype(thatRecordType2) ).isFalse();
    // RecordType1 is a subtype of RecordTypeC if RecordTypeC is a subtype of the property type of RecordType1
    JSType propertyType = recordType.getPropertyType("a");
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(new IntType(registry));
    JSType greatestSubtype = propertyType.getLeastSupertype(builder.build());
    assertThat( recordType.isSubtype(thatTypeC) ).isTrue();
    // RecordType1 is not a subtype of RecordTypeC if RecordTypeC is not a subtype of the property type of RecordType1
    greatestSubtype = new NumberType(registry).getLeastSupertype(builder.build());
    assertThat( recordType.isSubtype(thatTypeC) ).isFalse();
  }
}