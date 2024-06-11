package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CompilerPass.Stage.TOP_LEVEL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.ModuleGraph;
import com.google.javascript.rhino.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private IdGenerator idGenerator;
  private ModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    idGenerator = mock(IdGenerator.class);
    moduleGraph = mock(ModuleGraph.class);
  }

  @Test
  public void testMoveMethodToNextModule() {
    // Given
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(
        compiler, moduleGraph, true, false);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    when(idGenerator.newId()).thenReturn(1);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    Node root = new Node(Node.PROGRAM);
    Node externRoot = new Node(Node.PROGRAM);

    Node originalModule = new Node(Node.MODULE);
    originalModule.setProperty("name", "originalModule");
    root.addChildToFront(originalModule);
    externRoot.addChildToFront(originalModule);

    Node deeperModule = new Node(Node.MODULE);
    deeperModule.setProperty("name", "deeperModule");
    root.addChildToFront(deeperModule);
    when(moduleGraph.getModule(deeperModule.getString("name"))).thenReturn(
        deeperModule);

    Node method = new Node(Node.FUNCTION, null, ImmutableList.of(
        new Node(Node.IDENTIFIER, "a")), null);
    method.setProperty("name", "method");
    Node methodDeclaration = new Node(Node.ASSIGN, null,
        ImmutableList.of(new Node(Node.GETPROP, originalModule, "prototype"),
            new Node(Node.IDENTIFIER, "method"), method), null);

    originalModule.addChildrenToFront(methodDeclaration);

    Node deepestCommonModule = new Node(Node.MODULE);
    deepestCommonModule.setProperty("name", "deepestCommonModule");
    root.addChildToFront(deepestCommonModule);
    when(moduleGraph.getModule(deepestCommonModule.getString("name"))).thenReturn(
        deepestCommonModule);

    Node unstubbedMethod = new Node(Node.BLOCK, null, Collections.emptyList(), null);
    unstubbedMethod.setProperty("name", "unstubbedMethod");
    deeperModule.addChildrenToFront(unstubbedMethod);

    // When
    analyzer.process(externRoot, root);
    pass.process(externRoot, root);
    pass.moveMethods(analyzer.getAllNameInfo());

    // Then
    List<Node> methodDeclarations = new ArrayList<>();
    Iterator<Symbol> declarations = analyzer.getAllNameInfo().get(
        "method").getDeclarations().descendingIterator();
    while (declarations.hasNext()) {
      methodDeclarations.add(declarations.next().getValue());
    }
    assertTrue(methodDeclarations.get(0) instanceof Node);
    assertTrue(methodDeclarations.get(0) instanceof Node);
    Node stubCall = (Node) methodDeclarations.get(0);
    assertTrue(stubCall.getFirstChild().isIdentifier());
    assertTrue(stubCall.getFirstChild().getString().equals("JSCompiler_stubMethod"));
    assertTrue(stubCall.getLastChild().isFunction());

    Node unstubbedMethodBody = (Node) unstubbedMethod.getFirstChild();
    Node unstubbedMethodCall = unstubbedMethodBody.getFirstChild();
    assertTrue(unstubbedMethodCall.isCall());
    assertEquals(1, unstubbedMethodCall.getArguments().size());
    Node deepestCommonModuleRef = unstubbedMethodCall.getArguments().get(0);
    assertEquals("deepestCommonModule", deepestCommonModuleRef.getString());

    Node unstubbedMethodBodyParent = unstubbedMethodBody.getParent();
    Node unstubbedMethodBodyAssignment = unstubbedMethodBodyParent.getFirstChild();
    assertEquals("JSCompiler_unstubMethod", unstubbedMethodBodyAssignment.getString());
    assertEquals(2, unstubbedMethodBodyAssignment.getChildren().size());
    Node stubId = unstubbedMethodBodyAssignment.getChildren().get(0);
    Node unstubbedMethodBody = unstubbedMethodBodyAssignment.getChildren().get(1);
    assertEquals(1, stubId.getInt32());
    assertTrue(unstubbedMethodBody instanceof Node);
    assertTrue(unstubbedMethodBody.isBlock());

    // Edge cases
    when(idGenerator.newId()).thenReturn(Integer.MAX_VALUE);
    analyzer.process(externRoot, root);
    pass.process(externRoot, root);
    pass.moveMethods(analyzer.getAllNameInfo());
    Node methodDeclaration2 = new Node(Node.ASSIGN, null,
        ImmutableList.of(new Node(Node.GETPROP, originalModule, "prototype"),
            new Node(Node.IDENTIFIER, "method2"), method), null);
    originalModule.addChildrenToFront(methodDeclaration2);
    analyzer.process(externRoot, root);
    pass.moveMethods(analyzer.getAllNameInfo());
    Node stubCall2 = (Node) methodDeclarations.get(1);
    assertTrue(stubCall2.getFirstChild().getString().equals("JSCompiler_stubMethod"));
    assertTrue(stubCall2.getLastChild().isFunction());

    // Error scenarios
    when(moduleGraph.getModule(deepestCommonModule.getString("name"))).thenReturn(null);
    analyzer.process(externRoot, root);
    pass.process(externRoot, root);
    pass.moveMethods(analyzer.getAllNameInfo());
    assertTrue(analyzer.getAllNameInfo().isEmpty());

    when(moduleGraph.getModule(deeperModule.getString("name"))).thenReturn(null);
    analyzer.process(externRoot, root);
    pass.process(externRoot, root);
    pass.moveMethods(analyzer.getAllNameInfo());
    assertTrue(analyzer.getAllNameInfo().isEmpty());
  }

  private void assertEquals(Node expected, Node actual) {
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null) {
      fail("Expected: " + expected + ", Actual: " + actual);
    }
    if (expected.getClass() != actual.getClass()) {
      fail("Expected: " + expected.getClass() + ", Actual: " + actual.getClass());
    }
    if (!expected.getClass().isInstance(actual)) {
      fail("Expected: " + expected + ", Actual: " + actual);
    }
  }
}