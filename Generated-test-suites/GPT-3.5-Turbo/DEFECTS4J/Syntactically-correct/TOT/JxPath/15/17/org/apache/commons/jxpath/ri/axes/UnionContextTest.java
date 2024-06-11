
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.*;
import org.apache.commons.jxpath.*;

import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        int actualOrder = unionContext.getDocumentOrder();
        
        assertEquals(1, actualOrder);
    }

    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        boolean positionSet = unionContext.setPosition(2);
        
        assertTrue(positionSet);
    }

    @Test
    public void testSetPosition_NoPreparation() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        boolean positionSet = unionContext.setPosition(2);
        
        assertTrue(positionSet);
        
        // Try setting position again after preparation
        positionSet = unionContext.setPosition(3);
        
        assertFalse(positionSet);
    }

    @Test
    public void testSetPosition_EmptyNodeSet() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        // Test when the nodeSet is empty
        ((BasicNodeSet)unionContext.getNodeSet()).clear();
        
        boolean positionSet = unionContext.setPosition(2);
        
        assertFalse(positionSet);
    }

    @Test
    public void testSetPosition_DuplicatePointers() {
        EvalContext context1 = new EvalContext();
        context1.setCurrentNodePointer(new NodePointer());
        EvalContext context2 = new EvalContext();
        context2.setCurrentNodePointer(new NodePointer());

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context2});

        boolean positionSet = unionContext.setPosition(1);

        assertTrue(positionSet);
    }
}

