import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.rhino.ErrorReporter.Level;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.StaticScope.StaticSlot;

/**
 * Test class for Scope class.
 */
public class ScopeTest {

  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = Mockito.spy(new AbstractCompiler() {
      @Override
      public JSType getNativeObjectType(JSTypeNative nativeType) {
        return JSType.getNativeObjectType(nativeType);
      }
    });
    errorReporter = Mockito.spy(new ErrorReporter() {
      @Override
      public void error(Level level, int line, int lineNumber, String message) {
      }

      @Override
      public void warning(Level level, int line, int lineNumber, String message) {
      }
    });
  }

  @Test
  public void testBasicFunctionScope() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Preconditions.checkNotNull(functionScope);

    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    assertEquals("x", x.getName());
    assertEquals(JSType.getIntType(), x.getType());
  }

  @Test
  public void testVariableDeclarationWithValue() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Preconditions.checkNotNull(functionScope);

    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    assertEquals("x", x.getName());
    assertEquals(JSType.getIntType(), x.getType());
    assertEquals(1, x.getIndex());
  }

  @Test
  public void testVariableDeclarationWithoutValue() {
    Node rootNode = createFunctionNode("function foo() { var x; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Preconditions.checkNotNull(functionScope);

    Var x = functionScope.declare("x", null, null, new CompilerInput("foo.js"));
    assertEquals("x", x.getName());
    assertNull(x.getType());
    assertEquals(-1, x.getIndex());
  }

  @Test
  public void testDeclareSameVariableTwice() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Preconditions.checkNotNull(functionScope);

    Var x1 = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    Var x2 = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    assertEquals(x1, x2);
  }

  @Test
  public void testGetVariableType() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    JSType type = x.getType();
    assertEquals(JSType.getIntType(), type);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetTypeOnNonInferredVariable() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    x.setType(JSType.getStringType());
  }

  @Test
  public void testVariableShadowing() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; function bar() { var x = 2; } }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var xInFunction = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    Var xInBar = functionScope.getChildScope().declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertEquals(xInFunction, xInBar.getParentScope().getSlot("x"));
    assertEquals(2, xInBar.getIndex());
  }

  @Test
  public void testVariableIsDeclared() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);

    assertTrue(functionScope.isDeclared("x", true));
  }

  @Test
  public void testVariableNotDeclaredInCurrentScope() {
    Node rootNode = createFunctionNode("function foo() { }");

    Scope functionScope = new Scope(rootNode, compiler);

    assertFalse(functionScope.isDeclared("x", true));
  }

  @Test
  public void testVariableNotDeclaredInCurrentOrParentScope() {
    Node rootNode = createFunctionNode("function foo() { }; function bar() { }");

    Scope functionScope = new Scope(rootNode, compiler);

    assertFalse(functionScope.isDeclared("x", true));

    Iterator<Var> vars = functionScope.getVars();
    Scope barScope = vars.next().getScope();

    assertFalse(barScope.isDeclared("x", true));
  }

  @Test
  public void testGetVarCount() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; var y = 2; }");
    Scope functionScope = new Scope(rootNode, compiler);

    assertEquals(2, functionScope.getVarCount());
  }

  @Test(expected = NullPointerException.class)
  public void testGetVarNullScope() {
    Scope nullScope = null;
    nullScope.getSlot("x");
  }

  @Test
  public void testGetVarNullName() {
    Node rootNode = createFunctionNode("function foo() { }");
    Scope functionScope = new Scope(rootNode, compiler);

    NullPointerException exception = assertThrows(NullPointerException.class, () -> functionScope.getVar(null));
    assertEquals("name is marked non-null", exception.getMessage());
  }

  @Test
  public void testResolveType() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; x = 2; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    x.resolveType(errorReporter);
    assertEquals(JSType.getLeastSupertype(JSType.getIntType(), JSType.getIntType()), x.getType());
  }

  @Test
  public void testIsTypeInferred() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertTrue(x.isTypeInferred());
  }

  @Test
  public void testIsNoShadow() {
    Node rootNode = createFunctionNode("/** @noshide */ function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertTrue(x.isNoShadow());
  }

  @Test(expected = IllegalStateException.class)
  public void testIsNoShadowVariableWithoutJSDocInfo() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    x.isNoShadow();
  }

  @Test
  public void testGetSlotForUndefinedVariable() {
    Node rootNode = createFunctionNode("function foo() { }");
    Scope functionScope = new Scope(rootNode, compiler);

    assertNull(functionScope.getSlot("x"));
  }

  @Test
  public void testGetSlotForVariableWithDeclaredType() {
    Node rootNode = createFunctionNode("function foo() { var x: number = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertEquals(x, functionScope.getSlot("x"));
  }

  @Test
  public void testGetSlotForVariableWithInferredType() {
    Node rootNode = createFunctionNode("function foo() { var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var x = functionScope.declare("x", null, null, new CompilerInput("foo.js"));

    assertEquals(x, functionScope.getSlot("x"));
  }

  @Test
  public void testGetSlotForVariableWithJSDocInfo() {
    Node rootNode = createFunctionNode("/** @param {number} x */ function foo(x) { }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertEquals(x, functionScope.getSlot("x"));
  }

  @Test
  public void testGetSlotForVariableWithExternType() {
    Node rootNode = createFunctionNode("/** @extern */ function foo() {}");
    Scope functionScope = new Scope(rootNode, compiler);

    Var x = functionScope.declare("x", null, null, new CompilerInput("foo.js"));

    assertEquals(x, functionScope.getSlot("x"));
  }

  @Test
  public void testGetSlotForGlobalVariable() {
    Node rootNode = createFunctionNode("function foo() { /** @global */ var x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);
    Scope globalScope = functionScope.getParent();

    Var x = globalScope.declare("x", null, JSType.getIntType(), null);

    assertEquals(x, globalScope.getSlot("x"));

    Var xInFunctionScope = functionScope.getSlot("x");
    assertEquals(x, xInFunctionScope);
  }

  @Test
  public void testGetSlotForGlobalVariableWithFunctionScope() {
    Node rootNode = createFunctionNode("function foo() { /** @global */ function bar() { } }");
    Scope functionScope = new Scope(rootNode, compiler);
    Scope globalScope = functionScope.getParent();

    Var bar = globalScope.declare("bar", null, new FunctionType(JSType.getVoidType(), new JSType[0]), null);

    assertEquals(bar, globalScope.getSlot("bar"));

    Var barInFunctionScope = functionScope.getSlot("bar");
    assertEquals(bar, barInFunctionScope);
  }

  @Test
  public void testGetSlotForConstVariable() {
    Node rootNode = createFunctionNode("function foo() { const x = 1; }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var x = functionScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    assertEquals(x, functionScope.getSlot("x"));
    assertFalse(x.isTypeInferred());
  }

  @Test
  public void testGetSlotForDefineVariable() {
    Node rootNode = createFunctionNode("/** @define */ function foo() { }");
    Scope functionScope = new Scope(rootNode, compiler);

    Var define = functionScope.declare("define", null, null, null);

    assertEquals(define, functionScope.getSlot("define"));
  }

  @Test
  public void testJoinScopes() {
    Node rootNode = createFunctionNode("function foo() { function bar() {} }");
    Scope fooScope = new Scope(rootNode, compiler);
    Scope barScope = fooScope.getChildScope();

    Var xInFoo = fooScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));
    Var xInBar = barScope.declare("x", null, JSType.getIntType(), new CompilerInput("foo.js"));

    Iterator<Var> fooVars = fooScope.getVars();
    Iterator<Var> barVars = barScope.getVars();

    Scope joinedScope = DataFlowAnalysis.joinScopes(fooScope, barScope, new StaticScope.ScopeJoinOptions());

    JSType joinType = JSType.getLeastSupertype(xInFoo.getType(), xInBar.getType());

    assertEquals(joinedScope.getVarCount(), fooScope.getVarCount() + barScope.getVarCount());

    Iterator<Var> joinedVars = joinedScope.getVars();

    Var x = joinedVars.next();
    assertEquals(x, xInFoo);
    assertEquals(x.getType(), joinType);

    Var yInBar = barVars.next();
    assertEquals(yInBar, joinedVars.next());
  }

  private Node createFunctionNode(String code) {
    Node rootNode = NodeTraversal.parse(code, null, null);
    NodeTraversal.typeCheck(rootNode, compiler, errorReporter);
    return rootNode;
  }
}