import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private HotSwapCompilerPass scopedAliasesPass;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position) {
        System.out.println("Alias found: fileName=" + fileName +
            ", position=" + position);
      }
    });

    scopedAliasesPass = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
  }

  @Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    assertEquals(1, scopedAliasesPass.getAliasDefinitionsInOrder().size());
    assertEquals("goog.dom",
        scopedAliasesPass.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.dom.TagName.DIV",
        scopedAliasesPass.getAliasUsages().get(0).applyAlias().getString());
  }

  @Test
  public void testScopeCallWithBadParameters() {
    String input = "goog.scope(function() {" +
        "  function foo() {}" +
        "  goog.scope(foo);" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    assertEquals(1, scopedAliasesPass.getAliasDefinitionsInOrder().size());
    assertEquals("foo",
        scopedAliasesPass.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("function foo() {}",
        scopedAliasesPass.getAliasUsages().get(0).applyAlias().toString());

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0));
  }

  @Test
  public void testScopeCallWithThis() {
    String input = "goog.scope(function() {" +
        "  this.foo = 1;" +
        "  goog.scope(function() {" +
        "    this.foo = 2;" +
        "  });" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0));
  }

  @Test
  public void testScopeCallWithReturn() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    return 1;" +
        "  }" +
        "  goog.scope(foo);" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0));
  }

  @Test
  public void testScopeCallWithThrow() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error(\"test\");" +
        "  }" +
        "  goog.scope(foo);" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0));
  }

  @Test
  public void testScopeCallWithAliasRedefinition() {
    String input = "goog.scope(function() {" +
        "  var foo = 1;" +
        "  goog.scope(function() {" +
        "    var foo = 2;" +
        "  });" +
        "  goog.scope(function() {" +
        "    var foo = 3;" +
        "  });" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }

  @Test
  public void testComplexScopedAlias() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.foo;" +
        "  }" +
        "  foo();" +
        "});";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    assertEquals(1, scopedAliasesPass.getAliasDefinitionsInOrder().size());
    assertEquals("foo",
        scopedAliasesPass.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.foo",
        scopedAliasesPass.getAliasUsages().get(0).applyAlias().toString());
  }

  @Test
  public void testAliasDefinitionInGlobalScope() {
    String input = "goog.scope(function() {" +
        "});" +
        "goog.x = 1;";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  @Test
  public void testAliasDefinitionInFunctionScope() {
    String input = "function foo() {" +
        "  goog.scope(function() {" +
        "    var x = goog.x;" +
        "  });" +
        "  goog.x = 1;" +
        "}" +
        "foo();";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  @Test
  public void testAliasDefinitionInBlockScope() {
    String input = "function foo() {" +
        "  if (true) {" +
        "    goog.scope(function() {" +
        "      var x = goog.x;" +
        "    });" +
        "  }" +
        "  goog.x = 1;" +
        "}" +
        "foo();";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  @Test
  public void testAliasDefinitionInLoopScope() {
    String input = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    goog.scope(function() {" +
        "      var x = goog.x;" +
        "    });" +
        "  }" +
        "  goog.x = 1;" +
        "}" +
        "foo();";

    Node root = parseTree(input);
    scopedAliasesPass.process(null, root);

    List<DiagnosticType> errors = compiler.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  private Node parseTree(String input) {
    Node root = NodeUtil.parseTree(input, null, null);
    compiler.compile(ImmutableList.of(root), null, null, null);
    return root;
  }
}