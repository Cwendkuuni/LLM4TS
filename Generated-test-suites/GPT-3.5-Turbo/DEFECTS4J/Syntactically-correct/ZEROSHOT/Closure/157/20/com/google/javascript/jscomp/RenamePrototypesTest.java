import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes(null, true, null, null);
  }

  @Test
  public void testCount() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 1;
    property.objLitCount = 2;
    property.refCount = 3;
    assertEquals(6, property.count());
  }

  @Test
  public void testCanRenamePrototypeProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.oldName = "Test";
    assertTrue(property.canRenamePrototypeProperty());
  }

  @Test
  public void testCanRenameObjLitProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.oldName = "Test";
    assertFalse(property.canRenameObjLitProperty());
  }
}