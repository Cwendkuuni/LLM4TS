import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Parser parser;
  private NodeTraversal traversal;
  private Diagnostics diagnostics;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    nodeFactory = Mockito.mock(NodeFactory.class);
    parser = Mockito.mock(Parser.class);
    traversal = new NodeTraversal(parser, nodeFactory, compiler);
    diagnostics = new Diagnostics();
  }

  @Test
  public void testSafeUsageOfThis() {
    String code = "function Foo() {}";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics(), instanceOf(Diagnostics.class));
    diagnostics.clear();
    compiler.setDiagnostics(diagnostics);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(0));
  }

  @Test
  public void testUnsafeAssignmentOfThis() {
    String code = "var x = this;";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(1));
    assertThat(compiler.getDiagnostics().getError(0).getMessage(),
        is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafePropertyAccessOfThis() {
    String code = "var x = this.foo;";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(1));
    assertThat(compiler.getDiagnostics().getError(0).getMessage(),
        is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function Foo@this() {}";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(0));
  }

  @Test
  public void testConstructorFunction() {
    String code = "function Foo() {}";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(0));
  }

  @Test
  public void testPrototypeMethod() {
    String code = "function Foo() {} Foo.prototype.bar = function() { this.baz = 42; }";
    Node node = parser.parse(code, null, null);
    traversal.traverse(node, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(parser, nodeFactory, compiler);
    traversal.addCallback(checker);
    traversal.traverse(node, null);
    assertThat(compiler.getDiagnostics().getNumberOfErrors(), is(0));
  }
}