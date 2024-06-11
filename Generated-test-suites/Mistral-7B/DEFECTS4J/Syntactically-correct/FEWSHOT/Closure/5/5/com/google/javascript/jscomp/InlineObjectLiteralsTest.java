package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPassManager passManager;

  public InlineObjectLiteralsTest(String jsCode) {
    this.compiler = new Compiler();
    this.passManager = new CompilerPassManager(compiler);
  }

  @Parameterized.Parameters(name = "{0}")
  public static List<Object> data() {
    return Lists.newArrayList(
        "var obj = {a: 1, b: 2};",
        "function foo() { var obj = {a: 1, b: 2}; }",
        "function foo() { return {a: 1, b: 2}; }",
        "function foo() { var obj = {a: {b: 1}, c: [1, 2]}; }",
        "function foo() { var obj = {a: [1, 2], b: {c: 1}}; }",
        "function foo() { var obj = {a: function() {}, b: function() {}}; }",
        "function foo() { var obj = {a: null, b: null}; }",
        "function foo() { var obj = {a: undefined, b: undefined}; }",
        "function foo() { var obj = {a: true, b: false}; }",
        "function foo() { var obj = {a: [], b: []}; }",
        "function foo() { var obj = {a: /foo/, b: /bar/}; }"
    );
  }

  @Test
  public void testInlineObjectLiterals() {
    passManager.addPass(new InlineObjectLiterals(compiler, Node::newNameSupplier));
    Scope rootScope = compiler.getRootScope();
    Node rootNode = Node.parse(jsCode);

    passManager.process(rootScope, rootNode);
    Node transformedNode = rootScope.getRootNode();

    // Check if the object literal has been split and variables have been created.
    ReferenceCollection referenceInfo = new ReferenceCollectingCallback(
        compiler, new ObjectInlineBehavior()).getReferenceMap(rootScope);
    for (Reference ref : referenceInfo.references) {
      Node node = ref.getNode();
      if (node.isObjectLit()) {
        // The object literal should be removed and replaced with variable assignments.
        assertFalse(node.isObjectLit());
        Map<String, Node> initvals = Maps.newHashMap();
        InliningBehavior inliningBehavior = new InliningBehavior();
        inliningBehavior.fillInitialValues(ref, initvals);
        for (Map.Entry<String, Node> entry : initvals.entrySet()) {
          Node expectedVarNode = NodeUtil.newVarNode(entry.getKey(), entry.getValue());
          Node actualVarNode = NodeUtil.findChildByName(transformedNode, entry.getKey());
          assertTrue(NodeUtil.isNodeEqual(expectedVarNode, actualVarNode));
        }
        break;
      }
    }
  }

  private static class ObjectInlineBehavior implements ReferenceCollectingCallback.Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      // Do nothing.
    }

    @Override
    public void visitClassOrInterface(Node node) {
      // Do nothing.
    }

    @Override
    public void visitComment(Node node) {
      // Do nothing.
    }

    @Override
    public void visitDirective(Node node) {
      // Do nothing.
    }

    @Override
    public void visitExceptionHandler(Node node) {
      // Do nothing.
    }

    @Override
    public void visitFunctionDeclaration(Node node) {
      // Do nothing.
    }

    @Override
    public void visitFunctionExpression(Node node) {
      // Do nothing.
    }

    @Override
    public void visitFunctionCall(Node node) {
      // Do nothing.
    }

    @Override
    public void visitIdentifier(Node node) {
      // Do nothing.
    }

    @Override
    public void visitIfStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitLabeledStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitLiteral(Node node) {
      // Do nothing.
    }

    @Override
    public void visitList(Node node) {
      // Do nothing.
    }

    @Override
    public void visitMember(Node node) {
      // Do nothing.
    }

    @Override
    public void visitStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitSwitchStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitTemplateLiteral(Node node) {
      // Do nothing.
    }

    @Override
    public void visitTryStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitVariableDeclarationList(Node node) {
      // Do nothing.
    }

    @Override
    public void visitWhileStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitWithStatement(Node node) {
      // Do nothing.
    }

    @Override
    public void visitYieldExpression(Node node) {
      // Do nothing.
    }

    @Override
    public void visitAssign(Node node) {
      // Handle object literal assignments.
      if (node.getFirstChild().isObjectLit()) {
        ReferenceCollection referenceInfo = getReferenceMap(node);
        for (Reference ref : referenceInfo.references) {
          if (ref.getParent().isGetProp()) {
            String key = ref.getParent().getLastChild().getString();
            Node expectedVarNode = NodeUtil.newVarNode(key, null);
            Node actualVarNode = NodeUtil.findChildByName(node.getParent(), key);
            assertTrue(NodeUtil.isNodeEqual(expectedVarNode, actualVarNode));
          }
        }
      }

      super.visitAssign(node);
    }

    private ReferenceCollection getReferenceMap(Node node) {
      Scope scope = NodeUtil.getScope(node);
      ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
          compiler, this);
      callback.process(node.getParent(), null);
      return callback.getReferenceMap(scope);
    }
  }

  private static class InliningBehavior implements ReferenceCollectingCallback.Behavior {
    // Implement the InliningBehavior class as in the original code.
    // ...
  }
}