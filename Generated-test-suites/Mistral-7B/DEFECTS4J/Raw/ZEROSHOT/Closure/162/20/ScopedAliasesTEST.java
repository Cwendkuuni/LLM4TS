import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = "" +
        "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});\n";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NO_REPLACES, null);

    Compiler compiler = new Compiler();
    compiler.setPrintMessagesToStdErr(false);
    compiler.setOptionsForTest(options);

    List<Node> ast = compiler.parse(sourceCode, null, null);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertFalse("Should not have errors.", scopedAliases.hasErrors());

    Node googScopeCall = findNodeByName(ast.get(0), "goog.scope");
    assertNotNull("Should have a goog.scope call.", googScopeCall);
    assertEquals(1, googScopeCall.getChildCount());

    Node anonymousFunction = googScopeCall.getChildAtIndex(0);
    assertEquals(2, anonymousFunction.getChildCount());

    Node functionBody = anonymousFunction.getChildAtIndex(1);
    assertEquals("function () {}\n", functionBody.getPrintableSource());

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    // The order of alias definitions and usages should match.
    for (int i = 0; i < aliasDefinitions.size(); i++) {
      Node aliasDefinition = aliasDefinitions.get(i);
      Node aliasUsage = aliasUsages.get(i);
      assertEquals("Alias definitions and usages should match.",
          aliasDefinition, aliasUsage.getParent());
      assertEquals("AliasDefinition and AliasUsage should be the same node.",
          aliasDefinition, aliasUsage);
    }

    // Check that the alias is used correctly.
    Node domNode = findNodeByName(ast.get(0), "dom");
    Node createdDIV = findNodeByName(ast.get(0), "DIV");

    assertEquals("dom.createElement(DIV)", domNode.toString());
    assertEquals("goog.dom.createElement(DIV)", createdDIV.toString());
  }

  private Node findNodeByName(Node node, String name) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }
}