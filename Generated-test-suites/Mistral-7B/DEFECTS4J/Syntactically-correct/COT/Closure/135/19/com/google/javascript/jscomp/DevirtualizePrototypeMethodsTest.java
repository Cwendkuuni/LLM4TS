import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest extends CompilerPassTestBase {

  @Override
  protected CompilerPass getPass() {
    return new DevirtualizePrototypeMethods(compiler);
  }

  @Override
  protected String getPassDescription() {
    return "DevirtualizePrototypeMethods";
  }

  // Basic test case for process method
  public void testProcess() {
    assertProcessorError("TestCases/DevirtualizePrototypeMethods/process_fail.js",
        "EXCEPTION: TypeError: Cannot read property 'prototype' of null");

    Node root = parseTestSource("TestCases/DevirtualizePrototypeMethods/process_success.js");
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    // Check if any definition was rewritten
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction()) {
        Node originalMethodDefinition = definition.getNode();
        String originalMethodName = originalMethodDefinition.getLastChild().getString();
        String rewrittenMethodName = "JSCompiler_StaticMethods_" + originalMethodName;
        Node rewrittenMethodDefinition = findNodeByName(root, rewrittenMethodName);
        assertTrue(rewrittenMethodDefinition != null);
      }
    }
  }

  // Edge case test for isPrototypeMethodDefinition method
  public void testIsPrototypeMethodDefinitionEdgeCases() {
    String testCode =
        "a = { getA : function() { return this.a; } }; " +
        "function test() { return a.getA(); }";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());
  }

  // Edge case test for isEligibleDefinition method
  public void testIsEligibleDefinitionEdgeCases() {
    String testCode =
        "function Test() {} " +
        "Test.prototype.test = function() {}; " +
        "Test.prototype.test.call(null);";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DefinitionSite definitionSite = defFinder.getDefinitionSites().get(0);
    boolean isEligible = isEligibleDefinition(defFinder, definitionSite);
    assertFalse(isEligible);
  }

  // Test for rewriteDefinitionIfEligible method
  public void testRewriteDefinitionIfEligible() {
    String testCode =
        "a = { accumulate : function(value) { this.total += value; } }; " +
        "var total = a.accumulate(2);";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node rewrittenMethodDefinition = findNodeByName(root, "accumulate");
    assertNotNull(rewrittenMethodDefinition);
    Node rewrittenAccumulateFunction = rewrittenMethodDefinition.getLastChild();
    assertNotNull(rewrittenAccumulateFunction);

    Node newMethodNameNode = rewrittenAccumulateFunction.getFirstChild();
    String newMethodName = newMethodNameNode.getString();
    assertEquals("JSCompiler_StaticMethods_accumulate", newMethodName);

    List<Node> methodCallSites = Lists.newArrayList();
    for (Node child : root.getDescendants()) {
      if (NodeUtil.isCall(child)) {
        Node methodCallSite = child;
        Node methodNameNode = methodCallSite.getFirstChild();
        String methodName = methodNameNode.getString();
        if (methodName.equals("accumulate")) {
          methodCallSites.add(methodCallSite);
        }
      }
    }

    assertEquals(1, methodCallSites.size());

    Node methodCallSite = methodCallSites.get(0);
    Node thisNode = methodCallSite.getFirstChild();
    Node rewrittenMethodCallSite =
        NodeFactory.createCall(findNodeByName(root, newMethodName), thisNode, 2);
    methodCallSite.replaceChildren(rewrittenMethodCallSite);

    assertCodeEqual("TestCases/DevirtualizePrototypeMethods/rewriteDefinitionIfEligible_success.js", root);
  }

  // Test for rewriteCallSites method
  public void testRewriteCallSites() {
    String testCode =
        "a = { accumulate : function(value) { this.total += value; return this.total; } }; " +
        "var total = a.accumulate(2);";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node rewrittenMethodDefinition = findNodeByName(root, "accumulate");
    assertNotNull(rewrittenMethodDefinition);
    Node rewrittenAccumulateFunction = rewrittenMethodDefinition.getLastChild();
    assertNotNull(rewrittenAccumulateFunction);

    List<Node> methodCallSites = Lists.newArrayList();
    for (Node child : root.getDescendants()) {
      if (NodeUtil.isCall(child)) {
        Node methodCallSite = child;
        Node methodNameNode = methodCallSite.getFirstChild();
        String methodName = methodNameNode.getString();
        if (methodName.equals("accumulate")) {
          methodCallSites.add(methodCallSite);
        }
      }
    }

    assertEquals(1, methodCallSites.size());

    Node methodCallSite = methodCallSites.get(0);
    Node originalMethodCallSite = methodCallSite.getFirstChild();
    Node objectNode = originalMethodCallSite.getPrevious();
    Node rewrittenMethodCallSite =
        NodeFactory.createCall(findNodeByName(root, "JSCompiler_StaticMethods_accumulate"), objectNode, 2);
    methodCallSite.replaceChildren(rewrittenMethodCallSite);

    assertCodeEqual("TestCases/DevirtualizePrototypeMethods/rewriteCallSites_success.js", root);
  }

  // Test for rewriteDefinition method
  public void testRewriteDefinition() {
    String testCode =
        "a = { " +
        "accumulate : function(value) { this.total += value; return this.total; } " +
        "};";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node rewrittenMethodDefinition = findNodeByName(root, "accumulate");
    assertNotNull(rewrittenMethodDefinition);
    Node rewrittenAccumulateFunction = rewrittenMethodDefinition.getLastChild();
    assertNotNull(rewrittenAccumulateFunction);

    Node newMethodNameNode = rewrittenAccumulateFunction.getFirstChild();
    String newMethodName = newMethodNameNode.getString();
    assertEquals("JSCompiler_StaticMethods_accumulate", newMethodName);

    Node rewrittenMethodBody = rewrittenAccumulateFunction.getLastChild();

    String expectedMethodBody = "function(self, value) { " +
        "self.total += value; return self.total; }";

    assertTrue(rewrittenMethodBody.toString().equals(expectedMethodBody));
  }

  // Test for fixFunctionType method
  public void testFixFunctionType() {
    String testCode =
        "function Test() {} " +
        "Test.prototype.test = function() {};";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node rewrittenMethodDefinition = findNodeByName(root, "test");
    assertNotNull(rewrittenMethodDefinition);
    Node rewrittenFunctionNode = rewrittenMethodDefinition.getLastChild();
    assertNotNull(rewrittenFunctionNode);

    FunctionType originalFunctionType = (FunctionType) rewrittenFunctionNode.getJSType();
    List<JSType> originalParameterTypes = originalFunctionType.getParameters();

    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();
    ObjectType thisType = typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);

    // Check if the function type has been updated
    FunctionType rewrittenFunctionType = (FunctionType) rewrittenFunctionNode.getJSType();
    List<JSType> rewrittenParameterTypes = rewrittenFunctionType.getParameters();
    assertEquals(1, rewrittenParameterTypes.size());
    assertEquals(thisType, rewrittenParameterTypes.get(0));
    assertEquals(originalFunctionType.getReturnType(), rewrittenFunctionType.getReturnType());
  }

  // Test for replaceReferencesToThis method
  public void testReplaceReferencesToThis() {
    String testCode =
        "function Test() {} " +
        "Test.prototype.test = function() { this.total = 0; }";

    Node root = parseTestSource(testCode);
    compileTestSource(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<Node> prototypeMethodDefinitions = Lists.newArrayList();
    for (Definition definition : defFinder.getDefinitions()) {
      if (definition.getRValue() != null && definition.getRValue().isFunction() &&
          isPrototypeMethodDefinition(definition.getNode())) {
        prototypeMethodDefinitions.add(definition.getNode());
      }
    }

    assertEquals(1, prototypeMethodDefinitions.size());

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node rewrittenMethodDefinition = findNodeByName(root, "test");
    assertNotNull(rewrittenMethodDefinition);
    Node rewrittenFunctionNode = rewrittenMethodDefinition.getLastChild();
    assertNotNull(rewrittenFunctionNode);

    Node rewrittenMethodBody = rewrittenFunctionNode.getLastChild();

    String expectedMethodBody = "function(self) { self.total = 0; }";

    assertTrue(rewrittenMethodBody.toString().equals(expectedMethodBody));
  }
}