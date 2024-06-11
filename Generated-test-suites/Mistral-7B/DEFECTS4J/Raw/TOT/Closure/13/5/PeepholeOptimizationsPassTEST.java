import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PeepholeOptimizationsPassTest {

 private Compiler compiler;
 private CompilerOptions options;
 private PeepholeOptimizationsPass pass;

 @Before
 public void setUp() {
 options = new CompilerOptions();
 compiler = new Compiler();
 compiler.setOptions(options);
 pass = new PeepholeOptimizationsPass(compiler, new ConstantFoldingOptimization(), new UselessCodeRemovalOptimization());
 }

 @Test
 public void testPeepholeOptimizationsPass() {
 Node input = Node.objectLiteral();
 input.addProperty("a", Node.numberLiteral(1));
 input.addProperty("b", Node.numberLiteral(2));
 input.addProperty("c", Node.objectLiteral().addProperty("d", Node.numberLiteral(3)));

 Node expectedOutput = Node.objectLiteral().addProperty("a", Node.numberLiteral(1))
 .addProperty("b", Node.numberLiteral(2))
 .addProperty("c", Node.objectLiteral().addProperty("d", Node.numberLiteral(3)));

 Node actualOutput = pass.process(null, input).getRootNode();

 assertNotNull(actualOutput);
 assertTrue(actualOutput.equalsTree(expectedOutput));
 }

 @Test
 public void testConstantFolding() {
 Node input = Node.objectLiteral()
 .addProperty("a", Node.numberLiteral(1 + 1))
 .addProperty("b", Node.numberLiteral(2 * 2))
 .addProperty("c", Node.objectLiteral()
 .addProperty("d", Node.numberLiteral(3 + 3)));

 Node expectedOutput = Node.objectLiteral()
 .addProperty("a", Node.numberLiteral(2))
 .addProperty("b", Node.numberLiteral(4))
 .addProperty("c", Node.objectLiteral().addProperty("d", Node.numberLiteral(6)));

 Node actualOutput = pass.process(null, input).getRootNode();

 assertNotNull(actualOutput);
 assertTrue(actualOutput.equalsTree(expectedOutput));
 }

 @Test
 public void testUselessCodeRemoval() {
 Node input = Node.objectLiteral()
 .addProperty("a", Node.numberLiteral(1))
 .addProperty("b", Node.objectLiteral().addProperty("c", Node.numberLiteral(2))
 .addProperty("d", Node.numberLiteral(3)));

 Node expectedOutput = Node.objectLiteral().addProperty("a", Node.numberLiteral(1));

 Node actualOutput = pass.process(null, input).getRootNode();

 assertNotNull(actualOutput);
 assertTrue(actualOutput.equalsTree(expectedOutput));
 }

 @Test(expected = RuntimeException.class)
 public void testErrorScenario() {
 Node input = Node.objectLiteral()
 .addProperty("a", Node.objectLiteral().addProperty("b", null));

 pass.process(null, input);
 }
}