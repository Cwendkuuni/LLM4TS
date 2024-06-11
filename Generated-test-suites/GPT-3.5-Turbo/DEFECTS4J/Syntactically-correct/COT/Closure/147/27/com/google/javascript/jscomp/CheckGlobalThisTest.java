
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private NodeTraversal.Callback callback;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    callback = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructor_NoThisAnnotation_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    boolean result = callback.shouldTraverse(t, functionNode, null);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ThisAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordConstructor();
    functionNode.setJSDocInfo(jsDocBuilder.build());

    boolean result = callback.shouldTraverse(t, functionNode, null);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_InterfaceAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordInterface();
    functionNode.setJSDocInfo(jsDocBuilder.build());

    boolean result = callback.shouldTraverse(t, functionNode, null);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThisAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordThisType();
    functionNode.setJSDocInfo(jsDocBuilder.build());

    boolean result = callback.shouldTraverse(t, functionNode, null);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordOverride();
    functionNode.setJSDocInfo(jsDocBuilder.build());

    boolean result = callback.shouldTraverse(t, functionNode, null);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotAssignableToThisNames_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToBack(nameNode);

    Node blockNode = new Node(Token.BLOCK);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(blockNode);

    boolean result = callback.shouldTraverse(t, functionNode, blockNode);

    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssignment_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(thisNode);

    boolean result = callback.shouldReportThis(thisNode, assignNode);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);

    boolean result = callback.shouldReportThis(thisNode, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisWithoutPropertyAccess_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    boolean result = callback.shouldReportThis(thisNode, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_ThisAndShouldReportThis_CallsCompilerReport() {
    NodeTraversal t = new NodeTraversal(compiler, callback);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(thisNode);

    callback.visit(t, thisNode, parent);

    List<DiagnosticType> errors = compiler.getErrors();

    assertEquals(1, errors.size());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, errors.get(0));
  }
}
