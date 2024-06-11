package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  
  @Test
  public void testProcess() {
    // create Compiler and UnreachableCodeElimination objects
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // create dummy externs and toplevel nodes
    Node externs = new Node(Token.EXTERN, new Node(Token.OUT)) ;
    Node toplevel = new Node(Token.TOP, new Node(Token.SCRIPT));
    
    // process the nodes
    elimination.process(externs, toplevel);
    
    // assert statements
    // ...
  }
  
  @Test
  public void testEliminationPass() {
    // create GraphReachability, Compiler and UnreachableCodeElimination objects
    GraphReachability<Node, ControlFlowGraph.Branch> graphReachability = new GraphReachability<Node, ControlFlowGraph.Branch>();
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination.EliminationPass eliminationPass = 
            new UnreachableCodeElimination(compiler,true).new EliminationPass(null);
    
    // create dummy NodeTraversal, Node, and DiGraphNode objects
    NodeTraversal t = new NodeTraversal(compiler, null, null);
    Node n = new Node(Token.SCRIPT, new Node(Token.STRING));
    Node parent = new Node(Token.SCRIPT);
    DiGraphEdge<Node, ControlFlowGraph.Branch> edge = 
            new DiGraphEdge<Node, ControlFlowGraph.Branch>(null, ControlFlowGraph.Branch.UNCOND);
    DiGraphNode<Node, ControlFlowGraph.Branch> gNode = 
            new DiGraphNode<Node, ControlFlowGraph.Branch>(n, null);

    // call visit method
    eliminationPass.visit(t, n, parent);
    
    // assert statements
    // ...
  }
  
  @Test
  public void testTryRemoveUnconditionalBranching() {
    // create Compiler and UnreachableCodeElimination objects
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // create dummy Node and DiGraphEdge objects
    Node n = new Node(Token.SCRIPT, new Node(Token.STRING));
    DiGraphEdge<Node, ControlFlowGraph.Branch> edge = 
            new DiGraphEdge<Node, ControlFlowGraph.Branch>(n, ControlFlowGraph.Branch.UNCOND);
    
    // call tryRemoveUnconditionalBranching method
    elimination.tryRemoveUnconditionalBranching(n);
    
    // assert statements
    // ...
  }
  
  @Test
  public void testComputeFollowing() {
    // create Compiler and UnreachableCodeElimination objects
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // create dummy Node object
    Node n = new Node(Token.SCRIPT, new Node(Token.STRING));
    
    // call computeFollowing method
    Node result = elimination.computeFollowing(n);
    
    // assert statements
    // ...
  }
  
  @Test
  public void testRemoveDeadExprStatementSafely() {
    // create Compiler and UnreachableCodeElimination objects
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // create dummy Node object
    Node n = new Node(Token.SCRIPT, new Node(Token.STRING));
    
    // call removeDeadExprStatementSafely method
    elimination.removeDeadExprStatementSafely(n);
    
    // assert statements
    // ...
  }
  
  @Test
  public void testRemoveNode() {
    // create Compiler and UnreachableCodeElimination objects
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    
    // create dummy Node object
    Node n = new Node(Token.SCRIPT, new Node(Token.STRING));
    
    // call removeNode method
    elimination.removeNode(n);
    
    // assert statements
    // ...
  }
}