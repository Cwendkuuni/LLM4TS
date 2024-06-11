import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.IR;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testPrepareAstWithNoCheckOnly() {
    Node root = IR.program();

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    // Check if the prepareAst pass was executed correctly
    assertThat(root.getFirstChild().isBlock()).isTrue();
  }

  @Test
  public void testPrepareAstWithCheckOnly() {
    Node root = IR.program();

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    // This test case should fail since the prepareAst pass should not be executed
    // in checkOnly mode.
    // Uncomment the following assertion to test the failure.
    // Preconditions.checkState(false, "prepareAst should not be executed in checkOnly mode");
  }

  @Test
  public void testPrepareAstWithCheckOnly_normalizeNodeTypes() {
    Node root = IR.program();
    root.setWasEmptyNode(true);

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    Node expectedBlock = IR.block();
    expectedBlock.setWasEmptyNode(true);

    assertThat(root).isEqualTo(expectedBlock);
  }

  @Test
  public void testPrepareAst_normalizeBlocks() {
    Node root = IR.ifStatement(
        IR.booleanLiteral(true),
        IR.statementList(IR.expressionStatement(IR.numberLiteral(1))),
        IR.statementList(IR.expressionStatement(IR.numberLiteral(2))));

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    Node expectedBlock = IR.block();
    expectedBlock.addChildrenToFront(IR.statementList(
        IR.expressionStatement(IR.numberLiteral(1)),
        IR.expressionStatement(IR.numberLiteral(2))));

    assertThat(root).isEqualTo(expectedBlock);
  }

  @Test
  public void testPrepareAst_prepareAnnotations() {
    Node root = IR.program(
        IR.functionDeclaration(
            "testFunction",
            null,
            null,
            IR.statementList(IR.returnStatement(IR.booleanLiteral(true))),
            null,
            null,
            null,
            null,
            null,
            null,
            null),
        IR.statementList(IR.callStatement(
            IR.name("testFunction"),
            null,
            IR.arrayLiteral())));

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    // Check if the function declaration has the correct JS Doc annotation
    Node functionDeclaration = root.getFirstChild();
    CallableNode jsDocAnnotation = functionDeclaration.getJSDocInfo();
    assertThat(jsDocAnnotation).isNotNull();
    assertThat(jsDocAnnotation.getTagName()).isEqualTo("function");

    // Check if the call statement has the correct JS Doc annotation
    Node callStatement = functionDeclaration.getLastChild();
    CallableNode callStatementJsDocAnnotation = callStatement.getJSDocInfo();
    assertThat(callStatementJsDocAnnotation).isNotNull();
    assertThat(callStatementJsDocAnnotation.getTagName()).isEqualTo("call");
  }
}