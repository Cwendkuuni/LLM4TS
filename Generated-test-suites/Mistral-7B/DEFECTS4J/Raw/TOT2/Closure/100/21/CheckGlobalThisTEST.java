import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    // When
    Node node = createNode("function evil() { this.useful = undefined; }");
    checker.visit(this, node, null);

    // Then
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    // When
    Node constructorNode = createNode("function EvilConstructor() {}");
    checker.visit(this, constructorNode, null);

    // Then
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeProperty() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    // When
    Node prototypeNode = createNode("function EvilPrototype {}");
    Node thisNode = createNode("this.useful = undefined;");
    Node prototypePropertyNode = createNode("EvilPrototype.prototype.useful = thisNode;");
    Node objectNode = createNode("new EvilPrototype();");

    Node programNode = createNode("var obj = objectNode;");
    Node traversalRootNode = createNode("traversal.traverse(programNode);");

    checker.visit(this, traversalRootNode, null);

    // Then
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  private Node createNode(String nodeString) {
    // ... implementation to create Node from nodeString
  }

  
}