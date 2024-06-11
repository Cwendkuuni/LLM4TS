package com.google.javascript.jscomp;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import jdk.jsinterop.annotations.JsFunction;
import jdk.jsinterop.annotations.JsInterface;
import jdk.jsinterop.annotations.JsMethod;

@RunWith(JUnit4.class)
public class VarCheckTest {

  @JsInterface
  static class TestClass {
    @JsFunction
    public void foo() {}

    @JsMethod
    public int bar() { return 0; }

    public void baz() {}

    public int qux;
  }

  @Test
  public void testVarCheck() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        ))
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());

    varCheck.process(input.getExterns(), root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());
  }

  @Test
  public void testVarCheckWithExterns() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        ))
    );

    Node externs = Node.newBlock(
        0, null,
        new Node(Token.VAR, "qux"),
        new Node(Token.ASSIGN, null, Node.newNumber(42))
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    CompilerInput externsInput = new CompilerInput("externs.js", externs);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());
    compiler.compile(externsInput, new CompilerOptions());

    varCheck.process(externs, root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());
  }

  @Test
  public void testVarCheckWithModuleDependency() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        )),
        new Node(Token.ASSIGN, null, Node.newString("dep.qux = 42;"))
    );

    Node depRoot = Node.newBlock(
        0, "dep",
        new Node(Token.VAR, "qux")
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    CompilerInput depInput = new CompilerInput("dep.js", depRoot);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());
    compiler.compile(depInput, new CompilerOptions());

    varCheck.process(depInput.getExterns(), depRoot);
    varCheck.process(input.getExterns(), root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());
  }

  @Test
  public void testVarCheckWithDuplicateDeclaration() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        )),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        ))
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());

    varCheck.process(input.getExterns(), root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());
  }

  @Test
  public void testVarCheckWithUndefinedVariable() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        )),
        new Node(Token.ASSIGN, null, Node.newString("undefinedVar = 42;"))
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());

    varCheck.process(input.getExterns(), root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());

    Node undefinedVarNode = findNode(root, "undefinedVar");
    assertThat(undefinedVarNode, is(nullValue()));
  }

  @Test
  public void testVarCheckWithMissingModuleDependency() {
    TestClass testClass = new TestClass();

    Node root = Node.newBlock(
        0, "test",
        new Node(Token.VAR, "testClass"),
        new Node(Token.ASSIGN, null, Node.newObjectLiteral(
            new Node(Token.PROPS, null,
                new Node(Token.PROP, Node.newString("TestClass"), null, testClass)
            )
        )),
        new Node(Token.ASSIGN, null, Node.newString("dep.qux = 42;"))
    );

    Node depRoot = Node.newBlock(
        0, "dep",
        Node.newComment("// This file is not loaded.")
    );

    VarCheck varCheck = new VarCheck(new Compiler() {
      @Override
      public Node getAstRoot(CompilerInput input) {
        return input.getAstRoot(this);
      }
    });

    CompilerInput input = new CompilerInput("test.js", root);
    CompilerInput depInput = new CompilerInput("dep.js", depRoot);
    Compiler compiler = new Compiler();
    compiler.compile(input, new CompilerOptions());

    varCheck.process(depInput, depRoot);
    varCheck.process(input.getExterns(), root);

    Node testClassNode = findNode(root, "TestClass");
    checkVarDeclarations(testClassNode, testClass.getClass().getDeclaredFields());

    Node quxNode = findNode(root, "qux");
    assertThat(quxNode, is(nullValue()));
  }

  private void checkVarDeclarations(Node node, Field[] fields) {
    for (Field field : fields) {
      Node varNode = findNode(node, field.getName());
      assertThat(varNode, is(not(nullValue())));
      assertThat(varNode.getFirstChild().getType(), is(Token.VAR));
      assertThat(varNode.getFirstChild().getFirstChild().getString(), is(field.getName()));
    }
  }

  private Node findNode(Node node, String name) {
    if (node.getType() == Token.NAME && node.getString().equals(name)) {
      return node;
    }

    for (Node child : node.getChildren()) {
      Node result = findNode(child, name);
      if (result != null) {
        return result;
      }
    }

    return null;
  }
}

class Field {
  final String name;

  Field(String name) {
    this.name = name;
  }
}

class Compiler {
  // Empty implementation for JUnit test.
}