import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ScopeCreator scopeCreator;

  @Before
  public void setUp() throws Exception {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @After
  public void tearDown() throws Exception {
    compiler = null;
    scopeCreator = null;
  }

  @Test
  public void testCreateScope_GlobalBlock_ReturnsGlobalScope() {
    Node root = new Node(Token.SCRIPT);
    Scope scope = scopeCreator.createScope(root, null);
    
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateScope_Function_ReturnsFunctionScope() {
    Node root = new Node(Token.FUNCTION);
    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);
    
    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testCreateScope_FunctionWithName_DeclaresFunctionVariable() {
    Node root = new Node(Token.FUNCTION);
    Node functionName = new Node(Token.NAME, "foo");
    root.addChildToBack(functionName);
    
    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);
    
    assertTrue(scope.isDeclared("foo", false));
  }

  @Test
  public void testCreateScope_FunctionWithArgs_DeclaresArgsVariables() {
    Node root = new Node(Token.FUNCTION);
    Node functionName = new Node(Token.NAME, "foo");
    Node argsNode = new Node(Token.LP);
    Node arg1 = new Node(Token.NAME, "arg1");
    Node arg2 = new Node(Token.NAME, "arg2");
    argsNode.addChildToBack(arg1);
    argsNode.addChildToBack(arg2);
    root.addChildToBack(functionName);
    root.addChildToBack(argsNode);

    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);

    assertTrue(scope.isDeclared("arg1", false));
    assertTrue(scope.isDeclared("arg2", false));
  }

  @Test
  public void testCreateScope_VarDeclaration_DeclaresVariables() {
    Node root = new Node(Token.VAR);
    Node var1 = new Node(Token.NAME, "x");
    Node var2 = new Node(Token.NAME, "y");
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
  }

  @Test
  public void testCreateScope_CatchBlock_DeclaresCatchVar() {
    Node root = new Node(Token.CATCH);
    Node var = new Node(Token.NAME, "e");
    Node block = new Node(Token.BLOCK);
    root.addChildToFront(var);
    root.addChildToBack(block);

    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);

    assertTrue(scope.isDeclared("e", false));
  }

  @Test
  public void testCreateScope_StatementBlock_DeclaresVariables() {
    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR);
    Node var2 = new Node(Token.VAR);
    Node v1 = new Node(Token.NAME, "x");
    Node v2 = new Node(Token.NAME, "y");
    var1.addChildToBack(v1);
    var2.addChildToBack(v2);
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    Scope parent = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(root, parent);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
  }
}