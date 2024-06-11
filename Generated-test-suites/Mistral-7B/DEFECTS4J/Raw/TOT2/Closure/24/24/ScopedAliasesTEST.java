import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.AbstractCompiler.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.AliasTransformationHandler.LogLevel.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler(LOG_ERRORS);
    compiler.setOptionsForTest(new CompilerOptions().withAliasTransformation(
        new AliasTransformation(transformationHandler)));
    traversal = new Traversal(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testScopedAliases_BasicUsage() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement('DIV');" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage_MultipleStatements() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement('DIV');" +
        "goog.dom.createElement('DIV');" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage_MultipleParameters() {
    String sourceCode = "goog.scope(function(a, b) {" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage_ThisReference() {
    String sourceCode = "goog.scope(function() {" +
        "function foo() {" +
        " this.bar = 1;" +
        "}" +
        " foo();" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage_ReturnStatement() {
    String sourceCode = "goog.scope(function() {" +
        "function foo() {" +
        " return 1;" +
        "}" +
        " foo();" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage_ThrowStatement() {
    String sourceCode = "goog.scope(function() {" +
        "function foo() {" +
        " throw new Error('Test Error');" +
        "}" +
        " foo();" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
  }

  @Test
  public void testScopedAliases_AliasRedefined() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var dom = goog.dom;" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
    assertFalse(traversal.hasErrors());

    List<String> aliases = Lists.newArrayList();
    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        aliases.add(aliasedNode.aliasDefinition.getString());
      }
    }
    assertEquals(1, aliases.size());
    assertEquals("goog.dom", aliases.get(0));
  }

  @Test
  public void testScopedAliases_NonAliasLocal() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var element = document.createElement('DIV');" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);
    assertFalse(traversal.hasErrors());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<String> aliases = Lists.newArrayList();
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        aliases.add(aliasedNode.aliasDefinition.getString());
      }
    }
    assertTrue(aliases.isEmpty());
  }

  @Test
  public void testScopedAliases_GoogScopeUsedAtRoot() {
    String sourceCode = "goog.scope(function() {" +
        "goog.dom.createElement('DIV');" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);

    List<String> aliases = Lists.newArrayList();
    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        aliases.add(aliasedNode.aliasDefinition.getString());
      }
    }
    assertEquals(1, aliases.size());
    assertEquals("goog.dom", aliases.get(0));
  }

  @Test
  public void testScopedAliases_AliasTransformation() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement('DIV');" +
        "});";

    Node root = parse(sourceCode);
    traversal.process(null, root);

    String transformedSourceCode =
        "var goog_dom = com.google.javascript.jscomp.Scope.getGlobal();" +
        "goog_dom.createElement(com.google.javascript.jscomp.Scope.getGlobal().goog.dom.TagName.DIV);" +
        ";";

    Node transformedRoot = parse(transformedSourceCode);
    assertEquals(transformedSourceCode, transformedRoot.toString());
  }

  private Node parse(String code) {
    Node node = parse(code, null, null);
    assertEquals(code.hashCode(), node.hashCode());
    return node;
  }

  private static Node parse(String code, Node parent, Node previous) {
    return RhinoUtils.parse(code, parent, previous);
  }
}