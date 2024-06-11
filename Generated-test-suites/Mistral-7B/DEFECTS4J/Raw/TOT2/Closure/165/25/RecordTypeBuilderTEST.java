import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    JSType type = JSType.STRING;
    Node propertyNode = new Node(); // Create a mock node for testing

    RecordProperty recordProperty = recordTypeBuilder.addProperty("name", type, propertyNode);
    assertNotNull(recordProperty);
    assertTrue(recordTypeBuilder.isEmpty() == false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddProperty_NullRegistry() {
    recordTypeBuilder = new RecordTypeBuilder(null);
    JSType type = JSType.STRING;
    Node propertyNode = new Node(); // Create a mock node for testing
    recordTypeBuilder.addProperty("name", type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddProperty_NullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    JSType type = JSType.STRING;
    recordTypeBuilder.addProperty("name", type, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddProperty_NullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    Node propertyNode = new Node(); // Create a mock node for testing
    recordTypeBuilder.addProperty("name", null, propertyNode);
  }

  @Test
  public void testAddProperty_DuplicatePropertyName() {
    JSType type = JSType.STRING;
    Node propertyNode1 = new Node(); // Create a mock node for testing
    Node propertyNode2 = new Node(); // Create a mock node for testing

    RecordProperty recordProperty1 = recordTypeBuilder.addProperty("name", type, propertyNode1);
    assertNotNull(recordProperty1);

    RecordProperty recordProperty2 = recordTypeBuilder.addProperty("name", type, propertyNode2);
    assertNull(recordProperty2);
  }

  @Test
  public void testBuild() {
    JSType type1 = JSType.STRING;
    Node propertyNode1 = new Node(); // Create a mock node for testing
    RecordProperty recordProperty1 = recordTypeBuilder.addProperty("name1", type1, propertyNode1);

    JSType type2 = JSType.NUMBER;
    Node propertyNode2 = new Node(); // Create a mock node for testing
    RecordProperty recordProperty2 = recordTypeBuilder.addProperty("name2", type2, propertyNode2);

    RecordType recordType = recordTypeBuilder.build();
    assertTrue(recordType instanceof RecordType);
    HashMap<String, RecordProperty> propertiesMap = ((RecordType) recordType).getProperties();
    assertEquals(2, propertiesMap.size());
    assertTrue(propertiesMap.containsKey("name1"));
    assertTrue(propertiesMap.containsKey("name2"));
  }

  @Test
  public void testBuild_EmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    RecordType recordType = recordTypeBuilder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertTrue(recordType == registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }
}