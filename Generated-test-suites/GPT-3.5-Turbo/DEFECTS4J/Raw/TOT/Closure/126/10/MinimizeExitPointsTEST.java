import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MinimizeExitPointsTest {

    @Test
    public void testProcess_EmptyFunction_NoChange() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node root = IR.function(IR.name("emptyFunction"), IR.block());
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(0, compiler.getChangeCount());
    }

    @Test
    public void testProcess_LabelBreak_NoChange() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node root = IR.function(IR.name("labelBreakFunction"), 
            IR.labelname("myLabel"), 
            IR.block(IR.breakNode("myLabel")));
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(0, compiler.getChangeCount());
    }

    @Test
    public void testProcess_LabelBreak_MinimizeBreak() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node root = IR.function(IR.name("labelBreakFunction"), 
            IR.labelname("myLabel"), 
            IR.block(IR.breakNode("myLabel"), IR.returnNode()));
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(1, compiler.getChangeCount());
        assertEquals(IR.block(IR.returnNode()), root.getLastChild());
    }

    @Test
    public void testProcess_WhileLoop_ContinueMinimize() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node whileLoop = IR.whileNode(
            IR.trueNode(), 
            IR.block(IR.block(IR.continueNode())));
        Node root = IR.function(IR.name("whileLoopFunction"), whileLoop);
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(1, compiler.getChangeCount());
        assertNull(whileLoop.getNext());
        assertTrue(whileLoop.hasOneChild());
    }

    @Test
    public void testProcess_DoWhileLoopBranchedPaths_NoChange() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node doWhileLoop = IR.doNode(
            IR.block(IR.continueNode()),
            IR.trueNode());
        Node ifNode = IR.ifNode(
            IR.trueNode(),
            IR.block(
                IR.ifNode(IR.falseNode(),
                    IR.block(IR.breakNode()))),
            IR.block());
        Node root = IR.function(IR.name("doWhileLoopBranchedFunction"),
            IR.block(doWhileLoop, ifNode));
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(0, compiler.getChangeCount());
    }

    @Test
    public void testProcess_DoWhileLoop_UnreachableBreakRemoved() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node doWhileLoop = IR.doNode(
            IR.block(IR.continueNode()),
            IR.falseNode());
        Node ifNode = IR.ifNode(
            IR.trueNode(),
            IR.block(
                IR.ifNode(IR.trueNode(),
                    IR.block(IR.breakNode()))),
            IR.block());
        Node root = IR.function(IR.name("doWhileLoopBranchedFunction"),
            IR.block(doWhileLoop, ifNode));
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(1, compiler.getChangeCount());
        assertNull(doWhileLoop.getNext());
    }

    @Test
    public void testProcess_ReturnInFunction_SubtreeReturnedDirectly() {
        // Setup
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints();
        AbstractCompiler compiler = new AbstractCompiler();
        minimizeExitPoints.compiler = compiler;
        Node externs = new Node();
        Node function = IR.function(
            IR.name("returnFunction"), 
            IR.returnNode());
        Node root = IR.function(
            IR.name("mainFunction"),
            IR.block(
                IR.expressionResult(IR.call(function))));
        
        // Execute
        minimizeExitPoints.process(externs, root);
        
        // Assert
        assertEquals(1, compiler.getChangeCount());
        assertEquals(IR.returnNode(), root.getLastChild().getLastChild());
    }
}