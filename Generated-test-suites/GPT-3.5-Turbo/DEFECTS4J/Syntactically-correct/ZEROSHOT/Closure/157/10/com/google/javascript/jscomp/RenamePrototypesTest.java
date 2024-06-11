import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

  @Test
  public void testCanRenamePrototypeProperty() {
    RenamePrototypes renamePrototypes = new RenamePrototypes();
    Property property = renamePrototypes.new Property("oldName");
    boolean result = property.canRenamePrototypeProperty();
    assertTrue(result);
  }

  @Test
  public void testCanRenameObjLitProperty() {
    RenamePrototypes renamePrototypes = new RenamePrototypes();
    Property property = renamePrototypes.new Property("oldName");
    boolean result = property.canRenameObjLitProperty();
    assertFalse(result);
  }

  @Test
  public void testCount() {
    RenamePrototypes renamePrototypes = new RenamePrototypes();
    Property property = renamePrototypes.new Property("oldName");
    int result = property.count();
    assertEquals(0, result);
  }

  @Test
  public void testCanRename() {
    RenamePrototypes renamePrototypes = new RenamePrototypes();
    Property property = renamePrototypes.new Property("oldName");
    boolean result = property.canRename();
    assertTrue(result);
  }

  @Test
  public void testGetPropertyMap() {
    RenamePrototypes renamePrototypes = new RenamePrototypes();
    VariableMap variableMap = renamePrototypes.getPropertyMap();
    assertNotNull(variableMap);
  }
  
}