import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstruction() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNull(propertyPointer.getPropertyName());
        assertEquals(0, propertyPointer.getPropertyIndex());
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexNegativeNumber() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(new QName(null, propertyName), propertyPointer.getName());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        Object expected = new Object();
        propertyPointer.setBaseValue(expected);
        assertEquals(expected, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        Object collection = new Object()[5];
        propertyPointer.setBaseValue(collection);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        Object leaf = new Object();
        propertyPointer.setNode(leaf);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        Object collection = new Object[3];
        propertyPointer.setBaseValue(collection);
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer());
        Object expected = new Object();
        propertyPointer.setBaseValue(expected);
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(propertyPointer, immediateValuePointer.getParent());
        assertEquals(expected, immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePathSuccess() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        propertyPointer.setPropertyIndex(1);
        Object node = new Object();
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory() {
            @Override
            public Object createObject(JXPathContext context, NodePointer parent, Object bean, String name, int index) {
                return node;
            }
        };
        propertyPointer.createPath(context, bean);
        assertEquals(node, propertyPointer.getNode());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathFailure() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        propertyPointer.setPropertyIndex(1);
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory() {
            @Override
            public Object createObject(JXPathContext context, NodePointer parent, Object bean, String name, int index) {
                throw new RuntimeException();
            }
        };
        propertyPointer.createPath(context, bean);
    }

    @Test
    public void testCreateChildSuccess() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        Object node = new Object();
        JXPathContext context = new JXPathContext();
        PropertyPointer childPropertyPointer = propertyPointer.createChild(context, null, 0, node);
        assertEquals(propertyPointer, childPropertyPointer.getParent());
        assertEquals(node, childPropertyPointer.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNullName() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.createChild(null, null, 0, new Object());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildIndexOutOfBounds() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        propertyPointer.setBaseValue(new Object[5]);
        PropertyPointer childPropertyPointer = propertyPointer.createChild(null, null, 10, new Object());
    }
}