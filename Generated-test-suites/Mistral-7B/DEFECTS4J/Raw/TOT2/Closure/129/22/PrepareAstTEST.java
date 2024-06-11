import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;

public class PrepareAstTest {

  @Test
  public void testPrepareAst_NormalFunctionCall() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node functionCall = IR.call("testFunction");
    root.addChild(functionCall);
    prepareAst.process(null, root);
    assertTrue(functionCall.isCall());
  }

  @Test
  public void testPrepareAst_VoidCall() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node voidCall = IR.call("voidFunction");
    root.addChild(voidCall);
    prepareAst.process(null, root);
    assertTrue(voidCall.isCall());
    assertTrue(voidCall.getBooleanProp(Node.FREE_CALL));
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAst_NullParent() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node call = IR.call("testFunction");
    prepareAst.process(null, root);
    root.addChild(call);
  }

  @Test
  public void testPrepareAst_DirectEvalCall() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node directEvalCall = IR.call("eval");
    root.addChild(directEvalCall);
    prepareAst.process(null, root);
    assertTrue(directEvalCall.isCall());
    assertTrue(directEvalCall.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAst_NullChild() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node call = IR.call("testFunction");
    Node parent = IR.block();
    parent.addChild(call);
    root.addChild(parent);
    prepareAst.process(null, root);
  }

  @Test
  public void testPrepareAst_FunctionLiteral() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.program();
    Node functionLiteral = IR.function(null, null, null);
    Node assignment = IR.assign(IR.name("testFunction"), functionLiteral);
    root.addChild(assignment);
    prepareAst.process(null, root);
    Node testFunction = root.getFirstChild().getLastChild();
    assertTrue(testFunction.isFunction());
    assertTrue(testFunction.getParent().getJSDocInfo() != null);
  }
}