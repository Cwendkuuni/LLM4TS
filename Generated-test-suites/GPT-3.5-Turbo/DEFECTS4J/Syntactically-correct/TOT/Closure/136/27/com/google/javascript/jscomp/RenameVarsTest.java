import static org.junit.Assert.*;

import com.google.javascript.jscomp.*;

import org.junit.Before;
import org.junit.Test;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private RenameVars renameVars;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    renameVars = new RenameVars(compiler, "prefix_", true, true, true, null, null, null);
  }

  @Test
  public void testProcess_AllMethods_CorrectlyRenameVars() {
    // Given
    String input = "" +
        "function foo(a, b) {" +
        "  var c = a + b;" +
        "  console.log(c);" +
        "}" +
        "" +
        "function bar(a, b) {" +
        "  var c = a + (b * 2);" +
        "  console.log(c);" +
        "}";

    Node externsRoot = compiler.parseTestCode(input);
    Node jsRoot = compiler.parseTestCode(input);

    // When
    renameVars.process(externsRoot, jsRoot);

    // Then
    String expectedOutput = "" +
        "function prefix_foo(d, e) {" +
        "  var prefix_c = d + e;" +
        "  console.log(prefix_c);" +
        "}" +
        "" +
        "function prefix_bar(d, e) {" +
        "  var prefix_c = d + (e * 2);" +
        "  console.log(prefix_c);" +
        "}";
    String actualOutput = compiler.toSource(jsRoot);

    assertEquals(expectedOutput, actualOutput);
  }

}