import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SharedTestUtils.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation(
            fileName, position.getLineNumber(), position.getCharacterNumber(),
            Collections.emptyList());
      }
    };
  }

  @After
  public void tearDown() {
    compiler = null;
  }

  /**
   * Test method for ScopedAliases constructor.
   */
  @Test
  public void testScopedAliasesConstructor() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    assertNotNull(scopedAliases);
  }

  /**
   * Test method for process method.
   */
  @Test
  public void testProcess() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<Node> actualScopeCalls = scopedAliases.getScopeCalls();
    List<Node> expectedScopeCalls = getNodesByType(root, Node.CALL);
    assertEquals("Unexpected number of scope calls.", expectedScopeCalls.size(), actualScopeCalls.size());
    for (int i = 0; i < expectedScopeCalls.size(); ++i) {
      Node expectedScopeCall = expectedScopeCalls.get(i);
      Node actualScopeCall = actualScopeCalls.get(i);
      assertEquals("Unexpected scope call.", expectedScopeCall, actualScopeCall);
    }
  }

  /**
   * Test method for applyAlias method of AliasedNode class.
   */
  @Test
  public void testApplyAlias() {
    Node aliasReference = createNode("goog.dom.DOMUtil.getDocument");
    Node aliasDefinition = createNode("document");

    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);
    aliasedNode.applyAlias();

    assertEquals("Unexpected alias reference.", aliasDefinition, aliasReference);
  }

  /**
   * Test method for applyAlias method of AliasedTypeNode class.
   */
  @Test
  public void testApplyAliasType() {
    Node aliasReference = createNode("goog.dom.TagName.DIV");
    Node aliasedNode = createNode("java.lang.String");

    AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(aliasReference, "Div");
    aliasedTypeNode.applyAlias();

    assertEquals("Unexpected alias reference.", "Div", aliasReference.getString());
  }

  /**
   * Test method for getAliasDefinitionsInOrder method.
   */
  @Test
  public void testGetAliasDefinitionsInOrder() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var document = document;\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<Node> actualAliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> expectedAliasDefinitions = Arrays.asList(
        getNodeByType(root, Node.NAME, "document"),
        getNodeByType(root, Node.NAME, "DIV"));
    assertEquals("Unexpected alias definitions.", expectedAliasDefinitions, actualAliasDefinitions);
  }

  /**
   * Test method for validateScopeCall method with invalid goog.scope call.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallInvalidCall() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with valid goog.scope call.
   */
  @Test
  public void testValidateScopeCallValidCall() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  function myFunction() {\n" +
        "  }\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);

    // The validateScopeCall method doesn't throw any exception here.
  }

  /**
   * Test method for validateScopeCall method with invalid goog.scope parameter.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallInvalidParameter() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  goog.scope(1);\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with goog.scope that references 'this'.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallThis() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  this.test = 1;\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with goog.scope that uses 'return'.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallReturn() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  return 1;\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with goog.scope that uses 'throw'.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallThrow() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  throw new Error('Test error');\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with goog.scope that redefines an alias.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallAliasRedefined() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  var DOM = goog.dom;\n" +
        "  var DIV = DOM.TagName.DIV;\n" +
        "  var DOM = 'document';\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }

  /**
   * Test method for validateScopeCall method with goog.scope that assigns a non-alias local.
   */
  @Test(expected = AssertionError.class)
  public void testValidateScopeCallNonAliasLocal() {
    SourceFile sourceFile = createSourceFile(
        "TestFile.js",
        "goog.scope(function() {\n" +
        "  var test = 1;\n" +
        "});");

    Node root = parser(sourceFile);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, this);
    scopedAliases.process(null, root);
    nodeTraversal.traverse(root);
  }
}