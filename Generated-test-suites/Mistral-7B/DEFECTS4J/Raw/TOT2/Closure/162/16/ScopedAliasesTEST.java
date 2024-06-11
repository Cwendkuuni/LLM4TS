import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

public class ScopedAliasesTest {

  @SuppressWarnings("unchecked")
  private static <T> T getInstance(Class<T> clazz, Compiler compiler) {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation());
    options.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFile, SourcePosition position) {
        return new AliasTransformation();
      }
    });
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    return (T) scopedAliases;
  }

  @Test
  public void testProcessingScopedAliases() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("dom", aliasUsage.getParent().getString());
  }

  @Test
  public void testProcessingScopedAliasesWithFunctionArgument() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function(dom) {" +
        "  goog.dom.createElement(dom.TagName.DIV);" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("dom", aliasUsage.getParent().getString());
  }

  @Test
  public void testProcessingScopedAliasesWithMultipleAliases() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var link = goog.dom.createLink;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.dom.createLink(null, null, null, null, null);" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversal.getScopeCalls().size());
    assertEquals(3, traversal.getAliasUsages().size());

    Node domAliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", domAliasDefinition.getString());
    Node domAliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("dom", domAliasUsage.getParent().getString());

    Node linkAliasDefinition = traversal.getAliasDefinitionsInOrder().get(1);
    assertEquals("link", linkAliasDefinition.getString());
    Node linkAliasUsage = traversal.getAliasUsages().get(1);
    assertEquals("link", linkAliasUsage.getParent().getString());
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithFunctionBody() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  function test() {}" +
        "    return 42;" +
        "  }" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for function bodies in goog.scope.");
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithArguments() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function(a, b) {" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for arguments in goog.scope.");
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithReturn() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for return statements in goog.scope.");
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithThrow() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  throw new Error('test');" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for throw statements in goog.scope.");
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithThis() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  this.test = 42;" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for 'this' in goog.scope.");
  }

  @Test(expected = AssertionError.class)
  public void testProcessingScopedAliasesWithDuplicateAlias() {
    Compiler compiler = new Compiler();
    ScopedAliases scopedAliases = getInstance(ScopedAliases.class, compiler);
    Node root = NodeUtil.parseString("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom;" +
        "});");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    fail("Expected an error for duplicate alias definitions.");
  }
}