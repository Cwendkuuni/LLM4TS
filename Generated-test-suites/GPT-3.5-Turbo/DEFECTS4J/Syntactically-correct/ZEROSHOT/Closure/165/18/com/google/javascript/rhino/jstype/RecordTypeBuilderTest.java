import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }
  
  @Test
  public void testAddProperty() {
    String name = "testProperty";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNotNull(result);
    
    Map<String, RecordTypeBuilder.RecordProperty> properties = getProperties(recordTypeBuilder);
    assertTrue(properties.containsKey(name));
    assertEquals(type, properties.get(name).getType());
    assertEquals(propertyNode, properties.get(name).getPropertyNode());
  }
  
  @Test
  public void testAddDuplicateProperty() {
    String name = "testProperty";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    
    String duplicateName = "duplicateProperty";
    JSType duplicateType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node duplicatePropertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(duplicateName, duplicateType, duplicatePropertyNode);
    assertNull(result);
    
    Map<String, RecordTypeBuilder.RecordProperty> properties = getProperties(recordTypeBuilder);
    assertTrue(properties.containsKey(name));
    assertEquals(type, properties.get(name).getType());
    assertEquals(propertyNode, properties.get(name).getPropertyNode());
    assertFalse(properties.containsKey(duplicateName));
  }
  
  @Test
  public void testBuildEmptyRecord() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    JSType result = recordTypeBuilder.build();
    
    assertEquals(objectType, result);
  }
  
  @Test
  public void testBuildNonEmptyRecord() {
    String name = "testProperty";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    
    JSType result = recordTypeBuilder.build();
    assertTrue(result instanceof RecordType);
    
    RecordType recordType = (RecordType) result;
    Map<String, RecordTypeBuilder.RecordProperty> properties = recordType.getProperties();
    
    assertTrue(properties.containsKey(name));
    assertEquals(type, properties.get(name).getType());
    assertEquals(propertyNode, properties.get(name).getPropertyNode());
    
    Map<String, RecordTypeBuilder.RecordProperty> expectedProperties = getProperties(recordTypeBuilder);
    assertEquals(expectedProperties, properties);
  }
  
  private Map<String, RecordTypeBuilder.RecordProperty> getProperties(RecordTypeBuilder builder) {
    try {
      HashMap<String, RecordTypeBuilder.RecordProperty> properties = 
          (HashMap<String, RecordTypeBuilder.RecordProperty>) builder.getClass()
          .getDeclaredField("properties")
          .get(builder);
      return Collections.unmodifiableMap(properties);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }
}