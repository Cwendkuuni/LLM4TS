import org.junit.Test;

import static org.junit.Assert.*;

public class RenamePrototypesTest {

    @Test
    public void testCanRenamePrototypeProperty_ExportedName_ReturnFalse() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        assertFalse(renamePrototypes.canRenamePrototypeProperty("exportedName"));
    }

    @Test
    public void testCanRenamePrototypeProperty_PrivateName_ReturnTrue() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        assertTrue(renamePrototypes.canRenamePrototypeProperty("privateName"));
    }

    @Test
    public void testCanRenamePrototypeProperty_AggressiveRenaming_ReturnTrue() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        assertTrue(renamePrototypes.canRenamePrototypeProperty("aggressiveRenaming"));
    }

    @Test
    public void testCanRenamePrototypeProperty_LowercaseLetters_ReturnFalse() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        assertFalse(renamePrototypes.canRenamePrototypeProperty("lowercaseletters"));
    }

    @Test
    public void testCanRenamePrototypeProperty_LeadingUnderscore_ReturnTrue() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        assertTrue(renamePrototypes.canRenamePrototypeProperty("_leadingUnderscore"));
    }

    @Test
    public void testCanRenamePrototypeProperty_ContainsNonLowercaseLetter_ReturnTrue() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        assertTrue(renamePrototypes.canRenamePrototypeProperty("ContainsNonLowercaseLetter"));
    }

    @Test
    public void testCanRenamePrototypeProperty_NotExported_NotPrivate_ReturnFalse() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        assertFalse(renamePrototypes.canRenamePrototypeProperty("notExportedOrPrivate"));
    }

    @Test
    public void testCanRenameObjLitProperty_ExportedName_ReturnFalse() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        assertFalse(renamePrototypes.canRenameObjLitProperty("exportedName"));
    }

    @Test
    public void testCanRenameObjLitProperty_PrivateName_ReturnTrue() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        assertTrue(renamePrototypes.canRenameObjLitProperty("privateName"));
    }

    @Test
    public void testCanRenameObjLitProperty_NotExported_NotPrivate_ReturnFalse() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        assertFalse(renamePrototypes.canRenameObjLitProperty("notExportedOrPrivate"));
    }
}