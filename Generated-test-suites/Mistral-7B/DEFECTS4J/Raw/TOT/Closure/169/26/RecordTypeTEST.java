import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeWithSimpleProperties() {
    TypeRegistry registry = new TypeRegistry();
    RecordProperty aProperty = new RecordProperty("a", registry.getStringType());
    RecordProperty bProperty = new RecordProperty("b", registry.getIntType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", aProperty);
    properties.put("b", bProperty);

    RecordType recordType = new RecordType(registry, properties);

    // Test setting and getting properties
    assertEquals("a", recordType.getPropertyNames()[0]);
    assertEquals("String", recordType.getPropertyType(0).getTypeName());

    Node aNode = new Node("a");
    recordType.defineProperty("a", registry.getStringType(), false, aNode);

    Node bNode = new Node("b");
    recordType.defineProperty("b", registry.getIntType(), false, bNode);

    // Test setting and getting values
    recordType.setProperty("a", "hello");
    assertEquals("hello", recordType.getProperty("a"));

    recordType.setProperty("b", 42);
    assertEquals(42, recordType.getProperty("b"));

    // Test setting and getting values with nodes
    recordType.setProperty("a", aNode);
    assertEquals("", aNode.toString());

    recordType.setProperty("b", bNode);
    assertEquals(JSTypeNative.NULL, bNode.getValueType());

    // Test property access with incorrect property name
    try {
      recordType.getProperty("c");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test property access with null property name
    try {
      recordType.getProperty(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test subtyping
    RecordProperty cProperty = new RecordProperty("c", registry.getIntType());
    RecordType subType = new RecordType(registry, Maps.newHashMap("c", cProperty));

    RecordType parentType = new RecordType(registry, properties);
    RecordType intersectionType = parentType.getGreatestSubtype(subType);

    // Test property exists
    assertTrue(intersectionType.hasProperty("a"));
    assertTrue(intersectionType.hasProperty("b"));
    assertTrue(intersectionType.hasProperty("c"));

    // Test property types
    assertEquals(registry.getStringType(), intersectionType.getPropertyType("a"));
    assertEquals(registry.getIntType(), intersectionType.getPropertyType("b"));
    assertEquals(registry.getIntType(), intersectionType.getPropertyType("c"));

    // Test property nodes
    assertEquals(aNode, intersectionType.getPropertyNode("a"));
    assertEquals(bNode, intersectionType.getPropertyNode("b"));
    assertNull(intersectionType.getPropertyNode("c"));

    // Test equals
    RecordType equalRecordType = new RecordType(registry, properties);
    assertTrue(equalRecordType.equals(parentType));
    assertTrue(parentType.equals(parentType));

    // Test not equals
    RecordType notEqualRecordType = new RecordType(registry, Maps.newHashMap());
    assertFalse(parentType.equals(notEqualRecordType));

    // Test toString
    assertEquals("{a: String, b: Int}", parentType.toString());

    // Test frozen
    parentType.freeze();
    try {
      parentType.defineProperty("a", registry.getStringType(), false, aNode);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeWithInheritedProperties() {
    TypeRegistry registry = new TypeRegistry();
    RecordProperty aProperty = new RecordProperty("a", registry.getIntType());
    RecordProperty bProperty = new RecordProperty("b", registry.getStringType());

    Map<String, RecordProperty> parentProperties = Maps.newHashMap();
    parentProperties.put("a", aProperty);

    Map<String, RecordProperty> childProperties = Maps.newHashMap();
    childProperties.put("b", bProperty);
    childProperties.putAll(parentProperties);

    RecordType parentType = new RecordType(registry, parentProperties, false);
    RecordType childType = new RecordType(registry, childProperties, false);

    // Test setting and getting properties
    assertEquals("a", childType.getPropertyNames()[0]);
    assertEquals("Int", childType.getPropertyType(0).getTypeName());
    assertEquals("b", childType.getPropertyNames()[1]);
    assertEquals("String", childType.getPropertyType(1).getTypeName());

    // Test setting and getting values
    childType.setProperty("a", 42);
    assertEquals(42, childType.getProperty("a"));

    childType.setProperty("b", "hello");
    assertEquals("hello", childType.getProperty("b"));

    // Test setting and getting values with nodes
    Node aNode = new Node("a");
    childType.defineProperty("a", registry.getIntType(), false, aNode);

    Node bNode = new Node("b");
    childType.defineProperty("b", registry.getStringType(), false, bNode);

    // Test property access with incorrect property name
    try {
      childType.getProperty("c");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test property access with null property name
    try {
      childType.getProperty(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test subtyping
    RecordProperty cProperty = new RecordProperty("c", registry.getIntType());
    RecordType subType = new RecordType(registry, Maps.newHashMap("c", cProperty));

    RecordType grandParentType = new RecordType(registry, parentProperties, false);
    RecordType intersectionType = childType.getGreatestSubtype(subType);

    // Test property exists
    assertTrue(intersectionType.hasProperty("a"));
    assertTrue(intersectionType.hasProperty("b"));
    assertTrue(intersectionType.hasProperty("c"));

    // Test property types
    assertEquals(registry.getIntType(), intersectionType.getPropertyType("a"));
    assertEquals(registry.getStringType(), intersectionType.getPropertyType("b"));
    assertEquals(registry.getIntType(), intersectionType.getPropertyType("c"));

    // Test property nodes
    assertEquals(aNode, intersectionType.getPropertyNode("a"));
    assertNull(intersectionType.getPropertyNode("c"));

    // Test equals
    RecordType equalChildType = new RecordType(registry, childProperties, false);
    assertTrue(equalChildType.equals(childType));
    assertTrue(childType.equals(childType));

    // Test not equals
    RecordType notEqualChildType = new RecordType(registry, Maps.newHashMap());
    assertFalse(childType.equals(notEqualChildType));

    // Test toString
    assertEquals("{a: Int, b: String, c: Int}", childType.toString());

    // Test frozen
    childType.freeze();
    try {
      childType.defineProperty("a", registry.getIntType(), false, aNode);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeWithEmptyProperties() {
    TypeRegistry registry = new TypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    // Test empty properties
    assertEquals(0, recordType.getPropertyNames().length);

    // Test empty property types
    assertEquals(JSTypeNative.VOID, recordType.getPropertyType(0));

    // Test empty property nodes
    assertNull(recordType.getPropertyNodes());

    // Test empty record equals itself
    assertTrue(recordType.equals(recordType));

    // Test empty record not equal to non-empty record
    RecordType nonEmptyRecord = new RecordType(registry, Maps.newHashMap("a", registry.getIntType()));
    assertFalse(recordType.equals(nonEmptyRecord));

    // Test toString
    assertEquals("{}", recordType.toString());
  }

  @Test
  public void testRecordTypeWithNullProperties() {
    TypeRegistry registry = new TypeRegistry();
    RecordType recordType = new RecordType(registry, null);

    // Test null properties
    assertNull(recordType.getPropertyNames());

    // Test null property types
    assertNull(recordType.getPropertyType(0));

    // Test null property nodes
    assertNull(recordType.getPropertyNodes());

    // Test null record equals itself
    assertTrue(recordType.equals(recordType));

    // Test null record not equal to non-null record
    RecordType nonNullRecord = new RecordType(registry, Maps.newHashMap("a", registry.getIntType()));
    assertFalse(recordType.equals(nonNullRecord));

    // Test toString
    assertEquals("null", recordType.toString());
  }
}