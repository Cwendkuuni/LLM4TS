import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.CompilationUnit;
import com.google.javascript.rhino.SourceFile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private CompilerPassManager passManager;
 private PeepholeOptimizationsPass peepholeOptimizationsPass;

 @Before
 public void setup() {
 compiler = mock(AbstractCompiler.class);
 passManager = Mockito.spy(new CompilerPassManager());
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler,
 new ConstantFolding(), new DeadCodeElimination());
 }

 @Test
 public void testProcess() {
 CompilationUnit cu = new CompilationUnit();
 SourceFile sf = new SourceFile("Test.js", "test code");
 Node root = sf.getTree();

 peepholeOptimizationsPass.process(cu.getExterns(), root);

 // Verify that all optimizations were called
 for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
 verify(optimization).beginTraversal(compiler);
 verify(optimization).endTraversal(compiler);
 }
 }

 @Test
 public void testVisit() {
 Node node = Mockito.mock(Node.class);
 PeepholeChangeHandler changeHandler = Mockito.mock(PeepholeChangeHandler.class);
 compiler.addChangeHandler(changeHandler);
 peepholeOptimizationsPass.visit(node);
 compiler.removeChangeHandler(changeHandler);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNode() {
 peepholeOptimizationsPass.visit(null);
 }
}