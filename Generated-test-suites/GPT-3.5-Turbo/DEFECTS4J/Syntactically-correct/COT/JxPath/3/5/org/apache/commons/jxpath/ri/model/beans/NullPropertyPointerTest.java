import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetValue_nullParent_throwsException() {
        try {
            nullPropertyPointer.setValue("value");
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetValue_nonContainerParent_throwsException() {
        NodePointer parent = new PropertyOwnerPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            nullPropertyPointer.setValue("value");
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreatePath_nonPropertyPointerParent_returnsPropertyPointer() {
        NodePointer parent = new PropertyOwnerPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);

        NodePointer newPath = nullPropertyPointer.createPath(null);

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_propertyOwnerParent_returnsPropertyPointer() {
        NodePointer parent = new PropertyOwnerPointer();
        PropertyOwnerPointer propertyOwnerParent = new PropertyOwnerPointer();
        propertyOwnerParent.setPropertyPointer(parent);
        nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);

        NodePointer newPath = nullPropertyPointer.createPath(null);

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_nonAttribute_returnsChildPointer() {
        nullPropertyPointer.setPropertyName("property");
        NodePointer newPath = nullPropertyPointer.createPath(null);

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_attribute_returnsAttributePointer() {
        nullPropertyPointer.setPropertyName("property");
        nullPropertyPointer.setAttribute(true);
        NodePointer newPath = nullPropertyPointer.createPath(null);

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_withValue_nonAttribute_returnsChildPointer() {
        nullPropertyPointer.setPropertyName("property");
        NodePointer newPath = nullPropertyPointer.createPath(null, "value");

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_withValue_attribute_returnsAttributePointer() {
        nullPropertyPointer.setPropertyName("property");
        nullPropertyPointer.setAttribute(true);
        NodePointer newPath = nullPropertyPointer.createPath(null, "value");

        Assert.assertTrue(newPath instanceof PropertyPointer);
    }

    @Test
    public void testGetPropertyName() {
        nullPropertyPointer.setPropertyName("property");
        Assert.assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("property");
        Assert.assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testAsPath() {
        nullPropertyPointer.setPropertyName("property");
        nullPropertyPointer.setIndex(1);
        String expectedPath = "[@name='property'][2]";
        Assert.assertEquals(expectedPath, nullPropertyPointer.asPath());
    }
}