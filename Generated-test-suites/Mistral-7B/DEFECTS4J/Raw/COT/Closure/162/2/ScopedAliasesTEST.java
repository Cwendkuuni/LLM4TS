import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    sourceFile = new SourceFile("Test.js");
  }

  @Test
  public void testScopedAliases() {
    // Test basic functionality
    testScopingMethodName("goog.scope");
    testScopeCallWithSingleParameter();
    testAliasDefinitionAndUsage();

    // Test edge cases and exception handling
    testScopingMethodNameWithParameters();
    testScopingMethodNameWithoutParentheses();
    testScopingMethodNameWithReturnStatement();
    testScopingMethodNameWithThrowStatement();
    testScopingMethodNameWithThisKeyword();
    testAliasDefinitionRedefined();
    testAliasDefinitionWithNonAliasLocalVariable();
  }

  private void testScopingMethodName(String methodName) {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = String.format("function %s() { %s }", methodName, methodName);
    sourceFile.setCode(code);

    // When
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());

    // Then
    List<Node> rootNodes = sourceFile.getRootNodes();
    assertEquals("Expected one root node", 1, rootNodes.size());
    Node rootNode = rootNodes.get(0);
    assertNotNull("Expected a node for the call to " + methodName, rootNode);
    assertEquals("Expected the call to " + methodName, methodName,
        rootNode.getQualifiedName());
  }

  private void testScopeCallWithSingleParameter() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { var a = 1; })";
    sourceFile.setCode(code);

    // When
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());

    // Then
    List<Node> rootNodes = sourceFile.getRootNodes();
    assertEquals("Expected one root node", 1, rootNodes.size());
    Node rootNode = rootNodes.get(0);
    assertNotNull("Expected a node for the call to goog.scope", rootNode);
    Node scopeCallNode = rootNode.getLastChild();
    assertNotNull("Expected a node for the call to goog.scope", scopeCallNode);
    List<Node> children = scopeCallNode.getChildren();
    assertEquals("Expected two children for the call to goog.scope", 2,
        children.size());
    Node functionNode = children.get(1);
    assertNotNull("Expected a node for the anonymous function", functionNode);
    assertEquals("Expected the anonymous function to have no children", 0,
        functionNode.getChildCount());
  }

  private void testAliasDefinitionAndUsage() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { var foo = goog.dom; foo.createElement('DIV'); })";
    sourceFile.setCode(code);

    // When
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());

    // Then
    List<Node> rootNodes = sourceFile.getRootNodes();
    assertEquals("Expected one root node", 1, rootNodes.size());
    Node rootNode = rootNodes.get(0);
    assertNotNull("Expected a node for the call to goog.scope", rootNode);
    Node scopeCallNode = rootNode.getLastChild();
    assertNotNull("Expected a node for the call to goog.scope", scopeCallNode);
    List<Node> children = scopeCallNode.getChildren();
    assertEquals("Expected two children for the call to goog.scope", 2,
        children.size());
    Node functionNode = children.get(1);
    assertNotNull("Expected a node for the anonymous function", functionNode);
    assertEquals("Expected the anonymous function to have one child", 1,
        functionNode.getChildCount());
    Node variableDeclarationNode = functionNode.getChildren().get(0);
    assertNotNull("Expected a node for the variable declaration", variableDeclarationNode);
    assertEquals("Expected the variable declaration to have two children", 2,
        variableDeclarationNode.getChildCount());
    Node varNode = variableDeclarationNode.getChildren().get(0);
    assertEquals("Expected the variable declaration to have a 'var' node as its first child",
        Token.VAR, varNode.getType());
    Node nameNode = varNode.getChildren().get(0);
    assertEquals("Expected the variable declaration to have a 'name' node as its second child",
        Token.NAME, nameNode.getType());
    String name = nameNode.getString();
    assertEquals("Expected the variable name to be 'foo'", "foo", name);
    Node initializerNode = variableDeclarationNode.getChildren().get(1);
    assertNotNull("Expected a node for the variable initializer", initializerNode);
    Node dotNode = initializerNode.getChildren().get(0);
    assertEquals("Expected the variable initializer to have a '.' node as its first child",
        Token.DOT, dotNode.getType());
    Node aliasNode = dotNode.getNext();
    assertEquals("Expected the variable initializer to have a node for the alias as its second child",
        Token.NAME, aliasNode.getType());
    String alias = aliasNode.getString();
    assertEquals("Expected the alias to be 'goog.dom'", "goog.dom");
    Node createElementNode = initializerNode.getLastChild();
    assertNotNull("Expected a node for the DOM createElement call", createElementNode);
    assertEquals("Expected the DOM createElement call to have three children", 3,
        createElementNode.getChildCount());
    Node tagNameNode = createElementNode.getChildren().get(0);
    assertEquals("Expected the DOM createElement call to have a tag name node as its first child",
        Token.NAME, tagNameNode.getType());
    String tagName = tagNameNode.getString();
    assertEquals("Expected the tag name to be 'DIV'", "DIV");
    Node leftParenthesisNode = createElementNode.getChildren().get(1);
    assertEquals("Expected the DOM createElement call to have a '(' node as its second child",
        Token.LPAREN, leftParenthesisNode.getType());
    Node rightParenthesisNode = createElementNode.getChildren().get(2);
    assertEquals("Expected the DOM createElement call to have a ')' node as its third child",
        Token.RPAREN, rightParenthesisNode.getType());
  }

  // Test edge cases and exception handling

  private void testScopingMethodNameWithParameters() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function(a, b) {})";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_HAS_BAD_PARAMETERS);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testScopingMethodNameWithoutParentheses() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope function() {}";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_USED_IMPROPERLY);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testScopingMethodNameWithReturnStatement() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { return 1; })";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_USES_RETURN);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testScopingMethodNameWithThrowStatement() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { throw new Error('Test') })";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_USES_THROW);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testScopingMethodNameWithThisKeyword() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { this.a = 1; })";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_REFERENCES_THIS);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testAliasDefinitionRedefined() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { var foo = 1; var foo = 2; })";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_ALIAS_REDEFINED, "foo");
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void testAliasDefinitionWithNonAliasLocalVariable() {
    // Given
    options.setAliasTransformation(ALWAYS);
    compiler.setOptions(options);
    String code = "goog.scope(function() { var foo = 1; var bar = 2; })";
    sourceFile.setCode(code);

    // When and Then
    expectException(AliasTransformation.GOOG_SCOPE_NON_ALIAS_LOCAL, "bar");
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, sourceFile.getAST());
  }

  private void expectException(DiagnosticType diagnosticType) {
    expectException(diagnosticType, new Object[0]);
  }

  private void expectException(DiagnosticType diagnosticType, Object... arguments) {
    try {
      HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
      scopedAliases.process(null, sourceFile.getAST());
      fail("Expected an exception: " + diagnosticType.getMessage() + " " +
          Arrays.toString(arguments));
    } catch (RuntimeException e) {
      DiagnosticType actualDiagnosticType = e.getCause()
          .getMessage().charAt(0) == 'J'
          ? DiagnosticType.parse(e.getMessage())
          : null;
      assertEquals("Expected: " + diagnosticType.getMessage() + " " +
          Arrays.toString(arguments),
          diagnosticType, actualDiagnosticType);
    }
  }
}