import static org.junit.Assert.*;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_EmptyCode() {
    Node externs = Node.newNull();
    Node root = Node.newNull();

    scopedAliases.process(externs, root);

    // No errors expected
    verify(compiler, never()).report(any(Node.class), any(DiagnosticType.class),
        anyStringArray());
  }

  @Test
  public void testProcess_EmptyScopeBlock() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "(){}");

    scopedAliases.process(externs, root);

    // No errors expected
    verify(compiler, never()).report(any(Node.class), any(DiagnosticType.class),
        anyStringArray());
  }

  @Test
  public void testProcess_InvalidScopeBlock() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "(function(){})");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testProcess_ScopeBlockWithInvalidFunction() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "function(){}");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testProcess_ScopeBlockWithThisReference() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "function(){\\this.x;}");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root.getFirstChild().getFirstChild()),
        eq(GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testProcess_ScopeBlockWithReturnStatement() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "function(){return;}");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root.getFirstChild()), eq(GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testProcess_ScopeBlockWithThrowStatement() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope", "function(){throw;}");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root.getFirstChild()), eq(GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testProcess_ScopeBlockWithAliasDefinition() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope",
        "function(){\\ var dom = goog.dom; var DIV = dom.TagName.DIV; }");

    scopedAliases.process(externs, root);

    // No errors expected
    verify(compiler, never()).report(any(Node.class), any(DiagnosticType.class),
        anyStringArray());

    // Check if alias definitions were processed correctly
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    Node domAliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.VAR, domAliasDefinition.getType());
    Var domAliasVar = domAliasDefinition.getParent().getVar();
    assertEquals("dom", domAliasVar.getName());
    Node domAliasInit = domAliasDefinition.getLastChild();
    assertEquals(Node.OP_ASSIGN, domAliasInit.getType());
    assertEquals(Node.NEW, domAliasInit.getFirstChild().getType());
    Node domAliasInitQualifiedName = domAliasInit.getFirstChild().getFirstChild();
    assertEquals("goog.dom", domAliasInitQualifiedName.getQualifiedName());

    Node divAliasDefinition = aliasDefinitions.get(1);
    assertEquals(Token.VAR, divAliasDefinition.getType());
    Var divAliasVar = divAliasDefinition.getParent().getVar();
    assertEquals("DIV", divAliasVar.getName());
    Node divAliasInit = divAliasDefinition.getLastChild();
    assertEquals(Token.QUAL_IDENT, divAliasInit.getType());
    assertEquals("TagName", divAliasInit.getString());
  }

  @Test
  public void testProcess_ScopeBlockWithAliasRedefinition() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope",
        "function(){\\ var dom = goog.dom; var DIV = dom.TagName.DIV;\n" +
        "    var DIV = 'div'; }");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_ALIAS_REDEFINED),
        eq("DIV"));
  }

  @Test
  public void testProcess_ScopeBlockWithNonAliasLocalVariable() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope",
        "function(){\\ var x = 1; }");

    scopedAliases.process(externs, root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_NON_ALIAS_LOCAL),
        eq("x"));
  }

  @Test
  public void testProcess_ScopeBlockWithValidAliases() {
    Node externs = Node.newNull();
    Node root = Node.newQualifiedName("goog", "scope",
        "function(){\\ var goog = this,\n" +
        "           dom = goog.dom,\n" +
        "           DIV = dom.TagName.DIV,\n" +
        "           foo = function(){\\}; }");

    scopedAliases.process(externs, root);

    // No errors expected
    verify(compiler, never()).report(any(Node.class), any(DiagnosticType.class),
        anyStringArray());

    // Check if alias definitions were processed correctly
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(4, aliasDefinitions.size());

    Node googAliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.VAR, googAliasDefinition.getType());
    Var googAliasVar = googAliasDefinition.getParent().getVar();
    assertEquals("goog", googAliasVar.getName());
    Node googAliasInit = googAliasDefinition.getLastChild();
    assertEquals(Token.ASSIGN, googAliasInit.getType());
    assertEquals(Token.NAME, googAliasInit.getFirstChild().getType());
    assertEquals("this", googAliasInit.getFirstChild().getString());

    Node domAliasDefinition = aliasDefinitions.get(1);
    assertEquals(Token.VAR, domAliasDefinition.getType());
    Var domAliasVar = domAliasDefinition.getParent().getVar();
    assertEquals("dom", domAliasVar.getName());
    Node domAliasInit = domAliasDefinition.getLastChild();
    assertEquals(Node.OP_ASSIGN, domAliasInit.getType());
    assertEquals(Node.NEW, domAliasInit.getFirstChild().getType());
    Node domAliasInitQualifiedName = domAliasInit.getFirstChild().getFirstChild();
    assertEquals("goog.dom", domAliasInitQualifiedName.getQualifiedName());

    Node DIVAliasDefinition = aliasDefinitions.get(2);
    assertEquals(Token.VAR, DIVAliasDefinition.getType());
    Var DIVAliasVar = DIVAliasDefinition.getParent().getVar();
    assertEquals("DIV", DIVAliasVar.getName());
    Node DIVAliasInit = DIVAliasDefinition.getLastChild();
    assertEquals(Token.QUAL_IDENT, DIVAliasInit.getType());
    assertEquals("TagName", DIVAliasInit.getString());

    Node fooAliasDefinition = aliasDefinitions.get(3);
    assertEquals(Token.VAR, fooAliasDefinition.getType());
    Var fooAliasVar = fooAliasDefinition.getParent().getVar();
    assertEquals("foo", fooAliasVar.getName());
    Node fooAliasInit = fooAliasDefinition.getLastChild();
    assertEquals(Token.FUNCTION, fooAliasInit.getType());
  }

  @Test
  public void testHotSwapScript() {
    // TODO: Implement this test
  }
}