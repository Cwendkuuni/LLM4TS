package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.testing.compiler.CompilationTestFactory;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

/**
 * Tests for the {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private final NodeFactory nodeFactory = new NodeFactory();
  private final AbstractCompiler compiler;

  public CheckSideEffectsTest() {
    CompilerOptions options = new CompilerOptions(true, null);
    compiler = new TestCompiler(options);
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    String input =
        "function foo(x) {\n" +
        "  // Suspicious code. {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x == foo();  // should that be '='?\n" +
        "  foo();;\n" +
        "  // probably just a stray-semicolon. Doesn't hurt to check though\n" +
        "}\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setCode(input, null);
    Node root = compiler.parse(inputCompiled, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertFalse("No problem nodes expected", problemNodes.isEmpty());
    assertEquals("Expected 3 problem nodes", 3, problemNodes.size());
    Node fooCall1 = problemNodes.get(0);
    Node fooCall2 = problemNodes.get(1);
    Node semicolon = problemNodes.get(2);

    assertNotNull("Expected a JSDocInfo for foo", fooCall1.getJSDocInfo());
    assertNull("Expected no JSDocInfo for equals comparison", fooCall1.getChild(0).getJSDocInfo());

    assertNotNull("Expected a JSDocInfo for foo call 2", fooCall2.getJSDocInfo());
    assertNull("Expected no JSDocInfo for foo call 2", fooCall2.getChild(0).getJSDocInfo());

    assertNotNull("Expected a JSDocInfo for semicolon", semicolon.getParent().getJSDocInfo());

    // Check that the problem nodes have been flagged as warnings
    int warningCount = compiler.getDiagnostics().length;
    assertEquals("Expected 3 warnings", 3, warningCount);

    // Check the warning messages
    DiagnosticType useLessCodeError = CheckSideEffects.USELESS_CODE_ERROR;
    String warningMessage1 = useLessCodeError.getMessage(null,
        compiler.getDiagnostics()[0].getMessageData()[0]);
    String warningMessage2 = useLessCodeError.getMessage(null,
        compiler.getDiagnostics()[1].getMessageData()[0]);
    String warningMessage3 = useLessCodeError.getMessage(null,
        compiler.getDiagnostics()[2].getMessageData()[0]);

    assertTrue("Expected warning message for foo() call 1 to start with 'Suspicious code'",
        warningMessage1.startsWith("Suspicious code."));

    assertTrue("Expected warning message for foo() call 2 to start with 'Suspicious code'",
        warningMessage2.startsWith("Suspicious code."));

    assertTrue("Expected warning message for stray semicolon to start with 'Is there a missing '+ on the previous line'",
        warningMessage3.startsWith("Is there a missing '+' on the previous line?"));
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    String input =
        "function foo() {\n" +
        "  // This code lacks side-effects. Is there a bug?\n" +
        "}\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setCode(input, null);
    Node root = compiler.parse(inputCompiled, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(null, root);

    // Check that the problem nodes have not been flagged as warnings
    int warningCount = compiler.getDiagnostics().length;
    assertEquals("Expected no warnings", 0, warningCount);

    // Check that the protected nodes have been replaced with calls to a new extern function
    Node protectorFn = checkSideEffects.getExternalFunction();
    assertNotNull("Expected a new extern function", protectorFn);

    // Check that the problem node has been replaced with a call to the extern function
    Node foo = root.getFirstChild();
    assertNotNull("Expected a problem node", foo.getFirstChild());
    Node callToProtectorFn = foo.getFirstChild();
    assertNotNull("Expected a call to the extern function", callToProtectorFn);
    Node argument = callToProtectorFn.getLastChild();
    assertNotNull("Expected an argument to the call to the extern function", argument);
    assertEquals("Expected the problem node as an argument", foo, argument.getParent());
  }

  @org.junit.Test
  public void testStripProtection() {
    String input =
        "function foo() {\n" +
        "  // @jscomp.protect\n" +
        "  // This code lacks side-effects. Is there a bug?\n" +
        "}\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setCode(input, null);
    Node root = compiler.parse(inputCompiled, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(null, root);

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);

    // Check that the protected nodes have been removed
    Node protectorFn = checkSideEffects.getExternalFunction();
    assertNull("Expected the external function to have been removed", protectorFn);

    Node foo = root.getFirstChild();
    assertNotNull("Expected the problem node to still be present", foo);
  }

  private Node getExternalFunction() {
    for (Node n : compiler.getSynthesizedExternsInput().getAstRoot(compiler).getChildren()) {
      if (n.isName() && n.getString().equals(CheckSideEffects.PROTECTOR_FN)) {
        return n;
      }
    }
    return null;
  }
}