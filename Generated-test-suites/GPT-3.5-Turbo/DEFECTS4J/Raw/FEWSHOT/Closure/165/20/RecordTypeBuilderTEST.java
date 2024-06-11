import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    builder = new RecordTypeBuilder();
  }
  
  @Test
  public void testAddProperty() {
    // Test adding a property with a unique name
    assertTrue(builder.addProperty("name", JSType.STRING, new Node()));
    
    // Test adding a property with a duplicate name
    assertNull(builder.addProperty("name", JSType.NUMBER, new Node()));
  }
  
  @Test
  public void testBuild() {
    // Test building an empty record type
    JSType emptyRecordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecordType.getNativeObjectType());
    
    // Test building a record type with properties
    builder.addProperty("name", JSType.STRING, new Node());
    builder.addProperty("age", JSType.NUMBER, new Node());
    JSType recordType = builder.build();
    assertEquals(2, recordType.getProperties().size());
  }
  
  @Test
  public void testRecordProperty() {
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(type, propertyNode);
    
    assertEquals(type, property.getType());
    assertEquals(propertyNode, property.getPropertyNode());
  }
}