import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      protected void process(TestSourceFile srcFile, Node rootNode, CompilerOptions options) {
        traversal = new NodeTraversal(rootNode, this);
        new CheckGlobalThis(this, WARNING).traverse(traversal, rootNode, null);
      }
    };
  }

  @Test
  public void testSafeUsesOfThis() {
    String source = "function Foo() {}";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    assertEquals(0, traversal.getDiagnostics().length);
  }

  @Test
  public void testUnsafeAssignToThis() {
    String source = "function Foo() { this = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    Node errorNode = traversal.getDiagnostics()[0].getSourceNode();
    assertEquals(Token.THIS, errorNode.getType());
    assertEquals(WARNING, traversal.getDiagnostics()[0].getLevel());
  }

  @Test
  public void testUnsafeAssignToPrototype() {
    String source = "function Foo() {} Foo.prototype.x = function() { this = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    Node errorNode = traversal.getDiagnostics()[0].getSourceNode();
    Node getPropNode = errorNode.getLastChild();
    assertEquals(Token.GETPROP, getPropNode.getType());
    assertEquals("prototype", getPropNode.getLastChild().getString());
  }

  @Test
  public void testSafePrototypeMethod() {
    String source = "function Foo() {} Foo.prototype.x = function() { this.y = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    assertEquals(0, traversal.getDiagnostics().length);
  }

  @Test
  public void testUnsafeFunctionWithNoAnnotation() {
    String source = "function Foo() { this.x = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    Node errorNode = traversal.getDiagnostics()[0].getSourceNode();
    assertEquals(Token.FUNCTION, errorNode.getType());
  }

  @Test
  public void testSafeFunctionWithThisAnnotation() {
    String source = "/** @this {Object} */ function Foo() { this.x = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    assertEquals(0, traversal.getDiagnostics().length);
  }

  @Test
  public void testSafeFunctionWithContructorAnnotation() {
    String source = "function Foo() {} /** @constructor */ function Foo() { this.x = 1; }";
    TestSourceFile file = new TestSourceFile("Foo.js", source);
    compiler.compile(file, null);
    assertEquals(0, traversal.getDiagnostics().length);
  }
}