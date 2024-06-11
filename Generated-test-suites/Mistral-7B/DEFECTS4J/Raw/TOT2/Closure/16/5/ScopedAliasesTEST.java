import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.TreePrinter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScopedAliasesTest {

  private Compiler compiler;
  private SourceFile sourceFile;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testSimpleAlias() {
    String jsCode =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  assertEquals('x', x);" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.singletonList(AliasTransformation.GOOG_X));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("x", traversal.getAliasUsages().get(0).getAliasReference().getString());
  }

  @Test
  public void testAliasWithNamespace() {
    String jsCode =
        "goog.scope(function() {" +
        "  var goog = {" +
        "    x: 1," +
        "  };" +
        "  var g = goog;" +
        "  var x = g.x;" +
        "  assertEquals(1, x);" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.singletonList(AliasTransformation.GOOG));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("goog", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog", traversal.getAliasUsages().get(0).getAliasReference().getString());
  }

  @Test
  public void testMultipleAliases() {
    String jsCode =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var y = goog.y;" +
        "  assertEquals('x', x);" +
        "  assertEquals('y', y);" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(
        Arrays.asList(AliasTransformation.GOOG_X, AliasTransformation.GOOG_Y));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.y", traversal.getAliasDefinitionsInOrder().get(1).getString());
    assertEquals("x", traversal.getAliasUsages().get(0).getAliasReference().getString());
    assertEquals("y", traversal.getAliasUsages().get(1).getAliasReference().getString());
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefined() {
    String jsCode =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var x = 1;" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.singletonList(AliasTransformation.GOOG_X));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    fail("Should have thrown an error: " + GOOG_SCOPE_ALIAS_REDEFINED);
  }

  @Test(expected = AssertionError.class)
  public void testAliasNonLocal() {
    String jsCode =
        "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.singletonList(AliasTransformation.GOOG_X));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    fail("Should have thrown an error: " + GOOG_SCOPE_NON_ALIAS_LOCAL);
  }

  @Test(expected = AssertionError.class)
  public void testScopeWithReturnStatement() {
    String jsCode =
        "goog.scope(function() {" +
        "  return 1;" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.emptyList());
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    fail("Should have thrown an error: " + GOOG_SCOPE_USES_RETURN);
  }

  @Test(expected = AssertionError.class)
  public void testScopeWithThrowStatement() {
    String jsCode =
        "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.emptyList());
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    fail("Should have thrown an error: " + GOOG_SCOPE_USES_THROW);
  }

  @Test
  public void testPrintTree() throws IOException {
    String jsCode =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  assertEquals('x', x);" +
        "});";

    sourceFile = new SourceFile("Test.js", jsCode);
    options.setAliasTransformations(Collections.singletonList(AliasTransformation.GOOG_X));
    options.setAliasTransformationHandler(transformationHandler);

    Traversal traversal = new Traversal();
    compiler.process(sourceFile, options, traversal);

    TreePrinter printer = new TreePrinter(100);
    String result = printer.printTree(traversal.getRootNode());
    System.out.println(result);
  }
}