import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
  }

  @Test
  public void testProcess_ValidInput_CorrectIssuesReported() {
    // Arrange
    String code = "var x = 'test';";
    Node externs = null;
    Node root = parse(code);

    // Act
    checkSideEffects.process(externs, root);

    // Assert
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertNotNull(checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testHotSwapScript_ValidInput_CorrectIssuesReported() {
    // Arrange
    String code = "var x = 'test';";
    Node scriptRoot = parse(code);
    Node originalRoot = parse(code);

    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // Assert
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertNotNull(checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testVisit_ValidInput_CorrectIssuesReported() {
    // Arrange
    String code = "var x = 'test';";
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = parse(code);
    Node parent = n.getParent();

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertNotNull(checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testProtectSideEffects_ValidInput_ProtectionAppliedSuccessfully() {
    // Arrange
    String code = "var x = 'test';";
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    List<Node> problemNodes = new ArrayList<>();
    problemNodes.add(parse(code));
    checkSideEffects.setProblemNodes(problemNodes);

    // Act
    checkSideEffects.protectSideEffects();

    // Assert
    Node root = checkSideEffects.getCompiler().getRoot();
    assertTrue(root.getLastChild().getLastChild().isName());
    assertEquals("JSCOMPILER_PRESERVE", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testAddExtern_ExternAlreadyAdded_ExternNotAddedAgain() {
    // Arrange
    String code = "var x = 'test';";
    Node externs = new Node(Token.ROOT);
    externs.addChildToBack(parse(code));
    Node root = new Node(Token.ROOT);

    // Act
    checkSideEffects.addExtern(externs, root);

    // Assert
    assertEquals(1, externs.getChildCount());
  }

  @Test
  public void testStripProtection_ValidProtection_ProtectionStrippedSuccessfully() {
    // Arrange
    String code = "JSCOMPILER_PRESERVE();";
    Node root = parse(code);
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());

    // Act
    CheckSideEffects.StripProtection.stripProtection(t, root);

    // Assert
    assertEquals(";", root.getOnlyChild().getLastChild().getString());
  }

  private Node parse(String code) {
    Compiler compiler = new Compiler();
    compiler.disableThreads();
    CompilerOptions options = new CompilerOptions();
    options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT6_STRICT);
    Node root = compiler.parseTestCode(code);
    assertTrue(compiler.getErrors().isEmpty());
    assertTrue(compiler.getWarnings().isEmpty());
    return root;
  }
}