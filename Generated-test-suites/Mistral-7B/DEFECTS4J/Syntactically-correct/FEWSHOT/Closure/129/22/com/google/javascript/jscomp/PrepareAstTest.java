package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    // Test empty AST
    Node root = IR.nullKeyword();
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Test simple AST
    Node root2 = IR.block();
    root2.addChild(IR.print("1"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root2);

    // Test AST with control structures
    Node root3 = IR.ifStatement(IR.booleanLiteral(true), IR.print("true"), IR.print("false"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root3);

    // Test AST with object literal
    Node root4 = IR.objectLiterals();
    root4.addChild(IR.name("x"));
    root4.getLastChild().addChild(IR.name("a"));
    root4.getLastChild().getLastChild().setString("1");
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root4);

    // Test AST with object literal and annotations
    Node root5 = IR.objectLiterals();
    root5.addChild(IR.name("x"));
    root5.getLastChild().addChild(IR.name("a"));
    root5.getLastChild().getLastChild().setString("1");
    root5.getLastChild().addChild(IR.name("b"));
    root5.getLastChild().getLastChild().setJSDocInfo(
        new JsDocInfo("@param {number} a"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root5);

    // Test AST with call expressions
    Node root6 = IR.call(IR.name("eval"), IR.stringLiteral("1"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root6);

    Node root7 = IR.call(IR.name("eval"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root7);

    Node root8 = IR.call(IR.name("eval"), IR.booleanLiteral(true));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root8);

    // Test AST with function expressions
    Node root9 = IR.expressionStatement(IR.assignment(
        IR.name("f"),
        IR.function(
            null,
            null,
            null,
            IR.block(),
            IR.returnStatement(IR.stringLiteral("Hello World")))));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root9);

    Node root10 = IR.call(IR.name("f"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root10);

    // Test AST with dispatchers
    Node root11 = IR.expressionStatement(IR.assignment(
        IR.name("dispatcher"),
        IR.function(
            null,
            null,
            null,
            IR.block(),
            IR.returnStatement(IR.thisValue()))));
    Node root12 = IR.expressionStatement(IR.assignment(
        IR.name("obj"),
        IR.objectLiterals()));
    root12.addChild(IR.name("dispatch"));
    root12.getLastChild().addChild(IR.name("dispatcher"));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root12);

    Node root13 = IR.expressionStatement(IR.call(IR.name("obj.dispatch"), IR.stringLiteral("event")));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root13);

    // Test AST with free calls
    Node root14 = IR.expressionStatement(IR.call(IR.name("console.log"), IR.stringLiteral("Hello World")));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root14);
    assertFalse(root14.getBooleanProp(Node.FREE_CALL));

    Node root15 = IR.expressionStatement(IR.call(IR.name("console.log"), null));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root15);
    assertTrue(root15.getBooleanProp(Node.FREE_CALL));

    // Test AST with direct eval
    Node root16 = IR.expressionStatement(IR.call(IR.name("eval"), IR.stringLiteral("1")));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root16);
    assertFalse(root16.getBooleanProp(Node.DIRECT_EVAL));

    Node root17 = IR.expressionStatement(IR.call(IR.name("eval"), null));
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root17);
    assertTrue(root17.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAstCheckOnly() {
    // Test empty AST
    Node root = IR.nullKeyword();
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
    checkState(false, "normalizeNodeType constraints violated");

    // Test simple AST
    Node root2 = IR.block();
    root2.addChild(IR.print("1"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root2);

    // Test AST with control structures
    Node root3 = IR.ifStatement(IR.booleanLiteral(true), IR.print("true"), IR.print("false"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root3);

    // Test AST with object literal
    Node root4 = IR.objectLiterals();
    root4.addChild(IR.name("x"));
    root4.getLastChild().addChild(IR.name("a"));
    root4.getLastChild().getLastChild().setString("1");
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root4);

    // Test AST with object literal and annotations
    Node root5 = IR.objectLiterals();
    root5.addChild(IR.name("x"));
    root5.getLastChild().addChild(IR.name("a"));
    root5.getLastChild().getLastChild().setString("1");
    root5.getLastChild().addChild(IR.name("b"));
    root5.getLastChild().getLastChild().setJSDocInfo(
        new JsDocInfo("@param {number} a"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root5);
    checkState(false, "normalizeNodeType constraints violated");

    // Test AST with call expressions
    Node root6 = IR.call(IR.name("eval"), IR.stringLiteral("1"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root6);
    checkState(false, "normalizeNodeType constraints violated");

    Node root7 = IR.call(IR.name("eval"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root7);
    checkState(false, "normalizeNodeType constraints violated");

    Node root8 = IR.call(IR.name("eval"), IR.booleanLiteral(true));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root8);
    checkState(false, "normalizeNodeType constraints violated");

    // Test AST with function expressions
    Node root9 = IR.expressionStatement(IR.assignment(
        IR.name("f"),
        IR.function(
            null,
            null,
            null,
            IR.block(),
            IR.returnStatement(IR.stringLiteral("Hello World")))));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root9);
    checkState(false, "normalizeNodeType constraints violated");

    Node root10 = IR.call(IR.name("f"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root10);
    checkState(false, "normalizeNodeType constraints violated");

    // Test AST with dispatchers
    Node root11 = IR.expressionStatement(IR.assignment(
        IR.name("dispatcher"),
        IR.function(
            null,
            null,
            null,
            IR.block(),
            IR.returnStatement(IR.thisValue()))));
    Node root12 = IR.expressionStatement(IR.assignment(
        IR.name("obj"),
        IR.objectLiterals()));
    root12.addChild(IR.name("dispatch"));
    root12.getLastChild().addChild(IR.name("dispatcher"));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root12);
    checkState(false, "normalizeNodeType constraints violated");

    Node root13 = IR.expressionStatement(IR.call(IR.name("obj.dispatch"), IR.stringLiteral("event")));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root13);
    checkState(false, "normalizeNodeType constraints violated");

    // Test AST with free calls
    Node root14 = IR.expressionStatement(IR.call(IR.name("console.log"), IR.stringLiteral("Hello World")));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root14);
    checkState(false, "normalizeNodeType constraints violated");

    Node root15 = IR.expressionStatement(IR.call(IR.name("console.log"), null));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root15);
    checkState(false, "normalizeNodeType constraints violated");

    // Test AST with direct eval
    Node root16 = IR.expressionStatement(IR.call(IR.name("eval"), IR.stringLiteral("1")));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root16);
    checkState(false, "normalizeNodeType constraints violated");

    Node root17 = IR.expressionStatement(IR.call(IR.name("eval"), null));
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root17);
    checkState(false, "normalizeNodeType constraints violated");
  }

  private static class JsDocInfo {
    private final String description;
    private final String[] tags;

    JsDocInfo(String description) {
      this.description = description;
      this.tags = new String[0];
    }

    JsDocInfo(String description, String... tags) {
      this.description = description;
      this.tags = tags;
    }

    String getDescription() {
      return description;
    }

    String[] getTags() {
      return tags;
    }
  }
}