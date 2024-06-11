import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        Assert.assertEquals("Property name should be set to * by default.", "*", pointer.getPropertyName());
        Assert.assertFalse("ByName attribute should be false by default.", pointer.isByNameAttribute());
        Assert.assertNull("Base value should be null.", pointer.getBaseValue());
        Assert.assertNull("Immediate node should be null.", pointer.getImmediateNode());
        Assert.assertTrue("Is leaf should be true.", pointer.isLeaf());
        Assert.assertNull("Value pointer should be null.", pointer.getValuePointer());
        Assert.assertFalse("Is actual property should be false.", pointer.isActualProperty());
        Assert.assertFalse("Is actual should be false.", pointer.isActual());
        Assert.assertTrue("Is container should be true.", pointer.isContainer());

        try {
            pointer.setValue(new Object());
            Assert.fail("Should not be able to set value for NullPropertyPointer.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createPath(new JXPathContext());
            Assert.fail("Should not be able to create path for NullPropertyPointer.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createPath(new JXPathContext(), new Object());
            Assert.fail("Should not be able to create path with value for NullPropertyPointer.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createChild(new JXPathContext(), new QName("test"), 1);
            Assert.fail("Should not be able to create child for NullPropertyPointer.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createChild(new JXPathContext(), new QName("test"), 1, new Object());
            Assert.fail("Should not be able to create child with value for NullPropertyPointer.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testNullPropertyPointerWithNameAttribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setPropertyName("test");
        pointer.setByNameAttribute(true);

        Assert.assertEquals("Property name should be set to test.", "test", pointer.getPropertyName());
        Assert.assertTrue("ByName attribute should be true.", pointer.isByNameAttribute());
        Assert.assertNull("Base value should be null.", pointer.getBaseValue());
        Assert.assertNull("Immediate node should be null.", pointer.getImmediateNode());
        Assert.assertTrue("Is leaf should be true.", pointer.isLeaf());
        Assert.assertNull("Value pointer should be null.", pointer.getValuePointer());
        Assert.assertFalse("Is actual property should be false.", pointer.isActualProperty());
        Assert.assertFalse("Is actual should be false.", pointer.isActual());
        Assert.assertTrue("Is container should be true.", pointer.isContainer());

        try {
            pointer.setValue(new Object());
            Assert.fail("Should not be able to set value for NullPropertyPointer with NameAttribute.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createPath(new JXPathContext());
            Assert.fail("Should not be able to create path for NullPropertyPointer with NameAttribute.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createPath(new JXPathContext(), new Object());
            Assert.fail("Should not be able to create path with value for NullPropertyPointer with NameAttribute.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createChild(new JXPathContext(), new QName("test2"), 1);
            Assert.fail("Should not be able to create child for NullPropertyPointer with NameAttribute.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }

        try {
            pointer.createChild(new JXPathContext(), new QName("test2"), 1, new Object());
            Assert.fail("Should not be able to create child with value for NullPropertyPointer with NameAttribute.");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }
}