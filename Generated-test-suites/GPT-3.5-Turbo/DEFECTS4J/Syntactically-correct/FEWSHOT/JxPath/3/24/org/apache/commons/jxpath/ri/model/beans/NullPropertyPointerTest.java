import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.PropertyUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private static JXPathContext context;
    private static NodePointer pointer;
    
    @BeforeClass
    public static void setup() {
        Object obj = new NullPropertyPointer(new NodePointer());
        context = JXPathContextReferenceImpl.newContext(null, obj);
        pointer = context.getPointer("");
    }
    
    @Test
    public void testGetName() {
        assertEquals("propertyName", pointer.getName().getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
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
        assertTrue(pointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        assertTrue(pointer.getValuePointer() instanceof NullPointer);
    }
    
    @Test
    public void testIsActualProperty() {
        assertFalse(pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertTrue(pointer.isContainer());
    }
    
    @Test(expected = JXPathNotFoundException.class)
    public void testSetValue_NullParent() {
        pointer.setValue("value");
    }
    
    @Test(expected = JXPathNotFoundException.class)
    public void testSetValue_NonContainerParent() {
        Object obj = new Object();
        JXPathContext ctx = JXPathContextReferenceImpl.newContext(null, obj);
        NodePointer ptr = ctx.getPointer("");
        ptr.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NoDynamicDeclarationSupported() {
        Object obj = new PropertyOwnerPointer();
        JXPathContext ctx = JXPathContextReferenceImpl.newContext(null, obj);
        NodePointer ptr = ctx.getPointer("");
        ptr.setValue("value");
    }
    
    @Test
    public void testCreatePath() {
        NodePointer newPointer = pointer.createPath(context);
        assertTrue(newPointer instanceof NullPointer);
    }
    
    @Test
    public void testCreatePath_WithAttributeValue() {
        pointer.setNameAttributeValue("attributeValue");
        NodePointer newPointer = pointer.createPath(context);
        assertTrue(newPointer instanceof NullPointer);
    }
    
    @Test
    public void testCreatePath_WithValue() {
        NodePointer newPointer = pointer.createPath(context, "value");
        assertTrue(newPointer instanceof NullPointer);
    }
    
    @Test
    public void testCreateChild() {
        NodePointer newPointer = pointer.createChild(context, new QName("name"), 0);
        assertTrue(newPointer instanceof NullPointer);
    }
    
    @Test
    public void testCreateChild_WithValue() {
        NodePointer newPointer = pointer.createChild(context, new QName("name"), 0, "value");
        assertTrue(newPointer instanceof NullPointer);
    }
    
    @Test
    public void testAsPath() {
        assertEquals("", pointer.asPath());
        
        pointer.setNameAttributeValue("attributeValue");
        assertEquals("[@name='attributeValue']", pointer.asPath());
    }
    
    @Test
    public void testEscape() {
        String string = "test's";
        String escapedString = pointer.escape(string);
        assertEquals("test&apos;s", escapedString);
    }
}