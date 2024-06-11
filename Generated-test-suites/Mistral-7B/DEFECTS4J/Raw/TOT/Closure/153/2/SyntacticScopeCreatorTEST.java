import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.ScopeCreator.RedeclarationHandler;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.NodeMatcher;
import static com.google.javascript.rhino.JSDocInfo.SuppressedSuppressions.*;
import static com.google.common.collect.ImmutableList.of;

public class SyntacticScopeCreatorTest {

 private ScopeCreator creator;
 private AbstractCompiler compiler;
 private Scope rootScope;

 @Before
 public void setUp() {
 compiler = new TestCompiler();
 creator = new SyntacticScopeCreator(compiler);
 rootScope = creator.createScope(null, null);
 }

 @Test
 public void testCreateGlobalScope() {
 Scope globalScope = creator.createScope(
     NodeFactory.createScript("var x = 1;", "test.js"), null);

 assertSame(rootScope, globalScope.getParent());
 assertFalse(globalScope.isLocal());
 assertEquals(1, globalScope.size());
 assertEquals("x", globalScope.getVars().get(0).getName());
 assertEquals(JSType.NUMBER, globalScope.getVars().get(0).getType());
 }

 @Test
 public void testCreateFunctionScope() {
 Node functionNode = NodeFactory.createFunction("function foo() {}", "test.js");
 Scope functionScope = creator.createScope(functionNode, rootScope);

 assertEquals("foo", functionScope.getName());
 assertEquals(rootScope, functionScope.getParent());
 assertFalse(functionScope.isGlobal());
 assertEquals(0, functionScope.size());
 }

 @Test
 public void testCreateFunctionWithArguments() {
 Node functionNode = NodeFactory.createFunction("function foo(x) {}", "test.js");
 Scope functionScope = creator.createScope(functionNode, rootScope);

 assertEquals("foo", functionScope.getName());
 assertEquals(rootScope, functionScope.getParent());
 assertFalse(functionScope.isGlobal());
 assertEquals(1, functionScope.size());
 assertEquals("x", functionScope.getVars().get(0).getName());
 assertEquals(JSType.UNDEFINED, functionScope.getVars().get(0).getType());
 }

 @Test
 public void testCreateFunctionWithVariables() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x, y) {\n" +
     "  var z = 1;\n" +
     "  var x = 2;\n" +
     "}", "test.js");
 Scope functionScope = creator.createScope(functionNode, rootScope);

 assertEquals("foo", functionScope.getName());
 assertEquals(rootScope, functionScope.getParent());
 assertFalse(functionScope.isGlobal());
 assertEquals(3, functionScope.size());
 assertEquals("x", functionScope.getVars().get(1).getName());
 assertEquals(JSType.NUMBER, functionScope.getVars().get(1).getType());
 assertEquals("z", functionScope.getVars().get(2).getName());
 assertEquals(JSType.NUMBER, functionScope.getVars().get(2).getType());
 }

 @Test
 public void testCreateBlockScope() {
 Node blockNode = NodeFactory.createBlock("if (true) { var x = 1; }", "test.js");
 Scope blockScope = creator.createScope(blockNode, rootScope);

 assertEquals(rootScope, blockScope.getParent());
 assertFalse(blockScope.isGlobal());
 assertEquals(1, blockScope.size());
 assertEquals("x", blockScope.getVars().get(0).getName());
 assertEquals(JSType.NUMBER, blockScope.getVars().get(0).getType());
 }

 @Test
 public void testDeclareDuplicateVariableInGlobalScope() {
 Node dupeNode = NodeFactory.createVariable("var x = 1;", "test.js");
 creator.declareVar("x", dupeNode, null, null, null, dupeNode);

 Scope globalScope = creator.createScope(
     NodeFactory.createVariable("var x = 2;", "test.js"), null);
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);

 creator.declareVar("x", dupeNode, null, null, null, dupeNode);
 verify(handler).onRedeclaration(
     eq(rootScope), eq("x"), eq(dupeNode), eq(null), eq(null), eq(dupeNode));
 }

 @Test
 public void testDeclareDuplicateVariableInFunctionScope() {
 Node dupeNode = NodeFactory.createVariable("var x = 1;", "test.js");
 Node functionNode = NodeFactory.createFunction("function foo() { var x = 2; }", "test.js");
 Scope functionScope = creator.createScope(functionNode, rootScope);
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", dupeNode, null, functionScope, null, dupeNode);
 verify(handler).onRedeclaration(
     eq(functionScope), eq("x"), eq(dupeNode), eq(null), eq(rootScope), eq(dupeNode));
 }

 @Test
 public void testDeclareDuplicateVariableInBlockScope() {
 Node dupeNode = NodeFactory.createVariable("var x = 1;", "test.js");
 Node blockNode = NodeFactory.createBlock(
     "if (true) { var x = 2; }", "test.js");
 Scope blockScope = creator.createScope(blockNode, rootScope);
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", dupeNode, null, blockScope, null, dupeNode);
 verify(handler).onRedeclaration(
     eq(blockScope), eq("x"), eq(dupeNode), eq(null), eq(rootScope), eq(dupeNode));
 }

 @Test
 public void testDeclareDuplicateVariableWithArgumentsShadowing() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x) {\n" +
     "  var x = 2;\n" +
     "}", "test.js");
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", NodeFactory.createVariable("var x = 1;", "test.js"), null, functionNode, null, null);
 verify(handler).onRedeclaration(
     eq(functionNode.getFirstChild()), eq("x"), eq(NodeFactory.createVariable("var x = 1;", "test.js")),
     eq(functionNode), eq(rootScope), eq(NodeFactory.createVariable("var x = 1;", "test.js"))) ;
 }

 @Test
 public void testDeclareDuplicateVariableWithArgumentsAndFunction() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x) {\n" +
     "  function bar() {\n" +
     "    var x = 2;\n" +
     "  }\n" +
     "}", "test.js");
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", NodeFactory.createVariable("var x = 1;", "test.js"), null, functionNode, null, null);
 verify(handler).onRedeclaration(
     eq(functionNode), eq("x"), eq(NodeFactory.createVariable("var x = 1;", "test.js")),
     eq(functionNode.getFirstChild().getFirstChild()), eq(rootScope), eq(NodeFactory.createVariable("var x = 1;", "test.js"))) ;
 }

 @Test
 public void testDeclareDuplicateVariableWithArgumentsAndBlock() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x) {\n" +
     "  if (true) {\n" +
     "    var x = 2;\n" +
     "  }\n" +
     "}", "test.js");
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", NodeFactory.createVariable("var x = 1;", "test.js"), null, functionNode, null, null);
 verify(handler).onRedeclaration(
     eq(functionNode), eq("x"), eq(NodeFactory.createVariable("var x = 1;", "test.js")),
     eq(functionNode.getFirstChild().getFirstChild().getFirstChild()), eq(rootScope), eq(NodeFactory.createVariable("var x = 1;", "test.js"))) ;
 }

 @Test
 public void testDeclareDuplicateVariableWithSuppressedDuplicate() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x) {\n" +
     "  /**\n" +
     "   * @suppress {duplicate}\n" +
     "   */\n" +
     "  var x = 2;\n" +
     "}", "test.js");
 RedeclarationHandler handler = mock(RedeclarationHandler.class);
 creator = new SyntacticScopeCreator(compiler, handler);
 creator.declareVar("x", NodeFactory.createVariable("var x = 1;", "test.js"), null, functionNode, null, null);
 verifyZeroInteractions(handler);
 }

 @Test
 public void testDeclareVariableWithJSDocType() {
 Node functionNode = NodeFactory.createFunction(
     "function foo(x:number) {\n" +
     "  var y:string = 1;\n" +
     "}", "test.js");
 Scope functionScope = creator.createScope(functionNode, rootScope);
 assertEquals(2, functionScope.size());
 assertEquals("x", functionScope.getVars().get(0).getName());
 assertEquals(JSType.OBJECT, functionScope.getVars().get(0).getType());
 assertEquals("y", functionScope.getVars().get(1).getName());
 assertEquals(JSType.STRING, functionScope.getVars().get(1).getType());
 }
}