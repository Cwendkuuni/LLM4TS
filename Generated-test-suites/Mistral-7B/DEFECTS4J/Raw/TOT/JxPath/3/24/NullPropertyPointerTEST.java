import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        Assert.assertEquals("Property name should be set to *", "*", nullPropertyPointer.getPropertyName());
        Assert.assertFalse("ByNameAttribute should be false by default", nullPropertyPointer.isByNameAttribute());
        Assert.assertNull("Base value should be null", nullPropertyPointer.getBaseValue());
        Assert.assertNull("Immediate node should be null", nullPropertyPointer.getImmediateNode());
        Assert.assertTrue("Is leaf should be true", nullPropertyPointer.isLeaf());
        Assert.assertNull("Value pointer should be a NullPointer", nullPropertyPointer.getValuePointer());
        Assert.assertFalse("Is actual property should be false", nullPropertyPointer.isActualProperty());
        Assert.assertFalse("Is actual should be false", nullPropertyPointer.isActual());
        Assert.assertTrue("Is container should be true", nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSettingValueOnNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSettingValueOnNonContainerParent() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSettingValueOnPropertyOwnerParentWithDynamicPropertyDeclarationSupport() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSettingValueOnPropertyOwnerParentWithoutDynamicPropertyDeclarationSupport() {
        NodePointer parent = new PropertyOwnerPointer(new Object()) {
            @Override
            public boolean isDynamicPropertyDeclarationSupported() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test
    public void testCreatingPathWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer result = nullPropertyPointer.createPath(context);
        Assert.assertNull("Result should be null", result);
    }

    @Test
    public void testCreatingPathWithContainerParent() {
        NodePointer parent = new Object();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer result = nullPropertyPointer.createPath(context);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Result class should be PropertyOwnerPointer", PropertyOwnerPointer.class, result.getClass());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatingPathWithNonContainerParent() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer result = nullPropertyPointer.createPath(context);
    }

    @Test
    public void testCreatingPathWithAttributeProperty() {
        NodePointer parent = new NodePointer(new Object());
        NodePointer attribute = parent.createAttribute(new JXPathContext(new Object()), new QName("test"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(attribute);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer result = nullPropertyPointer.createPath(context);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Result class should be AttributePointer", AttributePointer.class, result.getClass());
    }
}