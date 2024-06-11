package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.test.TestNodeBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private static Compiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new DevirtualizePrototypeMethods(compiler));
    return compiler;
  }

  private static Node buildTestCode() {
    TestNodeBuilder builder = new TestNodeBuilder();

    // Object with prototype method
    Node objDecl = builder.newObjectDeclaration("obj", null);
    Node objProto = builder.newObjectLiteral();
    Node protoProp = builder.newPropertyDeclaration("prototype", null, null);
    Node protoMethod = builder.newFunctionDeclaration(
        "prototypeMethod", null,
        ImmutableList.of(),
        new Node(Token.BLOCK, builder.newEmptyStatementList()));
    objProto.addChildToBack(protoProp);
    objProto.addChildToBack(protoMethod);
    objDecl.addChildToBack(objProto);

    // Call to prototype method
    Node callSite = builder.newCallExpression(
        builder.newGetExpression(
            builder.newIdent("obj"), "prototypeMethod"),
        ImmutableList.of());

    // Return the call site
    return callSite;
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = createCompiler();
    Node root = buildTestCode();

    CompilerResult result = compiler.compile(ImmutableList.of(root), null, null);

    // Ensure the code was transformed correctly
    List<Definition> definitions = SimpleDefinitionFinder.extractDefinitions(root);
    Definition definition = definitions.get(0);

    JSType type = definition.getRValue().getJSType();
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();
    ObjectType originalFunctionType = (ObjectType) type;

    String originalMethodName = originalFunctionType.getName();
    String newMethodName = "JSCompiler_StaticMethods_" + originalMethodName;

    Node transformedCallSite = result.getRootNode();
    Node transformedFunction = findFunctionByName(transformedCallSite, newMethodName);

    assertEquals("Unexpected number of arguments for transformed function",
                 4, transformedFunction.getNumberOfChildren());

    Node transformedThis = transformedFunction.getChild(0);
    assertEquals("Unexpected name for 'this' in transformed function",
                 "obj", transformedThis.getString());

    Node transformedBody = transformedFunction.getLastChild();
    List<Node> bodyChildren = transformedBody.children();
    assertEquals("Unexpected number of statements in transformed function body",
                 1, bodyChildren.size());

    Node transformedThisInBody = bodyChildren.get(0);
    assertEquals("Unexpected name for 'this' in transformed function body",
                 "this", transformedThisInBody.getString());

    // Ensure the original call site was replaced
    Node originalCallSite = findNodeBySimpleName(root, "prototypeMethod");
    assertNull("Unexpected original call site present", originalCallSite);

    // Ensure the transformed function was added to the global scope
    Node transformedFunctionInGlobalScope = findNodeBySimpleName(
        result.getRootNode(), newMethodName);
    assertNotNull("Unexpected transformed function not present",
                  transformedFunctionInGlobalScope);
  }

  private Node findFunctionByName(Node node, String name) {
    for (Node child = node; child != null; child = child.getParent()) {
      if (child.getNodeType() == Token.FUNCTION) {
        FunctionType functionType = (FunctionType) child.getJSType();
        if (functionType.getName().equals(name)) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findNodeBySimpleName(Node node, String name) {
    for (Node child = node; child != null; child = child.getParent()) {
      if (child.getNodeType() == Token.IDENT) {
        if (child.getString().equals(name)) {
          return child;
        }
      }
    }
    return null;
  }
}