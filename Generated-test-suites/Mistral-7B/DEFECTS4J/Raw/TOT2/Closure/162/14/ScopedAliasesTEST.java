import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    this.compiler = new Compiler();
    this.compiler.setPrintDiagnosticMessages(true);
    this.preprocessorSymbolTable = new PreprocessorSymbolTable();
    this.transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testScopedAliases() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    Node rootNode = this.compiler.parseTree(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        this.compiler,
        this.preprocessorSymbolTable,
        this.transformationHandler);

    scopedAliases.process(null, rootNode);

    // Check if the scoped aliases were processed correctly
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getQualifiedName());
    assertEquals("DIV", aliasDefinitionsInOrder.get(1).getQualifiedName());
    assertEquals("goog", aliasDefinitionsInOrder.get(2).getQualifiedName());

    assertEquals(3, aliasUsages.size());
    assertEquals(aliasDefinitionsInOrder.get(0), aliasUsages.get(0).aliasDefinition);
    assertEquals(aliasDefinitionsInOrder.get(1), aliasUsages.get(1).aliasDefinition);
    assertEquals(aliasDefinitionsInOrder.get(2), aliasUsages.get(2).aliasDefinition);

    // Check if alias nodes were replaced correctly
    assertEquals(3, aliasUsages.size());
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        Node aliasNode = aliasUsage.aliasReference;
        Node aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
        assertEquals(aliasDefinition, aliasNode.getParent());
      }
    }

    // Check if scope calls were removed correctly
    assertEquals(0, scopeCalls.size());

    // Check if the code was transformed correctly
    JSDocInfo info = rootNode.getFirstChild();
    assertEquals(1, info.getTypeNodes().size());
    Node typeNode = info.getTypeNodes().get(0);
    assertEquals("(function() {\n" +
        "  var goog = this;\n" +
        "  goog.dom = {\n" +
        "    TagName: {\n" +
        "      DIV: 0\n" +
        "    }\n" +
        "  };\n" +
        "  function createElement(tagName) {\n" +
        "    return document.createElement(tagName);\n" +
        "  }\n" +
        "})()",
        rootNode.toStringTree(Printer.jsPretty).getTree().toString());
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  });" +
        "});";

    Node rootNode = this.compiler.parseTree(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        this.compiler,
        this.preprocessorSymbolTable,
        this.transformationHandler);

    scopedAliases.process(null, rootNode);

    // Check if an error was reported for the invalid call
    assertEquals(1, scopedAliases.getTraversal().getErrors().size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        scopedAliases.getTraversal().getErrors().get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var div = document.createElement('DIV');" +
        "});";

    Node rootNode = this.compiler.parseTree(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        this.compiler,
        this.preprocessorSymbolTable,
        this.transformationHandler);

    scopedAliases.process(null, rootNode);

    // Check if an error was reported for the non-alias local variable
    assertEquals(1, scopedAliases.getTraversal().getErrors().size());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        scopedAliases.getTraversal().getErrors().get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithAliasRedefined() {
    String sourceCode = "goog.scope(function() {" +
        "  var goog = {};" +
        "  goog.dom = {};" +
        "  goog.scope(function() {" +
        "    goog.dom = {};" +
        "  });" +
        "});";

    Node rootNode = this.compiler.parseTree(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        this.compiler,
        this.preprocessorSymbolTable,
        this.transformationHandler);

    scopedAliases.process(null, rootNode);

    // Check if an error was reported for the alias redefined
    assertEquals(1, scopedAliases.getTraversal().getErrors().size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        scopedAliases.getTraversal().getErrors().get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithInvalidParameter() {
    String sourceCode = "goog.scope(function(a) {" +
        "});";

    Node rootNode = this.compiler.parseTree(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        this.compiler,
        this.preprocessorSymbolTable,
        this.transformationHandler);

    scopedAliases.process(null, rootNode);

    // Check if an error was reported for the invalid parameter
    assertEquals(1, scopedAliases.getTraversal().getErrors().size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        scopedAliases.getTraversal().getErrors().get(0).getType().name());
  }
}