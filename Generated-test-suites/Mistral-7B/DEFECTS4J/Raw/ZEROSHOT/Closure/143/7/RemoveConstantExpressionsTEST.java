package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    CompilerPass pass = new RemoveConstantExpressions(compiler);

    Node root = new Node(Token.PROGRAM, null);
    Node expression = new Node(Token.EXPR_STATEMENT, null);
    Node addition = new Node(Token.ADD, null);
    Node leftAddend = new Node(Token.NUMBER, 1.0);
    Node rightAddend = new Node(Token.CALL, null, null, new Node(Token.IDENT, "foo"));

    addition.addChildToStatementList(leftAddend);
    addition.addChildToStatementList(rightAddend);
    expression.addChild(addition);
    root.addChild(expression);

    Result result = new Result();
    NodeTraversal nodeTraversal = new NodeTraversal(null, root, pass);

    nodeTraversal.traverse();

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> parentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> replacementsCaptor = ArgumentCaptor.forClass(List.class);

    when(compiler.getASTChangeProxy()).thenReturn(new AstChangeProxy() {
      @Override
      public void replaceWith(Node parent, Node node, List<Node> replacements) {
        parent.getParent().removeChild(parent);
        parent.getParent().addChild(replacements.get(0));
        for (Node replacement : replacements.subList(1, replacements.size())) {
          parent.addChild(replacement);
        }
      }
    });

    pass.process(null, root);

    Node fooCall = root.getFirstChild();
    Node foo = fooCall.getFirstChild();
    Node barCall = fooCall.getLastChild();
    Node bar = barCall.getFirstChild();

    List<Node> children = Lists.newArrayList(foo, bar);
    Node simplifiedExpression = expression.getFirstChild();
    assertThat(simplifiedExpression.getNumChildren()).isEqualTo(children.size());
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      Node childInTree = simplifiedExpression.getChild(i);
      assertThat(child).isEqualTo(childInTree);
    }

    when(compiler.getParallelCompilerPassesManager()).thenReturn(mock(ParallelCompilerPassesManager.class));
    TestCompiler testCompiler = new TestCompiler();
    testCompiler.addPass(pass);
    testCompiler.compile(null, root, new CompilerOptions(), true, null);

    Node tree = testCompiler.getRootNode();
    Node simplifiedTree = tree.getFirstChild();
    assertThat(simplifiedTree.getNumChildren()).isEqualTo(children.size());
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      Node childInTree = simplifiedTree.getChild(i);
      assertThat(child).isEqualTo(childInTree);
    }
  }
}

class AstChangeProxy {
  public AstChangeProxy registerListener(ChangeListener listener) {
    throw new UnsupportedOperationException();
  }
}

interface ChangeListener {
  void nodeRemoved(Node node);
}