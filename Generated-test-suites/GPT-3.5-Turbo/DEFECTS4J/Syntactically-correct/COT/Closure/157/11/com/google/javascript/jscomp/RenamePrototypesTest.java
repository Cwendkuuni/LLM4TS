import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RenamePrototypesTest {
    
    @Test
    public void testPropertyCanBeRenamed() {
        RenamePrototypes renamePrototypes = new RenamePrototypes();
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        boolean canRename = property.canRename();
        assertEquals(true, canRename);
    }
    
    @Test
    public void testPropertyCannotBeRenamed() {
        RenamePrototypes renamePrototypes = new RenamePrototypes();
        RenamePrototypes.Property property = renamePrototypes.new Property("indexOf");
        boolean canRename = property.canRename();
        assertEquals(false, canRename);
    }
    
    @Test
    public void testObjLitPropertyCanBeRenamed() {
        RenamePrototypes renamePrototypes = new RenamePrototypes();
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.objLitCount++;
        boolean canRename = property.canRenameObjLitProperty();
        assertEquals(false, canRename);
    }
    
    @Test
    public void testPrototypePropertyCanBeRenamed() {
        RenamePrototypes renamePrototypes = new RenamePrototypes();
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount++;
        boolean canRename = property.canRenamePrototypeProperty();
        assertEquals(true, canRename);
    }
}