package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Token;

import java.util.List;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = new Compiler();
    compiler.addPasses(new CheckSideEffects(compiler, CheckLevel.WARNING, false));
    String code = "var x = foo();";
    Node root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    List<String> messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "var x = 1; var x = foo();";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("Suspicious code. This code lacks side-effects. Is there a bug?"));

    code = "function foo() { return 1; } var x = foo();";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } var x = foo(); var y = 2;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("Suspicious code. This code lacks side-effects. Is there a bug?"));

    code = "function foo() { return 1; } var x = foo(); var y = 2; x = y;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo();";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo();;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo();; x = 2;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("Suspicious code. This code lacks side-effects. Is there a bug?"));

    code = "function foo() { return 1; } x = foo(); x = 2; x = 3;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x = 2; x += 1;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x = 2; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'postfix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 2; x--;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'postfix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 2; x--";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 2; x = x + 1";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x = x + 1; x = 2";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("Suspicious code. Is there a missing '+' on the previous line?"));

    code = "function foo() { return 1; } x = foo(); x++; x = 2";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); ++x; x = 2";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); ++x; x = 2;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); ++x; x = 2; x = 3";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); ++x; x = 3;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("Suspicious code. This code lacks side-effects. Is there a bug?"));

    code = "function foo() { return 1; } x = foo(); x++; x = 3;";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x++; x = 3; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x++; x = 3; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));

    code = "function foo() { return 1; } x = foo(); x = 3; x = 4; x++";
    root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    messages = compiler.getDiagnostics();
    assertTrue(messages.size() == 1);
    assertTrue(messages.get(0).contains("The result of the 'prefix-' operator is not being used."));
  }

  @Test
  public void testProtectCode() {
    Compiler compiler = new Compiler();
    compiler.addPasses(new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    String code = "function main() { x = 1; }";
    Node root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    List<String> messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    code = "function main() { x = 1; }";
    root = Nodeops.parseCompilationUnit(code);
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checker);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    Node functionNode = NodeUtil.findFunction("main", root);
    Node bodyNode = functionNode.getBody();
    Node xAssignment = bodyNode.getFirstChild();
    Node xValue = xAssignment.getNextSibling();

    checker.protectSideEffects();
    assertTrue(functionNode.getChildCount() > bodyNode.getChildCount());
    Node newXAssignment = functionNode.getLastChild();
    assertTrue(newXAssignment.getType() == Token.CALL);
    Node protectorFn = newXAssignment.getFirstChild();
    assertTrue(protectorFn.isName());
    assertTrue(protectorFn.getString().equals("JSCOMPILER_PRESERVE"));
    Node xValueProtected = protectorFn.getLastChild();
    assertTrue(xValueProtected.getType() == Token.EXPR_RESULT);
    assertTrue(xValueProtected.getChildCount() == 1);
    assertTrue(xValueProtected.getChild(0).isSameValueAs(xValue));
  }

  @Test
  public void testRemoveProtection() {
    Compiler compiler = new Compiler();
    compiler.addPasses(new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    compiler.addPasses(new CheckSideEffects.StripProtection(compiler));
    String code = "function main() { JSCOMPILER_PRESERVE(x = 1); }";
    Node root = Nodeops.parseCompilationUnit(code);
    compiler.process(root, null);
    List<String> messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    Node functionNode = NodeUtil.findFunction("main", root);
    Node bodyNode = functionNode.getBody();
    Node xAssignment = bodyNode.getFirstChild();

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checker);
    messages = compiler.getDiagnostics();
    assertTrue(messages.isEmpty());

    assertTrue(xAssignment.getParent().getChildCount() == 1);
    assertTrue(xAssignment.getParent().getChild(0).isSameValueAs(xAssignment));
  }
}