
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = getContext1();
        EvalContext context2 = getContext2();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext context1 = getContext1();
        EvalContext context2 = getContext2();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected1 = true;
        boolean expected2 = false;
        
        unionContext.setPosition(1);
        boolean actual1 = context1.getSingleNode() != null;
        boolean actual2 = context2.getSingleNode() != null;
        
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
    }
    
    private EvalContext getContext1() {
        JXPathContext dummyContext = JXPathContext.newContext(new Object());
        Compiler compiler = new Compiler();
        NodePointer[] pointers = compiler.selectNodePointers(dummyContext, "Test", null);
        BasicNodeSet nodeSet = new BasicNodeSet(pointers);
        return new EvalContext(dummyContext, nodeSet, nodeSet);
    }
    
    private EvalContext getContext2() {
        JXPathContext dummyContext = JXPathContext.newContext(new Object());
        Compiler compiler = new Compiler();
        NodePointer[] pointers = compiler.selectNodePointers(dummyContext, "Test", null);
        BasicNodeSet nodeSet = new BasicNodeSet();
        for (NodePointer pointer : pointers) {
            if (pointer != null) {
                nodeSet.add(pointer);
            }
        }
        return new EvalContext(dummyContext, nodeSet, nodeSet);
    }
}
