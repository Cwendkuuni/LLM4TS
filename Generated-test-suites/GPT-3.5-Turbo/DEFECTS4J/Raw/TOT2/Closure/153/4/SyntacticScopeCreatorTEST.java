import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  
  @Test
  public void testCreateScope_ValidNode_ReturnsScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(new Node(Token.BLOCK), new AbstractCompiler());
    
    Scope createdScope = creator.createScope(n, parent);
    
    assertNotNull(createdScope);
  }
  
  @Test
  public void testCreateScope_NullNode_ReturnsNullScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = null;
    Scope parent = new Scope(new Node(Token.BLOCK), new AbstractCompiler());
    
    Scope createdScope = creator.createScope(n, parent);
    
    assertNull(createdScope);
  }
  
  @Test
  public void testCreateScope_GlobalBlock_ReturnsScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    
    Scope createdScope = creator.createScope(n, parent);
    
    assertNotNull(createdScope);
  }
  
  @Test
  public void testScanVars_NodeWithVars_DeclaresVariables() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.BLOCK);
    
    creator.scanVars(n, parent);
    
    assertNotNull(creator.getScope().getVar("var1"));
    assertNotNull(creator.getScope().getVar("var2"));
  }
  
  @Test
  public void testScanVars_FunctionExpression_NoVariableDeclared() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    creator.scanVars(n, parent);
    
    assertNull(creator.getScope().getVar("functionVar"));
  }
  
  @Test
  public void testScanVars_FunctionWithEmptyName_DeclaresVariable() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    creator.scanVars(n, parent);
    
    assertNotNull(creator.getScope().getVar(""));
  }
  
  @Test
  public void testScanVars_CatchNode_DeclaresVariable() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.CATCH);
    Node parent = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.NAME, "catchVar"));
    
    creator.scanVars(n, parent);
    
    assertNotNull(creator.getScope().getVar("catchVar"));
  }
  
  @Test
  public void testScanVars_ControlStructureNode_DeclaresVariables() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.IF);
    Node parent = new Node(Token.BLOCK);
    n.addChildToBack(new Node(Token.VAR));
    
    creator.scanVars(n, parent);
    
    assertNotNull(creator.getScope().getVar("var1"));
    assertNotNull(creator.getScope().getVar("var2"));
  }
}