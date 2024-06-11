import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes();
  }

  @Test
  public void testCount() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 2;
    property.objLitCount = 1;
    property.refCount = 3;
    
    int expectedCount = 6;
    int actualCount = property.count();
    
    assertEquals(expectedCount, actualCount);
  }

  @Test
  public void testCanRenamePrototypeProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");

    boolean actualResult = property.canRenamePrototypeProperty();
    
    assertTrue(actualResult);
  }

  @Test
  public void testCanRenameObjLitProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");

    boolean actualResult = property.canRenameObjLitProperty();
    
    assertFalse(actualResult);
  }

  @Test
  public void testCanRename() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 1;
    property.objLitCount = 1;

    boolean actualResult = property.canRename();
    
    assertTrue(actualResult);
  }

  @Test
  public void testCanRename_false() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");

    boolean actualResult = property.canRename();
    
    assertFalse(actualResult);
  }

  @Test
  public void testGetPropertyMap() {
    RenamePrototypes.Property property1 = renamePrototypes.new Property("test1");
    property1.newName = "newTest1";
    RenamePrototypes.Property property2 = renamePrototypes.new Property("test2");
    property2.newName = "newTest2";

    VariableMap expectedMap = new VariableMap();
    expectedMap.put("test1", "newTest1");
    expectedMap.put("test2", "newTest2");
    VariableMap actualMap = renamePrototypes.getPropertyMap();
    
    assertEquals(expectedMap, actualMap);
  }

}