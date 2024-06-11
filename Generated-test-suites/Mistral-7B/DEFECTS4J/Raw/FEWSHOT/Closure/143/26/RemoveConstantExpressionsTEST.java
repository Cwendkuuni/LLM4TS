package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class RemoveConstantExpressionsTest {
  @Test
  public void testRemoveConstantExpressions() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    // Test with empty nodes
    Node rootEmpty = new Node(Token.PROGRAM, null);
    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootEmpty, callback);
    assertNull(callback.getResult().changed);

    // Test with simple expressions
    Node rootAdd = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.IDENT, "foo")
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootAdd, callback);
    assertNull(callback.getResult().changed);

    // Test with constant expressions
    Node rootConstant = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.NUMBER, 2)
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootConstant, callback);
    assertTrue(callback.getResult().changed);
    Node simplified = callback.getResult().code;
    Node expected = new Node(
        Token.PROGRAM,
        new Node(Token.EXPR_STATEMENT, new Node(Token.NUMBER, 3))
    );
    assertThat(simplified).isEqualTo(expected);

    // Test with side effect expressions
    Node rootSideEffect = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(Token.IDENT, "bar")
            )
        ),
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.IDENT, "foo")
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootSideEffect, callback);
    assertTrue(callback.getResult().changed);
    simplified = callback.getResult().code;
    expected = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.IDENT, "bar"))
        ),
        new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.NUMBER, 1)))
    );
    assertThat(simplified).isEqualTo(expected);

    // Test with complex expressions
    Node rootComplex = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(
                    Token.PLUS,
                    new Node(Token.IDENT, "bar"),
                    new Node(Token.NUMBER, 1)
                )
            )
        ),
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(
                    Token.IDENT,
                    new Node(Token.IDENT, "baz"),
                    new Node(Token.NUMBER, 2)
                ),
                new Node(Token.IDENT, "qux")
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootComplex, callback);
    assertTrue(callback.getResult().changed);
    simplified = callback.getResult().code;
    expected = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(
                    Token.EXPR_STATEMENT,
                    new Node(Token.IDENT, "bar"),
                    new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.NUMBER, 1))
                )
            )
        ),
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.EXPR_STATEMENT,
                new Node(Token.IDENT, "baz"),
                new Node(Token.ASSIGN, new Node(Token.IDENT, "qux"), new Node(Token.NUMBER, 2))
            )
        )
    );
    assertThat(simplified).isEqualTo(expected);

    // Test with expressions with side effects and constants together
    Node rootSideEffectWithConstants = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(
                    Token.PLUS,
                    new Node(Token.IDENT, "bar"),
                    new Node(Token.NUMBER, 1)
                )
            )
        ),
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(Token.NUMBER, 2),
                new Node(Token.NUMBER, 3)
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootSideEffectWithConstants, callback);
    assertTrue(callback.getResult().changed);
    simplified = callback.getResult().code;
    expected = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(
                    Token.EXPR_STATEMENT,
                    new Node(Token.IDENT, "bar"),
                    new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.NUMBER, 1))
                )
            )
        ),
        new Node(Token.EXPR_STATEMENT, new Node(Token.NUMBER, 5))
    );
    assertThat(simplified).isEqualTo(expected);

    // Test with expressions containing unary operators
    Node rootUnary = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.NEGATION,
                new Node(
                    Token.PLUS,
                    new Node(Token.NUMBER, 1),
                    new Node(Token.IDENT, "foo")
                )
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootUnary, callback);
    assertTrue(callback.getResult().changed);
    simplified = callback.getResult().code;
    expected = new Node(
        Token.PROGRAM,
        new Node(Token.EXPR_STATEMENT, new Node(Token.NEGATION, new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.NUMBER, 1))))
    );
    assertThat(simplified).isEqualTo(expected);

    // Test with expressions containing binary operators
    Node rootBinary = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.PLUS,
                new Node(Token.IDENT, "foo"),
                new Node(
                    Token.IDENT,
                    new Node(Token.IDENT, "bar"),
                    new Node(Token.PLUS, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2))
                )
            )
        )
    );
    callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, rootBinary, callback);
    assertTrue(callback.getResult().changed);
    simplified = callback.getResult().code;
    expected = new Node(
        Token.PROGRAM,
        new Node(
            Token.EXPR_STATEMENT,
            new Node(
                Token.ASSIGN,
                new Node(Token.IDENT, "foo"),
                new Node(Token.IDENT, "bar")
            ),
            new Node(
                Token.EXPR_STATEMENT,
                new Node(Token.ASSIGN, new Node(Token.IDENT, "foo"), new Node(Token.NUMBER, 1)),
                new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.IDENT, "bar"), new Node(Token.NUMBER, 1)))
            )
        )
    );
    assertThat(simplified).isEqualTo(expected);
  }

  private boolean assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError("Expected true, but was false");
    }
    return true;
  }
}