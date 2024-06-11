import org.junit.Test;

import static org.junit.Assert.*;

public class RenamePrototypesTest {

    private RenamePrototypes renamePrototypes = new RenamePrototypes();

    @Test
    public void testCanRenamePrototypeProperty_ExportedName() {
        assertFalse(renamePrototypes.canRenamePrototypeProperty("indexOf"));
    }

    @Test
    public void testCanRenamePrototypeProperty_PrivateName() {
        assertTrue(renamePrototypes.canRenamePrototypeProperty("_private"));
    }

    @Test
    public void testCanRenamePrototypeProperty_AggressiveRenamingOn() {
        renamePrototypes.setAggressiveRenaming(true);
        assertTrue(renamePrototypes.canRenamePrototypeProperty("method"));
    }

    @Test
    public void testCanRenamePrototypeProperty_AggressiveRenamingOff() {
        renamePrototypes.setAggressiveRenaming(false);
        assertFalse(renamePrototypes.canRenamePrototypeProperty("method"));
    }

    @Test
    public void testCanRenamePrototypeProperty_ContainsUpperCase() {
        assertTrue(renamePrototypes.canRenamePrototypeProperty("Method"));
    }

    @Test
    public void testCanRenamePrototypeProperty_ContainsNonLetterCharacter() {
        assertTrue(renamePrototypes.canRenamePrototypeProperty("method_1"));
    }

    @Test
    public void testCanRenamePrototypeProperty_ContainsOnlyLowerCaseLetters() {
        assertFalse(renamePrototypes.canRenamePrototypeProperty("method"));
    }

    // Additional test cases for other methods in the class
}