import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {
    @Test
    public void testProcess() {
        // Test for if block with return
        Node ifNode = IR.ifNode(IR.name("x"), IR.returnNode()).srcref(1, 2);
        Node root = IR.block(ifNode).srcref(3, 4);
        CompilerPass minimizeExitPoints = new MinimizeExitPoints(null);
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.ifNode(IR.name("x"), IR.block())).toString(), root.toString());

        // Test for if block with else block and returns in both
        Node elseNode = IR.returnNode().srcref(5, 6);
        ifNode.addChildToFront(IR.block(elseNode));
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.ifNode(IR.name("x"), IR.block()).addChildToBack(elseNode)).toString(), root.toString());

        // Test for if block with else block and return only in else block
        ifNode.removeChild(elseNode);
        elseNode = IR.returnNode().srcref(7, 8);
        ifNode.addChildToFront(IR.block(elseNode));
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.ifNode(IR.name("x"), IR.block())).toString(), root.toString());

        //Test for if block with return in true and false conditions
        ifNode.removeChild(elseNode);
        Node trueReturnNode = IR.returnNode().srcref(9, 10);
        ifNode.getFirstChild().addChildToBack(trueReturnNode);
        elseNode = IR.returnNode().srcref(11, 12);
        ifNode.addChildToFront(IR.block(elseNode));
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.ifNode(IR.name("x"), IR.block()).addChildToBack(IR.ifNode(IR.not(IR.name("x")), trueReturnNode).addChildToBack(elseNode))).toString(), root.toString());

        // Test for while loop with return
        Node whileNode = IR.whileNode(IR.trueNode(), IR.returnNode()).srcref(13, 14);
        root.clearChildren();
        root.addChildToBack(whileNode);
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.whileNode(IR.trueNode(), IR.block())).toString(), root.toString());

        // Test for for loop with break
        Node forNode = IR.forNode(IR.empty(), IR.nul(), IR.nul(), IR.breakNode("breakLabel")).srcref(15, 16);
        root.clearChildren();
        root.addChildToBack(forNode);
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.forNode(IR.empty(), IR.nul(), IR.nul(), IR.block())).toString(), root.toString());

        // Test for do loop with break and condition always false
        Node doNode = IR.doNode(IR.block(), IR.falseNode(), "label", null).srcref(17, 18);
        root.clearChildren();
        root.addChildToBack(doNode);
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.doNode(IR.block(), IR.falseNode(), "label", null)).toString(), root.toString());

        // Test for function with return
        Node functionNode = IR.function(IR.name("foo"), IR.block(IR.returnNode())).srcref(19, 20);
        root.clearChildren();
        root.addChildToBack(functionNode);
        minimizeExitPoints.process(null, root);
        assertEquals(IR.block(IR.function(IR.name("foo"), IR.block())).toString(), root.toString());
    }
}