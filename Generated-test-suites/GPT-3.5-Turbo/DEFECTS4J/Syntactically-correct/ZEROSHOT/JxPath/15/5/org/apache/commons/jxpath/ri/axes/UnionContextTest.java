import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PreparedFalse() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = false;
        
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PreparedTrue() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = true;
        
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PreparedTrue_EmptyNodeSet() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = true;
        unionContext.setNodeSet(new BasicNodeSet());
        
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PreparedTrue_NodeSetNotEmpty() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = true;
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodePointer pointer = new NodePointer();
        nodeSet.add(pointer);
        unionContext.setNodeSet(nodeSet);
        
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PreparedTrue_NodeSetNotEmpty_PositionNotEqualToOne() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = true;
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodePointer pointer = new NodePointer();
        nodeSet.add(pointer);
        unionContext.setNodeSet(nodeSet);
        
        int position = 2;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        
        Assert.assertEquals(expected, actual);
    }
}