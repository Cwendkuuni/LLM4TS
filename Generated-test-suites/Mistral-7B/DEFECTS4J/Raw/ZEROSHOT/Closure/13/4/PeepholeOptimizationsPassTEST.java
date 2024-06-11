package com.google.javascript.jscomp.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

 @Mock
 private AbstractCompiler compiler;

 @Mock
 private AbstractPeepholeOptimization optimization1;

 @Mock
 private AbstractPeepholeOptimization optimization2;

 @InjectMocks
 private PeepholeOptimizationsPass peepholeOptimizationsPass;

 @Before
 public void setUp() {
 Mockito.when(compiler.addChangeHandler(Mockito.any()))
 .thenAnswer(i -> i.getArgument(0));
 Mockito.when(compiler.removeChangeHandler(Mockito.any()))
 .thenAnswer(i -> i.getArgument(0));

 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node root = NodeOps.newNode("root", null);
 Node externs = NodeOps.newNode("externs", null);

 peepholeOptimizationsPass.process(externs, root);
 }

 @Test
 public void testTraverse() {
 Node node1 = NodeOps.newNode("node1", null);
 Node node2 = NodeOps.newNode("node2", null);
 Node node3 = NodeOps.newNode("node3", null);
 Node node4 = NodeOps.newNode("node4", null);

 Node parent = NodeOps.newNode("parent", null);
 parent.setFirstChild(node1);
 parent.setNext(node2);
 node2.setFirstChild(node3);
 node2.setNext(node4);
 node3.setFirstChild(node4);

 peepholeOptimizationsPass.traverse(parent);
 }

 @Test
 public void testVisit() {
 Node node = NodeOps.newNode("node", null);
 Node originalNode = node;

 do {
 Preconditions.checkState(peepholeOptimizationsPass.shouldVisit(originalNode));
 peepholeOptimizationsPass.visit(originalNode);
 } while (peepholeOptimizationsPass.shouldRetraverse(originalNode));

 Preconditions.checkState(originalNode.getChildren().size() == 0);

 List<Node> visitedNodes = new ArrayList<>();
 for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
 Node optimizedNode = optimization.optimizeSubtree(originalNode);
 visitedNodes.add(optimizedNode);
 Preconditions.checkState(optimizedNode != null);
 Preconditions.checkState(originalNode.getChildren().size() == visitedNodes.size());
 for (Node child : originalNode.getChildren()) {
 Preconditions.checkState(Arrays.binarySearch(visitedNodes.toArray(new Node[0]), child) >= 0);
 }
 originalNode = optimizedNode;
 }
 }

 @Test
 public void testBeginTraversal() {
 for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
 peepholeOptimizationsPass.beginTraversal();
 Mockito.verify(optimization).beginTraversal(peepholeOptimizationsPass.getCompiler());
 }
 }

 @Test
 public void testEndTraversal() {
 for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
 peepholeOptimizationsPass.endTraversal();
 Mockito.verify(optimization).endTraversal(peepholeOptimizationsPass.getCompiler());
 }
 }
}