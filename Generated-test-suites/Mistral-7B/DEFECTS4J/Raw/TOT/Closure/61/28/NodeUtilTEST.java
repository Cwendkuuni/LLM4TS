import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.*;

import java.io.IOException;

public class NodeUtilTest {

  @Test
  public void testIsForIn() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
            "  for (var i in [1, 2, 3]) {\n" +
            "    // Do something with i\n" +
            "  }\n" +
            "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node forInNode = ast.getChild(0).getFirstChild();
    assertTrue(isForIn(forInNode));
  }

  @Test
  public void testIsStatement_StatementNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
            "  if (true) {\n" +
            "    // Do something\n" +
            "  }\n" +
            "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node ifStatementNode = ast.getChild(0).getFirstChild();
    assertTrue(isStatement(ifStatementNode));
  }

  @Test
  public void testIsStatement_ExpressionNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
            "  return 1 + 1;\n" +
            "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node returnNode = ast.getChild(0).getLastChild();
    assertFalse(isStatement(returnNode));
  }

  @Test
  public void testIsFunctionDeclaration_FunctionDeclarationNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
        "  // Do something\n" +
        "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node functionDeclarationNode = ast.getChild(0);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclaration_FunctionExpressionNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
        "}(function() {\n" +
        "  // Do something\n" +
        "})()");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node functionExpressionNode = ast.getChild(0).getLastChild();
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_HoistedFunctionDeclarationNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
        "  function bar() {\n" +
        "    // Do something\n" +
        "  }\n" +
        "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node hoistedFunctionDeclarationNode = ast.getChild(0).getChild(0);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NonHoistedFunctionDeclarationNode() throws IOException {
    SourceFile nodeSource = TestCompiler.getSourceFile("TestNodeUtil.js",
        "function foo() {\n" +
        "  function bar() {\n" +
        "    // Do something\n" +
        "  }\n" +
        "  var baz = 0;\n" +
        "}");
    Node ast = RhinoEngine.getScriptContext().compile(nodeSource);
    Node nonHoistedFunctionDeclarationNode = ast.getChild(0).getLastChild();
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}