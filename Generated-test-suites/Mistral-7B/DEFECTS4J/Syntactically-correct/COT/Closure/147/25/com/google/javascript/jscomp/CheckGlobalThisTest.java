import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeTraversal traverser;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler();
    checker = new Checker(compiler, options);
    traverser = new NodeTraversal(checker, new Parser().parse(""));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getJSDocInfo()).thenReturn(createFunctionJsDocInfo("constructor"));
    Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = new CheckGlobalThis(compiler, WARNING).shouldTraverse(traverser, node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getJSDocInfo()).thenReturn(createFunctionJsDocInfo("this"));
    Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = new CheckGlobalThis(compiler, WARNING).shouldTraverse(traverser, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = new CheckGlobalThis(compiler, WARNING).shouldTraverse(traverser, node, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideScriptContext() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(parent.getType()).thenReturn(Token.VAR);

    boolean result = new CheckGlobalThis(compiler, WARNING).shouldTraverse(traverser, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionInsideAssignment() {
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(left.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(right.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(parent.getFirstChild()).thenReturn(left);
    Mockito.when(parent.getLastChild()).thenReturn(right);

    boolean result = new CheckGlobalThis(compiler, WARNING).shouldTraverse(traverser, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(left.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(right.getType()).thenReturn(Token.THIS);
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(parent.getFirstChild()).thenReturn(left);
    Mockito.when(parent.getLastChild()).thenReturn(right);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.visit(traverser, node, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(left.getType()).thenReturn(Token.GETPROP);
    Mockito.when(left.getLastChild().getString()).thenReturn("this");
    Mockito.when(right.getType()).thenReturn(Token.THIS);
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parent.getType()).thenReturn(Token.GETPROP);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.visit(traverser, node, parent);
    assertTrue(compiler.hasErrors());
  }

  private JSDocInfo createFunctionJsDocInfo(String annotation) {
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.isConstructor()).thenReturn(annotation.equals("constructor"));
    Mockito.when(jsDoc.isInterface()).thenReturn(false);
    Mockito.when(jsDoc.hasThisType()).thenReturn(annotation.equals("this"));
    return jsDoc;
  }
}