import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RenamePrototypesTest {

    private RenamePrototypes renamePrototypes;

    @Before
    public void setUp() {
        renamePrototypes = new RenamePrototypes(null, true, null, null);
    }

    @Test
    public void testCanRenamePrototypeProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("oldName");
        boolean result = property.canRenamePrototypeProperty();
        assertEquals(true, result);
    }

    @Test
    public void testCanRenameObjLitProperty() {
        RenamePrototypes.Property property = renamePrototypes.new Property("oldName");
        boolean result = property.canRenameObjLitProperty();
        assertEquals(false, result);
    }
}