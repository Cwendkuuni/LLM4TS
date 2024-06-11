package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("unchecked")
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef_BasicFunctionality() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = Mockito.mock(Node.class);
    Node definitionNode = Mockito.mock(Node.class);

    GraphNode<Node, Branch> useGraphNode = Mockito.mock(GraphNode.class);
    Mockito.when(cfg.getNode(useNode)).thenReturn(useGraphNode);

    Mockito.when(useGraphNode.getAnnotation()).thenReturn(new FlowState<>());

    Mockito.when(jsScope.getVar("testVar")).thenReturn(new Var("testVar", definitionNode, jsScope.getRootNode()));

    Mockito.when(definitionNode.isName()).thenReturn(true);
    Mockito.when(definitionNode.getString()).thenReturn("test");

    Mockito.when(definitionNode.getFirstChild()).thenReturn(useNode);

    Mockito.when(cfg.hasNode(useNode)).thenReturn(true);

    Node def = mustBeReachingVariableDef.getDef("testVar", useNode);

    assertSame(definitionNode, def);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMustBeReachingVariableDef_NullUseNode() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = null;

    MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    mustBeReachingVariableDef.flowThrough(useNode, mustDef);
  }

  @Test
  public void testMustBeReachingVariableDef_NullDefNode() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = Mockito.mock(Node.class);
    Node definitionNode = null;

    GraphNode<Node, Branch> useGraphNode = Mockito.mock(GraphNode.class);
    Mockito.when(cfg.getNode(useNode)).thenReturn(useGraphNode);

    Mockito.when(useGraphNode.getAnnotation()).thenReturn(new FlowState<>());

    Mockito.when(jsScope.getVar("testVar")).thenReturn(new Var("testVar", definitionNode, jsScope.getRootNode()));

    MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    Node def = mustBeReachingVariableDef.getDef("testVar", useNode);

    assertNull(def);
  }

  @Test
  public void testMustBeReachingVariableDef_VariableNotDefined() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = Mockito.mock(Node.class);
    Node definitionNode = Mockito.mock(Node.class);

    GraphNode<Node, Branch> useGraphNode = Mockito.mock(GraphNode.class);
    Mockito.when(cfg.getNode(useNode)).thenReturn(useGraphNode);

    Mockito.when(useGraphNode.getAnnotation()).thenReturn(new FlowState<>());

    Mockito.when(jsScope.getVar("nonExistingVar")).thenReturn(null);

    MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    Node def = mustBeReachingVariableDef.getDef("nonExistingVar", useNode);

    assertNull(def);
  }

  @Test
  public void testMustBeReachingVariableDef_MultipleDefinitions() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = Mockito.mock(Node.class);

    Node definitionNode1 = Mockito.mock(Node.class);
    Mockito.when(definitionNode1.isName()).thenReturn(true);
    Mockito.when(definitionNode1.getString()).thenReturn("def1");

    Node definitionNode2 = Mockito.mock(Node.class);
    Mockito.when(definitionNode2.isName()).thenReturn(true);
    Mockito.when(definitionNode2.getString()).thenReturn("def2");

    GraphNode<Node, Branch> useGraphNode = Mockito.mock(GraphNode.class);
    Mockito.when(cfg.getNode(useNode)).thenReturn(useGraphNode);

    Mockito.when(useGraphNode.getAnnotation()).thenReturn(new FlowState<>());

    Mockito.when(jsScope.getVar("testVar")).thenReturn(new Var("testVar", definitionNode1, jsScope.getRootNode()));
    Mockito.when(jsScope.getVars()).thenReturn(Sets.newHashSet("testVar1", "testVar2").iterator());

    MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    Node def1 = mustBeReachingVariableDef.getDef("testVar", useNode);
    assertSame(definitionNode1, def1);

    Node def2 = mustBeReachingVariableDef.getDef("testVar1", useNode);
    assertNull(def2);

    Node def3 = mustBeReachingVariableDef.getDef("testVar2", useNode);
    assertNull(def3);
  }

  @Test
  public void testMustBeReachingVariableDef_VariableDefinedInAnotherScope() {
    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    Class<?> clazz = classLoader.definePackage("com.google.javascript.jscomp")
        .defineClass(MustBeReachingVariableDef.class.getName(), MustBeReachingVariableDef.class);

    ControlFlowGraph cfg = Mockito.mock(ControlFlowGraph.class);
    Scope jsScope = Mockito.mock(Scope.class);
    Scope otherScope = Mockito.mock(Scope.class);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        (MustBeReachingVariableDef) ReflectionUtils.newInstance(clazz, cfg, jsScope, compiler);

    Node useNode = Mockito.mock(Node.class);

    Node definitionNode = Mockito.mock(Node.class);
    Mockito.when(definitionNode.isName()).thenReturn(true);
    Mockito.when(definitionNode.getString()).thenReturn("testVar");

    GraphNode<Node, Branch> useGraphNode = Mockito.mock(GraphNode.class);
    Mockito.when(cfg.getNode(useNode)).thenReturn(useGraphNode);

    Mockito.when(useGraphNode.getAnnotation()).thenReturn(new FlowState<>());

    Mockito.when(jsScope.getVar("testVar")).thenReturn(null);
    Mockito.when(jsScope.getScope()).thenReturn(jsScope);
    Mockito.when(otherScope.getVar("testVar")).thenReturn(new Var("testVar", definitionNode, otherScope.getRootNode()));

    Mockito.when(jsScope.getVars()).thenReturn(Sets.newHashSet().iterator());

    MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    Node def = mustBeReachingVariableDef.getDef("testVar", useNode);

    assertNull(def);
  }
}