package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerContext;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockitoAnnotations;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Node root;
  private ControlFlowGraph<Node> controlFlowGraph;
  private LiveVariablesAnalysis liveVariablesAnalysis;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getFactory();
    root = nodeFactory.createProgram();
    controlFlowGraph = mock(ControlFlowGraph.class);
    liveVariablesAnalysis = mock(LiveVariablesAnalysis.class);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testEmptyProgram() {
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return false;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return null;
          }
        };
      }
    });
    when(liveVariablesAnalysis.analyze()).thenReturn(null);

    deadAssignmentsElimination.process(null, root);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    assertThat(context.getCode()).isEqualTo(root);
  }

  @Test
  public void testGlobalVariableIsNotRemoved() {
    Node globals = nodeFactory.createList();
    Node globalVar = nodeFactory.createName("x");
    globals.addChild(globalVar);

    Node assignGlobalVar = nodeFactory.createAssign(Token.ASSIGN, globalVar,
        nodeFactory.createNumberLiteral(5));
    root.addChild(assignGlobalVar);

    Node assignmentStatement = nodeFactory.createStatementList();
    assignmentStatement.addChild(assignGlobalVar);
    Node block = nodeFactory.createBlock(assignmentStatement);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return new DiGraphNode<>(block, null);
          }
        };
      }
    });

    when(liveVariablesAnalysis.analyze()).thenReturn(new LiveVariablesAnalysis(controlFlowGraph, null, compiler));

    deadAssignmentsElimination.process(null, root);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    assertThat(context.getCode()).isEqualTo(root);
  }

  @Test
  public void testDeadAssignmentIsRemoved() {
    Node assignX = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("x"), nodeFactory.createNumberLiteral(5));
    Node assignDeadX = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("x"), nodeFactory.createNumberLiteral(6));

    Node ifStatement = nodeFactory.createIfStatement(nodeFactory.createBinaryOp(Token.LOGICAL_AND,
        nodeFactory.createName("x"), nodeFactory.createBooleanLiteral(false)));

    Node statementList = nodeFactory.createStatementList();
    statementList.addChild(assignX);
    statementList.addChild(assignDeadX);
    ifStatement.setCondition(nodeFactory.createBlock(statementList));

    Node assignY = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("y"), nodeFactory.createNumberLiteral(4));

    Node block = nodeFactory.createBlock(nodeFactory.createStatementList());
    block.addChild(assignX);
    block.addChild(ifStatement);
    block.addChild(assignY);

    Node program = nodeFactory.createProgram();
    program.addChild(block);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return null;
          }
        };
      }
    });

    when(liveVariablesAnalysis.analyze()).thenReturn(new LiveVariablesAnalysis(controlFlowGraph, null, compiler));

    deadAssignmentsElimination.process(null, program);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    Node newCode = context.getCode();

    assertThat(newCode.getLastChild()).isNotNull();
    assertThat(newCode.getLastChild().getParent()).isNotNull();
    assertThat(newCode.getLastChild().getParent()).isEqualTo(newCode);
    assertThat(newCode.getLastChild()).isEqualTo(assignY);
  }

  @Test
  public void testDeadAssignmentInForLoopIsRemoved() {
    Node forNode = nodeFactory.createForStatement(nodeFactory.createName("i"),
        nodeFactory.createNumberLiteral(0), nodeFactory.createNumberLiteral(1), null);
    Node statementList = nodeFactory.createStatementList();
    Node deadAssignment = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("x"), nodeFactory.createNumberLiteral(5));
    statementList.addChild(deadAssignment);
    forNode.setBody(nodeFactory.createBlock(statementList));

    Node assignY = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("y"), nodeFactory.createNumberLiteral(4));

    Node block = nodeFactory.createBlock(nodeFactory.createStatementList());
    block.addChild(nodeFactory.createStatementList());
    block.addChild(forNode);
    block.addChild(assignY);

    Node program = nodeFactory.createProgram();
    program.addChild(block);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return null;
          }
        };
      }
    });

    when(liveVariablesAnalysis.analyze()).thenReturn(new LiveVariablesAnalysis(controlFlowGraph, null, compiler));

    deadAssignmentsElimination.process(null, program);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    Node newCode = context.getCode();

    assertThat(newCode.getLastChild()).isNotNull();
    assertThat(newCode.getLastChild().getParent()).isNotNull();
    assertThat(newCode.getLastChild()).isEqualTo(assignY);
  }

  @Test
  public void testDeadAssignmentInIfStatementIsRemoved() {
    Node ifNode = nodeFactory.createIfStatement(nodeFactory.createBinaryOp(Token.LOGICAL_AND,
        nodeFactory.createName("x"), nodeFactory.createBooleanLiteral(false)));

    Node statementList = nodeFactory.createStatementList();
    Node deadAssignment = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("x"), nodeFactory.createNumberLiteral(5));
    statementList.addChild(deadAssignment);
    ifNode.setCondition(nodeFactory.createBlock(statementList));

    Node assignY = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("y"), nodeFactory.createNumberLiteral(4));

    Node block = nodeFactory.createBlock(nodeFactory.createStatementList());
    block.addChild(assignY);

    Node program = nodeFactory.createProgram();
    program.addChild(block);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return null;
          }
        };
      }
    });

    when(liveVariablesAnalysis.analyze()).thenReturn(new LiveVariablesAnalysis(controlFlowGraph, null, compiler));

    deadAssignmentsElimination.process(null, program);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    Node newCode = context.getCode();

    assertThat(newCode.getLastChild()).isNotNull();
    assertThat(newCode.getLastChild().getParent()).isNotNull();
    assertThat(newCode.getLastChild()).isEqualTo(assignY);
  }

  @Test
  public void testDeadAssignmentInSwitchStatementIsRemoved() {
    Node switchNode = nodeFactory.createSwitchStatement(nodeFactory.createName("x"));

    Node statementList = nodeFactory.createStatementList();
    Node deadAssignment = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("x"), nodeFactory.createNumberLiteral(5));
    statementList.addChild(deadAssignment);
    switchNode.setBody(nodeFactory.createBlock(statementList));

    Node assignY = nodeFactory.createAssign(Token.ASSIGN,
        nodeFactory.createName("y"), nodeFactory.createNumberLiteral(4));

    Node block = nodeFactory.createBlock(nodeFactory.createStatementList());
    block.addChild(assignY);

    Node program = nodeFactory.createProgram();
    program.addChild(block);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return null;
          }
        };
      }
    });

    when(liveVariablesAnalysis.analyze()).thenReturn(new LiveVariablesAnalysis(controlFlowGraph, null, compiler));

    deadAssignmentsElimination.process(null, program);

    InOrder inOrder = inOrder(compiler);
    ArgumentCaptor<CompilerContext> compilerContextCaptor = ArgumentCaptor.forClass(CompilerContext.class);
    inOrder.verify(compiler).runPass(compilerContextCaptor.capture());
    CompilerContext context = compilerContextCaptor.getValue();
    Node newCode = context.getCode();

    assertThat(newCode.getLastChild()).isNotNull();
    assertThat(newCode.getLastChild().getParent()).isNotNull();
    assertThat(newCode.getLastChild()).isEqualTo(assignY);
  }
}