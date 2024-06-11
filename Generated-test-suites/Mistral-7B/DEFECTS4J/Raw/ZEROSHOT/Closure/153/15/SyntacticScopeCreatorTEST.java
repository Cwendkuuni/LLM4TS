import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.ScopeType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SyntacticScopeCreatorTest {

  @Mock private AbstractCompiler compiler;
  private SyntacticScopeCreator sut;
  private Scope rootScope;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    sut = new SyntacticScopeCreator(compiler);
    rootScope = new Scope(null, null);
  }

  @Test
  public void testEmptyFunction() {
    Node functionNode = createFunctionNode("func() {}");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    assertNull(returnedScope.getVar("func"));
  }

  @Test
  public void testFunctionWithArg() {
    Node functionNode = createFunctionNode("func(x) {}");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var argVar = returnedScope.getVar("x");
    assertEquals(JSType.UNDEFINED, argVar.type);
  }

  @Test
  public void testFunctionWithArgAndBody() {
    Node functionNode = createFunctionNode("func(x) { x = 1; }");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var argVar = returnedScope.getVar("x");
    assertEquals(JSType.NUMBER, argVar.type);
  }

  @Test
  public void testFunctionWithMultipleArgs() {
    Node functionNode = createFunctionNode("func(x, y) {}");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    Var yVar = returnedScope.getVar("y");
    assertEquals(JSType.UNDEFINED, xVar.type);
    assertEquals(JSType.UNDEFINED, yVar.type);
  }

  @Test
  public void testGlobalScope() {
    Node scriptNode = createScriptNode("x = 1; func() {}");
    Scope returnedScope = sut.createScope(scriptNode, null);
    assertEquals(ScopeType.GLOBAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    assertEquals(JSType.NUMBER, xVar.type);
  }

  @Test
  public void testGlobalScopeWithDuplicateDeclaration() {
    Node scriptNode = createScriptNode("x = 1; x = 2;");
    assertThrows(JSError.class, () -> {
      sut.createScope(scriptNode, null);
    });
  }

  @Test
  public void testLocalScopeWithDuplicateDeclaration() {
    Node functionNode = createFunctionNode("func() { x = 1; x = 2; }");
    assertThrows(JSError.class, () -> {
      sut.createScope(functionNode, rootScope);
    });
  }

  @Test
  public void testLocalScopeWithArgumentDuplicateDeclaration() {
    Node functionNode = createFunctionNode("func(x) { x = 1; x = 2; }");
    assertThrows(JSError.class, () -> {
      sut.createScope(functionNode, rootScope);
    });
  }

  @Test
  public void testLocalScopeWithDuplicateDeclarationByDifferentNode() {
    Node functionNode1 = createFunctionNode("func() { x = 1; }");
    Node functionNode2 = createFunctionNode("func() { x = 2; }");
    assertThrows(JSError.class, () -> {
      sut.createScope(functionNode2, rootScope);
    });
  }

  @Test
  public void testLocalScopeWithDuplicateDeclarationSuppressed() {
    DiagnosticType suppressType = DiagnosticType.suppress(
        "JSC_VAR_MULTIPLY_DECLARED_ERROR");
    JSDocInfo docInfo = createJSDocInfo(suppressType);
    Node functionNode = createFunctionNode("/** @suppress {JSC_VAR_MULTIPLE_DECLARED_ERROR} */ func() { x = 1; x = 2; }");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    assertEquals(JSType.UNDEFINED, xVar.type);
  }

  @Test
  public void testLocalScopeWithDuplicateDeclarationAndOtherDeclarations() {
    Node functionNode = createFunctionNode("func() { x = 1; y = 2; x = 3; }");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    Var yVar = returnedScope.getVar("y");
    assertEquals(JSType.NUMBER, xVar.type);
    assertEquals(JSType.NUMBER, yVar.type);
  }

  @Test
  public void testLocalScopeWithDuplicateDeclarationOfNamedFunction() {
    Node functionNode1 = createFunctionNode("func() {}");
    Node functionNode2 = createFunctionNode("func() {}");
    Scope returnedScope = sut.createScope(functionNode2, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var funcVar1 = returnedScope.getVar("func");
    Var funcVar2 = returnedScope.getVar("func");
    assertEquals(JSType.FUNCTION, funcVar1.type);
    assertEquals(JSType.FUNCTION, funcVar2.type);
  }

  @Test
  public void testLocalScopeWithDuplicateDeclarationOfAnonymousFunction() {
    Node functionNode1 = createFunctionNode("func = function() {}");
    Node functionNode2 = createFunctionNode("func = function() {}");
    Scope returnedScope = sut.createScope(functionNode2, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var funcVar1 = returnedScope.getVar("func");
    Var funcVar2 = returnedScope.getVar("func");
    assertEquals(JSType.FUNCTION, funcVar1.type);
    assertEquals(JSType.FUNCTION, funcVar2.type);
  }

  @Test
  public void testCatchScopeWithSameVariableDeclaration() {
    Node functionNode = createFunctionNode("func() { try { x = 1; } catch (e) { x = 2; } }");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    assertEquals(JSType.NUMBER, xVar.type);
  }

  @Test
  public void testCatchScopeWithDifferentVariableDeclarations() {
    Node functionNode = createFunctionNode("func() { try { x = 1; } catch (e) { y = 2; } }");
    Scope returnedScope = sut.createScope(functionNode, rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    Var yVar = returnedScope.getVar("y");
    assertEquals(JSType.NUMBER, xVar.type);
    assertEquals(JSType.NUMBER, yVar.type);
  }

  @Test(expected = JSError.class)
  public void testCatchScopeWithSameVariableDeclarationAndDifferentFunction() {
    Node functionNode1 = createFunctionNode("func1() { try { x = 1; } catch (e) { x = 2; } }");
    Node functionNode2 = createFunctionNode("func2() { try { x = 1; } catch (e) { x = 2; } }");
    sut.createScope(functionNode2, rootScope);
  }

  @Test
  public void testCatchScopeWithArgumentVariableShadowing() {
    Node functionNode = createFunctionNode("func(x) { try { x = 1; } catch (e) { x = 2; } }");
    assertThrows(JSError.class, () -> {
      sut.createScope(functionNode, rootScope);
    });
  }

  @Test
  public void testCatchScopeWithGlobalVariableShadowing() {
    Node scriptNode = createScriptNode("x = 1; func() { try { x = 2; } catch (e) { x = 3; } }");
    assertThrows(JSError.class, () -> {
      sut.createScope(createFunctionNode("func() {}"), rootScope);
    });
  }

  @Test
  public void testCatchScopeWithGlobalVariableDeclarationInCatchBlock() {
    Node scriptNode = createScriptNode("x = 1; func() { try { } catch (e) { x = 2; } }");
    Scope returnedScope = sut.createScope(createFunctionNode("func() {}"), rootScope);
    assertEquals(ScopeType.LOCAL, returnedScope.getType());
    Var xVar = returnedScope.getVar("x");
    assertEquals(JSType.NUMBER, xVar.type);
  }

  private Node createFunctionNode(String code) {
    return new CompilerOptions().parseTreeFactory().createProgramNodeFromString(code, null).getBody().getFirstChild();
  }

  private Node createScriptNode(String code) {
    return new CompilerOptions().parseTreeFactory().createProgramNodeFromString(code, null).getBody();
  }

  private JSDocInfo createJSDocInfo(DiagnosticType... suppressions) {
    JSDocInfo docInfo = new JSDocInfo();
    docInfo.setSuppressions(suppressions);
    return docInfo;
  }
}