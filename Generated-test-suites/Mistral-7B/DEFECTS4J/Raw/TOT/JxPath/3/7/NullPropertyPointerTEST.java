import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull(nullPropertyPointer.getBaseValue());
        Assert.assertFalse(nullPropertyPointer.isLeaf());
        Assert.assertEquals(0, nullPropertyPointer.getLength());
        Assert.assertNull(nullPropertyPointer.getValuePointer());
        Assert.assertFalse(nullPropertyPointer.isActual());
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
        Assert.assertFalse(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSettingValueToNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        try {
            nullPropertyPointer.setValue("value");
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testCreatingPathWithNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        try {
            NodePointer nodePointer = nullPropertyPointer.createPath(context);
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testSettingValueToNullPropertyPointerWithDynamicPropertyDeclaration() {
        NodePointer parent = new PropertyOwnerPointer(new Object()) {
            @Override
            public boolean isDynamicPropertyDeclarationSupported() {
                return true;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        PropertyPointer propertyPointer = parent.getPropertyPointer();
        propertyPointer.setPropertyName("name");
        propertyPointer.setValue("value");
        NodePointer nodePointer = context.getValue(propertyPointer);
        nullPropertyPointer.setValue("newValue");;
        Assert.assertEquals("newValue", nodePointer.getValue());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSettingValueToNullPropertyPointerWithNonContainerParent() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatingPathWithNullPropertyPointerAndAttribute() {
        NodePointer parent = new NodePointer(new Object()) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer nodePointer = nullPropertyPointer.createPath(context).createAttribute(context, new QName("name"));
        Assert.assertNull(nodePointer.getValue());
    }

    @Test
    public void testCreatingPathWithNullPropertyPointerAndChild() {
        NodePointer parent = new NodePointer(new Object()) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer nodePointer = nullPropertyPointer.createPath(context).createChild(context, new QName("name"), 0);
        Assert.assertNull(nodePointer.getValue());
    }

    @Test
    public void testSettingValueToNullPropertyPointerWithCollection() {
        NodePointer parent = new NodePointer(new Object()) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer nodePointer = nullPropertyPointer.createPath(context).createChild(context, new QName("name"), 0);
        nullPropertyPointer.setValue(new Object[]{1, 2, 3});
        Assert.assertArrayEquals(new Object[]{1, 2, 3}, nodePointer.getValue());
    }

    @Test
    public void testSettingValueToNullPropertyPointerWithCollectionAndIndex() {
        NodePointer parent = new NodePointer(new Object()) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer nodePointer = nullPropertyPointer.createPath(context).createChild(context, new QName("name"), 0);
        nullPropertyPointer.setValue(new Object[]{1, 2, 3}, 1);
        Assert.assertEquals(2, nodePointer.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingValueToNullPropertyPointerWithInvalidIndex() {
        NodePointer parent = new NodePointer(new Object()) {
            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer nodePointer = nullPropertyPointer.createPath(context).createChild(context, new QName("name"), 0);
        nullPropertyPointer.setValue(new Object[]{1}, 2);
    }

    @Test
    public void testSettingValueToNullPropertyPointerWithInvalidParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        try {
            nullPropertyPointer.setValue("value");
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testGettingPropertyNameFromNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSettingPropertyNameToNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("name");
        Assert.assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSettingNameAttributeValueToNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("name");
        Assert.assertEquals("name", nullPropertyPointer.getPropertyName());
        Assert.assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testCheckingIfCollectionForNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGettingPropertyCountForNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGettingPropertyNamesForNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testGettingPathForNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("", nullPropertyPointer.asPath());
    }
}