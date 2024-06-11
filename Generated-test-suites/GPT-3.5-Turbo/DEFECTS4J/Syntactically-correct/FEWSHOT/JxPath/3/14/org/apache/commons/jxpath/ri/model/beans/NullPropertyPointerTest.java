import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer pointer;
    private NodePointer parent;
    
    @Before
    public void setup() {
        parent = new NodePointer();
        pointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        assertEquals("*", pointer.getName().toString());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(0, pointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        assertNull(pointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(pointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        assertEquals(true, pointer.isLeaf());
    }
    
    @Test
    public void testIsActualProperty() {
        assertEquals(false, pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertEquals(false, pointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertEquals(true, pointer.isContainer());
    }
    
    @Test
    public void testSetValue_ThrowsJXPathInvalidAccessException() {
        try {
            pointer.setValue("test");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property /, the target object is null", e.getMessage());
        }
    }
    
    @Test
    public void testCreatePath_ReturnsNullPointer() {
        NodePointer newPath = pointer.createPath(new JXPathContext());
        assertEquals(NullPointer.class, newPath.getClass());
    }
    
    @Test
    public void testCreatePathWithValue_ReturnsNullPointerWithSetValue() {
        NodePointer newPath = pointer.createPath(new JXPathContext(), "test");
        assertEquals(NullPointer.class, newPath.getClass());
        assertEquals("test", newPath.getValue().toString());
    }
    
    @Test
    public void testCreateChild_ReturnsNullPointerWithCreateChild() {
        NodePointer newChild = pointer.createChild(new JXPathContext(), new QName("child"), 0);
        assertEquals(NullPointer.class, newChild.getClass());
        assertEquals("/child[1]", newChild.asPath());
    }
    
}