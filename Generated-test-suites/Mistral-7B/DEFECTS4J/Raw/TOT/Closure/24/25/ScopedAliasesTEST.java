package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.traverse.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AbstractCompiler abstractCompiler;
  private HotSwapCompilerPass scopedAliases;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    abstractCompiler = spy(new AbstractCompiler(compiler, preprocessorSymbolTable));
    scopedAliases = new ScopedAliases(
        abstractCompiler, preprocessorSymbolTable, new AliasTransformationHandler());
    rootNode = new Node(Token.SCRIPTLET, null, null);
  }

  @Test
  public void testSimpleScopedAlias() {
    // Customize the compiler options
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.STRICT);

    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));

    // Set up the alias definition
    Node aliasDefinition = new Node(Token.VAR, "Div", new Token[0], null, null, false, null);
    aliasDefinition.setFirstChild(new Node(Token.ASSIGN, null, null, null));
    aliasDefinition.getFirstChild().setFirstChild(
        new Node(Token.IDENT, "goog.dom.DIV", null, null));

    // Add the alias definition to the scope
    Scope scope = new Scope();
    scope.addVar(new Var("Div", aliasDefinition));

    // Set up the root node with the scope call and the alias definition
    rootNode.appendChild(scopeCall);
    scopeCall.appendChild(scope);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify the alias was applied correctly
    Node expectedNode = new Node(Token.IDENT, "Div", null, null);
    Node actualNode = findSingleNode(
        rootNode,
        n -> n.isGetProperty() && n.getFirstChild().isQualifiedName() &&
            n.getFirstChild().getQualifiedName().getString().equals("Div"));
    assertNotNull(actualNode);
    assertEquals(expectedNode, actualNode);
  }

  @Test
  public void testMultipleAliases() {
    // Customize the compiler options
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.STRICT);

    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));

    // Set up the alias definitions
    Node aliasDefinition1 = new Node(Token.VAR, "Div", new Token[0], null, null, false, null);
    aliasDefinition1.setFirstChild(new Node(Token.ASSIGN, null, null, null));
    aliasDefinition1.getFirstChild().setFirstChild(
        new Node(Token.IDENT, "goog.dom.DIV", null, null));

    Node aliasDefinition2 = new Node(Token.VAR, "Div2", new Token[0], null, null, false, null);
    aliasDefinition2.setFirstChild(new Node(Token.ASSIGN, null, null, null));
    aliasDefinition2.getFirstChild().setFirstChild(
        new Node(Token.IDENT, "goog.dom.DIV", null, null));

    // Add the alias definitions to the scope
    Scope scope = new Scope();
    scope.addVar(new Var("Div", aliasDefinition1));
    scope.addVar(new Var("Div2", aliasDefinition2));

    // Set up the root node with the scope call and the alias definitions
    rootNode.appendChild(scopeCall);
    scopeCall.appendChild(scope);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify the aliases were applied correctly
    Node divNode = findSingleNode(
        rootNode,
        n -> n.isGetProperty() && n.getFirstChild().isQualifiedName() &&
            n.getFirstChild().getQualifiedName().getString().equals("Div"));
    assertNotNull(divNode);

    Node div2Node = findSingleNode(
        rootNode,
        n -> n.isGetProperty() && n.getFirstChild().isQualifiedName() &&
            n.getFirstChild().getQualifiedName().getString().equals("Div2"));
    assertNotNull(div2Node);

    assertEquals(divNode, div2Node);
  }

  @Test
  public void testImproperScopeCall() {
    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));
    scopeCall.getLastChild().appendChild(
        new Node(Token.ASSIGN, null, null, null));
    scopeCall.getLastChild().getFirstChild().setFirstChild(
        new Node(Token.IDENT, "foo", null, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for improper scope call.");

    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithReturn() {
    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));
    scopeCall.getLastChild().appendChild(
        new Node(Token.RETURN, null, null, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for improper scope call with return.");

    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithThrow() {
    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));
    scopeCall.getLastChild().appendChild(
        new Node(Token.THROW, null, null, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for improper scope call with throw.");

    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithThis() {
    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));
    scopeCall.getLastChild().appendChild(
        new Node(Token.THIS, null, null, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for improper scope call with this.");

    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithAliasRedefinition() {
    // Customize the compiler options
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.STRICT);

    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));

    // Set up the alias definitions
    Node aliasDefinition1 = new Node(Token.VAR, "Div", new Token[0], null, null, false, null);
    aliasDefinition1.setFirstChild(new Node(Token.ASSIGN, null, null, null));
    aliasDefinition1.getFirstChild().setFirstChild(
        new Node(Token.IDENT, "goog.dom.DIV", null, null));

    Node aliasDefinition2 = new Node(Token.VAR, "Div", new Token[0], null, null, false, null);
    aliasDefinition2.setFirstChild(new Node(Token.ASSIGN, null, null, null));
    aliasDefinition2.getFirstChild().setFirstChild(
        new Node(Token.IDENT, "goog.dom.DIV", null, null));

    // Add the alias definitions to the scope
    Scope scope = new Scope();
    scope.addVar(new Var("Div", aliasDefinition1));
    scope.addVar(new Var("Div", aliasDefinition2));

    // Set up the root node with the improper scope call and the alias definitions
    rootNode.appendChild(scopeCall);
    scopeCall.appendChild(scope);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for alias redefinition.");

    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithNonAliasLocal() {
    // Customize the compiler options
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.STRICT);

    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for non-alias local variable.");

    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  @Test
  public void testImproperScopeCallWithBadParameters() {
    // Set up the node to be tested
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.setFirstChild(
        new Node(Token.IDENT, "goog.scope", null, null));
    scopeCall.setLastChild(
        new Node(Token.FUNCTION, null, new Token[0], null, null, false, null));
    scopeCall.getLastChild().appendChild(
        new Node(Token.IDENT, "foo", null, null));

    // Set up the root node with the improper scope call
    rootNode.appendChild(scopeCall);

    // Run the test
    scopedAliases.process(null, rootNode);

    // Verify an error was reported
    assertEquals(
        1,
        compiler.getDiagnostics().size(),
        "Expected an error to be reported for improper scope call with bad parameters.");

    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        compiler.getDiagnostics().get(0).getType().getName());
  }

  private Node findSingleNode(Node node, Predicate<Node> predicate) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (predicate.apply(child)) {
        return child;
      }
    }
    return null;
  }

  interface Predicate<T> {
    boolean apply(T t);
  }
}