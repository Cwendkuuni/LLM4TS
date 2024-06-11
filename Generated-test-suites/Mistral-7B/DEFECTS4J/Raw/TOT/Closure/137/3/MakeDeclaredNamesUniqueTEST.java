package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.traverse.NodeTraversalType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    Compiler compiler = mock(Compiler.class);
    Scope scope = mock(Scope.class);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, null);
    Node functionBody = Node.newBlock(Token.BLOCK, null);
    Node varDeclaration1 = Node.newVar(Token.VAR, "a", null, null, null);
    Node varDeclaration2 = Node.newVar(Token.VAR, "a", null, null, null);
    Node varDeclaration3 = Node.newVar(Token.VAR, "b", null, null, null);

    when(compiler.getFactory()).thenReturn(mockFactory());
    when(compiler.getGlobalScope()).thenReturn(scope);

    functionBody.addChild(varDeclaration1);
    functionBody.addChild(varDeclaration2);

    functionNode.setLastChild(functionBody);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, NodeTraversalType.PRE);
    nodeTraversal.traverse(functionNode);

    Iterator<Map.Entry<String, String>> iterator = scope.getVars().entrySet().iterator();
    List<String> declaredNames = Lists.newArrayList();
    while (iterator.hasNext()) {
      Map.Entry<String, Var> entry = iterator.next();
      declaredNames.add(entry.getKey());
    }

    assertThat(declaredNames).containsExactly("a1", "a2", "b");
  }

  NodeFactory mockFactory() {
    return mock(NodeFactory.class);
  }
}