package org.apache.commons.cli; // Replace with the actual package name of the DevirtualizePrototypeMethods class

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Test cases for the DevirtualizePrototypeMethods class
 */
public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private CompilerPass devirtualizer;
  private SimpleDefinitionFinder definitionFinder;
  private JSTypeRegistry typeRegistry;
  private Node root;
  private Node externs;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    typeRegistry = mock(JSTypeRegistry.class);
    compiler.getTypeRegistry = typeRegistry;

    devitationalizer = new DevirtualizePrototypeMethods(compiler);
    definitionFinder = new SimpleDefinitionFinder(compiler);

    externs = Node.objectLiteral();
    root = Node.script(externs);
  }

  @Test
  public void testRewritesFunctionDefinitions() {
    // Given a function definition in the global scope
    String functionName = "accumulate";
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);

    // When the function is eligible for devirtualization
    when(definitionFinder.getDefinitionSites(ArgumentMatchers.eq(definition.getName()))).thenReturn(Optional.of(definitionSite));
    when(definitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(newUseSite(root, definition, NodeUtil.newName(functionName), null, null)));

    // Then the function definition is rewritten as a global function
    devitationalizer.process(externs, root);

    Node newFunction = findFunctionByName(functionName, root);
    assertNotNull(newFunction);
    assertNotNull(newFunction.getFirstChild());
    assertNotNull(newFunction.getFirstChild().getNext());
    assertEquals("JSCompiler_StaticMethods_accumulate", newFunction.getFirstChild().getString());
  }

  @Test
  public void testDoesNotRewriteFunctionDefinitionsThatAreNotInGlobalScope() {
    // Given a function definition not in the global scope
    String functionName = "accumulate";
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);

    // When the function is not eligible for devirtualization because it's not in the global scope
    definitionSite.inGlobalScope = false;

    // Then the function definition is not rewritten
    devitationalizer.process(externs, root);

    Node newFunction = findFunctionByName(functionName, root);
    assertNull(newFunction);
  }

  @Test
  public void testDoesNotRewriteFunctionDefinitionsThatAreExported() {
    // Given a function definition that is exported
    String functionName = "accumulate";
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);

    // When the function is not eligible for devirtualization because it's exported
    Node exportNode = Node.newName(functionName + "_EXPORT");
    Node exportAssign = Node.newAssign(Token.ASSIGN, exportNode, definitionSite.node);
    root.replaceChild(definitionSite.node, exportAssign);

    // Then the function definition is not rewritten
    devitationalizer.process(externs, root);

    Node newFunction = findFunctionByName(functionName, root);
    assertNull(newFunction);
  }

  @Test
  public void testDoesNotRewriteFunctionDefinitionsWithVariableArguments() {
    // Given a function definition with variable arguments
    String functionName = "accumulate";
    Definition definition = Definition.builder(functionName, root, Optional.of(NodeUtil.newVarArgsFunctionType()))
        .build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);

    // When the function is not eligible for devirtualization because it has variable arguments
    when(definitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(newUseSite(root, definition, NodeUtil.newName(functionName), null, null)));

    // Then the function definition is not rewritten
    devitationalizer.process(externs, root);

    Node newFunction = findFunctionByName(functionName, root);
    assertNull(newFunction);
  }

  @Test
  public void testRewritesFunctionCallSites() {
    // Given a function call site
    String functionName = "accumulate";
    UseSite useSite = newUseSite(root, null, NodeUtil.newName(functionName), null, null);

    // When the function call site is eligible for devirtualization
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);
    when(definitionFinder.getDefinitionSite(useSite.node)).thenReturn(definitionSite);
    when(definitionFinder.getDefinitionSites(definition.getName())).thenReturn(Optional.of(definitionSite));

    // Then the function call site is rewritten as a call to the global function
    devitationalizer.process(externs, root);

    Node newFunctionCall = findFunctionCallByName(functionName, root);
    assertNotNull(newFunctionCall);
    assertEquals("JSCompiler_StaticMethods_accumulate", newFunctionCall.getLastChild().getString());
  }

  @Test
  public void testDoesNotRewriteFunctionCallSitesThatAreNotEligible() {
    // Given a function call site that is not eligible for devirtualization
    String functionName = "accumulate";
    UseSite useSite = newUseSite(root, null, NodeUtil.newName(functionName), null, null);

    // When the function call site is not eligible for devirtualization because it's not a call to the devirtualized function
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);
    when(definitionFinder.getDefinitionSite(useSite.node)).thenReturn(definitionSite);
    when(definitionFinder.getDefinitionSites(definition.getName())).thenReturn(Optional.empty());

    // Then the function call site is not rewritten
    devitationalizer.process(externs, root);

    Node newFunctionCall = findFunctionCallByName(functionName, root);
    assertNull(newFunctionCall);
  }

  @Test
  public void testRewritesFunctionCallSitesInDifferentModules() {
    // Given two modules, one with a function definition and another with a function call site
    String functionName = "accumulate";
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);

    // Module 1: Function definition
    Node module1Root = Node.script();
    Node module1Function = Node.newFunction(Token.FUNCTION, NodeUtil.newName(functionName), NodeUtil.newBlock(root), null);
    module1Root.addChildToBack(module1Function);

    // Module 2: Function call site
    Node module2Root = Node.script();
    UseSite useSite = newUseSite(module2Root, null, NodeUtil.newName(functionName), null, null);
    module2Root.addChildToBack(useSite.node);

    // When the function call site is eligible for devirtualization
    when(definitionFinder.getDefinitionSite(useSite.node)).thenReturn(definitionSite);
    when(definitionFinder.getDefinitionSites(definition.getName())).thenReturn(Optional.of(definitionSite));

    // Then the function call site is rewritten as a call to the global function
    devitationalizer.process(externs, module1Root);

    Node newFunctionCall = findFunctionCallByName(functionName, module2Root);
    assertNotNull(newFunctionCall);
    assertEquals("JSCompiler_StaticMethods_accumulate", newFunctionCall.getLastChild().getString());
  }

  @Test
  public void testRewritesFunctionCallSitesWithThisKeyword() {
    // Given a function call site with "this" keyword
    String functionName = "accumulate";
    UseSite useSite = newUseSite(root, null, NodeUtil.newThis(), NodeUtil.newName(functionName), null);

    // When the function call site is eligible for devirtualization
    Definition definition = Definition.builder(functionName, root, Optional.empty()).build();
    DefinitionSite definitionSite = new DefinitionSite(root, definition, null);
    when(definitionFinder.getDefinitionSite(useSite.node)).thenReturn(definitionSite);
    when(definitionFinder.getDefinitionSites(definition.getName())).thenReturn(Optional.of(definitionSite));

    // Then the function call site is rewritten as a call to the global function with "this" as the first argument
    devitationalizer.process(externs, root);

    Node newThisNode = findNodeWithName("this", root);
    Node newFunctionCall = findFunctionCallByName(functionName, root);

    assertNotNull(newFunctionCall);
    assertEquals("JSCompiler_StaticMethods_accumulate", newFunctionCall.getLastChild().getString());
    assertEquals("this", newThisNode.getString());
  }

  /**
   * Helper method to find a function with the given name in the given node
   *
   * @param functionName the name of the function to find
   * @param node the node to search in
   * @return the function node or null if not found
   */
  private Node findFunctionByName(String functionName, Node node) {
    return NodeUtil.findFunctionByName(functionName, node);
  }

  /**
   * Helper method to find a function call with the given name in the given node
   *
   * @param functionName the name of the function to find
   * @param node the node to search in
   * @return the function call node or null if not found
   */
  private Node findFunctionCallByName(String functionName, Node node) {
    return NodeUtil.findFunctionCallByName(functionName, node);
  }

  /**
   * Helper method to find a node with the given name in the given node
   *
   * @param nodeName the name of the node to find
   * @param node the node to search in
   * @return the node with the given name or null if not found
   */
  private Node findNodeWithName(String nodeName, Node node) {
    return NodeUtil.findNodeWithName(nodeName, node);
  }

  /**
   * Helper method to create a new use site for testing
   */
  private UseSite newUseSite(Node node, Node definition, Node name, Node object, Node callee) {
    return new UseSite(node, definition, name, object, callee);
  }
}