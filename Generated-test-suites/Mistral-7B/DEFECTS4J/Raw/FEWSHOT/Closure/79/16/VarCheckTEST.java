package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import java.util.Set;

/**
 * Test class for VarCheck class.
 */
public class VarCheckTest {

  private static final String TEST_FILE = "TestVarCheck.js";
  private static final String TEST_MODULE = "TestModule";

  @org.junit.Before
  public void setUp() {
    // Initialize mocks
    NodeFactory nodeFactory = mock(NodeFactory.class);
    NodeOperations nodeOperations = mock(NodeOperations.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getErrorLevel(any())).thenReturn(CheckLevel.WARNING);
    when(compiler.newExternInput(anyString())).thenAnswer(i -> new DummyCompilerInput());

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, nodeFactory, nodeOperations);
    traversal.setCallback(varCheck);

    this.compiler = compiler;
    this.traversal = traversal;
  }

  @org.junit.Test
  public void testInvalidFunctionDecl() {
    Node root = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n = NodeOperations.createChildAdd(root, Token.FUNCTION, null);
    NodeTraversal.traverse(compiler, root, traversal);

    verify(compiler, times(1)).report(n, INVALID_FUNCTION_DECL);
  }

  @org.junit.Test
  public void testUndefinedVarInCurrentFile() {
    Node root = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n = NodeOperations.createChildAdd(root, Token.VAR, "x");
    NodeTraversal.traverse(compiler, root, traversal);

    // No error reported since "x" is defined in the current file
  }

  @org.junit.Test
  public void testUndefinedVarInDifferentFile() {
    Node root1 = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n1 = NodeOperations.createChildAdd(root1, Token.VAR, "x");

    CompilerInput input2 = new DummyCompilerInput();
    Node root2 = input2.getAstRoot(compiler);
    Node n2 = NodeOperations.createChildAdd(root2, Token.VAR, "x");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root1, root2), traversal);

    verify(compiler, times(1)).report(n2, UNDEFINED_VAR_ERROR, "x");
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    Node root1 = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n1 = NodeOperations.createChildAdd(root1, Token.GETPROP, "m1.x");

    CompilerInput input2 = new DummyCompilerInput();
    input2.setModuleName(TEST_MODULE);
    Node root2 = input2.getAstRoot(compiler);
    Node m2 = NodeOperations.createChildAdd(root2, Token.NAME, TEST_MODULE);
    Node x = NodeOperations.createChildAdd(m2, Token.VAR, "x");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root1, root2), traversal);

    verify(compiler, times(1)).report(n1, MISSING_MODULE_DEP_ERROR, TEST_MODULE, TEST_MODULE, "x");
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    Node root1 = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n1 = NodeOperations.createChildAdd(root1, Token.GETPROP, "m1.x");

    CompilerInput input2 = new DummyCompilerInput();
    input2.setModuleName(TEST_MODULE);
    input2.setHasModuleDeclaration(true);
    Node root2 = input2.getAstRoot(compiler);
    Node m2 = NodeOperations.createChildAdd(root2, Token.NAME, TEST_MODULE);
    Node x = NodeOperations.createChildAdd(m2, Token.VAR, "x");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root1, root2), traversal);

    verify(compiler, never()).report(n1, STRICT_MODULE_DEP_ERROR, anyString(), anyString(), eq("x"));
  }

  @org.junit.Test
  public void testDuplicateVarDecl() {
    Node root = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node var1 = NodeOperations.createChildAdd(root, Token.VAR, "x");
    Node var2 = NodeOperations.createChildAdd(root, Token.VAR, "x");

    NodeTraversal.traverse(compiler, root, traversal);

    verify(compiler, times(1)).report(var2, "duplicate");
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    Node root = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n = NodeOperations.createChildAdd(root, Token.GETPROP, "m1.x");

    CompilerInput input = new DummyCompilerInput();
    Node root2 = input.getAstRoot(compiler);
    Node m2 = NodeOperations.createChildAdd(root2, Token.NAME, "m1");
    Node x = NodeOperations.createChildAdd(m2, Token.VAR, "x");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root, root2), traversal);

    verify(compiler, times(1)).report(n, NAME_REFERENCE_IN_EXTERNS_ERROR, "x");
  }

  @org.junit.Test
  public void testUndefinedExternVar() {
    Node root = NodeOperations.parseJsSourceToRoot(TEST_FILE, nodeFactory);
    Node n = NodeOperations.createChildAdd(root, Token.GETPROP, "m1.x");

    CompilerInput input = new DummyCompilerInput();
    input.setModuleName(TEST_MODULE);
    Node root2 = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root, root2), traversal);

    verify(compiler, never()).report(n, UNDEFINED_EXTERN_VAR_ERROR, "x");
  }

  private static class DummyCompilerInput implements CompilerInput {
    private String name;

    DummyCompilerInput() {
      name = "DummyCompilerInput";
    }

    DummyCompilerInput(String name) {
      this.name = name;
    }

    @Override
    public Node getAstRoot(AbstractCompiler compiler) {
      return NodeOperations.createRoot(Token.PROGRAM, null);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Set<String> getDependentFiles() {
      return Sets.newHashSet();
    }

    @Override
    public JSModule getModule() {
      return new JSModule() {
        @Override
        public String getName() {
          return name;
        }
      };
    }

    @Override
    public boolean isExtern() {
      return true;
    }
  }

  private AbstractCompiler compiler;
  private NodeTraversal traversal;
}