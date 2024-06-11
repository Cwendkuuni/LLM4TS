import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope scope = creator.createScope(createNode("function foo() {}"), null);
    assertEquals(scope.getClass(), Scope.class);
  }

  @Test
  public void testCreateScopeWithParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new Scope(createNode("function bar() {}"), null);
    Scope scope = creator.createScope(createNode("function foo() {}"), parentScope);

    assertEquals(scope.getClass(), Scope.class);
    assertEquals(scope.getParent(), parentScope);
  }

  @Test
  public void testDeclareVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope scope = creator.createScope(createNode("function foo() { var x = 1; }"), null);

    String name = "x";
    Node node = createNode("x");
    JSType declaredType = JSType.INT;

    creator.declareVar(name, node, createNode("x"), null, declaredType, node);

    Scope.Var var = scope.getVar(name);
    assertEquals(var.name, name);
    assertEquals(var.node, node);
    assertEquals(var.type, declaredType);
  }

  @Test
  public void testDeclareVariableWithJSDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope scope = creator.createScope(createNode("function foo() { /** @type {number} */ var x = 1; }"), null);

    String name = "x";
    Node node = createNode("x");
    JSType declaredType = JSType.INT;

    creator.declareVar(name, node, createNode("x"), null, declaredType, node);

    Scope.Var var = scope.getVar(name);
    assertEquals(var.name, name);
    assertEquals(var.node, node);
    assertEquals(var.type, declaredType);
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateVariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope scope = creator.createScope(
        createNode("function foo() { var x = 1; var x = 2; }"), null);
  }

  @Test
  public void testShadowingArgument() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope scope = creator.createScope(
        createNode(
            "function foo(x) { arguments.x = 1; var x = 2; }"), null);

    String name = "x";
    Node node = createNode("x");
    JSType declaredType = JSType.INT;

    creator.declareVar(name, node, createNode("x"), null, declaredType, node);

    Scope.Var var = scope.getVar(name);
    assertEquals(var.name, name);
    assertEquals(var.node, node);
    assertEquals(var.declaredType, declaredType);

    Node argumentsNode = createNode("arguments");
    assertEquals(argumentsNode.getType(), Token.NAME);
    assertEquals(argumentsNode.getString(), "arguments");

    Scope.Var argumentsVar = scope.getVar(ARGUMENTS);
    assertEquals(argumentsVar.name, ARGUMENTS);
    assertEquals(argumentsVar.node, argumentsNode);
    assertEquals(argumentsVar.declaredType, JSType.UNDEFINED);
  }

  @Test
  public void testRedeclarationHandler() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        System.out.println("Redeclaration: name=" + name +
            ", parent=" + parent +
            ", gramps=" + gramps +
            ", nodeWithLineNumber=" + nodeWithLineNumber);
      }
    });

    Scope scope = creator.createScope(
        createNode("function foo() { var x = 1; var x = 2; }"), null);

    String name = "x";
    Node node = createNode("x");
    JSType declaredType = JSType.INT;

    creator.declareVar(name, node, createNode("x"), null, declaredType, node);
    creator.declareVar(name, node, createNode("x"), null, declaredType, node);
  }

  @Test
  public void testRedeclarationWithSuppression() {
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        compiler.report(
            JSError.make(null, n,
                DiagnosticType.warning("DuplicateDeclaration"),
                name));
      }
    });

    Scope scope = creator.createScope(
        createNode("function foo() { var x = 1; /** @suppress {duplicate} */ var x = 2; }"), null);

    String name = "x";
    Node node = createNode("x");
    JSType declaredType = JSType.INT;

    creator.declareVar(name, node, createNode("x"), null, declaredType, node);
    creator.declareVar(name, node, createNode("x"), null, declaredType, node);

    // No warning should be reported
    assertTrue(scope.getDiagnostics().isEmpty());
  }

  private Node createNode(String source) {
    return new Node(source);
  }

  private Node createNode(String source, int type) {
    Node node = new Node(source);
    node.setType(type);
    return node;
  }
}