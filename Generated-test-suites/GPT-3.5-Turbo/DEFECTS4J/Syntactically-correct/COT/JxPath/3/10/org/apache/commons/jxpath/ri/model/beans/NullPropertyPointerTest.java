import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    private static final String PROPERTY_NAME = "*";

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        Assert.assertEquals(PROPERTY_NAME, pointer.getName().toString());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_ParentIsNotContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setValue("value");
    }

    @Test
    public void testSetValue_ParentSupportsDynamicPropertyDeclaration() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setValue("value");

        Assert.assertEquals("value", parent.getPropertyPointer().getValue());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_ParentDoesNotSupportDynamicPropertyDeclaration() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setValue("value");
    }

    // Additional test cases for edge cases and exceptions can be added here

}