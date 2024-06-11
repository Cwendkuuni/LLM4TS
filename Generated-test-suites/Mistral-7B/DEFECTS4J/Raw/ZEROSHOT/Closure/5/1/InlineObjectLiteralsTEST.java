package com.example.junit.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import junit.framework.Assert;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Setup test data
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Test case 1: Simple object literal
    Node root = Node.objectLit(
        Node.prop(Node.ident("a"), Node.number(1)),
        Node.prop(Node.ident("b"), Node.objectLit(
            Node.prop(Node.ident("c"), Node.number(2)),
            Node.prop(Node.ident("d"), Node.number(3))))
    );
    Scope scope = Node.newFunctionScope(Node.newTopLevelScope());
    inlineObjectLiterals.process(Node.SCOPE_GLOBALS, root);
    ReferenceCollection referenceInfo = scope.getReferenceInfo();

    // Check expected result
    List<Reference> refs = Lists.newArrayList(referenceInfo.getReferences("a"));
    Map<String, Node> initvals = Maps.newHashMap();
    inlineObjectLiterals.new InliningBehavior().fillInitialValues(refs.get(0), initvals);
    Node expected = Node.assign(
        Node.ident("JSCompiler_object_inline_a_0"),
        Node.objectLit(
            Node.prop(Node.ident("JSCompiler_object_inline_c_0"), initvals.get("c")),
            Node.prop(Node.ident("JSCompiler_object_inline_d_0"), initvals.get("d"))
        )
    );
    Node actual = referenceInfo.getReferences("a").get(0).getNode();
    Assert.assertEquals(expected.toSource(), actual.toSource());

    // Test case 2: Object literal with self-referential assignment
    root = Node.objectLit(
        Node.prop(Node.ident("x"), Node.objectLit(
            Node.prop(Node.ident("a"), Node.number(1)),
            Node.prop(Node.ident("b"), Node.ident("x"))
        ))
    );
    scope = Node.newFunctionScope(Node.newTopLevelScope());
    inlineObjectLiterals.process(Node.SCOPE_GLOBALS, root);
    referenceInfo = scope.getReferenceInfo();

    // Check that object literal is not inlined due to self-referential assignment
    Reference ref = referenceInfo.getReferences("x").get(0);
    Node actualSelfRef = ref.getNode();
    assertFalse(actualSelfRef.isObjectLit());
  }

  @Test
  public void testInlineObjectLiteralsWithInitValue() {
    // Setup test data
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Test case: Object literal with init value
    Node root = Node.objectLit(
        Node.prop(Node.ident("a"), Node.number(1)),
        Node.prop(Node.ident("b"), Node.number(2))
    );
    Scope scope = Node.newFunctionScope(Node.newTopLevelScope());
    inlineObjectLiterals.process(Node.SCOPE_GLOBALS, root);
    ReferenceCollection referenceInfo = scope.getReferenceInfo();

    // Check expected result
    List<Reference> refs = Lists.newArrayList(referenceInfo.getReferences("a"));
    Map<String, Node> initvals = Maps.newHashMap();
    inlineObjectLiterals.new InliningBehavior().fillInitialValues(refs.get(0), initvals);
    Node expected = Node.assign(
        Node.ident("JSCompiler_object_inline_a_0"),
        Node.objectLit(
            Node.prop(Node.ident("JSCompiler_object_inline_a_0"), Node.number(1)),
            Node.prop(Node.ident("JSCompiler_object_inline_b_0"), Node.number(2))
        )
    );
    Node actual = referenceInfo.getReferences("a").get(0).getNode();
    Assert.assertEquals(expected.toSource(), actual.toSource());

    // Test case: Object literal with init value and self-referential assignment
    root = Node.objectLit(
        Node.prop(Node.ident("x"), Node.objectLit(
            Node.prop(Node.ident("a"), Node.number(1)),
            Node.prop(Node.ident("b"), Node.ident("x"))
        )),
        Node.prop(Node.ident("y"), Node.number(2))
    );
    scope = Node.newFunctionScope(Node.newTopLevelScope());
    inlineObjectLiterals.process(Node.SCOPE_GLOBALS, root);
    referenceInfo = scope.getReferenceInfo();

    // Check that object literal with self-referential assignment is not inlined
    Reference refX = referenceInfo.getReferences("x").get(0);
    Node actualX = refX.getNode();
    assertFalse(actualX.isObjectLit());

    // Check that object literal with init value is inlined
    List<Reference> refsY = Lists.newArrayList(referenceInfo.getReferences("y"));
    Map<String, Node> initvalsY = Maps.newHashMap();
    inlineObjectLiterals.new InliningBehavior().fillInitialValues(refsY.get(0), initvalsY);
    Node expectedY = Node.assign(
        Node.ident("JSCompiler_object_inline_y_0"),
        Node.number(2)
    );
    Node actualY = referenceInfo.getReferences("y").get(0).getNode();
    Assert.assertEquals(expectedY.toSource(), actualY.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    // Setup test data
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Test case: Object literal with empty {}
    Node root = Node.objectLit();
    Scope scope = Node.newFunctionScope(Node.newTopLevelScope());
    inlineObjectLiterals.process(Node.SCOPE_GLOBALS, root);
    ReferenceCollection referenceInfo = scope.getReferenceInfo();
    List<Reference> refs = Lists.newArrayList(referenceInfo.getReferences(""));
    assertTrue(refs.isEmpty());
  }
}