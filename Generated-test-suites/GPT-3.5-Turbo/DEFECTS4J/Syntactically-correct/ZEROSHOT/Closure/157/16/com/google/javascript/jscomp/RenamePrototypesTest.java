import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

  @Test
  public void testPropertyNameCanRename() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    assertFalse(property.canRename());
  }

  @Test
  public void testPropertyNameCanNotRename() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    assertTrue(property.canRename());
  }

  @Test
  public void testCount() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 1;
    property.objLitCount = 2;
    property.refCount = 3;
    assertEquals(6, property.count());
  }

  @Test
  public void testGetPropertyMap() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    RenamePrototypes.Property property1 = renamePrototypes.new Property("test1");
    RenamePrototypes.Property property2 = renamePrototypes.new Property("test2");
    property1.newName = "newName1";
    property2.newName = "newName2";
    VariableMap expectedMap = new VariableMap();
    expectedMap.addMapping("test1", "newName1");
    expectedMap.addMapping("test2", "newName2");
    assertEquals(expectedMap, renamePrototypes.getPropertyMap());
  }

}