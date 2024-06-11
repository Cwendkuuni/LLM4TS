import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private PeepholeOptimizationsPass optimizationsPass;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 AbstractPeepholeOptimization optimization1 = new DummyPeepholeOptimization();
 AbstractPeepholeOptimization optimization2 = new DummyPeepholeOptimization();
 optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testSimpleFunction() {
 Node sourceNode = NodeFactory.createText("function add(x, y) { return x + y; }");
 Node rootNode = NodeFactory.parse(sourceNode);
 optimizationsPass.process(null, rootNode);
 Node resultNode = optimizationsPass.getCompiler().getRootNode();

 assertEquals("function add(x, y) { return x + y; }",
 NodePrinter.printNode(resultNode));
 }

 @Test
 public void testConstantFolding() {
 Node sourceNode = NodeFactory.createText("function add(x, y) { return 1 + x + y; }");
 Node rootNode = NodeFactory.parse(sourceNode);
 CompilerOptions options = new CompilerOptions();
 optimizationsPass.getCompiler().setOptions(options);
 optimizationsPass.process(null, rootNode);
 Node resultNode = optimizationsPass.getCompiler().getRootNode();

 assertEquals("function add(x, y) { return 1 + x + y; }",
 NodePrinter.printNode(resultNode));

 // Constant folding test
 Node constantFoldingSourceNode = NodeFactory.createText("function add(x, y) { return 1 + 5 + x + y; }");
 Node constantFoldingRootNode = NodeFactory.parse(constantFoldingSourceNode);
 optimizationsPass.process(null, constantFoldingRootNode);
 Node constantFoldingResultNode = optimizationsPass.getCompiler().getRootNode();

 assertEquals("function add(x, y) { return 6 + x + y; }",
 NodePrinter.printNode(constantFoldingResultNode));
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenario() {
 Node errorScenarioSourceNode = NodeFactory.createText("function add(x, y) { return 1 / 0; }");
 Node errorScenarioRootNode = NodeFactory.parse(errorScenarioSourceNode);
 optimizationsPass.process(null, errorScenarioRootNode);
 }
}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization, CompilerPass {
 // Dummy optimization for testing
 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }

 @Override
 public void beginTraversal(CompilerPassManager manager) {
 // Empty implementation
 }

 @Override
 public void endTraversal(CompilerPassManager manager) {
 // Empty implementation
 }
}