import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testConstructor(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNotNull(nullPropertyPointer);
    }

    @Test
    public void testGetName(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.toString());
    }

    @Test
    public void testSetPropertyIndex(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("test");
    }

    @Test
    public void testCreatePath(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer newNode = nullPropertyPointer.createPath(context);
        assertNotNull(newNode);
    }

    @Test
    public void testCreatePathWithValue(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer newNode = nullPropertyPointer.createPath(context, "test");
        assertNotNull(newNode);
        assertEquals("test", newNode.getValue());
    }

    @Test
    public void testCreateChild(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("test"), 1);
        assertNotNull(newChild);
    }

    @Test
    public void testCreateChildWithValue(){
        NodePointer parent = new NullPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("test"), 1, "value");
        assertNotNull(newChild);
        assertEquals("value", newChild.getValue());
    }

}