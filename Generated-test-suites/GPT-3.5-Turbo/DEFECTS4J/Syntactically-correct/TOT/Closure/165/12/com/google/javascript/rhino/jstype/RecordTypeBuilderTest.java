import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

public class RecordTypeBuilderTest {
  
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }
  
  @Test
  public void testAddProperty() {
    JSType expectedType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node(Node.VAR, new Node(Node.NAME, "name"), new Node(Node.STRING, "Test Property"));
    
    recordTypeBuilder.addProperty("testProperty", expectedType, propertyNode);
    
    RecordTypeBuilder.RecordProperty actualProperty = recordTypeBuilder.getProperties().get("testProperty");
    
    Assert.assertNotNull(actualProperty);
    Assert.assertEquals(expectedType, actualProperty.getType());
    Assert.assertEquals(propertyNode, actualProperty.getPropertyNode());
  }
  
  @Test
  public void testAddDuplicateProperty() {
    JSType expectedType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode1 = new Node(Node.VAR, new Node(Node.NAME, "name1"), new Node(Node.NUMBER, "123"));
    Node propertyNode2 = new Node(Node.VAR, new Node(Node.NAME, "name2"), new Node(Node.NUMBER, "456"));
    
    recordTypeBuilder.addProperty("testProperty", expectedType, propertyNode1);
    recordTypeBuilder.addProperty("testProperty", expectedType, propertyNode2);
    
    RecordTypeBuilder.RecordProperty actualProperty = recordTypeBuilder.getProperties().get("testProperty");
    
    Assert.assertNull(actualProperty);
  }
  
  @Test
  public void testBuildEmptyRecord() {
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    JSType actualType = recordTypeBuilder.build();
    
    Assert.assertEquals(expectedType, actualType);
  }
  
  @Test
  public void testBuildRecordWithProperties() {
    JSType expectedType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType expectedType2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    
    Node propertyNode1 = new Node(Node.VAR, new Node(Node.NAME, "name1"), new Node(Node.STRING, "Test Property 1"));
    Node propertyNode2 = new Node(Node.VAR, new Node(Node.NAME, "name2"), new Node(Node.NUMBER, "123"));
    
    recordTypeBuilder.addProperty("property1", expectedType1, propertyNode1);
    recordTypeBuilder.addProperty("property2", expectedType2, propertyNode2);
    
    JSType actualType = recordTypeBuilder.build();
    
    RecordType actualRecordType = (RecordType) actualType;
    
    Assert.assertEquals(expectedType1, actualRecordType.getPropertyType("property1"));
    Assert.assertEquals(expectedType2, actualRecordType.getPropertyType("property2"));
  }
}