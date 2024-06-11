import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RenamePrototypesTest {

    private RenamePrototypes renamePrototypes;

    @BeforeEach
    void setUp() {
        renamePrototypes = new RenamePrototypes(null, false, null, null);
    }

    @Test
    void testPropertyCount() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        assertEquals(0, property.count());
        property.prototypeCount = 5;
        property.objLitCount = 3;
        property.refCount = 2;
        assertEquals(10, property.count());
    }

    @Test
    void testCanRenamePrototypeProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "Test";
        assertFalse(property.canRenamePrototypeProperty());

        property.oldName = "_test";
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "test_";
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "_Test";
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "_123";
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "Test123";
        assertTrue(property.canRenamePrototypeProperty());

        property.oldName = "test123";
        assertFalse(property.canRenamePrototypeProperty());
    }

    @Test
    void testCanRenameObjLitProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        assertFalse(property.canRenameObjLitProperty());
    }

    @Test
    void testProcess() {
        // Set up necessary data
        RenamePrototypes.Property property1 = renamePrototypes.new Property("test1");
        property1.prototypeCount = 2;
        property1.objLitCount = 0;
        property1.refCount = 1;

        RenamePrototypes.Property property2 = renamePrototypes.new Property("test2");
        property2.prototypeCount = 0;
        property2.objLitCount = 3;
        property2.refCount = 4;

        renamePrototypes.properties.put("test1", property1);
        renamePrototypes.properties.put("test2", property2);

        // Run the process method
        renamePrototypes.process(null, null);

        // Verify that properties have been renamed
        assertNotEquals("test1", renamePrototypes.properties.get("test1").newName);
        assertNotEquals("test2", renamePrototypes.properties.get("test2").newName);
    }

    @Test
    void testReusePrototypeNames() {
        // Set up necessary data
        RenamePrototypes.Property property1 = renamePrototypes.new Property("test1");
        renamePrototypes.properties.put("test1", property1);

        VariableMap prevUsedRenameMap = new VariableMap();
        prevUsedRenameMap.addMapping("test1", "oldTest1");

        // Run the reusePrototypeNames method
        renamePrototypes.reusePrototypeNames(renamePrototypes.properties);

        // Verify that "test1" has been renamed to "oldTest1"
        assertEquals("oldTest1", renamePrototypes.properties.get("test1").newName);
    }
}