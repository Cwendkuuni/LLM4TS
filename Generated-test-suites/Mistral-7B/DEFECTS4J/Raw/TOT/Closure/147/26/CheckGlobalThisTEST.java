import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CheckGlobalThis checker;

 @Before
 public void setup() {
 compiler = mock(AbstractCompiler.class);
 checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testSafeUsesOfThis() {
 Node prototypeMethod = new Node(Token.FUNCSIGN, null);
 prototypeMethod.setJSDocInfo(new JSDocInfo("prototypeMethod", null, new String[]{"@method"}));
 Node constructor = new Node(Token.FUNCSIGN, prototypeMethod);
 constructor.setJSDocInfo(new JSDocInfo("Constructor", null, new String[]{"@constructor"}));
 Node thisAnnotatedFunction = new Node(Token.FUNCSIGN, null);
 thisAnnotatedFunction.setJSDocInfo(new JSDocInfo("thisAnnotatedFunction", null, new String[]{"@this"}));
 Node logicalPlaceFunction = new Node(Token.FUNCSIGN, null);
 logicalPlaceFunction.setJSDocInfo(new JSDocInfo("logicalPlaceFunction", null, new String[0]));

 Node blockTraversal = new Node(Token.BLOCK, null);
 Node scriptTraversal = new Node(Token.SCRIPT, blockTraversal);
 Node nameTraversal = new Node(Token.NAME, "name", logicalPlaceFunction);
 Node assignTraversal = new Node(Token.ASSIGN, nameTraversal, thisAnnotatedFunction);
 Node assignPrototypeTraversal = new Node(Token.ASSIGN, prototypeMethod, constructor);
 Node assignPrototypeSubPropertyTraversal = new Node(Token.ASSIGN, prototypeMethod, new Node(Token.GETPROP, prototypeMethod, new Node(Token.STRING, "prototype")));
 Node assignWithGetPropertyTraversal = new Node(Token.ASSIGN, new Node(Token.GETPROP, new Node(Token.NAME, "parent"), new Node(Token.STRING, "prototype")), constructor);

 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.getParent()).thenReturn(scriptTraversal);
 when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.UNDEFINED);

 checker.visit(traversal, assignLhsChild, assignTraversal);
 checker.visit(traversal, assignLhsChild, assignPrototypeTraversal);
 checker.visit(traversal, assignLhsChild, assignPrototypeSubPropertyTraversal);
 checker.visit(traversal, assignLhsChild, assignWithGetPropertyTraversal);

 checker.visit(traversal, assignLhsChild, constructor);
 checker.visit(traversal, assignLhsChild, prototypeMethod);
 checker.visit(traversal, assignLhsChild, logicalPlaceFunction);
 checker.visit(traversal, assignLhsChild, thisAnnotatedFunction);

 checker.shouldHaveNoDiagnostics();
 }

 @Test
 public void testUnsafeUsesOfThis() {
 Node unsafeFunction = new Node(Token.FUNCSIGN, null);
 Node assignLhs = new Node(Token.ASSIGN, unsafeFunction, Node.UNDEFINED);
 Node assignRhs = new Node(Token.THIS, Node.UNDEFINED);

 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.getParent()).thenReturn(Node.UNDEFINED);
 when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.UNDEFINED);

 checker.visit(traversal, assignLhsChild, assignLhs);
 checker.visit(traversal, assignLhsChild, assignRhs);

 checker.shouldHaveOneWarning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object", assignRhs);
 }

 @Test
 public void testGlobalThisInConditional() {
 Node conditional = new Node(Token.IF, new Node(Token.THIS, Node.UNDEFINED), new Node(Token.BLOCK, Node.UNDEFINED));
 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.getParent()).thenReturn(Node.UNDEFINED);
 when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.UNDEFINED);

 checker.visit(traversal, assignLhsChild, conditional);

 checker.shouldHaveNoDiagnostics();
 }

 @Test
 public void testThisAnnotatedFunctionInsideAnotherFunction() {
 Node outerFunction = new Node(Token.FUNCSIGN, null);
 outerFunction.setJSDocInfo(new JSDocInfo("outerFunction", null, new String[0]));
 Node innerFunction = new Node(Token.FUNCSIGN, null);
 innerFunction.setJSDocInfo(new JSDocInfo("innerFunction", null, new String[]{"@this"}));
 Node thisAnnotatedFunctionCall = new Node(Token.CALL, innerFunction, Node.UNDEFINED);

 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.getParent()).thenReturn(outerFunction);
 when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.UNDEFINED);

 checker.visit(traversal, assignLhsChild, thisAnnotatedFunctionCall);

 checker.shouldHaveNoDiagnostics();
 }

 @Test
 public void testThisAnnotatedFunctionWithArguments() {
 Node thisAnnotatedFunction = new Node(Token.FUNCSIGN, null);
 thisAnnotatedFunction.setJSDocInfo(new JSDocInfo("thisAnnotatedFunction", null, new String[]{"@this"}));
 Node arg1 = new Node(Token.NAME, "arg1");
 Node arg2 = new Node(Token.NAME, "arg2");
 Node assign = new Node(Token.ASSIGN, thisAnnotatedFunction, arg1);

 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.getParent()).thenReturn(Node.UNDEFINED);
 when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(Node.UNDEFINED);

 checker.visit(traversal, assignLhsChild, assign);

 checker.shouldHaveNoDiagnostics();
 }
}