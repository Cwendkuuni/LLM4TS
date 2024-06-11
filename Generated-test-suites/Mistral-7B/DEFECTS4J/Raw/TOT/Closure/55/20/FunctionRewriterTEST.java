package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.NodeOps.newNode;
import static com.google.javascript.rhino.NodeOps.newString;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseCode(String code, int lineno, int charno) {
        return newNode(Token.PROGRAM, lineno, charno, null);
      }
    };
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    String code = "function EmptyFunction() {}";
    Node root = compiler.parseCode(code, 1, 1);
    functionRewriter.process(null, root);
    Node expectedRoot = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.CALL, 1, 1, null,
            newString("JSCompiler_emptyFn"), null));
    assertThat(root).isEqualTo(expectedRoot);
  }

  @Test
  public void testIdentityFunctionReducer() {
    String code = "function IdentityFunction(a) {return a}";
    Node root = compiler.parseCode(code, 1, 1);
    functionRewriter.process(null, root);
    Node expectedRoot = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.CALL, 1, 1, null,
            newString("JSCompiler_identityFn"), null,
            newNode(Token.STRING, 1, 1, "a")));
    assertThat(root).isEqualTo(expectedRoot);
  }

  @Test
  public void testGetterReducer() {
    String code = "function getter(propName) {return function() {return this[propName]}}";
    Node root = compiler.parseCode(code, 1, 1);
    functionRewriter.process(null, root);
    Node expectedRoot = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.CALL, 1, 1, null,
            newString("JSCompiler_get"), null,
            newNode(Token.STRING, 1, 1, "propName")));
    Node expectedHelper = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.FUNCTION, 1, 1, null, null, ImmutableList.of(),
            newNode(Token.VAR, 1, 1, Token.NAME, "propName"), null,
            newNode(Token.RETURN, 1, 1,
                newNode(Token.GETPROP, 1, 1, newNode(Token.THIS, 1, 1),
                    newNode(Token.NAME, 1, 1, "propName")))));

    assertThat(root).isEqualTo(expectedRoot);
    Node helper = compiler.getHelperFunctions().get(0);
    assertThat(helper).isEqualTo(expectedHelper);
  }

  @Test
  public void testSetterReducer() {
    String code = "function setter(propName, value) {" +
        "return function() {" +
        "this[propName] = value" +
        "}}";
    Node root = compiler.parseCode(code, 1, 1);
    functionRewriter.process(null, root);
    Node expectedRoot = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.CALL, 1, 1, null,
            newString("JSCompiler_set"), null,
            newNode(Token.STRING, 1, 1, "propName")));
    Node expectedHelper = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.FUNCTION, 1, 1, null, null, ImmutableList.of(),
            newNode(Token.VAR, 1, 1, Token.NAME, "propName"), null,
            newNode(Token.VAR, 1, 1, Token.NAME, "value"), null,
            newNode(Token.RETURN, 1, 1,
                newNode(Token.SETPROP, 1, 1,
                    newNode(Token.THIS, 1, 1),
                    newNode(Token.NAME, 1, 1, "propName"),
                    newNode(Token.VALUE, 1, 1, "value")))));

    assertThat(root).isEqualTo(expectedRoot);
    Node helper = compiler.getHelperFunctions().get(0);
    assertThat(helper).isEqualTo(expectedHelper);
  }

  @Test
  public void testNoReduction() {
    String code = "function NoReductionFunction() {" +
        "function InnerFunction() {" +
        "return 1;" +
        "}";
    Node root = compiler.parseCode(code, 1, 1);
    functionRewriter.process(null, root);
    Node expectedRoot = newNode(Token.PROGRAM, 1, 1,
        newNode(Token.FUNCTION, 1, 1, null, null, ImmutableList.of(), null),
        newNode(Token.FUNCTION, 1, 1, null, null, ImmutableList.of(),
            newNode(Token.BLOCK, 1, 1, null, null, ImmutableList.of(),
                newNode(Token.RETURN, 1, 1, newNode(Token.INT_LITERAL, 1, 1, 1)))));

    assertThat(root).isEqualTo(expectedRoot);
  }
}