
import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        QName name = pointer.getName();
        assert(name.getLocalName().equals("*"));
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(5);
        assert(pointer.getIndex() == 0);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getLength() == 0);
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getBaseValue() == null);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getImmediateNode() == null);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.isLeaf() == true);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer valuePointer = pointer.getValuePointer();
        assert(valuePointer.getImmediateParentPointer() == pointer);
        assert(valuePointer.asPath().equals("/"));
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.isActualProperty() == false);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.isActual() == false);
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.isContainer() == true);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("test");
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer newPath = pointer.createPath(context);
        assert(newPath != null);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer newPath = pointer.createPath(context, "value");
        assert(newPath != null);
        assert(newPath.getValue().equals("value"));
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        NodePointer newPath = pointer.createChild(context, name, 0);
        assert(newPath != null);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        NodePointer newPath = pointer.createChild(context, name, 0, "value");
        assert(newPath != null);
        assert(newPath.getValue().equals("value"));
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getPropertyName().equals("*"));
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName("test");
        assert(pointer.getPropertyName().equals("test"));
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("test");
        assert(pointer.getPropertyName().equals("test"));
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.isCollection() == false);
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getPropertyCount() == 0);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assert(pointer.getPropertyNames().length == 0);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String path = pointer.asPath();
        assert(path.equals(""));
    }

    @Test
    public void testEscape() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String escaped = pointer.escape("te'st");
        assert(escaped.equals("te&apos;st"));
    }
}
