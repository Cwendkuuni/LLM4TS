package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testUndefinedVarError() {
    VarCheck varCheck = new VarCheck(null);
    Node root = Node.newObject("Object");
    Node n = Node.newIdentifier("undefinedVar");
    root.addChildToBack(n);
    varCheck.process(null, root);
    varCheck.visit(null, null, root);
  }

  @Test
  public void testDuplicateDeclaration() {
    VarCheck varCheck = new VarCheck(null);
    Node root = Node.newObject("Object");
    Node n1 = Node.newIdentifier("duplicate");
    Node n2 = Node.newIdentifier("duplicate");
    root.addChildToBack(n1);
    root.addChildToBack(n2);
    varCheck.process(null, root);
    varCheck.visit(null, null, root);
  }

  @Test
  public void testUndefinedVarInFunction() {
    VarCheck varCheck = new VarCheck(null);
    AbstractCompiler compiler = new CompilerPass() {}; // A dummy compiler.
    Node root = Node.newFunction("function() {}", null);
    Node n = Node.newIdentifier("undefinedVar");
    root.addChildToBack(n);
    varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    varCheck.visit(null, null, root);
  }

  @Test
  public void testUndefinedVarInGlobalScope() {
    VarCheck varCheck = new VarCheck(null);
    AbstractCompiler compiler = new CompilerPass() {}; // A dummy compiler.
    Node root = Node.newObject("Object");
    Node n = Node.newIdentifier("undefinedVar");
    root.addChildToBack(n);
    varCheck = new VarCheck(compiler, true);
    varCheck.process(null, root);
    varCheck.visit(null, null, root);
  }

  @Test
  public void testMissingModuleDependency() {
    VarCheck varCheck = new VarCheck(null);
    CompilerInput input1 = new CompilerInput("module1.js", null, new JSModule("module1"));
    CompilerInput input2 = new CompilerInput("module2.js", null, new JSModule("module2"));
    Node root1 = Node.newObject("module1");
    Node root2 = Node.newObject("module2");

    Node n1 = Node.newIdentifier("module1Var");
    Node n2 = Node.newIdentifier("module2Var");

    root1.addChildToBack(n1);
    root2.addChildToBack(n2);

    varCheck.process(input1, root1);
    varCheck.process(input2, root2);
    varCheck.visit(null, null, root1);
    varCheck.visit(null, null, root2);

    assertFalse(varCheck.strictExternCheck);
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
    assertTrue(varsToDeclareInExternsIsEmpty(varCheck));

    varCheck.synthesizedExternsInput = new CompilerInput("{SyntheticVarsDecl}");
    varCheck.synthesizedExternsRoot = Node.newObject("SyntheticVarsDecl");

    varCheck.process(input1, root1);
    varCheck.process(input2, root2);
    varCheck.visit(null, null, root1);
    varCheck.visit(null, null, root2);

    assertSame(varCheck.synthesizedExternsInput, getSynthesizedExternsInput(varCheck));
    assertSame(varCheck.synthesizedExternsRoot, getSynthesizedExternsRoot(varCheck));
    assertTrue(varsToDeclareInExternsIsEmpty(varCheck));
  }

  @Test
  public void testStrictModuleDependency() {
    VarCheck varCheck = new VarCheck(null);
    CompilerInput input1 = new CompilerInput("module1.js", null, new JSModule("module1"));
    CompilerInput input2 = new CompilerInput("module2.js", null, new JSModule("module2"));
    Node root1 = Node.newObject("module1");
    Node root2 = Node.newObject("module2");

    Node n1 = Node.newIdentifier("module1Var");
    Node n2 = Node.newIdentifier("module2Var");

    root1.addChildToBack(n1);
    root2.addChildToBack(n2);

    varCheck.process(input1, root1);
    varCheck.process(input2, root2);
    varCheck.visit(null, null, root1);
    varCheck.visit(null, null, root2);

    assertFalse(varCheck.strictExternCheck);
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
    assertTrue(varsToDeclareInExternsIsEmpty(varCheck));

    varCheck.synthesizedExternsInput = new CompilerInput("{SyntheticVarsDecl}");
    varCheck.synthesizedExternsRoot = Node.newObject("SyntheticVarsDecl");

    varCheck.process(input1, root1);
    varCheck.process(input2, root2);
    varCheck.visit(null, null, root1);
    varCheck.visit(null, null, root2);

    assertSame(varCheck.synthesizedExternsInput, getSynthesizedExternsInput(varCheck));
    assertSame(varCheck.synthesizedExternsRoot, getSynthesizedExternsRoot(varCheck));
    assertTrue(varsToDeclareInExternsIsEmpty(varCheck));

    varCheck.strictExternCheck = true;
    varCheck.process(input1, root1);
    varCheck.process(input2, root2);
    varCheck.visit(null, null, root1);
    varCheck.visit(null, null, root2);

    assertSame(varCheck.synthesizedExternsInput, getSynthesizedExternsInput(varCheck));
    assertSame(varCheck.synthesizedExternsRoot, getSynthesizedExternsRoot(varCheck));
    assertFalse(varsToDeclareInExternsIsEmpty(varCheck));
  }

  private static boolean varsToDeclareInExternsIsEmpty(VarCheck varCheck) {
    return varCheck.varsToDeclareInExterns.isEmpty();
  }

  private static CompilerInput getSynthesizedExternsInput(VarCheck varCheck) {
    return varCheck.synthesizedExternsInput;
  }

  private static Node getSynthesizedExternsRoot(VarCheck varCheck) {
    return varCheck.synthesizedExternsRoot;
  }
}