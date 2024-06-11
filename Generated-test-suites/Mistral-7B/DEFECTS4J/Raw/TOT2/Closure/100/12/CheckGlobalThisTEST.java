import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
  @org.junit.Test
  public void testCheckGlobalThis() {
    // Test case for CheckGlobalThis constructor
    Checker compiler = new Compiler().getCheckFactory().getChecker("checkGlobalThis", new CheckLevel(Warning));
    CheckGlobalThis checker = new CheckGlobalThis(compiler, Warning);
    // Test normal case
    Node normalNode = Node.newFunctionNode("function normalFunction() {}");
    NodeTraversal normalTraversal = new NodeTraversal(compiler.getRootNode()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checker.shouldTraverse(nt, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checker.visit(nt, n, parent);
      }
    };
    new NodeTraversal(normalNode).traverse(normalTraversal);
    assertEquals(0, compiler.getDiagnostics().size());

    // Test edge cases
    // Test function with constructor annotation
    Node constructorNode = Node.newFunctionNode("@constructor function constructorFunction() {}");
    NodeTraversal constructorTraversal = new NodeTraversal(compiler.getRootNode()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checker.shouldTraverse(nt, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checker.visit(nt, n, parent);
      }
    };
    new NodeTraversal(constructorNode).traverse(constructorTraversal);
    assertEquals(0, compiler.getDiagnostics().size());

    // Test function with @this annotation
    Node thisAnnotationNode = Node.newFunctionNode("@this function thisFunction() {}");
    NodeTraversal thisAnnotationTraversal = new NodeTraversal(compiler.getRootNode()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checker.shouldTraverse(nt, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checker.visit(nt, n, parent);
      }
    };
    new NodeTraversal(thisAnnotationNode).traverse(thisAnnotationTraversal);
    assertEquals(0, compiler.getDiagnostics().size());

    // Test function with no logical place to put a this annotation
    Node noThisAnnotationNode = Node.newFunctionNode("function noThisFunction() {}", Node.newIdentifier("this"));
    NodeTraversal noThisAnnotationTraversal = new NodeTraversal(compiler.getRootNode()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checker.shouldTraverse(nt, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checker.visit(nt, n, parent);
      }
    };
    new NodeTraversal(noThisAnnotationNode).traverse(noThisAnnotationTraversal);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getName());

    // Test exception case
    Node exceptionNode = Node.newThrowStatement(Node.newIdentifier("Error"), "Error message");
    NodeTraversal exceptionTraversal = new NodeTraversal(compiler.getRootNode()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checker.shouldTraverse(nt, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checker.visit(nt, n, parent);
      }
    };
    new NodeTraversal(exceptionNode).traverse(exceptionTraversal);
    assertEquals(1, compiler.getDiagnostics().size());
    diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getName());
  }
}