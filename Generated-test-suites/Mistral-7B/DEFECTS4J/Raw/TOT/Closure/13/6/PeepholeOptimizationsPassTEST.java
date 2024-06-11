import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testSimpleConstantFolding() {
 Node input = Node.newIntLiteral(5);
 Node output = Node.newIntLiteral(5);
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, input);
 assertEquals("Constant folding should not change simple constant", output, pass.getCompiler().getOutput());
 }

 @Test
 public void testMultipleConstantFolding() {
 Node input = Node.newBinOp("+", Node.newIntLiteral(5), Node.newIntLiteral(3));
 Node output = Node.newIntLiteral(8);
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, input);
 assertEquals("Multiple constant folding test", output, pass.getCompiler().getOutput());
 }

 @Test
 public void testVariableAssignment() {
 Node input = Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(5));
 Node output = Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(5));
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, input);
 assertEquals("Variable assignment test", output, pass.getCompiler().getOutput());
 }

 @Test
 public void testVariableReassignment() {
 Node input = Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(5));
 input = Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(10));
 Node output = Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(10));
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, input);
 assertEquals("Variable reassignment test", output, pass.getCompiler().getOutput());
 }

 @Test
 public void testNestedFunctions() {
 Node input = Node.newFunction("outer", Node.newFunction("inner", null, Node.newBlock(
 Node.newStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(5))),
 Node.newStatement(Node.newReturn(Node.newIntLiteral(10))))), null, null);
 Node output = Node.newFunction("outer", Node.newFunction("inner", null, Node.newBlock(
 Node.newStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newIntLiteral(5))),
 Node.newStatement(Node.newReturn(Node.newIntLiteral(10))))), null, null);
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new FunctionInlining(), new ConstantFolding());
 pass.process(null, input);
 assertEquals("Test for nested functions", output, pass.getCompiler().getOutput());
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenario() {
 Node input = Node.newFunction("main", null, Node.newBlock(
 Node.newStatement(Node.newCall("fail", Node.newString("Test failed!")))));
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, input);
 fail("Should throw an error if the optimizations change the behavior of the code");
 }

 @After
 public void tearDown() {
 compiler = null;
 options = null;
 }
}