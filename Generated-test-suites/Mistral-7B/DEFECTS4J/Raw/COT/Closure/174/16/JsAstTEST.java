import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeBranch.IS_BLOCK;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.jscomp.parser.tests.util.TestSourceFile.createSourceFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserConfig;
import com.google.javascript.rhino.Reporter;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class JsAstTest {

  @Test
  public void testJsAst_ValidJsFile() {
    String sourceCode = "function main() {\n" +
        "  var x = 1;\n" +
        "  return x + 2;\n" +
        "}";

    SourceFile sourceFile = createSourceFile("TestJsAst.js", sourceCode);
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    AbstractCompiler compiler = new Compiler();
    compiler.init(options);

    SourceAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Check if root node is script node
    assertEquals(Script(), root.getType());

    // Check if script contains a function node
    FunctionNode functionNode = findFunctionNodeByName("main", root);
    assertNotNull(functionNode);

    // Check if function node contains expected statements
    List<Node> statements = functionNode.body();
    assertNotNull(statements);
    assertEquals(3, statements.size());

    // Check first statement is a variable declaration
    VariableDeclarationNode variableDeclaration = (VariableDeclarationNode) statements.get(0);
    assertEquals("x", variableDeclaration.getName().getName());
    assertEquals(NUMBER_LITERAL, variableDeclaration.getInitialValue().getType());
    assertEquals(1, ((NumberLiteral) variableDeclaration.getInitialValue()).getValue().intValue());

    // Check second statement is an assignment
    AssignmentStatementNode assignmentStatement = (AssignmentStatementNode) statements.get(1);
    assertEquals("x", assignmentStatement.getLhs().getName().getName());
    assertEquals(NUMBER_LITERAL, assignmentStatement.getRhs().getType());
    assertEquals(2, ((NumberLiteral) assignmentStatement.getRhs()).getValue().intValue());

    // Check third statement is a return statement
    ReturnStatementNode returnStatement = (ReturnStatementNode) statements.get(2);
    assertEquals(RETURN_STATEMENT, returnStatement.getType());
    assertEquals(3, ((NumberLiteral) returnStatement.getExpression()).getValue().intValue());
  }

  @Test
  public void testJsAst_InvalidJsFile() {
    String sourceCode = "function main() {\n" +
        "  var x = 1;\n" +
        "  return x + 2;\n" +
        "}"; // Missing semicolon at the end of the file

    SourceFile sourceFile = createSourceFile("TestJsAst.js", sourceCode);
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    AbstractCompiler compiler = new Compiler();
    compiler.init(options);

    SourceAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Check if root node is script node
    assertEquals(Script(), root.getType());

    // Check if there is any compile error
    assertEquals(1, compiler.getDiagnostics().size());
    JSError error = compiler.getDiagnostics().get(0);
    assertEquals(AbstractCompiler.READ_ERROR, error.getCode());
    assertEquals("Unexpected end of input", error.getMessage());
  }

  @Test
  public void testJsAst_ParseFailure() {
    String sourceCode = "function main() {\n" +
        "  var x = 1;\n" +
        "  return x + 2;\n" +
        "} // Missing semicolon at the end of the function declaration";

    SourceFile sourceFile = createSourceFile("TestJsAst.js", sourceCode);
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    AbstractCompiler compiler = new Compiler();
    compiler.init(options);

    SourceAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Check if root node is script node
    assertEquals(Script(), root.getType());

    // Check if there is a dummy block node
    Node dummyBlock = findFirstChildWithType(root, IS_BLOCK);
    assertNotNull(dummyBlock);
  }

  private FunctionNode findFunctionNodeByName(String name, Node node) {
    if (FunctionDeclarationNode.class.isInstance(node)) {
      FunctionDeclarationNode functionDeclaration = (FunctionDeclarationNode) node;
      if (name.equals(functionDeclaration.getName().getName())) {
        return functionDeclaration;
      }
    }

    for (Node child : node.getChildren()) {
      FunctionNode result = findFunctionNodeByName(name, child);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  private Node findFirstChildWithType(Node node, int type) {
    for (Node child : node.getChildren()) {
      if (child.getType() == type) {
        return child;
      }
    }

    return null;
  }
}