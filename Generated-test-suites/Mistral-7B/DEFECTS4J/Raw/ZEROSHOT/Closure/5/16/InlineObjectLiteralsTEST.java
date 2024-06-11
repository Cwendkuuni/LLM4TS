import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testInlineObjectLiterals() {
    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 3}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  var z = obj.b;" +
        "  return x + y + z;" +
        "}";

    Node root = NodeUtil.parse(code);
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, NodeUtil::generateNameId);
    pass.process(null, root);
    assertTrue(compiler.getErrors().isEmpty());

    NodeTraversal traverser = new NodeTraversal(root);
    traverser.traverse();

    Iterator<Var> it = traverser.getScope().getVars();
    List<String> expectedVars = Lists.newArrayList("a", "c");
    List<String> actualVars = Lists.newArrayList();
    while (it.hasNext()) {
      Var v = it.next();
      actualVars.add(v.name);
    }
    assertEquals(expectedVars, actualVars);

    Node result = traverser.getScope().getRootNode();
    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var c = 3;" +
        "  var x = a;" +
        "  var y = c;" +
        "  var z = c;" +
        "  return x + y + z;" +
        "}";

    String actualCode = NodeUtil.print(result);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String code = "function foo() {" +
        "  var obj = {" +
        "    a: function() { return this.b; }," +
        "    b: null" +
        "  };" +
        "  obj.a = obj.a;" +
        "  return obj.a();" +
        "}";

    Node root = NodeUtil.parse(code);
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, NodeUtil::generateNameId);
    pass.process(null, root);

    assertEquals(1, compiler.getErrors().size());
    String errorMessage = compiler.getErrors().get(0).getMessage();
    assertTrue(errorMessage.contains("Cannot inline self-referential object literal"));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    String code = "function foo() {" +
        "  var obj = {};" +
        "  return obj;" +
        "}";

    Node root = NodeUtil.parse(code);
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, NodeUtil::generateNameId);
    pass.process(null, root);
    assertTrue(compiler.getErrors().isEmpty());

    Node result = traverser.getScope().getRootNode();
    String expectedCode = "function foo() {" +
        "  return {};" +
        "}";

    String actualCode = NodeUtil.print(result);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObject() {
    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }" +
        "  };" +
        "  return obj.b.c;" +
        "}";

    Node root = NodeUtil.parse(code);
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, NodeUtil::generateNameId);
    pass.process(null, root);
    assertTrue(compiler.getErrors().isEmpty());

    Node result = traverser.getScope().getRootNode();
    String expectedCode = "function foo() {" +
        "  var c = 2;" +
        "  return c;" +
        "}";

    String actualCode = NodeUtil.print(result);
    assertEquals(expectedCode, actualCode);
  }

  @After
  public void teardown() {
    // Clear the compiler's error list for the next test
    compiler.getErrors().clear();
  }
}