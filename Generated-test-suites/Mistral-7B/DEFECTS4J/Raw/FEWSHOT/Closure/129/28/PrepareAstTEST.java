package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node node = IR.block().addStatement(IR.exprStatement(IR.call(IR.name("foo"), IR.lit(1))));
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node result = prepareAst.process(null, node).getFirstChild();
    assertTrue(result.getType() == Token.BLOCK);
  }

  @Test
  public void testNormalizeNodeTypesWithCheckOnly() {
    Node node = IR.block().addStatement(IR.exprStatement(IR.call(IR.name("foo"), IR.lit(1))));
    PrepareAst prepareAst = new PrepareAst(null, true);
    Node result = prepareAst.process(null, node).getFirstChild();
    assertNull(result);
  }

  @Test
  public void testNormalizeBlocks() {
    Node ifNode = IR.ifStatement(IR.exprStatement(IR.literal(true)), IR.block(), IR.block());
    Node whileNode = IR.whileStatement(IR.literal(true), IR.block());

    PrepareAst prepareAst = new PrepareAst(null, false);

    Node ifResult = prepareAst.process(null, ifNode).getFirstChild();
    assertFalse(ifResult.isBlock());
    Node ifCondition = ifResult.getFirstChild();
    assertTrue(ifCondition.isGet());

    Node whileResult = prepareAst.process(null, whileNode).getFirstChild();
    assertFalse(whileResult.isBlock());
    Node whileCondition = whileResult.getFirstChild();
    assertTrue(whileCondition.isGet());

    Node ifElseResult = prepareAst.process(null, IR.ifStatement(IR.exprStatement(IR.literal(true)), IR.block(), IR.block()));
    Node ifElseCondition = ifElseResult.getFirstChild();
    assertTrue(ifElseResult.isBlock());
    assertTrue(ifElseResult.getFirstChild().isBlock());
    assertTrue(ifElseResult.getLastChild().isBlock());

    Node switchResult = prepareAst.process(null, IR.switchStatement(IR.literal(1), IR.block(), IR.block()));
    assertTrue(switchResult.isBlock());

    Node labelResult = prepareAst.process(null, IR.label("foo"));
    assertNull(labelResult);
  }

  @Test
  public void testPrepareAnnotations() {
    Node objlit = IR.objectLiteral()
        .addProperty(IR.name("a"))
        .addProperty(
            IR.objectLiteral()
                .addProperty(IR.name("x"))
                .addProperty(IR.name("y"))
        );

    PrepareAst prepareAst = new PrepareAst(null, false);
    Node result = prepareAst.process(null, objlit).getFirstChild();
    assertTrue(result.isObjectLit());

    Node a = result.getFirstChild();
    Node aKey = a.getFirstChild();
    Node aValue = a.getLastChild();
    assertTrue(aKey.isName());
    assertTrue(aValue.isStringLiteral());

    Node x = result.getLastChild().getFirstChild();
    Node xValue = x.getLastChild();
    assertTrue(xKey.isName());
    assertTrue(xValue.isFunction());

    Node y = result.getLastChild().getLastChild();
    Node yValue = y.getLastChild();
    assertTrue(yKey.isName());
    assertTrue(yValue.isStringLiteral());

    Node function = IR.function("", IR.block());
    prepareAst = new PrepareAst(null, false);
    Node functionResult = prepareAst.process(null, function).getFirstChild();
    assertTrue(functionResult.isFunction());
  }
}