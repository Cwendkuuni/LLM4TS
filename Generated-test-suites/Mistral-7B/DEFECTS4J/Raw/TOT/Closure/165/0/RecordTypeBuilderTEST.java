import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTest.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

  JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeBuilder() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Test adding a single property
    JSType type1 = JSType.STRING;
    Node propertyNode1 = createNode("prop1: String");
    RecordProperty property1 = builder.addProperty("prop1", type1, propertyNode1);
    assertNotNull(property1);

    // Test building the record type
    RecordType recordType = builder.build();
    assertInstanceOf(RecordType.class, recordType);

    Map<String, JSType> expectedProperties = Maps.newHashMap();
    expectedProperties.put("prop1", type1);
    Map<String, JSType> actualProperties = recordType.getProperties();
    assertEquals(expectedProperties, actualProperties);

    // Test adding a duplicate property
    String duplicateName = "prop1";
    JSType duplicateType = JSType.NUMBER;
    Node duplicatePropertyNode = createNode("prop1: Number");
    RecordProperty duplicateProperty = builder.addProperty(duplicateName, duplicateType, duplicatePropertyNode);
    assertNull(duplicateProperty);

    // Test adding multiple properties
    JSType type2 = JSType.BOOLEAN;
    Node propertyNode2 = createNode("prop2: Boolean");
    RecordProperty property2 = builder.addProperty("prop2", type2, propertyNode2);
    assertNotNull(property2);

    JSType type3 = JSType.ARRAY_TYPE.make(JSType.INT);
    Node propertyNode3 = createNode("prop3: int[]");
    RecordProperty property3 = builder.addProperty("prop3", type3, propertyNode3);
    assertNotNull(property3);

    // Test building the record type with multiple properties
    recordType = builder.build();
    expectedProperties.put("prop2", type2);
    expectedProperties.put("prop3", type3);
    actualProperties = recordType.getProperties();
    assertEquals(expectedProperties, actualProperties);
  }

  private Node createNode(String nodeValue) {
    return parseAndReturnRootAsNode(nodeValue);
  }
}