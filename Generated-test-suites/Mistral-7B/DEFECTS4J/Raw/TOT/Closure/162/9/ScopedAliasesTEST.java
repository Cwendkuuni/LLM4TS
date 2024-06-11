import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    scopedAliases = new ScopedAliases(compiler, null, null);
  }

  @Test
  public void testSimpleScopedAlias() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    Node root = NodeUtil.parseJsonString(source);
    scopedAliases.process(null, root);

    Node domElement = compiler.getOutput().getRootNode().getFirstChild();
    assertNotNull(domElement);
    assertEquals("goog.dom.createElement", domElement.getQualifiedName());
  }

  @Test
  public void testScopedAliasWithError() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  var dom = {}; " + // Duplicate definition
        "  dom.createElement(DIV); " +
        "});";

    Node root = NodeUtil.parseJsonString(source);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getDiagnostics().asList();
    assertFalse(errors.isEmpty());
    Node errorNode = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errorNode.getJSDocInfo().getCode());
  }

  @Test
  public void testScopedAliasWithFunctionReturn() {
    String source = "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV); " +
        "});";

    Node root = NodeUtil.parseJsonString(source);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getDiagnostics().asList();
    assertFalse(errors.isEmpty());
    Node errorNode = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errorNode.getJSDocInfo().getCode());
  }

  @Test
  public void testScopedAliasWithFunctionThrow() {
    String source = "goog.scope(function() {" +
        "  throw new Error('Test Error'); " +
        "});";

    Node root = NodeUtil.parseJsonString(source);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getDiagnostics().asList();
    assertFalse(errors.isEmpty());
    Node errorNode = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errorNode.getJSDocInfo().getCode());
  }
}