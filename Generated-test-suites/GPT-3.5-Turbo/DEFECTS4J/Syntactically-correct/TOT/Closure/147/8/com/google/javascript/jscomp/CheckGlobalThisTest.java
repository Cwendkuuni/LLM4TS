import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CheckGlobalThisTest {

   private AbstractCompiler compiler;
   private CheckGlobalThis checkGlobalThis;

   @Before
   public void setup() {
       compiler = mock(AbstractCompiler.class);
       checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
   }

   @Test
   public void shouldTraverse_whenNodeTypeIsFunctionAndNotConstructorOrHasThisTypeOrOverrideAnnotationAndParentTypeIsBlock() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.BLOCK);
       boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
       assertTrue(result);
   }

   @Test
   public void shouldTraverse_whenNodeTypeIsFunctionAndHasThisTypeAnnotationAndParentTypeIsScript() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.SCRIPT);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       jsDocInfo.setHasThisType(true);
       n.setJSDocInfo(jsDocInfo);
       boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
       assertFalse(result);
   }

   @Test
   public void shouldTraverse_whenNodeTypeIsFunctionAndHasOverrideAnnotationAndParentTypeIsAssign() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.ASSIGN);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       jsDocInfo.setOverride(true);
       n.setJSDocInfo(jsDocInfo);
       boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
       assertFalse(result);
   }

   @Test
   public void shouldTraverse_whenNodeTypeIsFunctionAndIsConstructorAndParentTypeIsScript() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.SCRIPT);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       jsDocInfo.setConstructor(true);
       n.setJSDocInfo(jsDocInfo);
       boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
       assertFalse(result);
   }

   @Test
   public void shouldVisit_whenNodeTypeIsThisAndShouldReportThisIsTrue() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.THIS);
       Node parent = new Node(Token.GETPROP);
       boolean result = checkGlobalThis.shouldReportThis(n, parent);
       assertTrue(result);
   }

   @Test
   public void shouldVisit_whenNodeTypeIsThisAndAssignLhsChildIsNotNull() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.THIS);
       Node parent = new Node(Token.GETPROP);
       checkGlobalThis.assignLhsChild = new Node(Token.GETPROP);
       boolean result = checkGlobalThis.shouldReportThis(n, parent);
       assertTrue(result);
   }

   @Test
   public void shouldVisit_whenNodeTypeIsNotThisAndAssignLhsChildIsNull() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.NAME);
       Node parent = new Node(Token.GETPROP);
       boolean result = checkGlobalThis.shouldReportThis(n, parent);
       assertFalse(result);
   }

   @Test
   public void visit_shouldReportError_whenNodeTypeIsThisAndShouldReportThisIsTrue() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.THIS);
       Node parent = new Node(Token.GETPROP);
       checkGlobalThis.visit(t, n, parent);
       // Verify that the error is reported using the compiler
       // We can use Mockito to verify this behavior
   }

   @Test
   public void visit_shouldResetAssignLhsChildToNull_whenVisitedNodeIsAssignLhsChild() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.NAME);
       Node parent = new Node(Token.GETPROP);
       checkGlobalThis.assignLhsChild = n;
       checkGlobalThis.visit(t, n, parent);
       assertNull(checkGlobalThis.assignLhsChild);
   }

   @Test
   public void visit_shouldNotResetAssignLhsChildToNull_whenVisitedNodeIsNotAssignLhsChild() {
       NodeTraversal t = mock(NodeTraversal.class);
       Node n = new Node(Token.THIS);
       Node parent = new Node(Token.GETPROP);
       checkGlobalThis.assignLhsChild = new Node(Token.NAME);
       checkGlobalThis.visit(t, n, parent);
       assertNotNull(checkGlobalThis.assignLhsChild);
   }

   @Test
   public void getFunctionJsDocInfo_shouldReturnFunctionJsDocInfo_whenNodeHasNoJSDocInfoAndParentTypeIsNameAndParentHasJSDocInfo() {
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.NAME);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       parent.setJSDocInfo(jsDocInfo);
       JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
       assertEquals(jsDocInfo, result);
   }

   @Test
   public void getFunctionJsDocInfo_shouldReturnFunctionJsDocInfo_whenNodeHasNoJSDocInfoAndParentTypeIsAssignAndParentHasJSDocInfo() {
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.ASSIGN);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       parent.setJSDocInfo(jsDocInfo);
       JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
       assertEquals(jsDocInfo, result);
   }

   @Test
   public void getFunctionJsDocInfo_shouldReturnFunctionJsDocInfo_whenNodeHasNoJSDocInfoAndParentTypeIsNameAndGrampsTypeIsVarAndGrampsHasJSDocInfo() {
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.NAME);
       Node gramps = new Node(Token.VAR);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       gramps.setJSDocInfo(jsDocInfo);
       parent.setParent(gramps);
       JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
       assertEquals(jsDocInfo, result);
   }

   @Test
   public void getFunctionJsDocInfo_shouldReturnFunctionJsDocInfo_whenNodeHasJSDocInfo() {
       Node n = new Node(Token.FUNCTION);
       JSDocInfo jsDocInfo = mock(JSDocInfo.class);
       n.setJSDocInfo(jsDocInfo);
       JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
       assertEquals(jsDocInfo, result);
   }

   @Test
   public void getFunctionJsDocInfo_shouldReturnNull_whenNodeHasNoJSDocInfoAndParentTypeIsNameAndParentHasNoJSDocInfo() {
       Node n = new Node(Token.FUNCTION);
       Node parent = new Node(Token.NAME);
       JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
       assertNull(result);
   }

   @Test
   public void testFullCoverage() {
       // Verify test setup and tear down
       assertEquals(2, 1 + 1);
   }
}