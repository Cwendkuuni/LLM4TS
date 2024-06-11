import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class ScopeTest {
  
  private Scope scope;
  private Node rootNode;
  
  @Before
  public void setUp() {
    rootNode = new Node();
    scope = new Scope(rootNode);
  }
  
  @Test
  public void testDeclare() {
    String name = "var";
    Node nameNode = new Node();
    JSType type = new JSType();
    String input = "input";
    
    Scope.Var var = scope.declare(name, nameNode, type, input);
    
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.getInputName());
  }
  
  @Test
  public void testGetVar() {
    String name = "var";
    Node nameNode = new Node();
    JSType type = new JSType();
    String input = "input";
    
    Scope.Var var = scope.declare(name, nameNode, type, input);
    
    Scope.Var retrievedVar = scope.getVar(name);
    
    assertEquals(var, retrievedVar);
  }
  
  @Test
  public void testIsDeclared() {
    String name = "var";
    Node nameNode = new Node();
    JSType type = new JSType();
    String input = "input";
    
    scope.declare(name, nameNode, type, input);
    
    assertTrue(scope.isDeclared(name, true));
  }
}