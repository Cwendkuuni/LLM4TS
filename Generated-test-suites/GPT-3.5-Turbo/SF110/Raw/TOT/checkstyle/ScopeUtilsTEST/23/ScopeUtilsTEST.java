
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScopeUtilsTest {
  
  @Test
  public void testGetScopeFromMods() {
    // Typical Use Case: Get Scope from DetailAST with "public" as token
    DetailAST ast = new DetailAST();
    ast.addChild(new DetailAST());
    ast.getFirstChild().setText("public");
    Scope expectedScope = Scope.PUBLIC;

    Scope actualScope = ScopeUtils.getScopeFromMods(ast);

    assertEquals(expectedScope, actualScope);
  }
  
  @Test
  public void testGetSurroundingScope() {
    // Typical Use Case: Get surrounding scope from DetailAST of type 14
    DetailAST ast = new DetailAST(14);
    DetailAST parent = new DetailAST(15);
    ast.setParent(parent);
    parent.addChild(ast);
    Scope expectedScope = Scope.PROTECTED;

    Scope actualScope = ScopeUtils.getSurroundingScope(ast);

    assertEquals(expectedScope, actualScope);
  }
  
  @Test
  public void testIsInInterfaceBlock() {
    // Typical Use Case: Check if DetailAST is in interface block
    DetailAST ast = new DetailAST(15);
    DetailAST parent = new DetailAST(136);
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInInterfaceBlock(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsInAnnotationBlock() {
    // Typical Use Case: Check if DetailAST is in annotation block
    DetailAST ast = new DetailAST(157);
    DetailAST parent = new DetailAST(15);
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInAnnotationBlock(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsInInterfaceOrAnnotationBlock() {
    // Typical Use Case: Check if DetailAST is in interface or annotation block
    DetailAST ast = new DetailAST(15);
    DetailAST parent = new DetailAST(157);
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsInEnumBlock() {
    // Typical Use Case: Check if DetailAST is in enum block
    DetailAST ast = new DetailAST(154);
    DetailAST parent = new DetailAST(15);
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInEnumBlock(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsInCodeBlock() {
    // Typical Use Case: Check if DetailAST is in code block
    DetailAST ast = new DetailAST(9);
    DetailAST parent = new DetailAST(15);
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInCodeBlock(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsOuterMostType() {
    // Typical Use Case: Check if DetailAST is the outermost type
    DetailAST ast = new DetailAST();
    DetailAST parent = null;
    ast.setParent(parent);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isOuterMostType(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsLocalVariableDef() {
    // Typical Use Case: Check if DetailAST is a local variable definition
    DetailAST ast = new DetailAST(10); // Variable definition type
    DetailAST parent = new DetailAST(7); // Parent type
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isLocalVariableDef(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsClassFieldDef() {
    // Typical Use Case: Check if DetailAST is a class field definition
    DetailAST ast = new DetailAST(10); // Variable definition type
    DetailAST parent = new DetailAST(14); // Parent type
    ast.setParent(parent);
    parent.addChild(ast);
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isClassFieldDef(ast);

    assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIsInScope() {
    // Typical Use Case: Check if DetailAST is in specified scope
    DetailAST ast = new DetailAST();
    DetailAST parent = new DetailAST(15); // Surrounding scope
    ast.setParent(parent);
    Scope scope = Scope.PROTECTED;
    boolean expectedValue = true;

    boolean actualValue = ScopeUtils.isInScope(ast, scope);

    assertEquals(expectedValue, actualValue);
  }

}

