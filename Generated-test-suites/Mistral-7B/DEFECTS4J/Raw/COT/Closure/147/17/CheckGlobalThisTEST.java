import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

 private CheckGlobalThis checkGlobalThis;
 private AbstractCompiler compiler;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 compiler = new AbstractCompiler(new CompilerOptions());
 checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
 }

 @Test
 public void testShouldTraverseFunctions() {
 Node functionNode = new Node(FUNCTION, new Object[] {});
 JSDocInfo jsDocInfo = new JSDocInfo("testFunction");
 functionNode.setJSDocInfo(jsDocInfo);

 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING), parentNode);
 checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode);
 assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseConstructorFunctions() {
 Node constructorFunctionNode = new Node(FUNCTION, new Object[] { CONSTRUCTOR }, new Object[] {});
 JSDocInfo jsDocInfo = new JSDocInfo("constructorTestFunction");
 constructorFunctionNode.setJSDocInfo(jsDocInfo);

 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(constructorFunctionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, constructorFunctionNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseFunctionsWithThisAnnotation() {
 Node thisFunctionNode = new Node(FUNCTION, new Object[] { THIS }, new Object[] {});
 JSDocInfo jsDocInfo = new JSDocInfo("thisTestFunction", new String[] { "@this" });
 thisFunctionNode.setJSDocInfo(jsDocInfo);

 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(thisFunctionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, thisFunctionNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseFunctionsWithOverrideAnnotation() {
 Node overrideFunctionNode = new Node(FUNCTION, new Object[] {}, new Object[] { OVERRIDE });
 JSDocInfo jsDocInfo = new JSDocInfo("overrideTestFunction");
 overrideFunctionNode.setJSDocInfo(jsDocInfo);

 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(overrideFunctionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, overrideFunctionNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseFunctionsInsideBlocks() {
 Node functionNode = new Node(FUNCTION, new Object[] {}, new Object[] {});
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldTraverseAssignments() {
 Node assignNode = new Node(ASSIGN, new Object[] { THIS, new Node(IDENT, "x") });
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertTrue(checkGlobalThis.shouldTraverse(traversal, assignNode, parentNode));
 }

 @Test
 public void testShouldTraverseGetProperties() {
 Node getPropertyNode = new Node(GETPROP, new Object[] { THIS, new Node(IDENT, "prototype") });
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(getPropertyNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertTrue(checkGlobalThis.shouldTraverse(traversal, getPropertyNode, parentNode));
 }

 @Test
 public void testShouldTraverseGetPropertiesWithNestedGetProperties() {
 Node getPropertyNode = new Node(GETPROP, new Object[] { THIS, new Node(IDENT, "prototype") });
 Node nestedGetPropertyNode = new Node(GETPROP, new Object[] { getPropertyNode, new Node(IDENT, "prototype") });
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(getPropertyNode);
 getPropertyNode.addChild(nestedGetPropertyNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertTrue(checkGlobalThis.shouldTraverse(traversal, nestedGetPropertyNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseGetPropertiesWithPrototypeProperty() {
 Node getPropertyNode = new Node(GETPROP, new Object[] { THIS, new Node(IDENT, "prototype") });
 Node prototypePropertyNode = new Node(IDENT, "prototype");
 prototypePropertyNode.setProperty("name", "prototypeProperty");
 getPropertyNode.setLastChild(prototypePropertyNode);
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(getPropertyNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, getPropertyNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseAssignmentsWithPrototypeProperties() {
 Node assignNode = new Node(ASSIGN, new Object[] { THIS, new Node(GETPROP, new Object[] { THIS, new Node(IDENT, "prototype") }, new Object[] { IDENT, "x" }) });
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 assertFalse(checkGlobalThis.shouldTraverse(traversal, assignNode, parentNode));
 }

 @Test
 public void testShouldReportGlobalThis() {
 Node globalThisNode = new Node(THIS, new Object[] {});
 Node parentNode = new Node(BLOCK, new Object[] {});
 parentNode.addChild(globalThisNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 checkGlobalThis.visit(traversal, globalThisNode, parentNode);
 assertEquals(1, compiler.getDiagnostics().size());
 DiagnosticPosition position = compiler.getDiagnostics().get(0).getPosition();
 assertEquals(globalThisNode.getStartPosition().line, position.getLine());
 assertEquals(globalThisNode.getStartPosition().column, position.getColumn());
 assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
 }

 @Test
 public void testShouldNotReportGlobalThisInsideFunction() {
 Node functionNode = new Node(FUNCTION, new Object[] {}, new Object[] {});
 Node globalThisNode = new Node(THIS, new Object[] {});
 Node parentNode = new Node(BLOCK, new Object[] { functionNode });
 functionNode.addChild(new Node(STMT, new Object[] { globalThisNode }));
 parentNode.addChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, parentNode);
 checkGlobalThis.visit(traversal, globalThisNode, parentNode);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test(expected = AssertionError.class)
 public void testShouldReportErrorWhenCheckingNullNode() {
 NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis, null);
 checkGlobalThis.shouldTraverse(traversal, null, null);
 }

}