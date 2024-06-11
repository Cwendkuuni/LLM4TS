import static org.junit.Assert.*;
import org.junit.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

    // Test case 1: var a; var b = 1; var c = 2;
    Node varNode1 = new Node(Token.VAR);
    Node name1 = new Node(Token.NAME, "a");
    Node varNode2 = new Node(Token.VAR);
    Node name2 = new Node(Token.NAME, "b");
    Node assign1 = new Node(Token.ASSIGN);
    assign1.addChildToBack(name2);
    assign1.addChildToBack(new Node(Token.NUMBER, "1"));
    Node varNode3 = new Node(Token.VAR);
    Node name3 = new Node(Token.NAME, "c");
    Node assign2 = new Node(Token.ASSIGN);
    assign2.addChildToBack(name3);
    assign2.addChildToBack(new Node(Token.NUMBER, "2"));
    varNode1.addChildToBack(name1);
    varNode2.addChildToBack(assign1);
    varNode3.addChildToBack(assign2);
    Node root1 = new Node(Token.ROOT);
    root1.addChildToBack(varNode1);
    root1.addChildToBack(varNode2);
    root1.addChildToBack(varNode3);

    collapse.process(null, root1);

    // Check if the variable declarations have been collapsed
    Node expectedVarNode1 = new Node(Token.VAR);
    expectedVarNode1.addChildToBack(name1);
    expectedVarNode1.addChildToBack(assign1);
    assertTrue(compareNodes(expectedVarNode1, varNode1));
    assertNull(varNode2);
    assertNull(varNode3);
  }

  @Test
  public void testCollapseAssignments() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

    // Test case 2: a = true; b = true; var c = true;
    Node assignNode1 = new Node(Token.ASSIGN);
    Node name1 = new Node(Token.NAME, "a");
    assignNode1.addChildToBack(name1);
    assignNode1.addChildToBack(new Node(Token.TRUE));
    Node assignNode2 = new Node(Token.ASSIGN);
    Node name2 = new Node(Token.NAME, "b");
    assignNode2.addChildToBack(name2);
    assignNode2.addChildToBack(new Node(Token.TRUE));
    Node varNode = new Node(Token.VAR);
    Node name3 = new Node(Token.NAME, "c");
    Node assignNode3 = new Node(Token.ASSIGN);
    assignNode3.addChildToBack(name3);
    assignNode3.addChildToBack(new Node(Token.TRUE));
    varNode.addChildToBack(assignNode3);
    Node root2 = new Node(Token.ROOT);
    root2.addChildToBack(assignNode1);
    root2.addChildToBack(assignNode2);
    root2.addChildToBack(varNode);

    collapse.process(null, root2);

    // Check if the assignments have been collapsed
    Node expectedVarNode = new Node(Token.VAR);
    Node expectedLhs = new Node(Token.NAME, "c");
    expectedLhs.addChildToBack(assignNode2);
    expectedLhs.addChildToBack(assignNode1);
    assignNode3.addChildToBack(expectedLhs);
    assertTrue(compareNodes(expectedVarNode, varNode));
    assertNull(assignNode1);
    assertNull(assignNode2);
  }

  private boolean compareNodes(Node expected, Node actual) {
    if (expected == null && actual == null) {
      return true;
    }
    if (expected == null || actual == null) {
      return false;
    }
    if (expected.getToken() != actual.getToken()) {
      return false;
    }
    if (expected.hasChildren() && !actual.hasChildren()) {
      return false;
    }
    if (!expected.hasChildren() && actual.hasChildren()) {
      return false;
    }
    if (expected.getChildCount() != actual.getChildCount()) {
      return false;
    }
    for (int i = 0; i < expected.getChildCount(); i++) {
      if (!compareNodes(expected.getChildAtIndex(i), actual.getChildAtIndex(i))) {
        return false;
      }
    }
    return true;
  }

  @Before
  public void setUp() {
    // Set up steps before executing each test case
  }

  @After
  public void tearDown() {
    // Tear down steps after executing each test case
  }

  @BeforeClass
  public static void setUpClass() {
    // Set up steps before executing any test cases in the class
  }

  @AfterClass
  public static void tearDownClass() {
    // Tear down steps after executing all test cases in the class
  }
}