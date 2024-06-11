import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String jsSource = "function Foo() {}";
    String jsCode = "function bar(x) { return new Foo(); }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT_3);
    Node root = NodeUtil.parse(jsCode, null, options);

    // Add the test under test
    Node test = NodeUtil.parse(jsSource, null, options);
    root.addChildToFront(test);

    CompilerPass pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    // Check that the marker properties have been inserted
    Node fooPrototype = NodeUtil.getPrototypeOfClass(
        NodeUtil.getClassNodeByName("Foo"), root.getFirstChild());
    assertTrue(fooPrototype.getProperties().containsKey("instance_of__Foo"));
    assertTrue(fooPrototype.getProperties().containsKey("implements__Object"));

    // Check that the checkType calls have been inserted
    Node functionNode = NodeUtil.getFunctionNodeByName("bar", root.getFirstChild());
    Node checkNode = functionNode.getLastChild();
    assertTrue(checkNode.getType() == Token.CALL);
    Node checkTypeNode = checkNode.getFirstChild();
    assertTrue(checkTypeNode.getType() == Token.IDENT);
    assertTrue(checkTypeNode.getString().equals("checkType"));

    // Test that checkType returns the expected result
    assertTrue(NodeUtil.evaluateToBoolean(
        NodeUtil.runJS(checkTypeNode, null), null));

    // Test that checkType throws an error for incorrect types
    Node incorrectTypeNode = new Node(Token.NUMBER_LITERAL, 42);
    assertFalse(NodeUtil.evaluateToBoolean(
        NodeUtil.runJS(new Node(Token.CALL, checkTypeNode, incorrectTypeNode), null), null));
  }
}