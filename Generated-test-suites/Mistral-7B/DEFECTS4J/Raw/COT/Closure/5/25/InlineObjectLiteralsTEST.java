package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

@RunWith(Parameterized.class)
public class InlineObjectLiteralsTest {

  private final Node root;
  private final Node externs;

  public InlineObjectLiteralsTest(Node root, Node externs) {
    this.root = root;
    this.externs = externs;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return ImmutableList.of(
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'};}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x.b = 'bar';}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x.b = 'bar'; delete x.a;}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x = {c: 2};}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x.c = 2;}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x.a = 2;}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: {b: 1}}; x.a.b = 2;</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 1}; x.a = 2; x.b = 3;</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1}; x.a = {b: 1};}</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1}; x.a = {b: 1, c: {d: 1}};</script>"),
          Node.createTree("<extern name='f'/>")
        },
        new Object[] {
          Node.createTree("<script>function f() {var x = {a: 1, b: 'foo'}; x.b = 'bar'; x = {}; x.b = 'baz';}</script>"),
          Node.createTree("<extern name='f'/>")
        }
    );
  }

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> "JSCompiler_object_inline_");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    ReferenceMap referenceMap = referenceCollection.getMap();

    for (Map.Entry<Var, ReferenceCollection> entry : referenceMap.entrySet()) {
      Var var = entry.getKey();
      ReferenceCollection refs = entry.getValue();
      List<Reference> references = refs.getReferences();

      // Check if the number of references matches the number of keys
      // in the object literal.
      int numRefs = references.size();
      int numKeys = var.scope.getVars().size();

      Map<String, Node> keys = ImmutableMap.of();
      for (Node node : var.scope.getVars()) {
        String key = node.getString();
        keys.put(key, node);
      }

      // Check if each reference corresponds to a key in the object literal.
      for (int i = 0; i < numRefs; i++) {
        Reference reference = references.get(i);
        String key = reference.getString();
        Node keyNode = keys.get(key);

        assertNotNull(keyNode);
        assertNull(reference.getAssignedValue());
        assertNull(reference.getInitializingValue());
      }

      // Check if each key in the object literal corresponds to a reference.
      for (String key : keys.keySet()) {
        Reference reference = referenceCollection.getReference(key);
        assertNotNull(reference);
        assertSame(var, reference.getVar());
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotInlineSelfReferentialObject() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> "JSCompiler_object_inline_");

    Node root = Node.createTree("<script>function f() {var x = {a: x};}</script>");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(Node.createTree(""), root);
  }
}