import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckSideEffects.*;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

 private Compiler compiler;
 private SourceFile sourceFile;
 private NodeFactory nodeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 compiler = new Compiler();
 compiler.setOption(CompilerOptions. warnings, true);
 sourceFile = new SourceFile("CheckSideEffectsTest.js", null);
 nodeFactory = sourceFile.getFactory();
 }

 @Test
 public void testCheckSideEffects() {
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 Node rootNode = nodeFactory.parseTree("var x = foo();");
 NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects, rootNode);
 traversal.traverseAll();
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffectsWithWarning() {
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 Node rootNode = nodeFactory.parseTree("x == foo();");
 NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects, rootNode);
 traversal.traverseAll();
 assertTrue(checkSideEffects.problemNodes.size() == 1);
 compiler.getDiagnostics().dump();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckSideEffectsWithException() {
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 Node rootNode = nodeFactory.parseTree("var x = foo(); x = null;");
 NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects, rootNode);
 traversal.traverseAll();
 }

 @Test
 public void testCheckSideEffectsWithProtectedCode() {
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 Node rootNode = nodeFactory.parseTree("JSCOMPILER_PRESERVE(var x = \"this string is\");");
 NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects, rootNode);
 traversal.traverseAll();
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testStripProtection() {
 CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
 Node rootNode = nodeFactory.parseTree("JSCOMPILER_PRESERVE(function() {});");
 NodeTraversal traversal = new NodeTraversal(compiler, stripProtection, rootNode);
 traversal.traverseAll();
 Node functionNode = findFunctionNode(rootNode, "function");
 assertTrue(functionNode.getFirstChild().isName());
 assertTrue(functionNode.getFirstChild().getString().isEmpty());
 }

 private Node findFunctionNode(Node rootNode, String functionName) {
 Node currentNode = rootNode;
 while (currentNode != null) {
 if (currentNode.isFunction()) {
 if (currentNode.getString().equals(functionName)) {
 return currentNode;
 }
 }
 currentNode = currentNode.getUp();
 }
 return null;
 }

}