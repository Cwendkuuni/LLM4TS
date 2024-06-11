import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.NodeUtil.isObjectLit;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testPrepareAst_SimpleCase() {
    String input = "function foo() {}";
    Node root = Node.parse(input);

    passManager.runPasses(root, new PrepareAst(null));
    passManager.runPasses(root, new PrepareAnnotations());

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testPrepareAst_ControlStructureCase() {
    String input = "function foo() {" +
                   "if (true) {" +
                   "  // do something" +
                   "}" +
                   "}";
    Node root = Node.parse(input);

    passManager.runPasses(root, new PrepareAst(null));
    passManager.runPasses(root, new PrepareAnnotations());

    Node ifStatement = root.getFirstChild();
    Node ifBlock = ifStatement.getFirstChild();
    Node block = ifBlock.getFirstChild();

    assertEquals(0, ifBlock.getChildCount());
    assertEquals(isControlStructure(ifStatement), true);
    assertEquals(isControlStructureCodeBlock(ifStatement, block), true);
  }

  @Test
  public void testPrepareAst_ObjectLiteralCase() {
    String input = "var obj = {" +
                   "  a: 1," +
                   "  b: function() {}" +
                   "}";
    Node root = Node.parse(input);

    passManager.runPasses(root, new PrepareAst(null));
    passManager.runPasses(root, new PrepareAnnotations());

    Node objLiteralNode = root.getFirstChild();
    Node objectLiteral = objLiteralNode.getFirstChild();

    assertEquals(isObjectLit(objLiteralNode), true);
    assertEquals(objectLiteral.getChildCount(), 3);

    Node aProperty = objectLiteral.getChildAt(0);
    assertEquals(aProperty.isName(), true);
    assertEquals(aProperty.getString(), "a");

    Node functionProperty = objectLiteral.getChildAt(1);
    assertEquals(isFunction(functionProperty), true);
    assertEquals(functionProperty.getFirstChild().isName(), true);
    assertEquals(functionProperty.getFirstChild().getString(), "b");
  }

  @Test
  public void testPrepareAst_FreeCallCase() {
    String input = "function foo() {" +
                   "  (Math.random)();" +
                   "}" +
                   "function bar() {}";
    Node root = Node.parse(input);

    passManager.runPasses(root, new PrepareAst(null));
    passManager.runPasses(root, new PrepareAnnotations());

    Node randomCall = root.getFirstChild().getFirstChild();
    assertEquals(randomCall.getBooleanProp(Node.FREE_CALL), true);
  }

  @Test
  public void testPrepareAst_DirectEvalCase() {
    String input = "function foo() {" +
                   "  (eval)('var x = 1');" +
                   "}" +
                   "function bar() {}";
    Node root = Node.parse(input);

    passManager.runPasses(root, new PrepareAst(null));
    passManager.runPasses(root, new PrepareAnnotations());

    Node evalCall = root.getFirstChild().getFirstChild();
    assertEquals(evalCall.getBooleanProp(Node.DIRECT_EVAL), true);
  }
}