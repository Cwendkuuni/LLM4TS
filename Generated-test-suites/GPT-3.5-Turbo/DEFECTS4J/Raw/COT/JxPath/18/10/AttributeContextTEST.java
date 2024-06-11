
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testGetCurrentNodePointer() {
        DocumentContainer container = new DocumentContainer();
        container.setValue("//root/@attribute", "test");
        
        JXPathContext context = JXPathContext.newContext(container);
        AttributeContext attributeContext = new AttributeContext(context, null);
        Pointer pointer = attributeContext.getCurrentNodePointer();
        
        assertNull(pointer);
    }
    
    @Test
    public void testReset() {
        DocumentContainer container = new DocumentContainer();
        container.setValue("//root/@attribute", "test");
        
        JXPathContext context = JXPathContext.newContext(container);
        AttributeContext attributeContext = new AttributeContext(context, null);
        
        attributeContext.reset();
        
        assertFalse(attributeContext.setPosition(1));
    }
    
    @Test
    public void testSetPosition() {
        DocumentContainer container = new DocumentContainer();
        container.setValue("//root/@attribute", "test");
        
        JXPathContext context = JXPathContext.newContext(container);
        AttributeContext attributeContext = new AttributeContext(context, null);
        
        assertFalse(attributeContext.setPosition(2));
        assertTrue(attributeContext.setPosition(1));
    }
    
    @Test
    public void testNextNode() {
        DocumentContainer container = new DocumentContainer();
        container.setValue("//root/@attribute", "test");
        
        JXPathContext context = JXPathContext.newContext(container);
        AttributeContext attributeContext = new AttributeContext(context, null);
        
        assertTrue(attributeContext.nextNode());
        assertFalse(attributeContext.nextNode());
    }
    
    @Test
    public void testConstructor() {
        DocumentContainer container = new DocumentContainer();
        container.setValue("//root/@attribute", "test");
        
        JXPathContext context = JXPathContext.newContext(container);
        AttributeContext attributeContext = new AttributeContext(context, null);
        
        assertEquals(context, attributeContext.getParentContext());
        assertNull(attributeContext.getNodeTest());
    }
}
