import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testAddProperty_success() {
    JSType expectedType = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("propertyName", expectedType, propertyNode);
    
    assertNotNull(result);
    assertTrue(builder.build().getProperties().containsKey("propertyName"));
    assertEquals(expectedType, builder.build().getProperties().get("propertyName").getType());
    assertEquals(propertyNode, builder.build().getProperties().get("propertyName").getPropertyNode());
  }
  
  @Test
  public void testAddProperty_duplicateProperty() {
    JSType expectedType = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("propertyName", expectedType, propertyNode);
    
    RecordTypeBuilder result = builder.addProperty("propertyName", expectedType, propertyNode);
    
    assertNull(result);
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSType expectedType = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("propertyName", expectedType, propertyNode);
    builder.addProperty("propertyName2", expectedType, propertyNode);
    
    JSType result = builder.build();
    
    assertEquals(expectedType, result);
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    JSType expectedType = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("propertyName", expectedType, propertyNode);
    builder.addProperty("propertyName2", expectedType, propertyNode);
    builder.addProperty("propertyName3", expectedType, propertyNode);
    
    JSType result = builder.build();
    
    assertEquals(3, result.getProperties().size());
  }
  
  @Test
  public void testBuild_emptyRecord_returnsObjectType() {
    JSType result = builder.build();
    
    assertEquals(JSTypeNative.OBJECT_TYPE, result);
  }
}