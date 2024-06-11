import static org.junit.Assert.*;
import org.junit.Test;

public class RenamePrototypesTest {
  
  @Test
  public void testPropertyConstructor() {
    RenamePrototypes.Property prop = new RenamePrototypes.Property("testProp");
    assertNotNull(prop);
    assertEquals("testProp", prop.oldName);
    assertNull(prop.newName);
    assertEquals(0, prop.prototypeCount);
    assertEquals(0, prop.objLitCount);
    assertEquals(0, prop.refCount);
  }
  
  @Test
  public void testGetProperty() {
    RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);
    RenamePrototypes.Property prop = rp.new Property("testProp");
    prop.prototypeCount = 2;
    prop.objLitCount = 1;
    prop.refCount = 3;
    
    rp.properties.put("testProp", prop);
    
    RenamePrototypes.Property retrievedProp = rp.getProperty("testProp");
    assertNotNull(retrievedProp);
    assertEquals("testProp", retrievedProp.oldName);
    assertEquals(2, retrievedProp.prototypeCount);
    assertEquals(1, retrievedProp.objLitCount);
    assertEquals(3, retrievedProp.refCount);
  }
  
  // Add more test cases for other methods

}