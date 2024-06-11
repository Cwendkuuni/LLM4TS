import org.junit.Test;

public class RenamePrototypesTest {
    
    @Test
    public void testCanRenamePrototypeProperty() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.createProperty("oldName");
        Property property = renamePrototypes.getProperty("oldName");
        boolean canRename = property.canRenamePrototypeProperty();
        assertTrue(canRename);
    }
    
    @Test
    public void testCanRenameObjLitProperty() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.createProperty("oldName");
        Property property = renamePrototypes.getProperty("oldName");
        boolean canRename = property.canRenameObjLitProperty();
        assertFalse(canRename);
    }
    
    @Test
    public void testGetPropertyMap() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.createProperty("oldName");
        VariableMap propertyMap = renamePrototypes.getPropertyMap();
        assertEquals(propertyMap.get("oldName"), null);
    }
    
}