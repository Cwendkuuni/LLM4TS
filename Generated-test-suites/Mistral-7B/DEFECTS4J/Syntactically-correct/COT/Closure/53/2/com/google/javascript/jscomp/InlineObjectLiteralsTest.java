package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setup() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test";
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testSimpleInlineObjectLiteral() {
    Node externs = Node.object();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var = new Var("testObj", Node.SC_VAR, Node.ANY, null, null, null, root);

    assertThat(referenceInfo.getInitializingReference()).isNull();
    assertThat(referenceInfo.references.size()).isEqualTo(0);

    // Check that the variable 'testObj' was inlined.
    Var inlinedVar = var.getScope().getVar("JSCompiler_object_inline_testObj_0");
    assertThat(inlinedVar).isNotNull();
  }

  @Test
  public void testInlineObjectLiteralWithInitialValue() {
    Node externs = Node.object();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var = new Var("testObj", Node.SC_VAR, Node.ANY, null, null, null, root);

    Node initNode = Node.objectLiteral(
        Node.property("name", Node.string("test")),
        Node.property("age", Node.number(30))
    );

    Node init = Node.newAssign(Node.VAR_DECLARATION, var, Node.objectLiteral());
    init.getLastChild().addChildToFront(initNode);

    root.addChildToFront(init);

    assertThat(referenceInfo.getInitializingReference()).isEqualTo(init);
    assertThat(referenceInfo.references.size()).isEqualTo(2);

    // Check that the variables 'name' and 'age' were inlined.
    Var nameVar = var.getScope().getVar("JSCompiler_object_inline_name_0");
    Var ageVar = var.getScope().getVar("JSCompiler_object_inline_age_0");

    assertThat(nameVar).isNotNull();
    assertThat(ageVar).isNotNull();
  }

  @Test
  public void testInlineObjectLiteralWithNestedProperties() {
    Node externs = Node.object();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var = new Var("testObj", Node.SC_VAR, Node.ANY, null, null, null, root);

    Node initNode = Node.objectLiteral(
        Node.property("name", Node.objectLiteral(
            Node.property("first", Node.string("John")),
            Node.property("last", Node.string("Doe")))
        ),
        Node.property("age", Node.number(30))
    );

    Node init = Node.newAssign(Node.VAR_DECLARATION, var, Node.objectLiteral());
    init.getLastChild().addChildToFront(initNode);

    root.addChildToFront(init);

    assertThat(referenceInfo.getInitializingReference()).isEqualTo(init);
    assertThat(referenceInfo.references.size()).isEqualTo(4);

    // Check that the variables 'first', 'last', 'name' and 'age' were inlined.
    Var firstVar = var.getScope().getVar("JSCompiler_object_inline_first_0");
    Var lastVar = var.getScope().getVar("JSCompiler_object_inline_last_0");
    Var nameVar = var.getScope().getVar("JSCompiler_object_inline_name_0");
    Var ageVar = var.getScope().getVar("JSCompiler_object_inline_age_0");

    assertThat(firstVar).isNotNull();
    assertThat(lastVar).isNotNull();
    assertThat(nameVar).isNotNull();
    assertThat(ageVar).isNotNull();
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithSelfReference() {
    Node externs = Node.object();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var = new Var("testObj", Node.SC_VAR, Node.ANY, null, null, null, root);

    Node initNode = Node.objectLiteral(
        Node.property("self", Node.call(Node.identifier("testObj"), Node.IDENTIFIER, null))
    );

    Node init = Node.newAssign(Node.VAR_DECLARATION, var, Node.objectLiteral());
    init.getLastChild().addChildToFront(initNode);

    root.addChildToFront(init);

    inlineObjectLiterals.splitObject(var, referenceInfo);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithCircularReference() {
    Node externs = Node.object();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var1 = new Var("testObj1", Node.SC_VAR, Node.ANY, null, null, null, root);
    Var var2 = new Var("testObj2", Node.SC_VAR, Node.ANY, null, null, null, root);

    Node initNode1 = Node.objectLiteral(
        Node.property("ref", Node.call(Node.identifier("testObj2"), Node.IDENTIFIER, null))
    );

    Node initNode2 = Node.objectLiteral(
        Node.property("ref", Node.call(Node.identifier("testObj1"), Node.IDENTIFIER, null))
    );

    Node init1 = Node.newAssign(Node.VAR_DECLARATION, var1, Node.objectLiteral());
    init1.getLastChild().addChildToFront(initNode1);

    Node init2 = Node.newAssign(Node.VAR_DECLARATION, var2, Node.objectLiteral());
    init2.getLastChild().addChildToFront(initNode2);

    root.addChildToFront(init1);
    root.addChildToFront(init2);

    inlineObjectLiterals.splitObject(var1, referenceInfo);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithGlobalVariables() {
    Node externs = Node.object();
    Node root = Node.block(Node.newAssign(Node.VAR_DECLARATION, Node.newVar("window", Node.SC_GLOBAL, Node.ANY, null, null, null, root), Node.UNDEFINED));

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceInfo = callback.getReferenceInfo();

    Var var = new Var("testObj", Node.SC_VAR, Node.ANY, null, null, null, root);

    Node initNode = Node.objectLiteral(
        Node.property("window", Node.call(Node.identifier("window"), Node.IDENTIFIER, null))
    );

    Node init = Node.newAssign(Node.VAR_DECLARATION, var, Node.objectLiteral());
    init.getLastChild().addChildToFront(initNode);

    root.addChildToFront(init);

    inlineObjectLiterals.splitObject(var, referenceInfo);
  }
}