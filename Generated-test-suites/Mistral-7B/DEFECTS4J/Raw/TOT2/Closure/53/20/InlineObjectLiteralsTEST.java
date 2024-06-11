import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler(new CompilerOptions());
    safeNameIdSupplier = () -> "test_id";
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    compiler.addPass(pass);
  }

  @Test
  public void testInlineObjectLiteralWithSingleProperty() {
    Node root = toNode("{\"prop\": 1}");
    Node externs = emptyNode();
    compiler.process(externs, root);
    assertEquals("1", getTextValue(root, "prop"));
  }

  @Test
  public void testInlineObjectLiteralWithMultipleProperties() {
    Node root = toNode("{\"prop1\": 1, \"prop2\": 2}");
    Node externs = emptyNode();
    compiler.process(externs, root);
    assertEquals("1", getTextValue(root, "prop1"));
    assertEquals("2", getTextValue(root, "prop2"));
  }

  @Test
  public void testInlineObjectLiteralWithFunctionProperties() {
    Node root = toNode("{\"getProp\": function() { return 1; }, \"setProp\": function(x) {}}");
    Node externs = emptyNode();
    compiler.process(externs, root);
    // Function properties cannot be inlined, they will be reported as errors.
    assertCompilerErrorCount(1);
  }

  @Test
  public void testInlineObjectLiteralWithFunctionPropertiesAndOtherProperties() {
    Node root = toNode("{\"getProp\": function() { return 1; }, \"prop\": 1}");
    Node externs = emptyNode();
    compiler.process(externs, root);
    assertEquals("1", getTextValue(root, "prop"));
    // Function properties cannot be inlined, they will be reported as errors.
    assertCompilerErrorCount(1);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    Node root = toNode("var x = { a: x }");
    Node externs = emptyNode();
    compiler.process(externs, root);
    // Self-referential object literals are not inlined, they will be reported as errors.
    assertCompilerErrorCount(1);
  }

  @Test
  public void testInlineObjectLiteralWithLoopReference() {
    Node root = toNode("for (var i = 0; i < { length: 5 }; i++) {}", "");
    Node externs = emptyNode();
    compiler.process(externs, root);
    // Object literals with loops or other complex expressions as keys are not inlined, they will be reported as errors.
    assertCompilerErrorCount(1);
  }

  @Test
  public void testInlineObjectLiteralWithEmptyObject() {
    Node root = toNode("{}");
    Node externs = emptyNode();
    compiler.process(externs, root);
    // An empty object literal is replaced with an empty object.
    Node actual = root;
    Node expected = new Node(Token.OBJECT, null, null);
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithNestedObjects() {
    Node root = toNode("{ a: { b: 1 } }");
    Node externs = emptyNode();
    compiler.process(externs, root);
    // Object literals with nested objects are inlined recursively.
    Node actual = root;
    Node expected = new Node(
        Token.OBJECT,
        new Node(Token.PROPERTY, new Node(Token.NAME, "a"), null),
        new Node(
            Token.OBJECT,
            new Node(Token.PROPERTY, new Node(Token.NAME, "b"), null),
            new Node(Token.NUMBER, "1")
        )
    );
    assertEquals(expected, actual);
  }

  private static Node toNode(String source) {
    Node root = new Node(Token.SCRIPT, null, null);
    Node script = new Node(Token.SCRIPT, null, null);
    Node scriptBlock = new Node(Token.EXPR_STATEMENT, null, null);
    Node node = JavaScriptUtils.parse(source, null);
    NodeUtil.traverse(node, new NodeUtil.Visitor<Node>() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.SCRIPT) {
          script.addChildToFront(node);
          scriptBlock.addChildToFront(node);
          return;
        }
        scriptBlock.addChildToFront(node);
      }
    }, NodeUtil.MATCH_ALL);
    root.addChildToFront(script);
    return root;
  }

  private static Node emptyNode() {
    return new Node(Token.SCRIPT, null, null);
  }

  private static String getTextValue(Node node, String propertyName) {
    Node getprop = new Node(Token.GETPROP, new Node(Token.NAME, propertyName), null);
    Node value = NodeUtil.getFirstChildOfType(getprop, Token.NUMBER);
    return value != null ? value.getString() : "";
  }
}