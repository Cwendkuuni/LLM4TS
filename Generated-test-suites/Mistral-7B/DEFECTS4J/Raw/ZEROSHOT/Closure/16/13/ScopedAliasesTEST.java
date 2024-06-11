import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Test class for ScopedAliases class.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Set up the compiler and options.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE_ALL);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String qualifiedName) {
        System.out.println("Alias " + alias + " mapped to " + qualifiedName);
      }
    });

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());

    // Set up the test input.
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope.DOM = dom;" +
        "  goog.scope.TagName = goog.scope.DOM.TagName;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = Node.jsParse(input);

    // Run the test.
    scopedAliases.process(null, rootNode);

    // Verify the output.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.scope.DOM", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getFirstChild().getQualifiedName());

    assertEquals(1, aliasUsages.size());
    assertEquals("dom", aliasUsages.get(0).getClass().getSimpleName());
    assertTrue(aliasUsages.get(0) instanceof AliasedNode);
    Node aliasedNode = ((AliasedNode) aliasUsages.get(0)).aliasDefinition;
    assertEquals("goog.dom", aliasedNode.getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    // Verify the aliases were applied.
    Node domElementNode = NodeUtil.findSingleChildOfType(rootNode, Node.ELEMENT_NODE);
    assertEquals("goog.dom.createElement(goog.scope.TagName.DIV)",
        domElementNode.toString());

    // Verify the errors were reported.
    assertEquals(0, compiler.getNumErrors());
    assertEquals(0, compiler.getNumWarnings());

    // Verify the aliases were logged.
    List<AliasTransformation> transformations = options.getAliasTransformationHandler().getLoggedAliases();
    assertEquals(1, transformations.size());
    AliasTransformation transformation = transformations.get(0);
    assertEquals("DOM", transformation.getAlias());
    assertEquals("goog.dom", transformation.getQualifiedName());

  }
}