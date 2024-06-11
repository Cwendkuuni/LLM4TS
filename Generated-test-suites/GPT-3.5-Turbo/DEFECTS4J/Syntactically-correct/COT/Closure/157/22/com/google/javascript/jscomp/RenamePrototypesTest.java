import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

    @Test
    public void testCanRenamePrototypeProperty() {
        RenamePrototypes.Property property = new RenamePrototypes.Property("testProperty");
        boolean result = property.canRenamePrototypeProperty();
        assertFalse(result);
    }
    
    @Test
    public void testCanRenameObjLitProperty() {
        RenamePrototypes.Property property = new RenamePrototypes.Property("testProperty");
        boolean result = property.canRenameObjLitProperty();
        assertFalse(result);
    }

    @Test
    public void testCount() {
        RenamePrototypes.Property property1 = new RenamePrototypes.Property("testProperty1");
        RenamePrototypes.Property property2 = new RenamePrototypes.Property("testProperty2");
        RenamePrototypes.Property property3 = new RenamePrototypes.Property("testProperty3");
        
        property1.prototypeCount = 1;
        property1.objLitCount = 2;
        
        property2.prototypeCount = 3;
        
        property3.objLitCount = 4;
        property3.refCount = 5;
        
        int count1 = property1.count();
        int count2 = property2.count();
        int count3 = property3.count();

        assertEquals(3, count1);
        assertEquals(3, count2);
        assertEquals(9, count3);
    }

    @Test
    public void testCanRename() {
        RenamePrototypes.Property property1 = new RenamePrototypes.Property("testProperty1");
        RenamePrototypes.Property property2 = new RenamePrototypes.Property("testProperty2");
        
        property1.prototypeCount = 1;
        property1.objLitCount = 2;
        
        property2.prototypeCount = 3;
        
        boolean canRename1 = property1.canRename();
        boolean canRename2 = property2.canRename();

        assertTrue(canRename1);
        assertTrue(canRename2);
    }

}