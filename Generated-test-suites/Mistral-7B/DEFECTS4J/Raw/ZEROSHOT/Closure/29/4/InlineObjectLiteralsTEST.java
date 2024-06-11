import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private AbstractCompiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() throws Exception {
    compiler = getCompiler();
    InlineObjectLiterals.VAR_PREFIX = "test_";
    inlineObjectLiterals = new InlineObjectLiterals(compiler, Suppliers.ofInstance(new IdGenerator()));
  }

  @Test
  public void testInlineObjectLiterals() {
    String input = "function foo() {" +
        "  var obj = {a: 1, b: 1};" +
        "  var c = obj.a;" +
        "  obj.a = 2;" +
        "  var d = obj.b;" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    expectedAssignments.addChild(new Node(Token.VAR, "a", new Node(Token.NUM, 1)));
    expectedAssignments.addChild(new Node(Token.VAR, "b", new Node(Token.NUM, 1)));
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NUM, 2)));
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.NUM, 1)));
    expected.addChild(new Node(Token.VAR, "c", new Node(Token.NUM, 1)));
    expected.addChild(new Node(Token.VAR, "d", new Node(Token.NUM, 1)));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    String input = "function foo() {" +
        "  var obj = {a: {x: function() {}}, b: {x: function() {}}};" +
        "  var c = obj.a.x();" +
        "  obj.a.x = function() {" +
        "    return 'hello';" +
        "  };" +
        "  var d = obj.b.x();" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    Node funcA = new Node(Token.FUNCTION, null, null, Token.FUNCTION_DECLARATION, null, null, null, null, false, null);
    Node funcADecl = new Node(Token.VAR, "x", funcA);
    expectedAssignments.addChild(funcADecl);
    expectedAssignments.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "a"), funcA));
    Node funcB = new Node(Token.FUNCTION, null, null, Token.FUNCTION_DECLARATION, null, null, null, null, false, null);
    Node funcBDecl = new Node(Token.VAR, "x", funcB);
    expectedAssignments.addChild(funcBDecl);
    expectedAssignments.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "b"), funcB));
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.CALL, funcA, null)));
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "d"), new Node(Token.CALL, funcB, null)));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    String input = "function foo() {" +
        "  var objA = {a: 1, b: objB};" +
        "  var objB = {a: objA};" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    // Objects with cycles are not supported.
    assertEquals("function foo() {}\n", root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithUninitializedObject() {
    String input = "function foo() {" +
        "  var obj = {a: 1};" +
        "  obj.b = 2;" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    expectedAssignments.addChild(new Node(Token.VAR, "a", new Node(Token.NUM, 1)));
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NUM, 2)));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValue() {
    String input = "function foo() {" +
        "  var obj = {a: 1, b: 1};" +
        "  var c = obj.a;" +
        "  obj = {a: 3};" +
        "  var d = obj.b;" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments1 = new Node(Token.BLOCK);
    Node expectedAssignments2 = new Node(Token.BLOCK);
    expectedAssignments1.addChild(new Node(Token.VAR, "a", new Node(Token.NUM, 1)));
    expectedAssignments1.addChild(new Node(Token.VAR, "b", new Node(Token.NUM, 1)));
    expected.addChild(expectedAssignments1);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NUM, 3)));
    expected.addChild(expectedAssignments2);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.NUM, 1)));
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.NUM, 1)));
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "d"), new Node(Token.NUM, 1)));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithCallExpression() {
    String input = "function foo() {" +
        "  var obj = {a: {fn: function() {}}};" +
        "  obj.a.fn();" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    expectedAssignments.addChild(new Node(Token.VAR, "a", new Node(Token.OBJECT, null, null, null, null)));
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "fn"), new Node(Token.FUNCTION, null, null, null, null, null, null, null, false, null)));
    expected.addChild(new Node(Token.CALL, new Node(Token.NAME, "fn"), null));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithInitValueAndCallExpression() {
    String input = "function foo() {" +
        "  var obj = {a: {fn: function() {}}};" +
        "  obj.a.fn();" +
        "  obj.a = {fn: function() {}};" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    Node funcA = new Node(Token.FUNCTION, null, null, Token.FUNCTION_DECLARATION, null, null, null, null, false, null);
    Node funcADecl = new Node(Token.VAR, "fn", funcA);
    expectedAssignments.addChild(funcADecl);
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "fn"), funcA));
    expected.addChild(new Node(Token.CALL, new Node(Token.NAME, "fn"), null));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithImportedFunction() {
    String input = "function foo() {" +
        "  var obj = {a: {fn: Math.sqrt}};" +
        "  obj.a.fn(2);" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    Node funcA = new Node(Token.FUNCTION, null, null, null, null, null, null, null, false, null);
    funcA.addChild(new Node(Token.NAME, "sqrt"));
    expectedAssignments.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "fn"), funcA));
    expected.addChild(expectedAssignments);
    expected.addChild(new Node(Token.CALL, new Node(Token.NAME, "fn"), new Node(Token.NUM, 2)));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithReplaceObjectWithFunction() {
    String input = "function foo() {" +
        "  var obj = {a: function() {}};" +
        "  obj();" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected = new Node(Token.BLOCK);
    expected.addChild(new Node(Token.FUNCTION, null, null, null, null, null, null, null, false, null));

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithReplaceFunctionPropertyWithObject() {
    String input = "function foo() {" +
        "  var obj = {a: {fn: function() {}}};" +
        "  obj.a.fn = {a: 1};" +
        "}";

    Node root = parse(input);

    inlineObjectLiterals.process(null, root);

    // This test case is a bit tricky. The behavior of this test case
    // depends on the order of the transformations performed by the
    // compiler. However, the current test case should result in the
    // following result:
    // 
    //  var a;
    //  a = {a: 1};
    //
    Node expected = new Node(Token.BLOCK);
    Node expectedAssignments = new Node(Token.BLOCK);
    expectedAssignments.addChild(new Node(Token.VAR, "a", new Node(Token.OBJECT, null, null, null, null)));

    assertEquals(expected.toSource(), root.toSource());
  }

  private static Node parse(String input) {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT5);
    return compiler.parse(input, options, null);
  }

  private static class IdGenerator implements Supplier<String> {
    private int counter = 0;
    @Override
    public String get() {
      return "test_" + counter++;
    }
  }
}