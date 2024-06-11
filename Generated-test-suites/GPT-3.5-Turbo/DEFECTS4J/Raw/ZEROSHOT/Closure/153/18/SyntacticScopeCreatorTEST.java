import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AbstractCompiler;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    
    // Test case 1
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = null;
    Scope scope1 = creator.createScope(n1, parent1);
    assertNotNull(scope1);
    
    // Test case 2
    Node n2 = new Node(Token.VAR);
    Node parent2 = new Node(Token.SCRIPT);
    Scope scope2 = creator.createScope(n2, parent2);
    assertNotNull(scope2);
    
    // Test case 3
    Node n3 = new Node(Token.CATCH);
    Node parent3 = new Node(Token.FUNCTION);
    Scope scope3 = creator.createScope(n3, parent3);
    assertNotNull(scope3);
  }
  
  @Test
  public void testScanRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    
    // Test case 1
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = null;
    creator.scanRoot(n1, parent1);
    
    // Test case 2
    Node n2 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    creator.scanRoot(n2, parent2);
    
    // Test case 3
    Node n3 = new Node(Token.FUNCTION);
    Node parent3 = new Node(Token.SCRIPT);
    creator.scanRoot(n3, parent3);
  }
  
  @Test
  public void testScanVars() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    
    // Test case 1
    Node n1 = new Node(Token.VAR);
    Node parent1 = new Node(Token.SCRIPT);
    creator.scanVars(n1, parent1);
    
    // Test case 2
    Node n2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.SCRIPT);
    creator.scanVars(n2, parent2);
    
    // Test case 3
    Node n3 = new Node(Token.CATCH);
    Node parent3 = new Node(Token.FUNCTION);
    creator.scanVars(n3, parent3);
  }
  
  @Test
  public void testDeclareVar() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    
    // Test case 1
    String name1 = "x";
    Node n1 = new Node(Token.NAME, "x");
    Node parent1 = new Node(Token.SCRIPT);
    Node gramps1 = null;
    JSType declaredType1 = null;
    Node nodeWithLineNumber1 = new Node(Token.SCRIPT);
    creator.declareVar(name1, n1, parent1, gramps1, declaredType1, nodeWithLineNumber1);
    
    // Test case 2
    String name2 = "y";
    Node n2 = new Node(Token.NAME, "y");
    Node parent2 = new Node(Token.FUNCTION);
    Node gramps2 = new Node(Token.SCRIPT);
    JSType declaredType2 = null;
    Node nodeWithLineNumber2 = new Node(Token.FUNCTION);
    creator.declareVar(name2, n2, parent2, gramps2, declaredType2, nodeWithLineNumber2);
    
    // Test case 3
    String name3 = "z";
    Node n3 = new Node(Token.NAME, "z");
    Node parent3 = new Node(Token.CATCH);
    Node gramps3 = new Node(Token.FUNCTION);
    JSType declaredType3 = null;
    Node nodeWithLineNumber3 = new Node(Token.NAME, "z");
    creator.declareVar(name3, n3, parent3, gramps3, declaredType3, nodeWithLineNumber3);
  }
}