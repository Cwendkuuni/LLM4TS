import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticContext;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private DiagnosticContext diagnosticContext;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    diagnosticContext = Mockito.mock(DiagnosticContext.class);
    traversal = Mockito.mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunctions() {
    Node globalFunction = Mockito.mock(Node.class);
    Node globalFunctionParent = Mockito.mock(Node.class);
    Mockito.when(globalFunction.getParent()).thenReturn(globalFunctionParent);
    Mockito.when(globalFunctionParent.getType()).thenReturn(Token.SCRIPT);
    Mockito.when(globalFunction.getType()).thenReturn(FUNCTION);

    assertTrue(checker.shouldTraverse(traversal, globalFunction, globalFunctionParent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    Node constructorFunction = Mockito.mock(Node.class);
    Node constructorFunctionParent = Mockito.mock(Node.class);
    Mockito.when(constructorFunction.getJSDocInfo()).thenReturn(
        JSDocInfo.newConstructorInfo(null, null, null, null));
    Mockito.when(constructorFunctionParent.getType()).thenReturn(Token.SCRIPT);
    Mockito.when(constructorFunction.getType()).thenReturn(FUNCTION);

    assertFalse(checker.shouldTraverse(traversal, constructorFunction, constructorFunctionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    Node functionWithThisAnnotation = Mockito.mock(Node.class);
    Node functionWithThisAnnotationParent = Mockito.mock(Node.class);
    Mockito.when(functionWithThisAnnotation.getJSDocInfo()).thenReturn(
        JSDocInfo.newFunctionInfo(null, null, null, null, "@this"));
    Mockito.when(functionWithThisAnnotationParent.getType()).thenReturn(Token.SCRIPT);
    Mockito.when(functionWithThisAnnotation.getType()).thenReturn(FUNCTION);

    assertFalse(checker.shouldTraverse(traversal, functionWithThisAnnotation, functionWithThisAnnotationParent));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithOverrideAnnotation() {
    Node functionWithOverrideAnnotation = Mockito.mock(Node.class);
    Node functionWithOverrideAnnotationParent = Mockito.mock(Node.class);
    Mockito.when(functionWithOverrideAnnotation.getJSDocInfo()).thenReturn(
        JSDocInfo.newFunctionInfo(null, null, null, null, "@override"));
    Mockito.when(functionWithOverrideAnnotationParent.getType()).thenReturn(Token.SCRIPT);
    Mockito.when(functionWithOverrideAnnotation.getType()).thenReturn(FUNCTION);

    assertFalse(checker.shouldTraverse(traversal, functionWithOverrideAnnotation, functionWithOverrideAnnotationParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionWithNoLogicalPlaceForThisAnnotation = Mockito.mock(Node.class);
    Node functionWithNoLogicalPlaceForThisAnnotationParent = Mockito.mock(Node.class);
    Mockito.when(functionWithNoLogicalPlaceForThisAnnotationParent.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(functionWithNoLogicalPlaceForThisAnnotation.getType()).thenReturn(FUNCTION);

    assertTrue(checker.shouldTraverse(traversal, functionWithNoLogicalPlaceForThisAnnotation, functionWithNoLogicalPlaceForThisAnnotationParent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assign = Mockito.mock(Node.class);
    Node assignLhs = Mockito.mock(Node.class);
    Node assignRhs = Mockito.mock(Node.class);
    Mockito.when(assign.getType()).thenReturn(ASSIGN);
    Mockito.when(assign.getFirstChild()).thenReturn(assignLhs);
    Mockito.when(assignLhs.getType()).thenReturn(TOKEN);
    Mockito.when(assignLhs.getString()).thenReturn("this");
    Mockito.when(assignRhs.getType()).thenReturn(TOKEN);

    assertTrue(checker.shouldReportThis(assignLhs, assign));
  }

  @Test
  public void testShouldNotTraversePrototypePropertyOrSubproperty() {
    Node assign = Mockito.mock(Node.class);
    Node assignLhs = Mockito.mock(Node.class);
    Node assignRhs = Mockito.mock(Node.class);
    Node getProp = Mockito.mock(Node.class);
    Mockito.when(assign.getType()).thenReturn(ASSIGN);
    Mockito.when(assign.getFirstChild()).thenReturn(assignLhs);
    Mockito.when(assignLhs.getType()).thenReturn(TOKEN);
    Mockito.when(assignLhs.getString()).thenReturn("this");
    Mockito.when(assignRhs.getType()).thenReturn(TOKEN);
    Mockito.when(assignRhs.getString()).thenReturn("property");
    Mockito.when(assignLhs.getLastChild()).thenReturn(getProp);
    Mockito.when(getProp.getType()).thenReturn(GETPROP);
    Mockito.when(getProp.getLastChild().getString()).thenReturn("prototype");

    assertFalse(checker.shouldReportThis(assignLhs, assign));
  }

  @Test
  public void testShouldTraverseAssignmentToGlobalVar() {
    Node assign = Mockito.mock(Node.class);
    Node assignLhs = Mockito.mock(Node.class);
    Node assignRhs = Mockito.mock(Node.class);
    Node varDecl = Mockito.mock(Node.class);
    Node varName = Mockito.mock(Node.class);
    Mockito.when(assign.getType()).thenReturn(ASSIGN);
    Mockito.when(assign.getFirstChild()).thenReturn(assignLhs);
    Mockito.when(assign.getLastChild()).thenReturn(assignRhs);
    Mockito.when(assignLhs.getType()).thenReturn(TOKEN);
    Mockito.when(assignLhs.getString()).thenReturn("this");
    Mockito.when(assignRhs.getType()).thenReturn(TOKEN);
    Mockito.when(assignRhs.getString()).thenReturn("variableName");
    Mockito.when(assign.getParent().getType()).thenReturn(Token.VAR);
    Mockito.when(assign.getParent().getLastChild()).thenReturn(varDecl);
    Mockito.when(varDecl.getFirstChild()).thenReturn(varName);
    Mockito.when(varName.getType()).thenReturn(TOKEN);
    Mockito.when(varName.getString()).thenReturn("variableName");

    assertTrue(checker.shouldReportThis(assignLhs, assign));
  }

  @Test(expected = AssertionError.class)
  public void testShouldThrowExceptionForNullParent() {
    Node nullParent = Mockito.mock(Node.class);
    Node thisNode = Mockito.mock(Node.class);
    Mockito.when(thisNode.getType()).thenReturn(TOKEN);
    Mockito.when(thisNode.getString()).thenReturn("this");

    checker.shouldReportThis(thisNode, nullParent);
  }
}