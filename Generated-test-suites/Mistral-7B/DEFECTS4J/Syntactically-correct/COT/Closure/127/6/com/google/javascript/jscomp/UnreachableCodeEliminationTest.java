import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

 private Compiler compiler;
 private boolean removeNoOpStatements;

 @Before
 public void setup() {
 compiler = new Compiler();
 removeNoOpStatements = false;
 }

 @Test
 public void testProcess() {
 Node externs = null;
 Node toplevel = null;
 new UnreachableCodeElimination(compiler, removeNoOpStatements).process(externs, toplevel);
 verifyNoMoreChanges(compiler, externs, toplevel);
 }

 @Test
 public void testProcessWithRemoveNoOpStatements() {
 compiler.setOption("--compile_to_es5", true);
 compiler.setOption("--warn_unused_harmonic_imports", false);
 compiler.setOption("--experimental_warn_unused_import", false);
 compiler.setOption("--experimental_modern_ModuleResolution", false);
 compiler.setOption("--experimental_modern_js", false);
 compiler.setOption("--remove_all_unused_definition_statements", false);
 compiler.setOption("--remove_empty_constructor_body", false);
 compiler.setOption("--remove_redundant_construction", false);
 compiler.setOption("--remove_undefined_variables", false);
 compiler.setOption("--remove_unreachable", true);
 compiler.setOption("--remove_no_op_statements", true);

 Node externs = null;
 Node toplevel = null;
 new UnreachableCodeElimination(compiler, true).process(externs, toplevel);
 verifyNoMoreChanges(compiler, externs, toplevel);
 }

 @Test
 public void testVisitFunction() {
 Node root = Node.newFunction("f", null, null, new Node[]{}, new Node[]{}, new Node[]{});
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyNoMoreChanges(compiler, root);
 }

 @Test
 public void testRemoveDeadCodeAfterReturn() {
 String jsCode = "function f() { return; alert('unreachable'); }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode);
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("function f() { return; }", compiler.toSource(root));
 }

 @Test
 public void testRemoveNoOpStatement() {
 String jsCode = "function f() { a.b.MyClass.prototype.propertyName; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode);
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("function f() { }", compiler.toSource(root));
 }

 @Test
 public void testRemoveDeadCodeInsideFunction() {
 String jsCode = "function f() { if (true) { return; alert('unreachable'); } g(); } g()";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("function f() { g(); }", compiler.toSource(root));
 }

 @Test
 public void testRemoveBreakStatement() {
 String jsCode = "for (i = 0; i < 10; i++) { if (i == 5) break; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("for (i = 0; i < 10; i++) { }", compiler.toSource(root));
 }

 @Test
 public void testRemoveContinueStatement() {
 String jsCode = "for (i = 0; i < 10; i++) { if (i == 5) continue; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("for (i = 0; i < 10; i++) { }", compiler.toSource(root));
 }

 @Test
 public void testRemoveReturnStatementInsideFunction() {
 String jsCode = "function f() { if (true) { return; } g(); } g()";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("function f() { } g()", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableBreakInsideLoop() {
 String jsCode = "for (i = 0; i < 10; i++) { if (i == 5) break; if (true) { break; } }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("for (i = 0; i < 10; i++) { if (i == 5) break; }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableContinueInsideLoop() {
 String jsCode = "for (i = 0; i < 10; i++) { if (i == 5) continue; if (true) { continue; } }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("for (i = 0; i < 10; i++) { if (i == 5) continue; }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableReturnInsideFunction() {
 String jsCode = "function f() { if (true) { return; } if (true) { return; } }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("function f() { }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableBreakInsideSwitch() {
 String jsCode = "switch (x) { case 1: return; break; case 2: return; break; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("switch (x) { case 1: return; }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableReturnInsideSwitch() {
 String jsCode = "switch (x) { case 1: return; } return;";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("switch (x) { case 1: return; }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableBreakInsideTryCatch() {
 String jsCode = "try { return; } finally { break; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("try { } finally { }", compiler.toSource(root));
 }

 @Test
 public void testRemoveUnreachableReturnInsideTryCatch() {
 String jsCode = "try { return; } finally { return; }";
 Node root = ParserTest.parseAndCompile(compiler, jsCode).getFirstChild();
 ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false).process(null, root).getCfg();
 new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());
 new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg).visit(null, root, null);
 verifyCodeEquals("try { } finally { }", compiler.toSource(root));
 }

 private void verifyNoMoreChanges(Compiler compiler, Node... nodes) {
 NodeTraversal traversal = new NodeTraversal(compiler) {
 @Override
 public void visit(NodeTraversal t, Node n, Node parent) {
 super.visit(t, n, parent);
 assertEquals("Unexpected node change: " + n.getClass().getName(), null, n.getAddedChildren());
 assertEquals("Unexpected node change: " + n.getClass().getName(), null, n.getRemovedChildren());
 }
 };
 NodeTraversal.traverse(compiler, nodes[0]);
 }

 private void verifyCodeEquals(String expected, Node node) {
 String actual = compiler.toSource(node);
 assertEquals("Expected code:\n" + expected, expected, actual);
 }

 private void verifyCodeEquals(String expected, Compiler compiler, Node node) {
 String actual = compiler.toSource(node);
 assertEquals("Expected code:\n" + expected, expected, actual);
 }
}