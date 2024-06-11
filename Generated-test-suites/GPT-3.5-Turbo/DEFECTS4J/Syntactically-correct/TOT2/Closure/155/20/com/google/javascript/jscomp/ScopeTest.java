import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
 
public class ScopeTest {
 
  @Test
  public void testDeclareVar() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), null, null);
    
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(Token.NAME, var.getNameNode().getType());
    assertNull(var.getType());
  }
  
  @Test
  public void testUndeclareVar() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), null, null);
    
    assertNotNull(scope.getVar("testVar"));
    
    scope.undeclare(var);
    
    assertNull(scope.getVar("testVar"));
  }
  
  @Test
  public void testGetVar() {
    Scope parent = new Scope(null, new Node(Token.FUNCTION));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));
    Var var = scope.declare("testVar", new Node(Token.NAME, "testVar"), null, null);
    
    assertEquals(var, scope.getVar("testVar"));
  }
  
  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION));
    scope.declare("testVar", new Node(Token.NAME, "testVar"), null, null);
    
    assertTrue(scope.isDeclared("testVar", false));
    assertFalse(scope.isDeclared("nonExistentVar", false));
  }
 
}