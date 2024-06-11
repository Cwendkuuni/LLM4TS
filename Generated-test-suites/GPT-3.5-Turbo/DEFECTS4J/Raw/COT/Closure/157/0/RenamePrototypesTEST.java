
import org.junit.Assert;
import org.junit.Test;

public class RenamePrototypesTest {

    @Test
    public void testCanRenamePrototypeProperty_withAggressiveRenaming() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("testProperty");

        boolean result = property.canRenamePrototypeProperty();

        Assert.assertTrue(result);
    }

    @Test
    public void testCanRenamePrototypeProperty_withoutAggressiveRenaming() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("testProperty");

        boolean result = property.canRenamePrototypeProperty();

        Assert.assertTrue(result);
    }

    @Test
    public void testCanRenameObjLitProperty_withAggressiveRenaming() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("testProperty");

        boolean result = property.canRenameObjLitProperty();

        Assert.assertFalse(result);
    }

    @Test
    public void testCanRenameObjLitProperty_withoutAggressiveRenaming() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
        RenamePrototypes.Property property = renamePrototypes.new Property("testProperty");

        boolean result = property.canRenameObjLitProperty();

        Assert.assertFalse(result);
    }
}
