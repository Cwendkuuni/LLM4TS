import org.apache.commons.jxpath.ri.axes.AttributeContext;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {
    
    private AttributeContext attributeContext;
    
    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(0));
    }
    
    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
    
}