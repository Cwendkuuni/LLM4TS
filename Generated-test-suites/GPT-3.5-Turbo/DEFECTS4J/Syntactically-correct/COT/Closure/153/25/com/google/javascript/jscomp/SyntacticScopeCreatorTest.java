import org.junit.Assert;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = Node.newString(Token.NAME, "test");
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);
    Assert.assertNotNull(scope);
  }

  @Test
  public void testScanVars() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = Node.newString(Token.NAME, "test");
    Node parent = null;
    scopeCreator.scanVars(n, parent);
    // Add assertion here
  }

  @Test
  public void testDeclareVar() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    String name = "varName";
    Node n = Node.newString(Token.NAME, "test");
    Node parent = null;
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = null;
    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    // Add assertion here
  }
}