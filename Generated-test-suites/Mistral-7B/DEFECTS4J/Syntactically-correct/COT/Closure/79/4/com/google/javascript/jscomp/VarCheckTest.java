package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompilationResult;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private TestCompilationResult result;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testProcess_ValidExterns_NoDiagnostics() throws Exception {
    String externs = "var a = 1;";
    String source = "var b = a;";

    CompilerInput input = compiler.newInput(nodeFactory.createSourceNode("test.js", source));
    CompilerInput externsInput = compiler.newInput(nodeFactory.createSourceNode("externs.js", externs));
    Node externsRoot = nodeFactory.createRootNode(externs);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(externsRoot, input.getAstRoot(compiler));

    result = compiler.compile(input);
    assertNull(result.getDiagnostics());
  }

  @Test
  public void testProcess_UndefinedVar_ErrorDiagnostic() throws Exception {
    String source = "var x = y;";

    CompilerInput input = compiler.newInput(nodeFactory.createSourceNode("test.js", source));

    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    varCheck.process(null, root);

    result = compiler.compile(input);
    assertFalse(result.isSuccess());
    assertTrue(result.getDiagnostics().size() > 0);
  }

  // Add more test cases here for other methods in VarCheck class
}