import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;
  private Node externs;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    root = Node.object();
    externs = Node.object();
    scope = mock(Scope.class);

    when(compiler.newExternInput("{SyntheticVarsDeclar}"))
        .thenReturn(input);
    when(input.getAstRoot(compiler)).thenReturn(root);

    varCheck = new VarCheck(compiler);
  }

  @org.junit.Test
  public void testUndefinedVarError() {
    Node undeclaredVar = Node.newString(Token.NAME, "foo");

    when(scope.getVar("foo")).thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(compiler, input, null);

    Node currentNode = Node.NULL;
    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);

    traversal.traverse(undeclaredVar);

    assertEquals("variable foo is undefined",
        traversal.getLastDiagnostic().getMessage());
  }

  @org.junit.Test
  public void testDuplicateVarError() {
    Node firstVar = Node.newString(Token.NAME, "foo");
    Node secondVar = Node.newString(Token.NAME, "foo");

    Scope scope = mock(Scope.class);
    when(scope.getVar("foo")).thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(compiler, input, scope);

    Node currentNode = Node.NULL;
    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);

    traversal.traverse(firstVar);

    // The first declaration should be ignored because it is in externs.

    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);
    traversal.traverse(secondVar);

    assertEquals("variable foo is undefined",
        traversal.getLastDiagnostic().getMessage());
  }

  @org.junit.Test
  public void testMissingModuleDependencyWarning() {
    input.setModule(new JSModule("moduleA"));
    Node moduleB = Node.newObject(Token.LP,
        Node.newString(Token.NAME, "moduleB"), Node.RP);
    root.addChildToBack(moduleB);

    Node undeclaredVar = Node.newString(Token.NAME, "bar");
    Node moduleBVar = Node.newString(Token.NAME, "bar");
    moduleB.addChildToBack(moduleBVar);

    Scope scope = mock(Scope.class);
    when(scope.getVar("bar")).thenReturn(null);
    when(scope.isGlobal()).thenReturn(false);

    NodeTraversal traversal = new NodeTraversal(compiler, input, scope);

    Node currentNode = Node.NULL;
    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);

    traversal.traverse(undeclaredVar);

    assertEquals("missing module dependency; moduleA should depend " +
        "on moduleB because it references bar",
        traversal.getLastDiagnostic().getMessage());
  }

  @org.junit.Test
  public void testStrictModuleDependencyError() {
    input.setModule(new JSModule("moduleA"));
    Node moduleB = Node.newObject(Token.LP,
        Node.newString(Token.NAME, "moduleB"), Node.RP);
    root.addChildToBack(moduleB);

    Node undeclaredVar = Node.newString(Token.NAME, "bar");
    Node moduleBVar = Node.newString(Token.NAME, "bar");
    moduleB.addChildToBack(moduleBVar);

    Scope scope = mock(Scope.class);
    when(scope.getVar("bar")).thenReturn(null);
    when(scope.isGlobal()).thenReturn(false);

    NodeTraversal traversal = new NodeTraversal(compiler, input, scope);

    Node currentNode = Node.NULL;
    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);

    traversal.traverse(undeclaredVar);

    assertEquals("moduleA cannot reference moduleB, defined in " +
        "moduleB, since moduleA loads after moduleB",
        traversal.getLastDiagnostic().getMessage());
  }

  @org.junit.Test
  public void testNameReferenceInExternsWarning() {
    Node referenceToUndefinedVar = Node.newGetProp(
        Node.newString(Token.NAME, "undefined"), Node.NULL);

    NodeTraversal traversal = new NodeTraversal(compiler, input, null);

    Node currentNode = Node.NULL;
    traversal.setCurrentNode(currentNode);
    traversal.setParentNode(root);

    traversal.traverse(referenceToUndefinedVar);

    assertEquals("accessing name undefined in externs has no effect",
        traversal.getLastDiagnostic().getMessage());
  }
}