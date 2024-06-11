import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {
  
  @Test
  public void testGetScopeFromMods_public() {
    DetailAST aMods = new DetailAST();
    aMods.setText("public");
    
    Scope expected = Scope.PUBLIC;
    Scope actual = ScopeUtils.getScopeFromMods(aMods);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetScopeFromMods_protected() {
    DetailAST aMods = new DetailAST();
    aMods.setText("protected");
    
    Scope expected = Scope.PROTECTED;
    Scope actual = ScopeUtils.getScopeFromMods(aMods);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetScopeFromMods_private() {
    DetailAST aMods = new DetailAST();
    aMods.setText("private");
    
    Scope expected = Scope.PRIVATE;
    Scope actual = ScopeUtils.getScopeFromMods(aMods);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetScopeFromMods_package() {
    DetailAST aMods = new DetailAST();
    aMods.setText("package");
    
    Scope expected = Scope.PACKAGE;
    Scope actual = ScopeUtils.getScopeFromMods(aMods);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSurroundingScope_null() {
    DetailAST node = null;
    
    Scope expected = null;
    Scope actual = ScopeUtils.getSurroundingScope(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSurroundingScope_valid() {
    DetailAST node = new DetailAST();
    
    Scope expected = Scope.PUBLIC;
    Scope actual = ScopeUtils.getSurroundingScope(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInInterfaceBlock_true() {
    DetailAST node = new DetailAST();
    node.setType(15);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInInterfaceBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInInterfaceBlock_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInInterfaceBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInAnnotationBlock_true() {
    DetailAST node = new DetailAST();
    node.setType(157);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInAnnotationBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInAnnotationBlock_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInAnnotationBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInInterfaceOrAnnotationBlock_true() {
    DetailAST node = new DetailAST();
    node.setType(15);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInInterfaceOrAnnotationBlock_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInEnumBlock_true() {
    DetailAST node = new DetailAST();
    node.setType(154);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInEnumBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInEnumBlock_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInEnumBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInCodeBlock_true() {
    DetailAST node = new DetailAST();
    node.setType(9);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInCodeBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInCodeBlock_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInCodeBlock(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsOuterMostType_true() {
    DetailAST node = new DetailAST();
    node.setType(9);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isOuterMostType(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsOuterMostType_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isOuterMostType(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsLocalVariableDef_true() {
    DetailAST node = new DetailAST();
    node.setType(10);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isLocalVariableDef(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsLocalVariableDef_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isLocalVariableDef(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsClassFieldDef_true() {
    DetailAST node = new DetailAST();
    node.setType(10);
    
    boolean expected = true;
    boolean actual = ScopeUtils.isClassFieldDef(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsClassFieldDef_false() {
    DetailAST node = new DetailAST();
    node.setType(14);
    
    boolean expected = false;
    boolean actual = ScopeUtils.isClassFieldDef(node);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInScope_true() {
    DetailAST ast = new DetailAST();
    Scope scope = Scope.PUBLIC;
    
    boolean expected = true;
    boolean actual = ScopeUtils.isInScope(ast, scope);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsInScope_false() {
    DetailAST ast = new DetailAST();
    Scope scope = Scope.PRIVATE;
    
    boolean expected = false;
    boolean actual = ScopeUtils.isInScope(ast, scope);
    
    assertEquals(expected, actual);
  }
}