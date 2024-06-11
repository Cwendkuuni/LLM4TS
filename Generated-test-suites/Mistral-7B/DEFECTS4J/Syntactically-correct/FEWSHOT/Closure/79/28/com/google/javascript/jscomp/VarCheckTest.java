package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import java.util.Set;

public class VarCheckTest {

  private static Node createNode(AbstractCompiler compiler, String nodeType, String value) {
    NodeFactory nodeFactory = compiler.getFactory();
    return nodeFactory.newNode(new Token(nodeType, value));
  }

  private static Node createVarNode(AbstractCompiler compiler, String name) {
    NodeFactory nodeFactory = compiler.getFactory();
    return nodeFactory.newNode(Token.VAR, nodeFactory.newNameNode(name));
  }

  private static Node createFunctionNode(AbstractCompiler compiler) {
    NodeFactory nodeFactory = compiler.getFactory();
    Node body = NodeFactory.newBlock(NodeFactory.newNameNode("return null;"));
    return nodeFactory.newNode(Token.FUNCTION, null, null, null, null, null, body);
  }

  private static CompilerInput createInput(AbstractCompiler compiler, String code) {
    return compiler.newExternInput(code);
  }

  private static Node createAstRoot(AbstractCompiler compiler, CompilerInput input) {
    return input.getAstRoot(compiler);
  }

  private static Set<String> generateVarsToDeclareInExterns(VarCheck varCheck) {
    return varCheck.varsToDeclareInExterns;
  }

  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = createInput(compiler, "var a, b;");
    Node root = createAstRoot(compiler, input);

    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    varCheck = new VarCheck(compiler, true);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler, true);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "function foo() {} var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "function foo() {} var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler, true);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "function foo() {} var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler, false);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "function foo() {} var a = 1;");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler, true);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "var a, b; function foo() {}");
    root = createAstRoot(compiler, input);

    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    input = createInput(compiler, "var a, b; function foo() {}");
    root = createAstRoot(compiler, input);

    varCheck = new VarCheck(compiler, true);
    varCheck.process(createInput(compiler, ""), root);

    assertSame(input, varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeFactory nodeFactory = compiler.getFactory();
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Scope scope = mock(Scope.class);
    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(null);

    Node root = NodeFactory.newBlock(
        NodeFactory.newStatement(NodeFactory.newExpressionStatement(
            NodeFactory.newAssignment(createVarNode(compiler, "a"),
                NodeFactory.newNumberLiteral(1)))));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "a", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    root = NodeFactory.newBlock(
        NodeFactory.newStatementList(
            NodeFactory.newStatementList(
                NodeFactory.newStatement(NodeFactory.newExpressionStatement(
                    NodeFactory.newAssignment(createVarNode(compiler, "a"),
                        NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("foo")))))),
                NodeFactory.newStatement(NodeFactory.newExpressionStatement(
                    NodeFactory.newAssignment(createVarNode(compiler, "a"),
                        NodeFactory.newNumberLiteral(1)))))));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(null);

    Node parent = NodeFactory.newFunctionDeclaration(
        "foo", NodeFactory.newNameNode("foo"),
        NodeFactory.newBlock(
            NodeFactory.newStatementList(
                NodeFactory.newStatementList(
                    NodeFactory.newStatement(NodeFactory.newExpressionStatement(
                        NodeFactory.newAssignment(createVarNode(compiler, "a"),
                            NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("a")))))),
                    NodeFactory.newStatement(NodeFactory.newExpressionStatement(
                        NodeFactory.newAssignment(createVarNode(compiler, "b"),
                            NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("b"))))))))));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "a", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    Node parentFunction = createFunctionNode(compiler);
    parent.addChildToBack(parentFunction);
    root = parentFunction;

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "a", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    parent = NodeFactory.newLParen();
    parent.addChildToBack(createVarNode(compiler, "a"));
    parent.addChildToBack(NodeFactory.newRParen());

    root = NodeFactory.newNameNode("foo").getFirstChild();
    root.addChildToBack(parent);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "a", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    root = NodeFactory.newGetProp(
        NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("foo"))),
        NodeFactory.newNameNode("a"));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "a"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "a", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    root = NodeFactory.newGetProp(
        NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("foo"))),
        NodeFactory.newNameNode("b"));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "b"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "b", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    root = NodeFactory.newGetProp(
        NodeFactory.newIdentifier(NodeOperations.getName(NodeFactory.newNameNode("foo"))),
        NodeFactory.newNameNode("c"));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(generateVarsToDeclareInExterns(varCheck).contains("c"));

    when(scope.getVar(NodeOperations.getName(NodeFactory.newNode(Token.NAME, "c"))))
        .thenReturn(NodeFactory.newVar(nodeFactory, "c", null, null, null, null, null));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(createInput(compiler, ""), root), varCheck);

    assertTrue(generateVarsToDeclareInExterns(varCheck).isEmpty());

    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("c")) {
          t.report(n, new DiagnosticType("JSC_NAME_REFERENCE_IN_EXTERNS", null, null, null, null, null, null) {
            @Override
            public String getMessage(Node n) {
              return "accessing name " + n.getString() + " in externs has no effect";
            }
          });
        }
      }
    });

    assertFalse(generateVarsToDeclareInExterns(varCheck).isEmpty());
  }
}