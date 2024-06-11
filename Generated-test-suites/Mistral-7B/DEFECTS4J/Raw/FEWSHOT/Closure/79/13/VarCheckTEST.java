package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

import java.util.Set;

/**
 * Test class for {@link VarCheck}.
 */
@RunWith(JUnit4.class)
public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private NodeFactory nodeFactory;

  @Mock
  private NodeTraversal traversal;

  @Mock
  private CompilerInput input;

  @Mock
  private Node root;

  @Mock
  private Node externs;

  @Mock
  private JSModule module;

  @Mock
  private Scope scope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    varCheck = new VarCheck(compiler);

    when(input.getAstRoot(compiler)).thenReturn(root);
    when(input.getModule()).thenReturn(module);
    when(compiler.newExternInput("")).thenReturn(input);
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
  }

  @Test
  public void testVisit_undefinedVariable() {
    // Arrange
    when(scope.getVar("foo")).thenReturn(null);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    varCheck.compiler.report(root, UNDEFINED_VAR_ERROR, "foo");
  }

  @Test
  public void testVisit_functionDeclarationWithEmptyName() {
    // Arrange
    Node functionNode = new Node(NodeType.FUNCTION, null);

    // Act
    varCheck.visit(null, null, functionNode);

    // Assert
    Preconditions.checkState(NodeUtil.isFunction(functionNode.getParent()));
    if (!NodeUtil.isFunctionDeclaration(functionNode.getParent())) {
      varCheck.compiler.report(functionNode, INVALID_FUNCTION_DECL);
    }
  }

  @Test
  public void testVisit_variableDeclaredInSameFile() {
    // Arrange
    String varName = "foo";
    Var variable = new Var(varName, null, null, null, null, null, input);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    // No action taken
  }

  @Test
  public void testVisit_variableReferenceWithMissingModuleDependency() {
    // Arrange
    String varName = "foo";
    String moduleName = "bar";
    Var variable = new Var(varName, input, null, null, null, null, null);
    JSModule module = new JSModule(moduleName, null, null);
    when(scope.getVar(varName)).thenReturn(variable);
    when(input.getModule()).thenReturn(module);
    when(compiler.getModuleGraph().dependsOn(module, input.getModule())).thenReturn(false);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    if (!varCheck.sanityCheck) {
      varCheck.compiler.report(root, MISSING_MODULE_DEP_ERROR,
          input.getName(), moduleName, varName);
    }
  }

  @Test
  public void testVisit_variableReferenceWithStrictModuleDependency() {
    // Arrange
    String varName = "foo";
    String moduleName = "bar";
    Var variable = new Var(varName, input, null, null, null, null, null);
    JSModule module = new JSModule(moduleName, null, null);
    when(scope.getVar(varName)).thenReturn(variable);
    when(input.getModule()).thenReturn(module);
    when(compiler.getModuleGraph().dependsOn(input.getModule(), module)).thenReturn(true);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    if (varCheck.sanityCheck) {
      throw new IllegalStateException("Unexpected variable reference");
    } else {
      varCheck.compiler.report(root, STRICT_MODULE_DEP_ERROR,
          input.getName(), moduleName, varName);
    }
  }

  @Test
  public void testVisit_variableReferenceWithViolatedModuleDependency() {
    // Arrange
    String varName = "foo";
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    Var variable1 = new Var("foo", input, null, null, null, null, null);
    Var variable2 = new Var("foo", input, null, null, null, null, null);
    JSModule module1 = new JSModule(moduleName1, null, null);
    JSModule module2 = new JSModule(moduleName2, null, null);
    when(scope.getVar(varName)).thenReturn(variable2);
    when(input.getModule()).thenReturn(module1);
    when(compiler.getModuleGraph().dependsOn(module1, module2)).thenReturn(false);
    when(compiler.getModuleGraph().dependsOn(module2, module1)).thenReturn(true);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    if (varCheck.sanityCheck) {
      throw new IllegalStateException("Unexpected variable reference");
    } else {
      varCheck.compiler.report(root, VIOLATED_MODULE_DEP_ERROR,
          module2.getName(), module1.getName(), varName);
    }
  }

  @Test
  public void testVisit_variableReferenceWithDuplicateDeclaration() {
    // Arrange
    String varName = "foo";
    Node varNode1 = new Node(NodeType.VAR, null);
    varNode1.putStringProp("name", varName);
    Node varNode2 = new Node(NodeType.VAR, null);
    varNode2.putStringProp("name", varName);

    // Act
    varCheck.visit(null, null, root);

    // Assert
    varCheck.compiler.report(varNode1, DiagnosticType.DUPE);
  }

  @Test
  public void testVisit_variableReferenceWithEmptyNameFunctionDeclaration() {
    // Arrange
    Node functionNode = new Node(NodeType.FUNCTION, null);
    Node varNode = new Node(NodeType.VAR, null);

    // Act
    varCheck.visit(null, null, functionNode);

    // Assert
    Preconditions.checkState(NodeUtil.isFunction(functionNode.getParent()));
    if (!NodeUtil.isFunctionDeclaration(functionNode.getParent())) {
      varCheck.compiler.report(varNode, INVALID_FUNCTION_DECL);
    }
  }

  @Test
  public void testCreateSynthesizedExternVar() {
    // Arrange
    String varName = "foo";

    // Act
    varCheck.createSynthesizedExternVar(varName);

    // Assert
    Node nameNode = varCheck.getSynthesizedExternsRoot().getLastChild();
    assertThat(nameNode.getString()).isEqualTo(varName);
  }
}