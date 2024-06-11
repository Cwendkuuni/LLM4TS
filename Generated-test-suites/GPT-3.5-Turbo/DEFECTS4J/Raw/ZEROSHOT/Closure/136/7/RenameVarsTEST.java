import junit.framework.TestCase;

public class RenameVarsTest extends TestCase {

  public void testProcess() {
    // Create test compiler and initialize RenameVars object
    Compiler testCompiler = new Compiler();
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = null;
    Set<String> reservedNames = null;
    RenameVars renameVars = new RenameVars(testCompiler, "TEST", localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    // Set up input nodes for process method
    Node externs = new Node(Token.EXTERN, null);
    Node root = new Node(Token.ROOT, null);

    // Run the process method
    renameVars.process(externs, root);

    // Test the output of the process method
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("", variableMap.lookupNewName(""));
    assertEquals("", variableMap.lookupNewName("L "));
    assertEquals("", variableMap.lookupNewName("x"));
    assertEquals("", variableMap.lookupNewName("y"));
    assertEquals("", variableMap.lookupNewName("a"));
    assertEquals("", variableMap.lookupNewName("b"));
    assertEquals("", variableMap.lookupNewName("c"));
    assertEquals("", variableMap.lookupNewName("d"));
    assertEquals("", variableMap.lookupNewName("e"));
  }
}