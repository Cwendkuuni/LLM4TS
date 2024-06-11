package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR;
import static com.google.javascript.jscomp.SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler mockCompiler;
  private SyntacticScopeCreator mockScopeCreator;

  @Test
  public void testCreateScope() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope scope = mockScopeCreator.createScope(createNode("function foo() {}"), null);
    assertEquals(Scope.Type.FUNCTION, scope.getType());
  }

  @Test
  public void testCreateScopeWithParent() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("function bar() {}"));
    Scope scope = mockScopeCreator.createScope(createNode("function foo() {}"), parentScope);
    assertEquals(Scope.Type.FUNCTION, scope.getType());
    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testScanRootFunctionWithDeclaration() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("function bar() {}"));
    Scope scope = mockScopeCreator.createScope(createNode("function foo(x) {}"), parentScope);

    Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(Node.FUNCTION, xVar.getParentNode().getType());
    assertEquals(JSType.UNKNOWN, xVar.getType());
  }

  @Test
  public void testScanRootFunctionWithoutDeclaration() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("function bar() {}"));
    Scope scope = mockScopeCreator.createScope(createNode("function foo() {}"), parentScope);

    assertNull(scope.getVar("x"));
  }

  @Test
  public void testScanRootFunctionWithArguments() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntheticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("function bar(x, y) {}"));
    Scope scope = mockScopeCreator.createScope(createNode("function foo() {}"), parentScope);

    Var argumentsVar = scope.getVar("arguments");
    assertEquals("arguments", argumentsVar.getName());
    assertEquals(Node.FUNCTION, argumentsVar.getParentNode().getType());
    assertEquals(JSType.ARRAY, argumentsVar.getType());
  }

  @Test
  public void testScanRootScript() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope scope = mockScopeCreator.createScope(createNode("var x = 1, y = 2, z = 3"), null);

    Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(Node.VAR, xVar.getParentNode().getType());
    assertEquals(JSType.NUMBER, xVar.getType());

    Var yVar = scope.getVar("y");
    assertEquals("y", yVar.getName());
    assertEquals(Node.VAR, yVar.getParentNode().getType());
    assertEquals(JSType.NUMBER, yVar.getType());

    Var zVar = scope.getVar("z");
    assertEquals("z", zVar.getName());
    assertEquals(Node.VAR, zVar.getParentNode().getType());
    assertEquals(JSType.NUMBER, zVar.getType());
  }

  @Test
  public void testScanVarsWithFunctionNode() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("function bar(x) {}"));
    Scope scope = mockScopeCreator.createScope(createNode("function foo() { var x = 1; }"), parentScope);

    Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(Node.FUNCTION, xVar.getParentNode().getType());
    assertEquals(JSType.NUMBER, xVar.getType());
  }

  @Test
  public void testScanVarsWithVarNode() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode(""));
    Scope scope = mockScopeCreator.createScope(createNode("var x = 1"), parentScope);

    Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(Node.VAR, xVar.getParentNode().getType());
    assertEquals(JSType.NUMBER, xVar.getType());
  }

  @Test
  public void testScanVarsWithCatchNode() {
    mockCompiler = new AbstractCompiler() {
    };
    mockScopeCreator = new SyntacticScopeCreator(mockCompiler);

    Scope parentScope = new Scope(null, createNode("try { } catch (x) { }"));
    Scope scope = mockScopeCreator.createScope(createNode("try { } catch (x) { }"), parentScope);

    Var xVar = scope.getVar("x");
    assertEquals("x", xVar.getName());
    assertEquals(Node.CATCH, xVar.getParentNode().getType());
    assertEquals(JSType.UNKNOWN, xVar.getType());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclarationInTopLevelScope() {
    mockCompiler = new AbstractCompiler() {
    };
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(mockCompiler);
    SyntacticScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    scopeCreator.setRedeclarationHandler(redeclarationHandler);

    Scope scope = scopeCreator.createScope(createNode("var x = 1"), null);
    scopeCreator.createScope(createNode("var x = 2"), scope);

    assertEquals(1, scope.getVarCount());
    assertEquals("x", scope.getVar(0).getName());
    assertEquals(JSType.NUMBER, scope.getVar(0).getType());
  }

  @Test
  public void testRedeclarationHandlerOnRedeclarationInFunctionScope() {
    mockCompiler = new AbstractCompiler() {
    };
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(mockCompiler);
    SyntacticScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    scopeCreator.setRedeclarationHandler(redeclarationHandler);

    Scope parentScope = new Scope(null, createNode("function bar() {}"));
    Scope scope = scopeCreator.createScope(createNode("function foo() { var x = 1; }"), parentScope);
    scopeCreator.createScope(createNode("function foo() { var x = 2; }"), scope);

    assertEquals(1, parentScope.getVarCount());
    assertEquals("x", parentScope.getVar(0).getName());
    assertEquals(JSType.NUMBER, parentScope.getVar(0).getType());
  }

  @Test
  public void testRedeclarationHandlerOnVariableMultiplyDeclaredError() {
    mockCompiler = new AbstractCompiler() {
    };
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(mockCompiler);
    SyntacticScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    scopeCreator.setRedeclarationHandler(redeclarationHandler);

    Scope scope = scopeCreator.createScope(createNode("var x = 1"), null);
    scopeCreator.createScope(createNode("var x = 2"), scope);

    assertEquals(1, scope.getVarCount());
    assertEquals("x", scope.getVar(0).getName());
    assertEquals(JSType.NUMBER, scope.getVar(0).getType());

    String errorMessage =
        scopeCreator.getCompiler().getErrors().get(0).getMessage();

    assertEquals(VAR_MULTIPLY_DECLARED_ERROR, errorMessage.getDiagnosticType());
    assertEquals("x", errorMessage.getArgument(0));
    assertEquals("function", errorMessage.getArgument(1));
  }

  @Test
  public void testRedeclarationHandlerOnArgumentsVariableShadowedError() {
    mockCompiler = new AbstractCompiler() {
    };
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(mockCompiler);
    SyntacticScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        new SyntacticScopeCreator.DefaultRedeclarationHandler();
    scopeCreator.setRedeclarationHandler(redeclarationHandler);

    Scope parentScope = new Scope(null, createNode("function foo() { var arguments = 1; }"));
    Scope scope = scopeCreator.createScope(createNode("function bar() {}"), parentScope);

    String errorMessage =
        scopeCreator.getCompiler().getErrors().get(0).getMessage();

    assertEquals(VAR_ARGUMENTS_SHADOWED_ERROR, errorMessage.getDiagnosticType());
  }

  private Node createNode(String code) {
    return new JsCompilerTestBase().parseTree(code).getNode();
  }
}