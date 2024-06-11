import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler handler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    handler = new TestAliasTransformationHandler();
    options.setAliasTransformationHandler(handler);
    scopedAliases = new ScopedAliases(compiler, null, handler);
  }

  @After
  public void tearDown() {
    compiler.dispose();
  }

  @Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {" +
                   "  var dom = goog.dom;" +
                   "  dom.createElement(goog.dom.TagName.DIV);" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNull(root.getError());
    assertEquals(1, handler.getAliasDefinitions().size());
    assertEquals("dom", handler.getAliasDefinitions().get(0).getName());
    assertEquals("goog.dom", handler.getAliasDefinitions().get(0).getAlias());
  }

  @Test
  public void testScopedBlockWithMultipleAliases() {
    String input = "goog.scope(function() {" +
                   "  var a = goog.dom;" +
                   "  var b = goog.dom.createElement;" +
                   "  a.createElement(DIV);" +
                   "  b('DIV');" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNull(root.getError());
    assertEquals(2, handler.getAliasDefinitions().size());
    assertEquals("a", handler.getAliasDefinitions().get(0).getName());
    assertEquals("goog.dom", handler.getAliasDefinitions().get(0).getAlias());
    assertEquals("b", handler.getAliasDefinitions().get(1).getName());
    assertEquals("goog.dom.createElement", handler.getAliasDefinitions().get(1).getAlias());
  }

  @Test
  public void testScopedBlockWithFunctionReturn() {
    String input = "goog.scope(function() {" +
                   "  function foo() {" +
                   "    return goog.dom.createElement(DIV);" +
                   "  }" +
                   "  return foo();" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("goog.scope functions must not use 'return'",
                root.getError().getErrors().get(0).getMessage());
  }

  @Test
  public void testScopedBlockWithFunctionThrow() {
    String input = "goog.scope(function() {" +
                   "  function foo() {" +
                   "    throw new Error('Test');" +
                   "  }" +
                   "  foo();" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("goog.scope functions must not use 'throw'",
                root.getError().getErrors().get(0).getMessage());
  }

  @Test
  public void testScopedBlockWithThisReference() {
    String input = "goog.scope(function() {" +
                   "  this.foo = 'bar';" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("The body of a goog.scope function cannot reference 'this'",
                root.getError().getErrors().get(0).getMessage());
  }

  @Test
  public void testScopedBlockWithImproperUsage() {
    String input = "goog.scope(function() {" +
                   "  foo();" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("The call to goog.scope must be alone in a single statement.",
                root.getError().getErrors().get(0).getMessage());
  }

  @Test
  public void testScopedBlockWithDuplicateAliasDefinition() {
    String input = "goog.scope(function() {" +
                   "  var dom = goog.dom;" +
                   "  var dom = goog.dom;" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("The alias 'dom' is assigned a value more than once.",
                root.getError().getErrors().get(0).getMessage());
  }

  @Test
  public void testScopedBlockWithNonAliasLocalVariable() {
    String input = "goog.scope(function() {" +
                   "  var foo = 42;" +
                   "});";

    Node root = compiler.parseTree(input, null, null, null, null);
    scopedAliases.process(null, root);

    assertNotNull(root.getError());
    assertEquals(1, root.getError().getErrors().size());
    assertEquals("The local variable 'foo' is in a goog.scope and is not an alias.",
                root.getError().getErrors().get(0).getMessage());
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  private final List<AliasTransformation> aliasDefinitions =
      Lists.newArrayList();

  public List<AliasTransformation> getAliasDefinitions() {
    return aliasDefinitions;
  }
}