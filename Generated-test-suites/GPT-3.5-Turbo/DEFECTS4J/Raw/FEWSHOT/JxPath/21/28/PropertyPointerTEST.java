import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class PropertyPointerTest {
    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(PropertyPointerImpl.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());
        pointer.setPropertyIndex(5);
        assertEquals(5, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getBean());
        Object bean = new Object();
        pointer.setBean(bean);
        assertEquals(bean, pointer.getBean());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getPropertyName());
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getPropertyName());

        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());

        pointer.setPropertyName("newName");
        assertEquals("newName", pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(0, pointer.getPropertyCount());

        pointer.setPropertyCount(5);
        assertEquals(5, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        String[] propertyNames = pointer.getPropertyNames();
        assertEquals(0, propertyNames.length);

        String[] names = {"name1", "name2"};
        pointer.setPropertyNames(names);
        propertyNames = pointer.getPropertyNames();
        assertEquals(2, propertyNames.length);
        assertEquals("name1", propertyNames[0]);
        assertEquals("name2", propertyNames[1]);
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(true, pointer.isActualProperty());

        pointer.setActualProperty(false);
        assertEquals(false, pointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(PropertyPointerImpl.UNINITIALIZED, pointer.getImmediateNode());

        Object node = new Object();
        pointer.setImmediateNode(node);
        assertEquals(node, pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(false, pointer.isCollection());

        pointer.setCollection(true);
        assertEquals(true, pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(false, pointer.isLeaf());

        pointer.setLeaf(true);
        assertEquals(true, pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(0, pointer.getLength());

        pointer.setLength(5);
        assertEquals(5, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        NodePointer valuePointer = new NodePointer();
        pointer.setImmediateValuePointer(valuePointer);
        assertEquals(valuePointer, pointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        pointer.setAbstractFactory(factory);

        Object bean = new Object();
        pointer.setBean(bean);
        pointer.setPropertyName("name");
        pointer.setIndex(2);

        pointer.createPath(context);
        assertEquals(context, factory.getContext());
        assertEquals(pointer, factory.getPointer());
        assertEquals(bean, factory.getBean());
        assertEquals("name", factory.getPropertyName());
        assertEquals(2, factory.getIndex());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePath_Exception() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        pointer.setAbstractFactory(factory);

        Object bean = new Object();
        pointer.setBean(bean);
        pointer.setPropertyName("name");

        pointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        JXPathContext context = new JXPathContext();

        Object value = new Object();
        pointer.createPath(context, value);
        assertEquals(value, pointer.getValue());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName();

        Object value = new Object();
        NodePointer child = pointer.createChild(context, name, 2, value);
        assertEquals(pointer, child.getParent());
        assertEquals(name.toString(), ((PropertyPointer) child).getPropertyName());
        assertEquals(2, child.getIndex());
        assertEquals(value, child.getValue());
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(0, pointer.hashCode());

        pointer.setParent(new NodePointer());
        assertEquals(pointer.getParent().hashCode(), pointer.hashCode());

        pointer.setPropertyIndex(5);
        assertEquals(pointer.getParent().hashCode() + 5, pointer.hashCode());

        pointer.setPropertyName("name");
        assertEquals((pointer.getParent().hashCode() + 5) + pointer.getPropertyName().hashCode(), pointer.hashCode());

        pointer.setIndex(2);
        assertEquals(((pointer.getParent().hashCode() + 5) + pointer.getPropertyName().hashCode()) + 2, pointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = new PropertyPointerImpl(null);
        PropertyPointer pointer2 = new PropertyPointerImpl(null);

        assertEquals(true, pointer1.equals(pointer1));
        assertEquals(false, pointer1.equals(null));
        assertEquals(false, pointer1.equals(new Object()));
        assertEquals(true, pointer1.equals(pointer2));

        pointer1.setParent(new NodePointer());
        assertEquals(false, pointer1.equals(pointer2));

        pointer2.setParent(pointer1.getParent());
        assertEquals(true, pointer1.equals(pointer2));

        pointer1.setPropertyIndex(5);
        assertEquals(false, pointer1.equals(pointer2));

        pointer2.setPropertyIndex(5);
        assertEquals(true, pointer1.equals(pointer2));

        pointer1.setPropertyName("name");
        assertEquals(false, pointer1.equals(pointer2));

        pointer2.setPropertyName("name");
        assertEquals(true, pointer1.equals(pointer2));

        pointer1.setIndex(2);
        assertEquals(false, pointer1.equals(pointer2));

        pointer2.setIndex(2);
        assertEquals(true, pointer1.equals(pointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();

        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }
}