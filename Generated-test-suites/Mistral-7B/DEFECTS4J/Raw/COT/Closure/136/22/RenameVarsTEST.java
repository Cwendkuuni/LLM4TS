import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import org.junit.Before;
import org.junit.Test;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private RenameVars renameVars;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    passManager = new CompilerPassManager(compiler);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testProcess_EmptyProgram() {
    Node root = Node.root();
    renameVars.process(Node.root(), root);
    // Empty program should not change
    assertEquals("", compiler.getDebugLog());
  }

  @Test
  public void testProcess_GlobalVariable() {
    Node root = Node.objectLiteral(
        Node.PROPERTY, "a", Node.STRING("initial value")
    );
    Node externs = Node.root();
    renameVars.process(externs, root);
    Node a = root.getFirstChild();
    assertEquals("a", a.getString());
  }

  @Test
  public void testProcess_LocalVariable() {
    String code = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() {}" +
        "}";
    Node root = Parser.parse(code, null, null).getChildAt(0);
    Node fn = root.getFirstChild();
    renameVars.process(Node.root(), root);

    Node a = fn.getFirstChild().getFirstChild().getFirstChild();
    Node b = fn.getFirstChild().getNextSibling().getFirstChild().getFirstChild();
    Node c = fn.getFirstChild().getNextSibling().getNextSibling();

    // Local variables should be renamed to "L x", where x is the index of the variable in the function
    assertEquals("L 0", a.getString());
    assertEquals("L 1", b.getString());

    // Anonymous function should not be renamed
    assertEquals("function() {}", c.getString());
  }

  @Test
  public void testProcess_GlobalVariablesWithSameName() {
    String code = "var a = 1; var a = 2;";
    Node root = Parser.parse(code, null, null).getChildAt(0);
    renameVars.process(Node.root(), root);

    Node a1 = root.getFirstChild();
    Node a2 = a1.getNextSibling();

    // Global variables should not be renamed to the same name
    assertNotEquals("a", a1.getString());
    assertNotEquals("a", a2.getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_DuplicateLocalVariable() {
    String code = "function foo() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}";
    Node root = Parser.parse(code, null, null).getChildAt(0);
    renameVars.process(Node.root(), root);
  }

  @Test
  public void testProcess_ExternVariable() {
    String code = "var a = 1; function foo() { var a = 2; }";
    Node root = Parser.parse(code, null, null).getChildAt(0);
    renameVars.process(Node.root(), root);

    Node a1 = root.getFirstChild();
    Node a2 = root.getFirstChild().getNextSibling().getFirstChild();

    // Global variables are not renamed when local variables have the same name
    assertEquals("a", a1.getString());
    // Local variables are renamed to "L x"
    assertEquals("L 1", a2.getString());
  }

  @Test
  public void testProcess_ReservedKeywords() {
    String code = "function foo() {" +
        "  var var = 1;" +
        "  var if = 2;" +
        "  var break = 3;" +
        "  var function = 4;" +
        "}";
    Node root = Parser.parse(code, null, null).getChildAt(0);

    renameVars.process(Node.root(), root);

    Node var = root.getFirstChild().getNextSibling().getNextSibling();
    Node ifNode = root.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
    Node breakNode = root.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
    Node functionNode = root.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();

    // Reserved keywords should not be renamed
    assertEquals("var", var.getString());
    assertEquals("if", ifNode.getString());
    assertEquals("break", breakNode.getString());
    assertEquals("function", functionNode.getString());
  }
}