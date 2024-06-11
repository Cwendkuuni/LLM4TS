package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler() {};
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;

    InliningBehavior inliningBehavior = new InliningBehavior();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(abstractCompiler, () -> null);

    // Prepare input nodes
    Node externs = Node.root();
    Node root = Node.newObjectLiteral()
        .addMember("a", Node.newNumberLiteral(1))
        .addMember("b", Node.newNumberLiteral(2))
        .addMember("c", Node.newObjectLiteral()
            .addMember("x", Node.newNumberLiteral(3)))
        .addMember("d", Node.newStringLiteral("hello"))
        .addMember("e", Node.newFunction(
            null,
            ImmutableList.of(),
            Node.newBlock(
                Node.newVariable("result", Node.TYPE_NUMBER),
                Node.newAssign(Node.newVariable("result"), Node.newNumberLiteral(4)),
                Node.newReturn(Node.newVariable("result"))))
        );

    // Run the InlineObjectLiterals pass
    inlineObjectLiterals.process(externs, root);

    // Assert the expected output
    ReferenceCollectingCallback referenceCollectingCallback =
        new ReferenceCollectingCallback(abstractCompiler, inliningBehavior);
    referenceCollectingCallback.process(externs, root);

    ReferenceMap referenceMap = referenceCollectingCallback.getReferenceMap();

    // Verify the expected variables are inlined
    for (Iterator<Var> it = root.getScope().getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith(InlineObjectLiterals.VAR_PREFIX)) {
        ReferenceCollection refs = referenceMap.getReferences(v);
        if (refs != null) {
          assertEquals(1, refs.references.size());
          Reference ref = refs.references.get(0);
          assertNotNull(ref.getNode());
          assertNotNull(ref.getAssignedValue());
        }
      }
    }

    // Verify the expected variables are not forbidden from being inlined
    Var varForbidden = root.getScope().getVar("this");
    assertFalse(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(varForbidden));

    // Verify the expected variables are blacklisted when their references
    // are out of sync with the state of the AST
    Var varBlacklisted = NodeUtil.newVarNode(
        "varBlacklisted", Node.newObjectLiteral()
            .addMember("x", Node.newObjectLiteral()
                .addMember("y", Node.newNumberLiteral(1))));
    referenceCollectingCallback.getBehavior().blacklistVarReferencesInTree(
        varBlacklisted.getInitialValue(), varBlacklisted.scope);
    assertFalse(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(varBlacklisted));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    Compiler compiler = new Compiler() {};
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;

    InliningBehavior inliningBehavior = new InliningBehavior();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(abstractCompiler, () -> null);

    // Prepare input nodes
    Node externs = Node.root();
    Node root = Node.newObjectLiteral();

    // Run the InlineObjectLiterals pass
    inlineObjectLiterals.process(externs, root);

    // Assert the expected output
    ReferenceCollectingCallback referenceCollectingCallback =
        new ReferenceCollectingCallback(abstractCompiler, inliningBehavior);
    referenceCollectingCallback.process(externs, root);

    ReferenceMap referenceMap = referenceCollectingCallback.getReferenceMap();

    // Verify the expected variables are inlined
    for (Iterator<Var> it = root.getScope().getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith(InlineObjectLiterals.VAR_PREFIX)) {
        ReferenceCollection refs = referenceMap.getReferences(v);
        if (refs != null) {
          assertEquals(1, refs.references.size());
          Reference ref = refs.references.get(0);
          assertNotNull(ref.getNode());
          assertNotNull(ref.getAssignedValue());
        }
      }
    }

    // Verify the expected variables are not forbidden from being inlined
    Var varForbidden = root.getScope().getVar("this");
    assertFalse(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(varForbidden));

    // Verify there is no object to inline
    assertTrue(referenceMap.isEmpty());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialObject() {
    Compiler compiler = new Compiler() {};
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;

    InliningBehavior inliningBehavior = new InliningBehavior();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(abstractCompiler, () -> null);

    // Prepare input nodes
    Node externs = Node.root();
    Node root = Node.newObjectLiteral()
        .addMember("a", Node.newObjectLiteral()
            .addMember("x", Node.newObjectLiteral()
                .addMember("y", Node.newName("a"))));

    // Run the InlineObjectLiterals pass
    inlineObjectLiterals.process(externs, root);

    // Assert the expected output
    ReferenceCollectingCallback referenceCollectingCallback =
        new ReferenceCollectingCallback(abstractCompiler, inliningBehavior);
    referenceCollectingCallback.process(externs, root);

    ReferenceMap referenceMap = referenceCollectingCallback.getReferenceMap();

    // Verify the expected variables are not inlined
    for (Iterator<Var> it = root.getScope().getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith(InlineObjectLiterals.VAR_PREFIX)) {
        ReferenceCollection refs = referenceMap.getReferences(v);
        assertNull(refs);
      }
    }

    // Verify there is no object to inline
    assertTrue(referenceMap.isEmpty());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionObject() {
    Compiler compiler = new Compiler() {};
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;

    InliningBehavior inliningBehavior = new InliningBehavior();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(abstractCompiler, () -> null);

    // Prepare input nodes
    Node externs = Node.root();
    Node root = Node.newObjectLiteral()
        .addMember("a", Node.newFunction(
            null,
            ImmutableList.of(),
            Node.newBlock(
                Node.newVariable("result", Node.TYPE_NUMBER),
                Node.newAssign(Node.newVariable("result"), Node.newNumberLiteral(1)),
                Node.newReturn(Node.newVariable("result")))));

    // Run the InlineObjectLiterals pass
    inlineObjectLiterals.process(externs, root);

    // Assert the expected output
    ReferenceCollectingCallback referenceCollectingCallback =
        new ReferenceCollectingCallback(abstractCompiler, inliningBehavior);
    referenceCollectingCallback.process(externs, root);

    ReferenceMap referenceMap = referenceCollectingCallback.getReferenceMap();

    // Verify the expected variables are not inlined
    for (Iterator<Var> it = root.getScope().getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith(InlineObjectLiterals.VAR_PREFIX)) {
        ReferenceCollection refs = referenceMap.getReferences(v);
        assertNull(refs);
      }
    }

    // Verify there is no object to inline
    assertTrue(referenceMap.isEmpty());
  }
}