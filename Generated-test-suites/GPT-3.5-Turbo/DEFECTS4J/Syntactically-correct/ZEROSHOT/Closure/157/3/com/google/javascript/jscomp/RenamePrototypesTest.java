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
    public void testCanRenamePrototypeProperty() {
        Property property = newName("name");

        assertTrue(property.canRenamePrototypeProperty());
    }

    @Test
    public void testCanRenameObjLitProperty() {
        Property property = newName("name");

        assertFalse(property.canRenameObjLitProperty());
    }

    @Test
    public void testCanRename() {
        Property property = newName("name");

        assertTrue(property.canRename());
    }

    @Test
    public void testCount() {
        Property property = newName("name");
        property.prototypeCount = 1;
        property.objLitCount = 2;
        property.refCount = 3;

        int count = property.count();

        assertEquals(6, count);
    }

    @Test
    public void testComparator() {
        Property property1 = newName("a");
        property1.prototypeCount = 1;
        property1.objLitCount = 2;
        property1.refCount = 3;
        Property property2 = newName("b");
        property2.prototypeCount = 2;
        property2.objLitCount = 3;
        property2.refCount = 4;

        int result = FREQUENCY_COMPARATOR.compare(property1, property2);

        assertEquals(-1, result);
    }

    private Property newName(String oldName) {
        return new Property(oldName);
    }
}