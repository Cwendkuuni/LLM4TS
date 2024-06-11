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
    public void testCount() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount = 2;
        property.objLitCount = 3;
        property.refCount = 4;
        assertEquals(property.count(), 9);
    }

    @Test
    public void testCanRename_PrototypeProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount = 1;
        property.objLitCount = 0;
        property.refCount = 0;
        assertTrue(property.canRename());
    }

    @Test
    public void testCanRename_ObjLitProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount = 0;
        property.objLitCount = 1;
        property.refCount = 0;
        assertFalse(property.canRename());
    }

    @Test
    public void testCanRename_UnknownProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount = 1;
        property.objLitCount = 1;
        property.refCount = 0;
        assertFalse(property.canRename());
    }

    @Test
    public void testCanRename_PrivateProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("_test");
        property.prototypeCount = 1;
        property.objLitCount = 0;
        property.refCount = 0;
        assertTrue(property.canRename());
    }

    @Test
    public void testCanRename_ExternedProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("indexOf");
        property.prototypeCount = 1;
        property.objLitCount = 0;
        property.refCount = 0;
        assertFalse(property.canRename());
    }

    @Test
    public void testCanRename_AggressiveRenaming() {
        renamePrototypes = new RenamePrototypes(true);
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        property.prototypeCount = 1;
        property.objLitCount = 0;
        property.refCount = 0;
        assertTrue(property.canRename());
    }

}