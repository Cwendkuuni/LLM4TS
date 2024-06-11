import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }
  
  @Test
  public void addProperty_Test() {
    String name = "property1";
    JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = new Node();
    
    RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNotNull(builder);
    assertEquals(1, builder.properties.size());
    assertEquals(type, builder.properties.get(name).getType());
    assertEquals(propertyNode, builder.properties.get(name).getPropertyNode());
  }
  
  @Test
  public void addProperty_DuplicateName_Test() {
    String name = "property1";
    JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNull(builder);
    assertEquals(1, recordTypeBuilder.properties.size());
    assertEquals(type, recordTypeBuilder.properties.get(name).getType());
    assertEquals(propertyNode, recordTypeBuilder.properties.get(name).getPropertyNode());
  }
  
  @Test
  public void build_EmptyRecord_Test() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertEquals(objectType, recordType);
  }
  
  @Test
  public void build_NonEmptyRecord_Test() {
    String name1 = "property1";
    JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode1 = new Node();
    recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    
    String name2 = "property2";
    JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty(name2, type2, propertyNode2);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertTrue(recordType instanceof RecordType);
    assertEquals(2, ((RecordType) recordType).getProperties().size());
    assertEquals(type1, ((RecordType) recordType).getProperties().get(name1).getType());
    assertEquals(type2, ((RecordType) recordType).getProperties().get(name2).getType());
    assertEquals(propertyNode1, ((RecordType) recordType).getProperties().get(name1).getPropertyNode());
    assertEquals(propertyNode2, ((RecordType) recordType).getProperties().get(name2).getPropertyNode());
  }
  
  @Test
  public void build_EmptyRecord_Test() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertEquals(objectType, recordType);
  }
  
  @Test
  public void build_NonEmptyRecord_Test() {
    String name1 = "property1";
    JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode1 = new Node();
    recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    
    String name2 = "property2";
    JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty(name2, type2, propertyNode2);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertTrue(recordType instanceof RecordType);
    assertEquals(2, ((RecordType) recordType).getProperties().size());
    assertEquals(type1, ((RecordType) recordType).getProperties().get(name1).getType());
    assertEquals(type2, ((RecordType) recordType).getProperties().get(name2).getType());
    assertEquals(propertyNode1, ((RecordType) recordType).getProperties().get(name1).getPropertyNode());
    assertEquals(propertyNode2, ((RecordType) recordType).getProperties().get(name2).getPropertyNode());
  }
  
  @Test
  public void build_EmptyRecord_Test() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertEquals(objectType, recordType);
  }
  
  @Test
  public void build_NonEmptyRecord_Test() {
    String name1 = "property1";
    JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode1 = new Node();
    recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    
    String name2 = "property2";
    JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty(name2, type2, propertyNode2);
    
    JSType recordType = recordTypeBuilder.build();
    
    assertTrue(recordType instanceof RecordType);
    assertEquals(2, ((RecordType) recordType).getProperties().size());
    assertEquals(type1, ((RecordType) recordType).getProperties().get(name1).getType());
    assertEquals(type2, ((RecordType) recordType).getProperties().get(name2).getType());
    assertEquals(propertyNode1, ((RecordType) recordType).getProperties().get(name1).getPropertyNode());
    assertEquals(propertyNode2, ((RecordType) recordType).getProperties().get(name2).getPropertyNode());
  }

}