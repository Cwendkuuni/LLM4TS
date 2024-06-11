import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

    @Test
    public void testCanRenamePrototypeProperty() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("_privateProperty");
        assertTrue(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("publicProperty");
        assertTrue(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("aggressiveRenaming");
        assertTrue(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("_");
        assertTrue(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("a^b");
        assertTrue(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("Ab");
        assertFalse(property.canRenamePrototypeProperty());

        property = renamePrototypes.new Property("lowercase");
        assertFalse(property.canRenamePrototypeProperty());
    }

    @Test
    public void testCanRenameObjLitProperty() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("publicProperty");
        assertFalse(property.canRenameObjLitProperty());

        property = renamePrototypes.new Property("AggressiveRenaming");
        assertFalse(property.canRenameObjLitProperty());

        property = renamePrototypes.new Property("_");
        assertFalse(property.canRenameObjLitProperty());
    }

    @Test
    public void testGetPropertyMap() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.properties.put("oldName1", renamePrototypes.new Property("oldName1"));
        renamePrototypes.properties.get("oldName1").newName = "newName1";
        renamePrototypes.properties.put("oldName2", renamePrototypes.new Property("oldName2"));
        renamePrototypes.properties.get("oldName2").newName = "newName2";

        VariableMap expectedMap = new VariableMap();
        expectedMap.addMapping("oldName1", "newName1");
        expectedMap.addMapping("oldName2", "newName2");

        assertEquals(expectedMap, renamePrototypes.getPropertyMap());
    }
}