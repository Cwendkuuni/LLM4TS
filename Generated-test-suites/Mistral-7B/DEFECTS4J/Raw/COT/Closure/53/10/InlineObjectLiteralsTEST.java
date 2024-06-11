package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

/**
 * Test the InlineObjectLiterals class
 */
public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test_id_";
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testBasicInlineObjectLiteral() {
    Node externs = Node.newObjectLiteral();
    Node root = Node.newObjectLiteral();

    inlineObjectLiterals.process(externs, root);

    // TODO: Add assertions to check the modified AST here
  }

  @Test
  public void testInlineObjectLiteralWithInitializer() {
    Node externs = Node.newObjectLiteral();
    Node root = NodeFactory.createObjectLiteral(
        "x", NodeFactory.createObjectLiteral(
            "a", NodeFactory.createNumberLiteral(1),
            "b", NodeFactory.createNumberLiteral(2)
        ));

    inlineObjectLiterals.process(externs, root);

    // TODO: Add assertions to check the modified AST here
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    Node externs = Node.newObjectLiteral();
    Node root = NodeFactory.createObjectLiteral(
        "x", NodeFactory.createObjectLiteral(
            "a", NodeFactory.createObjectLiteral(
                "b", NodeFactory.createName("x"),
                "c", NodeFactory.createNumberLiteral(2)
            ),
            "c", NodeFactory.createName("x")
        ));

    inlineObjectLiterals.process(externs, root);

    // TODO: Add assertions to check the modified AST here
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithNonObjectLiteralValue() {
    Node externs = Node.newObjectLiteral();
    Node root = NodeFactory.createObjectLiteral(
        "x", NodeFactory.createNumberLiteral(1),
        "y", NodeFactory.createFunctionExpression(null, null, null)
    );

    inlineObjectLiterals.process(externs, root);
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    Node externs = NodeFactory.createObjectLiteral(
        "exports", NodeFactory.createObjectLiteral(
            "x", NodeFactory.createObjectLiteral(
                "a", NodeFactory.createNumberLiteral(1),
                "b", NodeFactory.createNumberLiteral(2)
            )
        ));

    Node root = NodeFactory.createObjectLiteral(
        "x", NodeFactory.createObjectLiteral(
            "a", NodeFactory.createNumberLiteral(1),
            "b", NodeFactory.createNumberLiteral(2)
        )
    );

    inlineObjectLiterals.process(externs, root);

    // TODO: Add assertions to check the modified AST here
  }
}