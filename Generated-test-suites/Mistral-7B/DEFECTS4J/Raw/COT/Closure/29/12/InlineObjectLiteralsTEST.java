package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerTestCase {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private ReferenceCollectingCallback referenceCollectingCallback;
  private InlineObjectLiterals inlineObjectLiterals;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    referenceCollectingCallback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @org.junit.Test
  public void testInlineObjectLiterals_BasicScenario() {
    Node externs = parseJs("{}");
    Node root = parseJs("var x = {a: 1, b: 2};");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    Iterator<Var> varIterator = scope.getVars();

    Var x = null;
    while (varIterator.hasNext()) {
      Var current = varIterator.next();
      if (current.name.equals("x")) {
        x = current;
        break;
      }
    }

    List<Reference> references = referenceCollectingCallback.getReferences(x);
    // Check if the object is inlined and split into individual variables
    for (Reference reference : references) {
      Node node = reference.getNode();
      Node expectedNode = null;
      if (node.isAssign()) {
        if (reference.isInitializingDeclaration()) {
          expectedNode = NodeUtil.newVarNode("a_JSCompiler_object_inline_", null);
        } else {
          expectedNode = NodeUtil.newVarNode("a_JSCompiler_object_inline_", NodeUtil.newUndefinedNode(null));
        }
      } else if (node.isGetProp()) {
        expectedNode = NodeUtil.newName("a_JSCompiler_object_inline_");
      }
      assertThat(node).isEqualTo(expectedNode);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiterals_SelfReference() {
    Node externs = parseJs("{}");
    Node root = parseJs("var x = {a: x, b: 1};");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    Iterator<Var> varIterator = scope.getVars();

    Var x = null;
    while (varIterator.hasNext()) {
      Var current = varIterator.next();
      if (current.name.equals("x")) {
        x = current;
        break;
      }
    }

    List<Reference> references = referenceCollectingCallback.getReferences(x);
    Node xAssignNode = references.get(0).getNode();
    Node expectedXAssignNode = NodeUtil.newAssign(
        NodeUtil.newName("a_JSCompiler_object_inline_a_"),
        NodeUtil.newName("a_JSCompiler_object_inline_x_")
    );

    // Check if the object is inlined but self reference is not handled
    assertThat(xAssignNode).isNotEqualTo(expectedXAssignNode);
  }

  @org.junit.Test
  public void testInlineObjectLiterals_NestedObject() {
    Node externs = parseJs("{}");
    Node root = parseJs("var x = {a: {b: 1}, c: 2};");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    Iterator<Var> varIterator = scope.getVars();

    Var x = null;
    while (varIterator.hasNext()) {
      Var current = varIterator.next();
      if (current.name.equals("x")) {
        x = current;
        break;
      }
    }

    List<Reference> references = referenceCollectingCallback.getReferences(x);
    Node xAssignNode = references.get(0).getNode();
    Node expectedXAssignNode = NodeUtil.newAssign(
        NodeUtil.newVarNode("a_JSCompiler_object_inline_x_a_"),
        NodeUtil.newObjectLit(
          NodeUtil.newProperty("b", NodeUtil.newObjectLit(
            NodeUtil.newProperty("1", null)
          ))
        )
    );

    // Check if the nested object is inlined
    assertThat(xAssignNode).isEqualTo(expectedXAssignNode);
  }

  @org.junit.Test
  public void testInlineObjectLiterals_InvalidObjectLiteral() {
    Node externs = parseJs("{}");
    Node root = parseJs("var x = {a: 1, b: 2, c: function(){}};");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    Iterator<Var> varIterator = scope.getVars();

    Var x = null;
    while (varIterator.hasNext()) {
      Var current = varIterator.next();
      if (current.name.equals("x")) {
        x = current;
        break;
      }
    }

    List<Reference> references = referenceCollectingCallback.getReferences(x);
    Node xAssignNode = references.get(0).getNode();

    // Check if the object with function is not inlined
    assertThat(xAssignNode.isObjectLit()).isFalse();
  }

  @org.junit.Test
  public void testInlineObjectLiterals_GlobalVariable() {
    Node externs = parseJs("{}");
    Node root = parseJs("var x = {g: 1};");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    Iterator<Var> varIterator = scope.getVars();

    Var x = null;
    while (varIterator.hasNext()) {
      Var current = varIterator.next();
      if (current.name.equals("x")) {
        x = current;
        break;
      }
    }

    List<Reference> references = referenceCollectingCallback.getReferences(x);
    Node xAssignNode = references.get(0).getNode();

    // Check if the global variable is not inlined
    assertThat(xAssignNode.isVar()).isTrue();
  }

  private static class InliningBehavior extends ReferenceCollectingCallback.Behavior {
  }
}