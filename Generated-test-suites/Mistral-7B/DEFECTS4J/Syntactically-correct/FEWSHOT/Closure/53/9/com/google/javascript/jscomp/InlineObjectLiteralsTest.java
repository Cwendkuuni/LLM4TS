package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test cases for the InlineObjectLiterals class.
 */
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Prepare test data
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test_inline_var_";

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Test simple object literals
    Node externs = Node.nullNode();
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), null);
    inlineObjectLiterals.process(externs, root);

    // Test case 1: Object literal with no references
    Var v1 = new Var("obj1", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope1 = new Scope();
    scope1.setRoot(root);
    scope1.declareVar(v1);

    Node obj1 = new Node(Token.OBJECTLIT, null, null);
    obj1.addChild(new Node(Token.KEY, "key1", null, null));
    obj1.addChild(new Node(Token.STRING, "value1", null, null));

    Node assignment1 = new Node(Token.ASSIGN, obj1, v1);
    Node statement1 = new Node(Token.EXPR_STATEMENT, assignment1, null);

    root.addChild(statement1);

    ReferenceCollection referenceInfo1 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo1);

    List<Reference> refs1 = referenceInfo1.references;
    assertEquals(0, refs1.size());

    // Test case 2: Object literal with references
    Var v2 = new Var("obj2", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope2 = new Scope();
    scope2.setRoot(root);
    scope2.declareVar(v2);

    Node obj2 = new Node(Token.OBJECTLIT, null, null);
    obj2.addChild(new Node(Token.KEY, "key1", null, null));
    obj2.addChild(new Node(Token.STRING, "value1", null, null));

    Var refVar1 = new Var("refVar1", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope refScope1 = new Scope();
    refScope1.setRoot(root);
    refScope1.declareVar(refVar1);

    Node getprop1 = new Node(Token.GETPROP, refVar1, new Node(Token.NAME, "key1", null, null));

    Node assignment2 = new Node(Token.ASSIGN, obj2, v2);
    Node statement2 = new Node(Token.EXPR_STATEMENT, assignment2, null);

    root.addChild(statement2);
    root.addChild(statement1);

    referenceInfo1 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo1);

    List<Reference> refs2 = referenceInfo1.references;
    assertEquals(2, refs2.size());
    Reference ref1 = refs2.get(0);
    assertEquals(Token.GETPROP, ref1.getNode().getType());
    assertEquals("refVar1", ref1.getNode().getLastChild().getString());
    Reference ref2 = refs2.get(1);
    assertEquals(Token.NAME, ref2.getNode().getType());
    assertEquals("key1", ref2.getNode().getString());

    // Test case 3: Object literal with circular reference
    Var v3 = new Var("obj3", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope3 = new Scope();
    scope3.setRoot(root);
    scope3.declareVar(v3);

    Node obj3 = new Node(Token.OBJECTLIT, null, null);
    obj3.addChild(new Node(Token.KEY, "key1", null, null));
    obj3.addChild(new Node(Token.STRING, "value1", null, null));

    Node getprop3 = new Node(Token.GETPROP, v3, new Node(Token.NAME, "key1", null, null));
    obj3.addChild(getprop3);

    getprop3.setParent(obj3);

    Node assignment3 = new Node(Token.ASSIGN, obj3, v3);
    Node statement3 = new Node(Token.EXPR_STATEMENT, assignment3, null);

    root.addChild(statement3);

    ReferenceCollection referenceInfo3 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo3);

    List<Reference> refs3 = referenceInfo3.references;
    assertEquals(1, refs3.size());
    Reference ref3 = refs3.get(0);
    assertEquals(Token.NAME, ref3.getNode().getType());
    assertEquals("obj3", ref3.getNode().getString());

    // Test case 4: Object literal with non-object literal value
    Var v4 = new Var("obj4", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope4 = new Scope();
    scope4.setRoot(root);
    scope4.declareVar(v4);

    Node obj4 = new Node(Token.OBJECTLIT, null, null);
    obj4.addChild(new Node(Token.KEY, "key1", null, null));
    obj4.addChild(new Node(Token.NUMBER, "1.0", null, null));

    Node assignment4 = new Node(Token.ASSIGN, obj4, v4);
    Node statement4 = new Node(Token.EXPR_STATEMENT, assignment4, null);

    root.addChild(statement4);

    ReferenceCollection referenceInfo4 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo4);

    List<Reference> refs4 = referenceInfo4.references;
    assertEquals(0, refs4.size());

    // Test case 5: Object literal with self reference
    Var v5 = new Var("obj5", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope5 = new Scope();
    scope5.setRoot(root);
    scope5.declareVar(v5);

    Node obj5 = new Node(Token.OBJECTLIT, null, null);
    obj5.addChild(new Node(Token.KEY, "key1", null, null));
    obj5.addChild(new Node(Token.NAME, v5.getName(), null, null));

    Node assignment5 = new Node(Token.ASSIGN, obj5, v5);
    Node statement5 = new Node(Token.EXPR_STATEMENT, assignment5, null);

    root.addChild(statement5);

    ReferenceCollection referenceInfo5 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo5);

    List<Reference> refs5 = referenceInfo5.references;
    assertNull(refs5);

    // Test case 6: Object literal with exported reference
    Var v6 = new Var("obj6", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope6 = new Scope();
    scope6.setRoot(root);
    scope6.declareVar(v6);

    Node obj6 = new Node(Token.OBJECTLIT, null, null);
    obj6.addChild(new Node(Token.KEY, "key1", null, null));
    obj6.addChild(new Node(Token.STRING, "value1", null, null));

    Node exportStatement = new Node(Token.EXPORT, new Node(Token.NAME, "obj6", null, null), null);

    root.addChild(exportStatement);
    root.addChild(statement1);

    ReferenceCollection referenceInfo6 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo6);

    List<Reference> refs6 = referenceInfo6.references;
    assertEquals(0, refs6.size());

    // Test case 7: Object literal with reference to undefined property
    Var v7 = new Var("obj7", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope7 = new Scope();
    scope7.setRoot(root);
    scope7.declareVar(v7);

    Node obj7 = new Node(Token.OBJECTLIT, null, null);
    obj7.addChild(new Node(Token.KEY, "key1", null, null));
    obj7.addChild(new Node(Token.NAME, "undefinedProperty", null, null));

    Node assignment7 = new Node(Token.ASSIGN, obj7, v7);
    Node statement7 = new Node(Token.EXPR_STATEMENT, assignment7, null);

    root.addChild(statement7);

    ReferenceCollection referenceInfo7 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo7);

    List<Reference> refs7 = referenceInfo7.references;
    assertEquals(0, refs7.size());

    // Test case 8: Object literal with self-referential property
    Var v8 = new Var("obj8", Var.VAR_TYPE_DECL, null, null, null, null, null, null, null, null, null, null);
    Scope scope8 = new Scope();
    scope8.setRoot(root);
    scope8.declareVar(v8);

    Node obj8 = new Node(Token.OBJECTLIT, null, null);
    obj8.addChild(new Node(Token.KEY, "key1", null, null));
    obj8.addChild(new Node(Token.KEY, "key2", null, null));

    Node getprop8 = new Node(Token.GETPROP, v8, new Node(Token.NAME, "key1", null, null));
    Node setprop8 = new Node(Token.SETPROP, getprop8, new Node(Token.NAME, "key2", null, null), new Node(Token.NAME, v8.getName(), null, null));

    obj8.addChild(getprop8);
    obj8.addChild(setprop8);

    Node assignment8 = new Node(Token.ASSIGN, obj8, v8);
    Node statement8 = new Node(Token.EXPR_STATEMENT, assignment8, null);

    root.addChild(statement8);

    ReferenceCollection referenceInfo8 = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(null, referenceInfo8);

    List<Reference> refs8 = referenceInfo8.references;
    assertEquals(0, refs8.size());
  }
}