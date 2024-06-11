import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testRecordTypeBuilderEmpty() {
    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertTrue(recordType.getClass().equals(JSTypeNative.ObjectType.class));
  }

  @Test
  public void testAddPropertyValid() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
    assertNotNull(recordProperty);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertTrue(properties.containsKey(name));
  }

  @Test
  public void testAddPropertyDuplicate() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    builder.addProperty(name, type, propertyNode);
    assertNull(builder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testAddPropertyNullType() {
    String name = "testProperty";
    Node propertyNode = new Node();
    assertNull(builder.addProperty(name, null, propertyNode));
  }

  @Test
  public void testAddPropertyNullPropertyNode() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    assertNull(builder.addProperty(name, type, null));
  }

  @Test
  public void testAddPropertyEmptyName() {
    String name = "";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    assertNull(builder.addProperty(name, type, propertyNode));
  }

  @Test(expected = RuntimeException.class)
  public void testAddPropertyInvalidType() {
    String name = "testProperty";
    Node propertyNode = new Node();
    JSType invalidType = new JSTypeNative(123);
    builder.addProperty(name, invalidType, propertyNode);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertTrue(recordType.getClass().equals(JSTypeNative.ObjectType.class));
  }

  @Test
  public void testBuildRecordWithProperties() {
    String name1 = "testProperty1";
    String name2 = "testProperty2";
    JSType type1 = JSType.STRING;
    JSType type2 = JSType.NUMBER;
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();
    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertTrue(properties.containsKey(name1));
    assertTrue(properties.containsKey(name2));
  }

  @Test
  public void testBuildRecordWithDuplicateProperties() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    builder.addProperty(name, type, propertyNode);
    builder.addProperty(name, type, propertyNode);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertTrue(properties.size() == 1);
  }
}