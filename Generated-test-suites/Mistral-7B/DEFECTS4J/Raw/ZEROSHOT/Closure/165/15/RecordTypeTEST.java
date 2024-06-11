import static org.junit.Assert.*;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.RecordType;
import com.google.javascript.jscomp.type.RecordTypeBuilder;
import com.google.javascript.jscomp.type.TypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType expectedRecordType = recordBuilder.buildRecordType("Person", recordProperties);

    RecordType actualRecordType = new RecordType(registry, recordProperties);
    assertEquals(expectedRecordType.getName(), actualRecordType.getName());
    assertEquals(expectedRecordType.getProperties().size(), actualRecordType.getProperties().size());
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithSameProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties);

    Map<String, RecordProperty> childRecordProperties = Maps.newHashMap();
    childRecordProperties.putAll(recordProperties);
    childRecordProperties.put("gender", new RecordProperty("gender", String.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", childRecordProperties);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithDifferentProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("color", new RecordProperty("color", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertFalse(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithEqualPropertiesButDifferentNames() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name1", new RecordProperty("name1", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name2", new RecordProperty("name2", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("name1", new RecordProperty("name1", String.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithMissingProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithDifferentDeclarationOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("age", new RecordProperty("age", Integer.class));
    recordProperties1.put("name", new RecordProperty("name", String.class));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithPropertyWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", TypeFactory.unknownType()));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithPropertyWithEmptyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", JSType.EMPTY));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithPropertyWithNoType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", null));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithPropertyWithArrayType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));
    recordProperties1.put("numbers", new RecordProperty("numbers", JSType.ARRAY_TYPE, new Node(Node.NULL)));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("numbers", new RecordProperty("numbers", JSType.ARRAY_TYPE, new Node(Node.INT_LITERAL(3))));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithPropertyWithUnknownArrayType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));
    recordProperties1.put("numbers", new RecordProperty("numbers", JSType.UNKNOWN_ARRAY_TYPE, new Node(Node.NULL)));

    RecordType parentRecordType = recordBuilder.buildRecordType("ParentRecordType", recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("numbers", new RecordProperty("numbers", JSType.ARRAY_TYPE, new Node(Node.INT_LITERAL(3))));

    RecordType childRecordType = recordBuilder.buildRecordType("ChildRecordType", recordProperties2);

    assertTrue(parentRecordType.isSubtype(childRecordType));
  }
}