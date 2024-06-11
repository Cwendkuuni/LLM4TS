import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Node;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.SortedMap;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @Test
  public void testConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType = new RecordType(registry, properties);

    Assert.assertFalse(recordType.isFrozen());

    SortedMap<String, JSType> sortedMap = recordType.properties;
    Assert.assertEquals(1, sortedMap.size());
    Assert.assertEquals(Integer.TYPE, sortedMap.get("prop1"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties2.put("prop2", new RecordProperty("prop2", String.TYPE));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties3.put("prop2", new RecordProperty("prop2", Double.TYPE));
    RecordType recordType3 = new RecordType(registry, properties3);

    Assert.assertTrue(recordType1.isEquivalentTo(recordType1));
    Assert.assertTrue(recordType1.isEquivalentTo(recordType2)); // edge case: identical properties
    Assert.assertFalse(recordType1.isEquivalentTo(recordType3)); // different properties
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap());
    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWhenFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;
    recordType.defineProperty("prop1", Integer.TYPE, false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", String.TYPE));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties3.put("prop2", new RecordProperty("prop2", Integer.TYPE));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties4.put("prop2", new RecordProperty("prop2", Integer.TYPE));
    RecordType recordType4 = new RecordType(registry, properties4);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    Assert.assertEquals(recordType1, greatestSubtype); // record types with same properties are equal

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType3);
    JSType unionType = unionTypeBuilder.build();
    Assert.assertEquals(unionType, greatestSubtype); // type with all properties of the first type and some additional properties is a subtype

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
    Assert.assertEquals(recordType1, greatestSubtype); // identical record types are equal

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NO_TYPE));
    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype); // record types are not subtypes of NO_TYPE
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties2.put("prop2", new RecordProperty("prop2", String.TYPE));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties3.put("prop2", new RecordProperty("prop2", Double.TYPE));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties4.put("prop2", new RecordProperty("prop2", Integer.TYPE));
    RecordType recordType4 = new RecordType(registry, properties4);

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType5 = new RecordType(registry, properties5);

    Map<String, RecordProperty> properties6 = Maps.newHashMap();
    properties6.put("prop1", new RecordProperty("prop1", String.TYPE));
    RecordType recordType6 = new RecordType(registry, properties6);

    Map<String, RecordProperty> properties7 = Maps.newHashMap();
    properties7.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    properties7.put("prop2", new RecordProperty("prop2", Double.TYPE));
    properties7.put("prop3", new RecordProperty("prop3", Integer.TYPE));
    RecordType recordType7 = new RecordType(registry, properties7);

    Assert.assertTrue(recordType1.isSubtype(recordType1));
    Assert.assertTrue(recordType1.isSubtype(recordType4)); // identical record types are subtypes of each other
    Assert.assertTrue(recordType1.isSubtype(recordType5)); // record type with fewer properties is a subtype of a record type with the same properties but more inferred
    Assert.assertFalse(recordType1.isSubtype(recordType2)); // record type with different properties is not a subtype
    Assert.assertFalse(recordType1.isSubtype(recordType3)); // record type with different properties is not a subtype
    Assert.assertFalse(recordType1.isSubtype(recordType6)); // record type with incompatible properties is not a subtype
    Assert.assertFalse(recordType1.isSubtype(recordType7)); // record type with more properties is not a subtype of a record type with fewer properties
  }
}