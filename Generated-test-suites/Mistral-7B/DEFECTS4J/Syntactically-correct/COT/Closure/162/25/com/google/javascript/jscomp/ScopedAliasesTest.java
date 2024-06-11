import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestingUtils.compile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.TestingUtils;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() throws Exception {
    String sourceCode = "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    SourceFile source = new SourceFile("Test.js", sourceCode);
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    options.setAliasTransformation(AliasTransformation.GOOG_ALIASES);
    AliasTransformationHandler handler = new AliasTransformationHandler();
    compiler.setOptions(options);
    compiler.setAliasTransformationHandler(handler);

    Diagnostics diagnostics = compile(compiler, source);

    assertEquals(0, diagnostics.getNumErrors());

    Node root = source.getTree();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("div", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("DIV", aliasDefinitionsInOrder.get(2).getString());

    assertEquals(3, aliasUsages.size());
    assertEquals(aliasDefinitionsInOrder.get(0), aliasUsages.get(0).getAliasDefinition());
    assertEquals(aliasDefinitionsInOrder.get(1), aliasUsages.get(1).getAliasDefinition());
    assertEquals(aliasDefinitionsInOrder.get(2), aliasUsages.get(2).getAliasDefinition());

    assertEquals(1, scopeCalls.size());
    assertEquals(root.getChildAtIndex(0), scopeCalls.get(0));

    Node domElement = root.getChildAtIndex(1).getLastChild();
    assertEquals("createElement", domElement.getFirstChild().getString());
    assertEquals("DIV", domElement.getLastChild().getString());

    assertEquals("goog.dom.createElement(DIV)", root.toString());
  }

  @Test
  public void testScopedAliasesWithErrors() throws Exception {
    String sourceCode = "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "\n" +
        "  goog.scope(function() {\n" +
        "    dom.createElement(DIV);\n" +
        "  });\n" +
        "});";

    SourceFile source = new SourceFile("Test.js", sourceCode);
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    options.setAliasTransformation(AliasTransformation.GOOG_ALIASES);
    AliasTransformationHandler handler = new AliasTransformationHandler();
    compiler.setOptions(options);
    compiler.setAliasTransformationHandler(handler);

    Diagnostics diagnostics = compile(compiler, source);

    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostics.getError(0).getType());

    Node root = source.getTree();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    assertEquals(1, root.getChildCount());
    assertEquals("goog.scope", root.getChild(0).getString());
  }

  @Test
  public void testScopedAliasesWithNonAliasedLocal() throws Exception {
    String sourceCode = "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var element = document.createElement(dom.TagName.DIV);\n" +
        "});";

    SourceFile source = new SourceFile("Test.js", sourceCode);
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    options.setAliasTransformation(AliasTransformation.GOOG_ALIASES);
    AliasTransformationHandler handler = new AliasTransformationHandler();
    compiler.setOptions(options);
    compiler.setAliasTransformationHandler(handler);

    Diagnostics diagnostics = compile(compiler, source);

    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostics.getError(0).getType());

    Node root = source.getTree();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    assertEquals(1, root.getChildCount());
    assertEquals("document.createElement", root.getChild(0).toString());
  }
}