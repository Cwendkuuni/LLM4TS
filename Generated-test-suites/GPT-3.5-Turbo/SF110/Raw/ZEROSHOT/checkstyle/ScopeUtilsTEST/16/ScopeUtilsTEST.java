import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
  
  @Test
  public void testGetScopeFromMods() {
    DetailAST mods = new DetailAST();
    
    Scope scope = ScopeUtils.getScopeFromMods(mods);
    assertEquals(Scope.PACKAGE, scope);
    
    mods.addChild(createToken("public"));
    scope = ScopeUtils.getScopeFromMods(mods);
    assertEquals(Scope.PUBLIC, scope);
    
    mods.addChild(createToken("protected"));
    scope = ScopeUtils.getScopeFromMods(mods);
    assertEquals(Scope.PROTECTED, scope);
    
    mods.addChild(createToken("private"));
    scope = ScopeUtils.getScopeFromMods(mods);
    assertEquals(Scope.PRIVATE, scope);
  }
  
  @Test
  public void testGetSurroundingScope() {
    DetailAST node = new DetailAST();
    
    Scope scope = ScopeUtils.getSurroundingScope(node);
    assertNull(scope);
    
    node.addChild(createToken(14)); // TYPE
    DetailAST mods = new DetailAST();
    mods.addChild(createToken("public"));
    node.addChild(mods);
    scope = ScopeUtils.getSurroundingScope(node);
    assertEquals(Scope.PUBLIC, scope);
    
    node.addChild(createToken(15)); // OBJBLOCK
    node.addChild(createToken(157)); // INTERFACE_DEF
    scope = ScopeUtils.getSurroundingScope(node);
    assertEquals(Scope.ANONINNER, scope);
    
    node.addChild(createToken(154)); // ENUM_DEF
    scope = ScopeUtils.getSurroundingScope(node);
    assertEquals(Scope.ENUM, scope);
  }
  
  @Test
  public void testIsInInterfaceBlock() {
    DetailAST node = new DetailAST();
    
    boolean isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
    assertFalse(isInInterfaceBlock);
    
    node.addChild(createToken(15)); // INTERFACE_DEF
    isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
    assertTrue(isInInterfaceBlock);
    
    node.addChild(createToken(154)); // ENUM_DEF
    isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
    assertFalse(isInInterfaceBlock);
  }
  
  @Test
  public void testIsInAnnotationBlock() {
    DetailAST node = new DetailAST();
    
    boolean isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
    assertFalse(isInAnnotationBlock);
    
    node.addChild(createToken(157)); // ANNOTATION_DEF
    isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
    assertTrue(isInAnnotationBlock);
    
    node.addChild(createToken(15)); // INTERFACE_DEF
    isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
    assertFalse(isInAnnotationBlock);
  }
  
  @Test
  public void testIsInInterfaceOrAnnotationBlock() {
    DetailAST node = new DetailAST();
    
    boolean isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertFalse(isInInterfaceOrAnnotationBlock);
    
    node.addChild(createToken(15)); // INTERFACE_DEF
    isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertTrue(isInInterfaceOrAnnotationBlock);
    
    node = new DetailAST();
    node.addChild(createToken(157)); // ANNOTATION_DEF
    isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertTrue(isInInterfaceOrAnnotationBlock);
    
    node.addChild(createToken(14)); // TYPE
    isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    assertFalse(isInInterfaceOrAnnotationBlock);
  }
  
  @Test
  public void testIsInEnumBlock() {
    DetailAST node = new DetailAST();
    
    boolean isInEnumBlock = ScopeUtils.isInEnumBlock(node);
    assertFalse(isInEnumBlock);
    
    node.addChild(createToken(154)); // ENUM_DEF
    isInEnumBlock = ScopeUtils.isInEnumBlock(node);
    assertTrue(isInEnumBlock);
    
    node.addChild(createToken(15)); // INTERFACE_DEF
    isInEnumBlock = ScopeUtils.isInEnumBlock(node);
    assertFalse(isInEnumBlock);
  }
  
  @Test
  public void testIsInCodeBlock() {
    DetailAST node = new DetailAST();
    
    boolean isInCodeBlock = ScopeUtils.isInCodeBlock(node);
    assertFalse(isInCodeBlock);
    
    node.addChild(createToken(9)); // OBJBLOCK
    isInCodeBlock = ScopeUtils.isInCodeBlock(node);
    assertTrue(isInCodeBlock);
    
    node.addChild(createToken(14)); // TYPE
    isInCodeBlock = ScopeUtils.isInCodeBlock(node);
    assertFalse(isInCodeBlock);
  }
  
  @Test
  public void testIsOuterMostType() {
    DetailAST node = new DetailAST();
    
    boolean isOuterMostType = ScopeUtils.isOuterMostType(node);
    assertTrue(isOuterMostType);
    
    node.addChild(createToken(14)); // TYPE
    isOuterMostType = ScopeUtils.isOuterMostType(node);
    assertFalse(isOuterMostType);
    
    DetailAST parent = new DetailAST();
    parent.addChild(createToken(14)); // TYPE
    node.setParent(parent);
    isOuterMostType = ScopeUtils.isOuterMostType(node);
    assertTrue(isOuterMostType);
  }
  
  @Test
  public void testIsLocalVariableDef() {
    DetailAST node = new DetailAST();
    
    boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
    assertFalse(isLocalVariableDef);
    
    node.setType(10); // VARIABLE_DEF
    DetailAST parent = new DetailAST();
    parent.setType(7); // METHOD_CALL
    node.setParent(parent);
    isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
    assertTrue(isLocalVariableDef);
    
    parent.setType(96); // METHOD_DEF
    isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
    assertFalse(isLocalVariableDef);
    
    node.setType(21); // LITERAL_for
    isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
    assertTrue(isLocalVariableDef);
  }
  
  @Test
  public void testIsClassFieldDef() {
    DetailAST node = new DetailAST();
    
    boolean isClassFieldDef = ScopeUtils.isClassFieldDef(node);
    assertFalse(isClassFieldDef);
    
    node.setType(10); // VARIABLE_DEF
    isClassFieldDef = ScopeUtils.isClassFieldDef(node);
    assertTrue(isClassFieldDef);
    
    node.setType(10); // VARIABLE_DEF
    node.addChild(createToken("int"));
    isClassFieldDef = ScopeUtils.isClassFieldDef(node);
    assertFalse(isClassFieldDef);
  }
  
  @Test
  public void testIsInScope() {
    DetailAST ast = new DetailAST();
    Scope scope = ScopeUtils.getSurroundingScope(ast);
    
    boolean isInScope = ScopeUtils.isInScope(ast, scope);
    assertTrue(isInScope);
  }
  
  private DetailAST createToken(String text) {
    DetailAST token = new DetailAST();
    token.setText(text);
    return token;
  }
  
  private DetailAST createToken(int type) {
    DetailAST token = new DetailAST();
    token.setType(type);
    return token;
  }
}