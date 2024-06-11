import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;
import java.util.List;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations_singleDeclaration() {
    String code = "var a = 1;";
    String expected = "var a = 1;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }
  
  @Test
  public void testCollapseVariableDeclarations_multipleDeclarations() {
    String code = "var a; var b = 1; var c = 2;";
    String expected = "var a, b = 1, c = 2;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }
  
  @Test
  public void testCollapseVariableDeclarations_multipleDeclarationsWithAssigns() {
    String code = "a = true; b = true; var c = true;";
    String expected = "var c = b = a = true;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }
  
  @Test
  public void testCollapseVariableDeclarations_functionDeclaration() {
    String code = "function foo() { var a; var b = 1; var c = 2; }";
    String expected = "function foo() { var a, b = 1, c = 2; }";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }
  
  @Test
  public void testCollapseVariableDeclarations_ifStatement() {
    String code = "if (x) { var a; } var b = 1;";
    String expected = "if (x) { var a; } var b = 1;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }
  
  @Test
  public void testCollapseVariableDeclarations_redeclaration() {
    String code = "var a; var a = true;";
    String expected = "var a; var a = true;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }  
  
  @Test
  public void testCollapseVariableDeclarations_noCollapse() {
    String code = "var a; b = 1; c = 2;";
    String expected = "var a; b = 1; c = 2;";
    CollapseVariableDeclarations.collapseVariableDeclarations(code);
    assertEquals(expected, code);
  }

}