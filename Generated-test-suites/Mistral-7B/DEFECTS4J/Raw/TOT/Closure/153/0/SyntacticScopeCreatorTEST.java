import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private SyntacticScopeCreator scopeCreator;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 scopeCreator = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScopeForFunction() {
 Node functionNode = createFunctionNode("function test() {}");
 Scope scope = scopeCreator.createScope(functionNode, null);
 assertEquals("test", scope.getVariables().get(0).getName());
 }

 @Test
 public void testCreateScopeForFunctionWithArguments() {
 Node functionNode = createFunctionNode("function test(x, y) {}");
 Scope scope = scopeCreator.createScope(functionNode, null);
 assertEquals("x", scope.getVariables().get(0).getName());
 assertEquals("y", scope.getVariables().get(1).getName());
 }

 @Test
 public void testCreateGlobalScope() {
 Node globalNode = createScriptNode("var x = 1, y = 2;");
 Scope scope = scopeCreator.createScope(globalNode, null);
 assertEquals(2, scope.getVariables().size());
 assertEquals("x", scope.getVariables().get(0).getName());
 assertEquals("y", scope.getVariables().get(1).getName());
 }

 @Test
 public void testCreateNestedScope() {
 Node parentNode = createScriptNode("var x = 1; function test() { var y = 2; }");
 Node childNode = NodeUtil.extractFunctionBody(parentNode);
 Scope parentScope = scopeCreator.createScope(parentNode, null);
 Scope childScope = scopeCreator.createScope(childNode, parentScope);
 assertEquals(2, childScope.getVariables().size());
 assertEquals("y", childScope.getVariables().get(0).getName());
 assertEquals("x", childScope.getParentScope().getVariables().get(0).getName());
 }

 @Test
 public void testCreateScopeWithDuplicateDeclaration() {
 Node parentNode = createScriptNode("var x = 1; var x = 2;");
 Node childNode = NodeUtil.extractStatementList(parentNode);
 Scope parentScope = scopeCreator.createScope(parentNode, null);
 Scope childScope = scopeCreator.createScope(childNode, parentScope);
 assertEquals(1, childScope.getVariables().size());
 assertNull(childScope.getVariables().get(0));
 assertTrue(compiler.hasError());
 assertEquals(
     DiagnosticType.JSC_VAR_MULTIPLY_DECLARED_ERROR,
     compiler.getDiagnostic(0).getType());
 }

 @Test
 public void testCreateScopeWithArgumentShadowing() {
 Node parentNode = createScriptNode("function test(x) { arguments.x = 2; }");
 Node childNode = NodeUtil.extractFunctionBody(parentNode);
 Scope parentScope = scopeCreator.createScope(parentNode, null);
 Scope childScope = scopeCreator.createScope(childNode, parentScope);
 assertEquals(1, childScope.getVariables().size());
 assertNull(childScope.getVariables().get(0));
 assertTrue(compiler.hasError());
 assertEquals(
     DiagnosticType.JSC_VAR_ARGUMENTS_SHADOWED_ERROR,
     compiler.getDiagnostic(0).getType());
 }

 @Test
 public void testCreateScopeWithDuplicateDeclarationInParent() {
 Node parentNode = createScriptNode("var x = 1;\nfunction test() { var x = 2; }");
 Node childNode = NodeUtil.extractFunctionBody(parentNode);
 Scope parentScope = scopeCreator.createScope(parentNode, null);
 Scope childScope = scopeCreator.createScope(childNode, parentScope);
 assertEquals(1, childScope.getVariables().size());
 assertNull(childScope.getVariables().get(0));
 assertTrue(compiler.hasError());
 assertEquals(
     DiagnosticType.JSC_VAR_MULTIPLY_DECLARED_ERROR,
     compiler.getDiagnostic(0).getType());
 }

 @Test
 public void testCreateScopeWithDuplicateDeclarationInParentAndChild() {
 Node parentNode = createScriptNode(
     "var x = 1;\nfunction test() { var x = 2; }");
 Node childNode = NodeUtil.extractFunctionBody(parentNode);
 Scope parentScope = scopeCreator.createScope(parentNode, null);
 Scope childScope = scopeCreator.createScope(childNode, parentScope);
 assertEquals(1, childScope.getVariables().size());
 assertNull(childScope.getVariables().get(0));
 assertEquals(1, parentScope.getVariables().size());
 assertNull(parentScope.getVariables().get(0));
 assertTrue(compiler.hasError());
 assertEquals(
     DiagnosticType.JSC_VAR_MULTIPLY_DECLARED_ERROR,
     compiler.getDiagnostic(0).getType());
 }

 private Node createFunctionNode(String script) {
 return NodeUtil.parseTree(script, null, null).getFirstChild();
 }

 private Node createScriptNode(String script) {
 return NodeUtil.parseTree(script, new SourceName("test.js"), null).getFirstChild();
}
}