import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testPrepareAstNormalizesNodeTypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Create a simple AST with a void expression
    Node root = IR.block().add(IR.voidExpr()).srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that the void expression is now an EXPR_RESULT
    Node result = root.getFirstChild();
    assertTrue(result.isExprResult());
  }

  @Test
  public void testPrepareAstNormalizesControlStructures() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Create a simple AST with a if statement without a block
    Node root = IR.block().add(IR.ifExpr(IR.lit(false), IR.lit(1), null)).srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that an empty block has been added
    Node ifNode = root.getFirstChild();
    assertTrue(ifNode.isIf());
    Node block = ifNode.getFirstChild();
    assertTrue(block.isBlock());
  }

  @Test
  public void testPrepareAstNormalizesFunctionCalls() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Create a simple AST with a free call
    Node root = IR.block().add(IR.callExpr(IR.name("log"), IR.lit(1))).srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that the call is marked as free
    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));

    // Test a direct call to eval
    root = IR.block().add(IR.callExpr(IR.name("eval"), IR.lit("1 + 1"))).srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that the call is marked as direct eval
    callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAstNormalizesDispatcherAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Create a simple AST with a dispatcher method and a JSDoc comment
    Node root = IR.block().add(IR.function(null, null, new Node[0], IR.block())
        .addJSDocInfo(new JSDocInfo("JavaDispatch", "dispatch"))
        .addStatement(IR.callExpr(IR.name("Print.print"), IR.string("Hello"))))
        .srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that the dispatcher method is annotated
    Node functionNode = root.getFirstChild();
    assertTrue(functionNode.isFunction());
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));

    // Test a function literal with a JSDoc comment
    root = IR.block().add(IR.expressionStatement(IR.assign(IR.name("x"), IR.function(null, null, new Node[0], IR.block().add(IR.returnStatement(IR.lit(1))).srcref("TestFunction"))))
        .addJSDocInfo(new JSDocInfo("JavaDispatch", "dispatchFunction")))
        .srcref(TestSource.class);

    prepareAst.process(null, root);

    // Assert that the function literal is annotated
    Node functionLiteralNode = root.getFirstChild().getLastChild();
    assertTrue(functionLiteralNode.isFunction());
    assertTrue(functionLiteralNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAstNormalizesObjectLiteralAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Create a simple AST with an object literal and JSDoc comments
    Node root = IR.block().add(IR.assign(IR.name("x"), IR.objectLit()
        .addProperty(IR.string("a"), IR.string("1"))
        .addProperty(IR.string("b"), IR.function(null, null, new Node[0], IR.block().add(IR.returnStatement(IR.lit(2)))).addJSDocInfo(new JSDocInfo("Method", "method"))))
        .srcref(TestSource.class));

    prepareAst.process(null, root);

    // Assert that the method is annotated in the value instead of the key
    Node objectLiteralNode = root.getFirstChild();
    Node propertyANode = objectLiteralNode.getFirstChild();
    Node propertyBNode = propertyANode.getNext();
    Node methodNode = propertyBNode.getFirstChild();
    assertTrue(methodNode.getBooleanProp(Node.IS_DISPATCHER));
  }
}

class TestSource {
}

class JSDocInfo {
  private String name;
  private String description;

  JSDocInfo(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}