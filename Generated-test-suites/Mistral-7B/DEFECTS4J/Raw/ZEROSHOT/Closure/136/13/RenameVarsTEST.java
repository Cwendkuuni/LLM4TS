import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String inputCode = "function foo(a, b) {" +
        "  var c = function(d, e) { return a; }" +
        "  var f = [1, 2, 3];" +
        "  return a + b;" +
        "}";

    RenameVars renamer = new RenameVars(
        null, "", false, false, false, null, null, null);

    CompilerInput compilerInput = new CompilerInput(
        "test.js", inputCode, null, null, null, null, null);

    Node externs = NodeOperations.createTopLevelNode(Token.BLOCK);
    Node root = NodeOperations.parseCharsToRoot(inputCode, 0, inputCode.length());

    renamer.process(externs, root);

    String outputCode = NodeOperations.printNodeRecursive(root);

    String expectedCode = "function x(a,b) {" +
        "  var g = function(d,e) { return a; }" +
        "  var h = [1,2,3];" +
        "  return a+b;" +
        "}";

    assertEquals(expectedCode, outputCode);
  }
}