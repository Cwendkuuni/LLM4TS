import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerInstance() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected property name to be set to *", "*", nullPropertyPointer.getPropertyName());
        Assert.assertFalse("Expected byNameAttribute to be false", nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = nullPropertyPointer.getName();
        Assert.assertEquals("Expected name to be set to the property name", "*", name.getLocalPart());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(1);
        Assert.assertEquals("Expected property index to be 0", 0, nullPropertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected length to be 0", 0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull("Expected base value to be null", nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull("Expected immediate node to be null", nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue("Expected isLeaf to be true", nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        Assert.assertNotNull("Expected valuePointer to be an instance of NullPointer", valuePointer instanceof NullPointer);
    }

    @Test(expected = AssertionError.class)
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Expected isActualProperty to be false", nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Expected isActual to be false", nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue("Expected isContainer to be true", nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull("Expected value to be null before setting", nullPropertyPointer.getValue());
        nullPropertyPointer.setValue("Test Value");
        Assert.assertEquals("Expected value to be 'Test Value' after setting", "Test Value", nullPropertyPointer.getValue());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer result = nullPropertyPointer.createPath(context);
        Assert.assertNull("Expected result to be null", result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.setValue("Test Value");
        NodePointer result = nullPropertyPointer.createPath(context, "Test Value");
        Assert.assertNull("Expected result to be null", result);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        NodePointer result = nullPropertyPointer.createChild(context, name, 0);
        Assert.assertNull("Expected result to be null", result);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
        Assert.assertNull("Expected result to be null", result);
    }

    @Test
    public void testCreatePathWithPropertyOwnerParent() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer result = nullPropertyPointer.createPath(context);
        Assert.assertNotNull("Expected result to be an instance of NodePointer", result != null);
        Assert.assertTrue("Expected result to be an instance of PropertyOwnerPointer", result instanceof PropertyOwnerPointer);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("Test Property");
        Assert.assertEquals("Expected property name to be 'Test Property'", "Test Property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("Test Attribute Name");
        Assert.assertEquals("Expected property name to be 'Test Attribute Name'", "Test Attribute Name", nullPropertyPointer.getPropertyName());
        Assert.assertTrue("Expected byNameAttribute to be true", nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Expected isCollection to be false", nullPropertyPointer.isCollection());
    }

    @Test
    public void testgetPropertyCount() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected getPropertyCount to be 0", 0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNames() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] result = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals("Expected getPropertyNames to return an empty array", new String[0], result);
    }

    @Test
    public void testasPath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String result = nullPropertyPointer.asPath();
        Assert.assertEquals("Expected asPath to be an empty string", "", result);
    }

    @Test
    public void testEscape() {
        String input = "\"Test String\"'with Single Quote'&amp;special&apos;character'";
        String result = nullPropertyPointer.escape(input);
        Assert.assertEquals("Expected escaped string to match input string", input, result);
    }
}