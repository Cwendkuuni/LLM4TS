import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
  
  @Test
  public void testGetScopeFromMods() {
    DetailAST mods = new DetailAST();
    
    // Test case 1
    mods.addChild(createASTToken("public"));
    Scope expected1 = Scope.PUBLIC;
    Scope actual1 = ScopeUtils.getScopeFromMods(mods);
    assertEquals(expected1, actual1);
    
    // Test case 2
    mods = new DetailAST();
    mods.addChild(createASTToken("protected"));
    Scope expected2 = Scope.PROTECTED;
    Scope actual2 = ScopeUtils.getScopeFromMods(mods);
    assertEquals(expected2, actual2);
    
    // Test case 3
    mods = new DetailAST();
    mods.addChild(createASTToken("private"));
    Scope expected3 = Scope.PRIVATE;
    Scope actual3 = ScopeUtils.getScopeFromMods(mods);
    assertEquals(expected3, actual3);
  }
  
  @Test
  public void testGetSurroundingScope() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    Scope expected1 = Scope.PACKAGE;
    Scope actual1 = ScopeUtils.getSurroundingScope(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(14);
    node.addChild(createASTToken("public"));
    Scope expected2 = Scope.PUBLIC;
    Scope actual2 = ScopeUtils.getSurroundingScope(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(15);
    Scope expected3 = Scope.PROTECTED;
    Scope actual3 = ScopeUtils.getSurroundingScope(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(157);
    Scope expected4 = Scope.PRIVATE;
    Scope actual4 = ScopeUtils.getSurroundingScope(node);
    assertEquals(expected4, actual4);
    
    // Test case 5
    node.setType(154);
    Scope expected5 = Scope.ANONINNER;
    Scope actual5 = ScopeUtils.getSurroundingScope(node);
    assertEquals(expected5, actual5);
  }
  
  @Test
  public void testIsInInterfaceBlock() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isInInterfaceBlock(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(15);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isInInterfaceBlock(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(14);
    boolean expected3 = false;
    boolean actual3 = ScopeUtils.isInInterfaceBlock(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(136);
    boolean expected4 = false;
    boolean actual4 = ScopeUtils.isInInterfaceBlock(node);
    assertEquals(expected4, actual4);
  }
  
  @Test
  public void testIsInAnnotationBlock() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isInAnnotationBlock(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(157);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isInAnnotationBlock(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(14);
    boolean expected3 = false;
    boolean actual3 = ScopeUtils.isInAnnotationBlock(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(136);
    boolean expected4 = false;
    boolean actual4 = ScopeUtils.isInAnnotationBlock(node);
    assertEquals(expected4, actual4);
  }
  
  @Test
  public void testIsInInterfaceOrAnnotationBlock() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(15);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(157);
    boolean expected3 = true;
    boolean actual3 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(14);
    boolean expected4 = false;
    boolean actual4 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertEquals(expected4, actual4);
    
    // Test case 5
    node.setType(136);
    boolean expected5 = false;
    boolean actual5 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertEquals(expected5, actual5);
  }
  
  @Test
  public void testIsInEnumBlock() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(154);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(15);
    boolean expected3 = false;
    boolean actual3 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(157);
    boolean expected4 = false;
    boolean actual4 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected4, actual4);
    
    // Test case 5
    node.setType(14);
    boolean expected5 = false;
    boolean actual5 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected5, actual5);
    
    // Test case 6
    node.setType(136);
    boolean expected6 = false;
    boolean actual6 = ScopeUtils.isInEnumBlock(node);
    assertEquals(expected6, actual6);
  }
  
  @Test
  public void testIsInCodeBlock() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isInCodeBlock(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(9);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isInCodeBlock(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(8);
    boolean expected3 = true;
    boolean actual3 = ScopeUtils.isInCodeBlock(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(11);
    boolean expected4 = true;
    boolean actual4 = ScopeUtils.isInCodeBlock(node);
    assertEquals(expected4, actual4);
    
    // Test case 5
    node.setType(12);
    boolean expected5 = true;
    boolean actual5 = ScopeUtils.isInCodeBlock(node);
    assertEquals(expected5, actual5);
  }
  
  @Test
  public void testIsOuterMostType() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = true;
    boolean actual1 = ScopeUtils.isOuterMostType(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    DetailAST parent = createParentASTToken(14);
    node.setParent(parent);
    boolean expected2 = false;
    boolean actual2 = ScopeUtils.isOuterMostType(node);
    assertEquals(expected2, actual2);
  }
  
  @Test
  public void testIsLocalVariableDef() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isLocalVariableDef(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(10);
    DetailAST parent2 = createParentASTToken(7);
    node.setParent(parent2);
    boolean expected2 = true;
    boolean actual2 = ScopeUtils.isLocalVariableDef(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(10);
    DetailAST parent3 = createParentASTToken(35);
    node.setParent(parent3);
    boolean expected3 = true;
    boolean actual3 = ScopeUtils.isLocalVariableDef(node);
    assertEquals(expected3, actual3);
    
    // Test case 4
    node.setType(10);
    DetailAST parent4 = createParentASTToken(156);
    node.setParent(parent4);
    boolean expected4 = true;
    boolean actual4 = ScopeUtils.isLocalVariableDef(node);
    assertEquals(expected4, actual4);
    
    // Test case 5
    node.setType(21);
    DetailAST parent5 = createParentASTToken(96);
    node.setParent(parent5);
    boolean expected5 = true;
    boolean actual5 = ScopeUtils.isLocalVariableDef(node);
    assertEquals(expected5, actual5);
  }
  
  @Test
  public void testIsClassFieldDef() {
    DetailAST node = new DetailAST();
    
    // Test case 1
    boolean expected1 = false;
    boolean actual1 = ScopeUtils.isClassFieldDef(node);
    assertEquals(expected1, actual1);
    
    // Test case 2
    node.setType(10);
    boolean expected2 = false;
    boolean actual2 = ScopeUtils.isClassFieldDef(node);
    assertEquals(expected2, actual2);
    
    // Test case 3
    node.setType(21);
    boolean expected3 = false;
    boolean actual3 = ScopeUtils.isClassFieldDef(node);
    assertEquals(expected3, actual3);
  }
  
  @Test
  public void testIsInScope() {
    DetailAST ast = new DetailAST();
    
    // Test case 1
    Scope scope1 = Scope.PACKAGE;
    boolean expected1 = true;
    boolean actual1 = ScopeUtils.isInScope(ast, scope1);
    assertEquals(expected1, actual1);
    
    // Test case 2
    ast.setType(14);
    ast.setParent(createParentASTToken(14));
    Scope scope2 = Scope.PRIVATE;
    boolean expected2 = false;
    boolean actual2 = ScopeUtils.isInScope(ast, scope2);
    assertEquals(expected2, actual2);
  }
  
  private DetailAST createASTToken(String text) {
    DetailAST ast = new DetailAST();
    ast.setText(text);
    return ast;
  }
  
  private DetailAST createParentASTToken(int type) {
    DetailAST parent = new DetailAST();
    parent.setType(type);
    return parent;
  }
}