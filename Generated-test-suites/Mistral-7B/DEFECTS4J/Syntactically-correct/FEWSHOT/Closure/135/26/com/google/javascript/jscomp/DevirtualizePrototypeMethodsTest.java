package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSTypeNative unknownType = typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
    JSType thisType = new ObjectType(typeRegistry, "Object", unknownType);

    Node root = new Node(Token.BLOCK, Lists.newArrayList());

    // Create prototype method definition
    Node prototype = new Node(Token.VAR, new Node(Token.NAME, "Prototype"));
    Node prototypeObject = new Node(Token.OBJECT_LITERAL, Lists.newArrayList());
    Node prototypeMethod = new Node(Token.METHOD, Lists.newArrayList());
    prototypeMethod.setProperty("value", new Node(Token.FUNCTION, Lists.newArrayList()));
    prototypeObject.addChildToBack(prototypeMethod);
    prototype.setProperty("prototype", prototypeObject);
    root.addChildToBack(prototype);

    // Create method call site
    Node object = new Node(Token.VAR, new Node(Token.NAME, "Prototype"));
    Node methodCall = new Node(Token.CALL, Lists.newArrayList(object, new Node(Token.STRING, "prototypeMethod")));
    root.addChildToBack(methodCall);

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions(), new ModuleManager(), new SourceFile(null), typeRegistry);
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    // Verify definition site
    SimpleDefinitionFinder definitionFinder = new SimpleDefinitionFinder(compiler);
    definitionFinder.process(null, root);
    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    DefinitionSite definitionSite = definitionSites.get(0);
    Definition definition = definitionSite.definition;
    Preconditions.checkState(definition != null);

    // Verify definition rewritten as global function
    Node rewrittenDefinition = definition.getRValue();
    Node functionNode = rewrittenDefinition.getLastChild();
    Node nameNode = functionNode.getFirstChild();
    String newMethodName = nameNode.getString();
    assertThat(newMethodName).isEqualTo("JSCompiler_StaticMethods_prototypeMethod");
    Node thisArg = functionNode.getFirstChild().getNext();
    Node argumentList = functionNode.getFirstChild().getNext().getNext();
    List<Node> arguments = Lists.newArrayList();
    arguments.add(thisArg.getFirstChild());
    arguments.addAll(Lists.newArrayList(argumentList.getChildren()));
    Node body = functionNode.getLastChild();

    // Verify body with replaced references to 'this'
    replaceReferencesToThis(body, "Prototype");

    // Verify return type is void
    FunctionType functionType = (FunctionType) functionNode.getJSType();
    Preconditions.checkState(functionType != null);
    Preconditions.checkState(functionType.getReturnType() == JSType.VOID);

    // Verify call site rewritten as call to global function
    Node rewrittenCallSite = methodCall.getFirstChild();
    Node newMethodNameNode = new Node(Token.NAME, "JSCompiler_StaticMethods_prototypeMethod");
    Preconditions.checkState(rewrittenCallSite.getLastChild().isIdenticalTo(object));
    rewrittenCallSite.replaceChild(rewrittenCallSite.getLastChild(), newMethodNameNode);
    Preconditions.checkState(rewrittenCallSite.isIdenticalTo(new Node(Token.CALL, Lists.newArrayList(newMethodNameNode, object.getFirstChild().getNext()))));

    // Verify function types updated
    Preconditions.checkState(functionNode.getJSType().getParameters().get(0).isIdenticalTo(thisType));
    Preconditions.checkState(functionNode.getJSType().getParameters().get(1).isIdenticalTo(definition.getNode().getFirstChild().getNext()));
  }

  /**
   * Replaces references to 'this' with a given name in a node.
   */
  private void replaceReferencesToThis(Node node, String name) {
    if (NodeUtil.isFunction(node)) {
      return;
    }

    for (Node child : node.children()) {
      if (NodeUtil.isThis(child)) {
        Node newName = Node.newString(Token.NAME, name);
        node.replaceChild(child, newName);
      } else {
        replaceReferencesToThis(child, name);
      }
    }
  }
}