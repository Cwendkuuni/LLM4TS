import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.SourceName;

import java.util.List;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testCreateGlobalScope() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node script = Node.newScript(
        "var x = 1; var y = 2; var z = 3;");
    Scope scope = creator.createScope(script, null);
    List<String> names = ImmutableList.of("x", "y", "z");
    for (String name : names) {
      assertEquals(name, scope.getVar(name).getName());
      assertNull(scope.getVar(name).getParentNode());
    }
  }

  @Test
  public void testCreateFunctionScope() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node function = Node.newFunction("func", null, null,
        Node.newBlock(
            Node.newVar("x"),
            Node.newVar("y"),
            Node.newVar("z")
        ));
    Scope parent = new Scope(Node.SCOPE, null);
    Scope scope = creator.createScope(function, parent);
    List<String> names = ImmutableList.of("x", "y", "z");
    for (String name : names) {
      Scope.Var var = scope.getVar(name);
      assertEquals(name, var.getName());
      assertEquals(function, var.getParentNode());
    }
  }

  @Test
  public void testCreateScopeWithArguments() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node function = Node.newFunction("func", null, null,
        Node.newBlock(
            Node.newIdent("arguments"),
            Node.newVar("x"),
            Node.newVar("y")
        ));
    Scope parent = new Scope(Node.SCOPE, null);
    Scope scope = creator.createScope(function, parent);
    List<String> names = ImmutableList.of("arguments", "x", "y");
    for (String name : names) {
      Scope.Var var = scope.getVar(name);
      assertEquals(name, var.getName());
      if (!name.equals("arguments")) {
        assertEquals(function, var.getParentNode());
      } else {
        assertEquals(parent, var.getParentNode());
      }
    }
  }

  @Test
  public void testCreateScopeWithRedeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node function = Node.newFunction("func", null, null,
        Node.newBlock(
            Node.newVar("x"),
            Node.newVar("x")
        ));
    Scope parent = new Scope(Node.SCOPE, null);
    try {
      Scope scope = creator.createScope(function, parent);
      fail("Expected a duplicate declaration error.");
    } catch (JSError error) {
      assertEquals(JSC_VAR_MULTIPLY_DECLARED_ERROR, error.getType());
      assertEquals("func", error.getSourceName());
      assertEquals("x", error.getMessage(0));
      assertEquals("x", error.getMessage(1));
    }
  }

  @Test
  public void testCreateScopeWithArgumentsRedeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node function = Node.newFunction("func", null, null,
        Node.newBlock(
            Node.newIdent("arguments"),
            Node.newVar("arguments")
        ));
    Scope parent = new Scope(Node.SCOPE, null);
    try {
      Scope scope = creator.createScope(function, parent);
      fail("Expected a shadowing error.");
    } catch (JSError error) {
      assertEquals(JSC_VAR_ARGUMENTS_SHADOWED_ERROR, error.getType());
      assertEquals("func", error.getSourceName());
    }
  }

  @Test
  public void testCreateScopeWithEmptyFunction() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node function = Node.newFunction("func", null, null, Node.NULL);
    Scope parent = new Scope(Node.SCOPE, null);
    Scope scope = creator.createScope(function, parent);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testCreateScopeWithEmptyBlock() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node script = Node.newScript("");
    Scope parent = new Scope(Node.SCOPE, null);
    Scope scope = creator.createScope(script, parent);
    assertEquals(0, scope.getVarCount());
  }
}

class Compiler extends AbstractCompiler {
  // Empty implementation
}