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
    public void testCanRenamePrototypeProperty_exportedName_returnsFalse() {
        assertFalse(renamePrototypes.canRenamePrototypeProperty("_exportedName"));
    }

    @Test
    public void testCanRenamePrototypeProperty_privateName_returnsTrue() {
        assertTrue(renamePrototypes.canRenamePrototypeProperty("_privateName"));
    }

    @Test
    public void testCanRenamePrototypeProperty_aggressiveRenamingOff_returnsFalse() {
        assertFalse(renamePrototypes.canRenamePrototypeProperty("propertyName"));
    }

    @Test
    public void testCanRenamePrototypeProperty_aggressiveRenamingOn_returnsTrue() {
        assertTrue(renamePrototypes.canRenamePrototypeProperty("PropertyName"));
    }

    @Test
    public void testCanRenameObjLitProperty_exportedName_returnsFalse() {
        assertFalse(renamePrototypes.canRenameObjLitProperty("_exportedName"));
    }

    @Test
    public void testCanRenameObjLitProperty_privateName_returnsFalse() {
        assertFalse(renamePrototypes.canRenameObjLitProperty("_privateName"));
    }

    @Test
    public void testCanRenameObjLitProperty_returnsTrue() {
        assertTrue(renamePrototypes.canRenameObjLitProperty("propertyName"));
    }
}