import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", nullPropertyPointer.getName().getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.getValuePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(NullPointer.class, nodePointer.getClass());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer parentPointer = new DOMNodePointer(null, 1);
        nullPropertyPointer.setValue(new Object());
        nullPropertyPointer.setParent(parentPointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonPropertyOwnerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        nullPropertyPointer.setValue(new Object());
        nullPropertyPointer.setParent(propertyOwnerPointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_WrongParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        propertyOwnerPointer.setPropertyName("testProperty");
        nullPropertyPointer.setValue(new Object());
        nullPropertyPointer.setParent(propertyOwnerPointer);
        propertyOwnerPointer.setPropertyPointer(null);
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testCreatePath_WithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        NodePointer childNodePointer = nodePointer.createChild(context, new QName("test"), 0, new Object());
        Assert.assertNotNull(childNodePointer);
    }

    @Test
    public void testCreateChild_WithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        NodePointer childNodePointer = nodePointer.createChild(context, new QName("test"), 0, new Object(), new Object());
        Assert.assertNotNull(childNodePointer);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", nullPropertyPointer.getPropertyName().getValue());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName(new QName("testProperty"));
        Assert.assertEquals("testProperty", nullPropertyPointer.getPropertyName().getValue());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        Assert.assertEquals("testAttribute", nullPropertyPointer.getPropertyName().getValue());
        Assert.assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testgetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(null);
        nullPropertyPointer1.setPropertyName(new QName("testProperty"));
        Assert.assertEquals("/testProperty", nullPropertyPointer1.asPath());

        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(null);
        nullPropertyPointer2.setPropertyName(new QName("testProperty"));
        nullPropertyPointer2.setNameAttributeValue("testAttribute");
        Assert.assertEquals("/testProperty[@name='testAttribute']", nullPropertyPointer2.asPath());

        NullPropertyPointer nullPropertyPointer3 = new NullPropertyPointer(null);
        nullPropertyPointer3.setPropertyName(new QName("testProperty"));
        nullPropertyPointer3.setNameAttributeValue("testAttribute");
        nullPropertyPointer3.setIndex(1);
        Assert.assertEquals("/testProperty[@name='testAttribute'][1]", nullPropertyPointer3.asPath());
    }
}