import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler(); // initialize Compiler
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    // Test with empty code
    Node externs = new Node();
    Node root = new Node();
    renameVars.process(externs, root);

    // Test simple variable renaming
    externs = NodeBuilder.newTextNode("var x = 1;");
    root = NodeBuilder.newTextNode("function f() { var y = 2; }");
    renameVars.process(externs, root);
    assertEquals("x => x", renameVars.getVariableMap().toString());
    assertEquals("y => y", renameVars.getVariableMap().toString());

    // Test variable renaming with prefix
    externs = NodeBuilder.newTextNode("var prefix_x = 1;");
    root = NodeBuilder.newTextNode("function f() { var prefix_y = 2; }");
    renameVars = new RenameVars(compiler, "prefix_", false, false, false, null, null, null);
    renameVars.process(externs, root);
    assertEquals("prefix_x => x", renameVars.getVariableMap().toString());
    assertEquals("prefix_y => y", renameVars.getVariableMap().toString());

    // Test local variable renaming
    externs = NodeBuilder.newTextNode("var x = 1;");
    root = NodeBuilder.newTextNode("function f() { var x = 2; }");
    renameVars = new RenameVars(compiler, "", true, false, false, null, null, null);
    renameVars.process(externs, root);
    assertEquals("x => x", renameVars.getVariableMap().toString());

    // Test local variables with the same name
    externs = NodeBuilder.newTextNode("var x = 1;");
    root = NodeBuilder.newTextNode("function f() { var x = 2; var x = 3; }");
    renameVars = new RenameVars(compiler, "", true, false, false, null, null, null);
    renameVars.process(externs, root);
    assertEquals("x => x_1", renameVars.getVariableMap().toString());
    assertEquals("x_2 => x_2", renameVars.getVariableMap().toString());

    // Test anonymous functions
    externs = NodeBuilder.newTextnote("function f() { var x = function() { return 1; }; }");
    root = NodeBuilder.newTextNode("function g() { var y = function() { return 2; }; }");
    renameVars = new RenameVars(compiler, "", false, true, false, null, null, null);
    renameVars.process(externs, root);
    assertEquals("x => x", renameVars.getVariableMap().toString());
    assertEquals("y => y", renameVars.getVariableMap().toString());

    // Test preserving anonymous function names
    externs = NodeBuilder.newTextnode("function f() { var x = function() { return 1; }; }");
    root = NodeBuilder.newTextNode("function g() { var y = function() { return 2; }; }");
    renameVars = new RenameVars(compiler, "", false, true, true, null, null, null);
    renameVars.process(externs, root);
    assertEquals("x => anonymous_function_1", renameVars.getVariableMap().toString());
    assertEquals("y => anonymous_function_2", renameVars.getVariableMap().toString());

    // Test extern variables
    externs = NodeBuilder.newTextNode("var x = 1; var y = 2;");
    root = NodeBuilder.newTextNode("function f() { var z = 3; }");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(externs, root);
    assertEquals("x => x", renameVars.getVariableMap().toString());
    assertEquals("y => y", renameVars.getVariableMap().toString());
    assertEquals("z => z", renameVars.getVariableMap().toString());

    // Test reserved variables
    String[] reserved = {"eval", "arguments", "function", "this"};
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, Sets.newHashSet(reserved));
    externs = NodeBuilder.newTextNode("var eval = 1;");
    root = NodeBuilder.newTextNode("function f() { var arguments = 2; }");
    renameVars.process(externs, root);
    assertEquals("eval => eval", renameVars.getVariableMap().toString());
    assertEquals("arguments => arguments", renameVars.getVariableMap().toString());

    // Test with empty list of reserved variables
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    externs = NodeBuilder.newTextNode("var x = 1;");
    root = NodeBuilder.newTextNode("function f() { var y = 2; }");
    renameVars.process(externs, root);
    assertEquals("x => x", renameVars.getVariableMap().toString());
    assertEquals("y => y", renameVars.getVariableMap().toString());
  }

  private NodeBuilder NodeBuilder;

  @Before
  public void setUp() {
    NodeBuilder = new NodeBuilder();
  }
}