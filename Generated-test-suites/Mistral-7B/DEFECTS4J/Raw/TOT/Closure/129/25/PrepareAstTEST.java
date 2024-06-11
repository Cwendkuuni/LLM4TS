import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testPrepareAstWithSimpleObjectLiterals() {
    PrepareAst compiler = new PrepareAst(null);

    Node root = IR.objectLit().addChild(
        IR.property("a", IR.stringLit("hello")))
        .addChild(IR.property("b", IR.numberLit(123)));

    Node externs = null;
    compiler.process(externs, root);

    // Assert that annotations are correctly normalized on the object literal value
    assertEquals("The 'a' property should have the correct JSDoc info",
        "/**\n * jsdoc:this\n */\n" +
        "Object {\n" +
        "  a: /**\n * jsdoc:this\n * @type {string}\n */\n" +
        "  'hello'\n" +
        ",\n" +
        "  b: /**\n * jsdoc:this\n * @type {number}\n */\n" +
        "  123\n" +
        "}",
        root.toString());
  }

  @Test
  public void testPrepareAstWithFunctionLiteralsAndJSDoc() {
    PrepareAst compiler = new PrepareAst(null);

    Node root = IR.objectLit().addChild(
        IR.property("fn", IR.function(
            null, null, null, IR.block().addChild(IR.returnStmt(IR.intLit(42))))));

    Node externs = null;
    compiler.process(externs, root);

    // Assert that JSDoc is correctly normalized on the function literal
    assertEquals("The 'fn' property should have the correct JSDoc info",
        "Object {\n" +
        "  fn: /**\n * jsdoc:this\n * @return {number}\n */\n" +
        "  function () {\n" +
        "    return 42;\n" +
        "  }\n" +
        "}",
        root.toString());
  }

  @Test
  public void testPrepareAstWithControlStructuresAndBlocks() {
    PrepareAst compiler = new PrepareAst(null);

    Node root = IR.ifStmt(
        IR.cond(IR.eq(IR.literal(5), IR.literal(5))),
        IR.block().addChild(IR.returnStmt(IR.literal(true))),
        IR.block().addChild(IR.returnStmt(IR.literal(false))));

    Node externs = null;
    compiler.process(externs, root);

    // Assert that control structures have blocks
    assertEquals("The if statement should have a block",
        "IfStatement {types: [IfStatement.IF], cond: BoolLiteral {types: [BoolLiteral], value: BooleanLiteral {types: [BooleanLiteral], value: Literal.BOOLEAN true}}, then: Block {types: [Block], body: [ReturnStatement {types: [ReturnStatement], value: Literal.BOOLEAN true}]}, else: Block {types: [Block], body: [ReturnStatement {types: [ReturnStatement], value: Literal.BOOLEAN false}]}}",
        root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAstWithNormalizeNodeTypesConstraintViolated() {
    PrepareAst compiler = new PrepareAst(null, true);

    Node root = IR.objectLit().addChild(IR.property("a", IR.stringLit("hello")))
        .addChild(IR.property("b", IR.numberLit(123)));

    compiler.process(null, root);
  }
}