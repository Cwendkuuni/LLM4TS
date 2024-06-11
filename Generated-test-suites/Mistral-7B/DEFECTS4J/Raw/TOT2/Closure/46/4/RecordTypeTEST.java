import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("name", String.class, null)
        .addProperty("age", Integer.class, null)
        .build();

    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordWithLessProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordWithMoreProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfDifferentType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(registry, properties);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertFalse(recordType.isSubtype(objectType));
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("name"));
  }

  @Test
  public void testRecordTypeDoesNotHaveUnknownProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.hasProperty("unknownProperty"));
  }

  @Test
  public void testRecordTypeGetsPropertiesTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeGetsPropertiesNodes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class, null));
    properties.put("age", new RecordProperty("age", Integer.class, null));

    RecordType recordType = new RecordType(registry, properties);

    assertNull(recordType.getPropertyNode("name"));
    assertNull(recordType.getPropertyNode("age"));

    Node nameNode = Node.newProperty("name");
    Node ageNode = Node.newProperty("age");

    properties.put("name", new RecordProperty("name", String.class, nameNode));
    properties.put("age", new RecordProperty("age", Integer.class, ageNode));

    recordType = new RecordType(registry, properties);

    assertEquals(nameNode, recordType.getPropertyNode("name"));
    assertEquals(ageNode, recordType.getPropertyNode("age"));
  }
}