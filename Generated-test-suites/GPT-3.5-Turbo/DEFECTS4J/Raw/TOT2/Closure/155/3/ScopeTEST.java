import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {

  // Extracted public methods:
  // 1. declare(String name, Node nameNode, JSType type, CompilerInput input)
  // 2. declare(String name, Node nameNode, JSType type, CompilerInput input, boolean inferred)
  // 3. getDepth()
  // 4. isBottom()
  // 5. getRootNode()
  // 6. getTypeOfThis()
  // 7. declareVar(String name, Node nameNode, JSType type, CompilerInput input)
  // 8. declareVar(String name, Node nameNode, JSType type, CompilerInput input, boolean inferred)
  // 9. undeclareVar(Var var)
  // 10. getVar(String name)
  // 11. isDeclared(String name, boolean recurse)
  // 12. isGlobal()
  // 13. isLocal()

  private Scope globalScope;
  private Scope nestedScope;

  @Before
  public void setUp() {
    globalScope = new Scope(Node.newString("global"), null);
    Node rootNode = Node.newString("function");
    nestedScope = new Scope(globalScope, rootNode);
  }

  @Test
  public void testDeclareVar() {
    Var var = globalScope.declareVar("testVar", Node.newString("node"), JSType.STRING, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.STRING, var.getType());
  }

  @Test
  public void testUndeclareVar() {
    Var var = globalScope.declareVar("testVar", Node.newString("node"), JSType.STRING, null);
    globalScope.undeclareVar(var);
    assertNull(globalScope.getVar("testVar"));
  }

  @Test
  public void testGetDepth() {
    assertEquals(0, globalScope.getDepth());
    assertEquals(1, nestedScope.getDepth());
  }

  @Test
  public void testIsBottom() {
    assertFalse(globalScope.isBottom());
    assertFalse(nestedScope.isBottom());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(Node.newString("global"), globalScope.getRootNode());
    assertEquals(Node.newString("function"), nestedScope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    assertNotNull(globalScope.getTypeOfThis());
    assertNotNull(nestedScope.getTypeOfThis());
  }

  @Test
  public void testIsDeclared() {
    assertFalse(globalScope.isDeclared("nonExistentVar", false));
    assertTrue(globalScope.isDeclared("testVar", false));
  }

  @Test
  public void testIsGlobal() {
    assertTrue(globalScope.isGlobal());
    assertFalse(nestedScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(globalScope.isLocal());
    assertTrue(nestedScope.isLocal());
  }

}