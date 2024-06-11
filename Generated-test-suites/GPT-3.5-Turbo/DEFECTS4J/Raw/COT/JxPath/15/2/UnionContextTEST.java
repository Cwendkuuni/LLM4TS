import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UnionContextTest {

    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testGetDocumentOrder() {
        int documentOrder = unionContext.getDocumentOrder();
        Assert.assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPositionNoPreparation() {
        boolean positionSet = unionContext.setPosition(1);
        Assert.assertTrue(positionSet);
    }

    @Test
    public void testSetPositionWithPreparation() {
        unionContext.setPosition(1);
        boolean positionSet = unionContext.setPosition(2);
        Assert.assertTrue(positionSet);
    }

    @Test
    public void testSetPositionInvalid() {
        boolean positionSet = unionContext.setPosition(-1);
        Assert.assertFalse(positionSet);
    }

    @Test
    public void testSetPositionOutOfRange() {
        boolean positionSet = unionContext.setPosition(3);
        Assert.assertFalse(positionSet);
    }

    @Test
    public void testSetPositionWithEmptyNodeSet() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        unionContext.setNodeSet(nodeSet);

        boolean positionSet = unionContext.setPosition(1);
        Assert.assertFalse(positionSet);
    }

    @Test
    public void testSetPositionWithSingleNodeSet() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(new NodePointer());
        unionContext.setNodeSet(nodeSet);

        boolean positionSet = unionContext.setPosition(1);
        Assert.assertTrue(positionSet);
    }

    @Test
    public void testSetPositionWithDuplicateNodes() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodePointer pointer = new NodePointer();
        nodeSet.add(pointer);
        nodeSet.add(pointer);
        unionContext.setNodeSet(nodeSet);

        boolean positionSet = unionContext.setPosition(1);
        Assert.assertTrue(positionSet);
    }

}