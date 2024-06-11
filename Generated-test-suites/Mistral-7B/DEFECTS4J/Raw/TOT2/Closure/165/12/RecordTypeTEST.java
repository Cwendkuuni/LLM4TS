import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE)));
    properties.put("age", new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));

    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testRecordTypeCreation() {
    assertNotNull(recordType);
    assertEquals(JSType.Type.OBJECT, recordType.getType());
  }

  @Test
  public void testDefineProperty() {
    recordType.defineProperty("gender", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    assertNotNull(recordType.getPropertyType("gender"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyAfterFreeze() {
    recordType.isFrozen = true;
    recordType.defineProperty("gender", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertEquals(recordType1, recordType2);
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> differentProperties = new LinkedHashMap<>();
    differentProperties.put("name", new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE)));
    differentProperties.put("age", new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    differentProperties.put("gender", new RecordProperty("gender", registry.getNativeType(JSTypeNative.STRING_TYPE)));

    RecordType differentRecord = new RecordType(registry, differentProperties);

    assertFalse(recordType.isEquivalentTo(differentRecord));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefinePropertyTwice() {
    recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    assertEquals(recordType.getPropertyType("name"), recordType.getPropertyType("name"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType firstRecord = new RecordType(registry, properties);
    RecordType secondRecord = new RecordType(registry, Arrays.asList("name", "age", "gender"));
    RecordType thirdRecord = new RecordType(registry, Arrays.asList("name", "age"));

    assertEquals(firstRecord, firstRecord.getGreatestSubtypeHelper(secondRecord));
    assertEquals(secondRecord, secondRecord.getGreatestSubtypeHelper(thirdRecord));
    assertEquals(firstRecord, firstRecord.getGreatestSubtypeHelper(thirdRecord));
  }

  @Test
  public void testEqualsToEmptyObjectType() {
    assertEquals(recordType, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordTypeSubtype = new RecordType(registry, Arrays.asList("name", "age", "gender"));
    RecordType recordTypeSupertype = new RecordType(registry, Arrays.asList("name", "age"));

    assertTrue(recordTypeSupertype.isSubtype(recordTypeSubtype));
  }

  @Test
  public void testIsSubtypeWithDifferentPropertyTypes() {
    RecordType recordTypeA = new RecordType(registry, Arrays.asList("name", "age", "number"));
    RecordType recordTypeB = new RecordType(registry, Arrays.asList("name", "age", "string"));

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithDifferentPropertyNames() {
    RecordType recordTypeA = new RecordType(registry, Arrays.asList("name", "age", "number"));
    RecordType recordTypeB = new RecordType(registry, Arrays.asList("age", "name", "number"));

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithEmptyRecordType() {
    RecordType emptyRecordType = new RecordType(registry, Collections.emptyMap());
    RecordType recordType = new RecordType(registry, Arrays.asList("name", "age"));

    assertFalse(emptyRecordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithUnknownTypeProperty() {
    RecordType recordTypeA = new RecordType(registry, Arrays.asList("name", "age", new UnknownType()));
    RecordType recordTypeB = new RecordType(registry, Arrays.asList("name", "age"));

    assertTrue(recordTypeB.isSubtype(recordTypeA));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsSubtypeWithNullRecordType() {
    RecordType recordType = new RecordType(registry, Arrays.asList("name", "age"));

    recordType.isSubtype(null);
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(registry, properties);
    Node namePropertyNode = new NameNode("name");
    Node agePropertyNode = new NameNode("age");

    recordType.resolveInternal(null, new StaticScope<>(registry));

    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("name"));
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("age"));
  }
}