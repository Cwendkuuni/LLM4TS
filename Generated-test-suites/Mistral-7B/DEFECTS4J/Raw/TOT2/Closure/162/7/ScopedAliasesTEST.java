import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.hotswap.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private static Compiler compiler;
  private static CompilerOptions options;
  private static AliasTransformationHandler transformationHandler;

  @BeforeClass
  public static void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    transformationHandler = new AliasTransformationHandler();
  }

  @AfterClass
  public static void tearDown() {
    compiler = null;
    options = null;
    transformationHandler = null;
  }

  /**
   * Basic test for ScopedAliases processing a simple goog.scope block.
   */
  @Test
  public void testBasic() {
    String source =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    assertTrue("Expected no diagnostics", diagnostics.isEmpty());

    Node root = compiler.getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Node googScopeCall = findNodeByQualifiedName(root, "goog.scope");
    assertNotNull("Expected goog.scope call", googScopeCall);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals("Expected 1 alias definition", 1, aliasDefinitions.size());
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals("Expected alias definition to be a var", Token.VAR, aliasDefinition.getType());
    assertEquals("Expected alias definition name to be 'dom'", "dom", aliasDefinition.getString());

    Node aliasUsage = findNodeByQualifiedName(root, "goog.dom.createElement");
    assertNotNull("Expected alias usage", aliasUsage);
    assertEquals("Expected alias usage to reference 'dom'", "dom", aliasUsage.getFirstChild().getString());
  }

  /**
   * Test for ScopedAliases when goog.scope is called with an invalid number of arguments.
   */
  @Test
  public void testInvalidArguments() {
    String source =
        "goog.scope("; // missing function parameter

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_USED_IMPROPERLY",
        GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when goog.scope is called with an invalid function parameter.
   */
  @Test
  public void testInvalidFunctionParameter() {
    String source =
        "goog.scope(123)"; // invalid function parameter

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_USED_IMPROPERLY",
        GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when goog.scope references 'this'.
   */
  @Test
  public void testRefersThis() {
    String source =
        "goog.scope(function() {" +
        "  var self = this;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_REFERENCES_THIS",
        GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when goog.scope uses 'return'.
   */
  @Test
  public void testUsesReturn() {
    String source =
        "goog.scope(function() {" +
        "  return 123;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_USES_RETURN",
        GOOG_SCOPE_USES_RETURN, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when goog.scope uses 'throw'.
   */
  @Test
  public void testUsesThrow() {
    String source =
        "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_USES_THROW",
        GOOG_SCOPE_USES_THROW, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when same alias is redefined.
   */
  @Test
  public void testAliasRedefined() {
    String source =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom;" + // redefine alias
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_ALIAS_REDEFINED",
        GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when alias is assigned to non-alias variable.
   */
  @Test
  public void testNonAliasLocal() {
    String source =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var domVar = dom;" + // assign alias to non-alias variable
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be exactly one diagnostic.
    assertEquals("Expected one diagnostic", 1, diagnostics.size());
    assertEquals("Expected diagnostic to be GOOG_SCOPE_NON_ALIAS_LOCAL",
        GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0));

    // Verify the root node was not compiled.
    assertNull("Expected root node to be null", compiler.getRootNode());
  }

  /**
   * Test for ScopedAliases when alias references local variable.
   */
  @Test
  public void testAliasReferencesLocal() {
    String source =
        "goog.scope(function() {" +
        "  var localVar = 123;" +
        "  var dom = goog.dom.createElement(localVar);" + // alias references local
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be no diagnostics.
    assertTrue("Expected no diagnostics", diagnostics.isEmpty());

    Node root = compiler.getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Node aliasUsage = findNodeByQualifiedName(root, "goog.dom.createElement");
    assertNotNull("Expected alias usage", aliasUsage);

    // Check that localVar is replaced with the alias.
    Node aliasDefinition = findNodeByQualifiedName(root, "localVar");
    assertNotNull("Expected alias definition", aliasDefinition);
    assertEquals("Expected alias definition to reference 'dom'", "dom", aliasDefinition.getFirstChild().getString());
  }

  /**
   * Test for ScopedAliases when alias is assigned a value multiple times.
   */
  @Test
  public void testAliasReassigned() {
    String source =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom.getElementById('test');" + // reassign alias
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(source, options);

    // There should be no diagnostics.
    assertTrue("Expected no diagnostics", diagnostics.isEmpty());

    Node root = compiler.getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Node aliasDefinition = findNodeByQualifiedName(root, "dom");
    assertNotNull("Expected alias definition", aliasDefinition);

    // Check that the alias is only assigned once.
    assertEquals("Expected alias definition to have one child", 1, aliasDefinition.getChildCount());
    Node aliasInitialValue = aliasDefinition.getChild(0);
    assertEquals("Expected alias initial value to be a function call", Token.CALL, aliasInitialValue.getType());
    String aliasInitialValueQualifiedName = aliasInitialValue.getQualifiedName();
    assertEquals("Expected alias initial value to be 'goog.dom'", "goog.dom", aliasInitialValueQualifiedName);
  }

  private Node findNodeByQualifiedName(Node root, String qualifiedName) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME && qualifiedName.equals(child.getQualifiedName())) {
        return child;
      }
    }
    return null;
  }
}