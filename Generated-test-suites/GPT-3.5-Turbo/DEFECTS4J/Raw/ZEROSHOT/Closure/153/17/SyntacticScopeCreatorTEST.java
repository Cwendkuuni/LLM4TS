import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope_withParent() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node();
    Scope parent = new Scope(n, new AbstractCompiler());

    // Execute
    Scope scope = creator.createScope(n, parent);

    // Verify
    assertNotNull(scope);
  }

  @Test
  public void testCreateScope_withoutParent() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node();

    // Execute
    Scope scope = creator.createScope(n, null);

    // Verify
    assertNotNull(scope);
  }

  @Test
  public void testScanVars_VarToken() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.FUNCTION);

    // Execute
    creator.scanVars(n, parent);

    // Verify
    // TODO: Add verification code
  }

  @Test
  public void testScanVars_FunctionToken() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();

    // Execute
    creator.scanVars(n, parent);

    // Verify
    // TODO: Add verification code
  }

  @Test
  public void testScanVars_CatchToken() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.CATCH);
    Node parent = new Node();

    // Execute
    creator.scanVars(n, parent);

    // Verify
    // TODO: Add verification code
  }

  @Test
  public void testScanRoot_FunctionToken() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(n, new AbstractCompiler());

    // Execute
    creator.scanRoot(n, parent);

    // Verify
    // TODO: Add verification code
  }

  @Test
  public void testScanRoot_NotFunctionToken() {
    // Setup
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node();
    Scope parent = new Scope(n, new AbstractCompiler());

    // Execute
    creator.scanRoot(n, parent);

    // Verify
    // TODO: Add verification code
  }
}