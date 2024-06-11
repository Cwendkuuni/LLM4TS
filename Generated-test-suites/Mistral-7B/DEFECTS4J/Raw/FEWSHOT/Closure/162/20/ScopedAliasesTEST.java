package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new TestAliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node(Token.SCRIPT, null, null, null, null);
    Node googScopeCall = new Node(Token.CALL, null, null, new Node(Token.NAME, "goog", null, null, null),
        new Node(Token.FUNCTION, null, null, null, null));
    Node anonymousFunction = new Node(Token.FUNCTION, null, null, null, null);
    Node aliasDefinition = new Node(Token.ASSIGN, null, null, new Node(Token.NAME, "dom", null, null, null),
        new Node(Token.NAME, "goog.dom", null, null, null));
    Node googScopeBlock = new Node(Token.BLOCK, null, null, null, null);
    Node aliasUsage = new Node(Token.NAME, "dom", null, null, null);
    Node createElementCall = new Node(Token.CALL, null, null, new Node(Token.NAME, "createElement", null, null, null),
        new Node(Token.STRING, "DIV", null, null, null));

    googScopeCall.setLastChild(anonymousFunction);
    anonymousFunction.setLastChild(googScopeBlock);
    googScopeBlock.addChild(aliasDefinition);
    aliasDefinition.setParent(googScopeBlock);
    aliasUsage.setParent(createElementCall);
    createElementCall.addChild(aliasUsage);
    googScopeCall.addChild(createElementCall);
    root.addChild(googScopeCall);

    // When
    scopedAliases.process(null, root);

    // Then
    AliasTransformation transformation = transformationHandler.getLastTransformation();
    assertTrue(transformation != null);
    assertTrue(transformation.getAliasMap().containsKey("dom"));
    assertEquals("goog.dom", transformation.getAliasMap().get("dom"));

    // Verify the root node has been transformed
    Node transformedRoot = compiler.getOutput();
    Node transformedCreateElementCall =
        findNodeWithNameAndChildren(transformedRoot, "goog.dom.createElement", new Node[0]);
    assertNotNull(transformedCreateElementCall);

    // Verify the alias definition has been removed
    Node transformedGoogScopeBlock = findNodeWithName(transformedRoot, "goog.scope");
    assertNull(transformedGoogScopeBlock);

    // Verify the alias usage has been transformed
    Node transformedAliasUsage = findNodeWithNameAndChildren(transformedRoot, "dom", new Node[0]);
    assertNotNull(transformedAliasUsage);
    assertEquals("goog.dom", transformedAliasUsage.getQualifiedName());

    // Verify the alias definition has been removed
    Node transformedAliasDefinition =
        findNodeWithNameAndChildren(transformedRoot, "dom", new Node[0], Node.TYPE_VAR_DECL);
    assertNull(transformedAliasDefinition);

    // Verify no errors were reported
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testProcess_MultipleAliases() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new TestAliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node(Token.SCRIPT, null, null, null, null);
    Node googScopeCall = new Node(Token.CALL, null, null, new Node(Token.NAME, "goog", null, null, null),
        new Node(Token.FUNCTION, null, null, null, null));
    Node anonymousFunction = new Node(Token.FUNCTION, null, null, null, null);
    Node aliasDefinition1 = new Node(Token.ASSIGN, null, null, new Node(Token.NAME, "dom1", null, null, null),
        new Node(Token.NAME, "goog.dom1", null, null, null));
    Node aliasDefinition2 = new Node(Token.ASSIGN, null, null, new Node(Token.NAME, "dom2", null, null, null),
        new Node(Token.NAME, "goog.dom2", null, null, null));
    Node googScopeBlock = new Node(Token.BLOCK, null, null, null, null);
    Node aliasUsage1 = new Node(Token.NAME, "dom1", null, null, null);
    Node aliasUsage2 = new Node(Token.NAME, "dom2", null, null, null);
    Node createElementCall1 = new Node(Token.CALL, null, null, new Node(Token.NAME, "createElement", null, null, null),
        new Node(Token.STRING, "DIV", null, null, null));
    Node createElementCall2 = new Node(Token.CALL, null, null, new Node(Token.NAME, "createElement", null, null, null),
        new Node(Token.STRING, "DIV", null, null, null));

    googScopeCall.setLastChild(anonymousFunction);
    anonymousFunction.setLastChild(googScopeBlock);
    googScopeBlock.addChild(aliasDefinition1);
    aliasDefinition1.setParent(googScopeBlock);
    googScopeBlock.addChild(aliasDefinition2);
    aliasDefinition2.setParent(googScopeBlock);
    aliasUsage1.setParent(createElementCall1);
    aliasUsage2.setParent(createElementCall2);
    createElementCall1.addChild(aliasUsage1);
    createElementCall2.addChild(aliasUsage2);
    googScopeCall.addChild(createElementCall1);
    googScopeCall.addChild(createElementCall2);
    root.addChild(googScopeCall);

    // When
    scopedAliases.process(null, root);

    // Then
    AliasTransformation transformation = transformationHandler.getLastTransformation();
    assertTrue(transformation != null);
    assertTrue(transformation.getAliasMap().containsKey("dom1"));
    assertEquals("goog.dom1", transformation.getAliasMap().get("dom1"));
    assertTrue(transformation.getAliasMap().containsKey("dom2"));
    assertEquals("goog.dom2", transformation.getAliasMap().get("dom2"));

    // Verify the root node has been transformed
    Node transformedRoot = compiler.getOutput();
    Node transformedCreateElementCall1 =
        findNodeWithNameAndChildren(transformedRoot, "goog.dom1.createElement", new Node[0]);
    assertNotNull(transformedCreateElementCall1);

    Node transformedCreateElementCall2 =
        findNodeWithNameAndChildren(transformedRoot, "goog.dom2.createElement", new Node[0]);
    assertNotNull(transformedCreateElementCall2);

    // Verify the alias definitions have been removed
    Node transformedGoogScopeBlock = findNodeWithName(transformedRoot, "goog.scope");
    assertNull(transformedGoogScopeBlock);

    // Verify the alias usages have been transformed
    Node transformedAliasUsage1 = findNodeWithNameAndChildren(transformedRoot, "dom1", new Node[0]);
    assertNotNull(transformedAliasUsage1);
    assertEquals("goog.dom1", transformedAliasUsage1.getQualifiedName());

    Node transformedAliasUsage2 = findNodeWithNameAndChildren(transformedRoot, "dom2", new Node[0]);
    assertNotNull(transformedAliasUsage2);
    assertEquals("goog.dom2", transformedAliasUsage2.getQualifiedName());

    // Verify no errors were reported
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testProcess_Errors() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new TestAliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node(Token.SCRIPT, null, null, null, null);
    Node googScopeCall = new Node(Token.CALL, null, null, new Node(Token.NAME, "goog", null, null, null),
        new Node(Token.FUNCTION, null, null, null, null));
    Node anonymousFunction = new Node(Token.FUNCTION, null, null, null, null);
    Node aliasDefinition = new Node(Token.ASSIGN, null, null, new Node(Token.NAME, "dom", null, null, null),
        new Node(Token.NAME, "goog.dom", null, null, null));
    Node googScopeBlock = new Node(Token.BLOCK, null, null, null, null);
    Node aliasUsage = new Node(Token.NAME, "dom", null, null, null);
    Node createElementCall = new Node(Token.CALL, null, null, new Node(Token.NAME, "createElement", null, null, null),
        new Node(Token.STRING, "DIV", null, null, null));

    googScopeCall.setLastChild(anonymousFunction);
    anonymousFunction.addChild(new Node(Token.VAR, null, null, new Node(Token.NAME, "this", null, null, null), null));
    anonymousFunction.setLastChild(googScopeBlock);
    googScopeBlock.addChild(aliasDefinition);
    aliasDefinition.setParent(googScopeBlock);
    aliasUsage.setParent(createElementCall);
    createElementCall.addChild(aliasUsage);
    googScopeCall.addChild(createElementCall);
    root.addChild(googScopeCall);

    // When
    scopedAliases.process(null, root);

    // Then
    AliasTransformation transformation = transformationHandler.getLastTransformation();
    assertNull(transformation);

    // Verify errors have been reported
    List<String> errors = compiler.getErrors();
    assertEquals(6, errors.size());
    assertErrorMessage(
        "The body of a goog.scope function cannot reference 'this'",
        errors.get(0));
    assertErrorMessage(
        "The body of a goog.scope function cannot use 'return'",
        errors.get(1));
    assertErrorMessage(
        "The body of a goog.scope function cannot use 'throw'",
        errors.get(2));
    assertErrorMessage(
        "The alias dom is assigned a value more than once",
        errors.get(3));
    assertErrorMessage(
        "The local variable dom is in a goog.scope and is not an alias",
        errors.get(4));
    assertErrorMessage(
        "The call to goog.scope must be alone in a single statement.",
        errors.get(5));
  }

  private void assertErrorMessage(String message, String error) {
    assertTrue(error.startsWith(message));
  }

  private Node findNodeWithName(Node node, String name) {
    Node currentNode = node;
    while (currentNode != null) {
      if (currentNode.getString().equals(name)) {
        return currentNode;
      }
      currentNode = currentNode.getParent();
    }
    return null;
  }

  private Node findNodeWithNameAndChildren(Node node, String name, Node... children) {
    Node currentNode = node;
    while (currentNode != null) {
      if (currentNode.getString().equals(name)) {
        Node matchingChild = null;
        for (Node child : children) {
          if (currentNode.getLastChild() != null &&
              currentNode.getLastChild().equals(child)) {
            matchingChild = currentNode;
            break;
          }
        }
        if (matchingChild != null) {
          return matchingChild;
        }
      }
      currentNode = currentNode.getParent();
    }
    return null;
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  private List<AliasTransformation> transformations = Lists.newArrayList();

  @Override
  public AliasTransformation logAliasTransformation(
      String fileName, SourcePosition position) {
    AliasTransformation transformation = new AliasTransformation();
    transformations.add(transformation);
    return transformation;
  }

  public AliasTransformation getLastTransformation() {
    return transformations.get(transformations.size() - 1);
  }
}