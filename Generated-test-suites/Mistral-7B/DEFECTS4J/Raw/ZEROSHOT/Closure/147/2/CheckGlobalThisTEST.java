import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CompilerOptions options;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler() {
      @Override
      public NodeFactory getFactory() {
        return new NodeFactory();
      }

      @Override
      public CompilerOptions getOptions() {
        return options;
      }

      @Override
      public DiagnosticListener getDiagnostics() {
        return new DiagnosticListener() {
          @Override
          public void report(DiagnosticType diagnosticType, Node sourceNode, int startPosition, int endPosition) {
            fail("Unexpected diagnostic: " + diagnosticType + " at position " + startPosition + ":" + endPosition);
          }
        };
      }

      @Override
      public CheckLevel getCheckLevel(DiagnosticType diagnosticType) {
        return WARNING;
      }
    };
    checker = new Checker(compiler);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node node = makeNode("function safeFunction() { this.property = 1; }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testPrototypeMethod() {
    Node node = makeNode("function ProtoType { get x() { return this.y; } }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testConstructorFunction() {
    Node node = makeNode("function Constructor() { this.property = 1; }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = makeNode("@this function FunctionWithThisAnnotation() { this.property = 1; }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithoutAnnotationButSafe() {
    Node node = makeNode("function FunctionWithoutAnnotationButSafe() { this.property = 1; }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testGlobalThis() {
    Node node = makeNode("var x = function() { this.property = 1; }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testGlobalThisWithPropertyAccess() {
    Node node = makeNode("var x = { get y() { return this.z; } }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testGlobalThisWithNestedPropertyAccess() {
    Node node = makeNode("var x = { get y() { return this.a.b; } }");
    NodeTraversal traversal = new NodeTraversal(checker, node);
    traversal.traverse();
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  private Node makeNode(String source) {
    return compiler.parseCompilationUnit(source, null, null).getTree();
  }
}