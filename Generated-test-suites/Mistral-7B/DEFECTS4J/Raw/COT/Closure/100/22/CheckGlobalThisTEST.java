import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test cases for the CheckGlobalThis class.
 *
 * @author your.name
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private DiagnosticGroup diagnostics;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new Checker(compiler, new CheckLevel(CheckLevel.Warning));
    diagnostics = new DiagnosticGroup();
  }

  @Test
  public void testCheckGlobalThis_FunctionOutsideOfPrototypeOrConstructor() {
    String code = "function evil() { this.x = 1; }";

    Node node = Node.newInstance(code);
    NodeTraversal traversal = new NodeTraversal(node, checker, diagnostics);
    checker.process(traversal);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertEquals(1, diagnostics.getDiagnostics().size());
    assertEquals(diagnosticCaptor.capture(diagnostics.getDiagnostics().get(0).getType()),
        CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithConstructor() {
    String code = "function MyConstructor() {}";

    Node node = Node.newInstance(code);
    NodeTraversal traversal = new NodeTraversal(node, checker, diagnostics);
    checker.process(traversal);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThis() {
    String code = "function MyFunction() { @this MyFunction }";

    Node node = Node.newInstance(code);
    NodeTraversal traversal = new NodeTraversal(node, checker, diagnostics);
    checker.process(traversal);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionInsidePrototype() {
    String code = "function MyObject() {} MyObject.prototype.evil = function() { this.x = 1; }";

    Node node = Node.newInstance(code);
    NodeTraversal traversal = new NodeTraversal(node, checker, diagnostics);
    checker.process(traversal);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess() {
    String code = "var obj = { x: 1 }; function evil() { obj.x = 1; }";

    Node node = Node.newInstance(code);
    NodeTraversal traversal = new NodeTraversal(node, checker, diagnostics);
    checker.process(traversal);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertEquals(1, diagnostics.getDiagnostics().size());
    assertEquals(diagnosticCaptor.capture(diagnostics.getDiagnostics().get(0).getType()),
        CheckGlobalThis.GLOBAL_THIS);
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_NullCompiler() {
    CheckGlobalThis checker = new CheckGlobalThis(null, new CheckLevel(CheckLevel.Warning));
  }
}