import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes(null, false, null, null);
  }

  @Test
  public void testPropertyCount() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 1;
    property.objLitCount = 2;
    property.refCount = 3;

    assertEquals(6, property.count());
  }

  @Test
  public void testCanRenamePrototypeProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 1;
    property.objLitCount = 0;

    assertEquals(true, property.canRename());
  }

  @Test
  public void testCanRenameObjLitProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("test");
    property.prototypeCount = 0;
    property.objLitCount = 1;

    assertEquals(false, property.canRename());
  }
}