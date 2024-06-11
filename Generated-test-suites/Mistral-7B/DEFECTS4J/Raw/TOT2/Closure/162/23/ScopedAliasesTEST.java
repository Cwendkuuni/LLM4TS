import static org.junit.Assert.*;
import static com.google.javascript.jscomp.tests.testing.TestUtils.*;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

 private static AbstractCompiler createCompiler() {
 return new Compiler();
 }

 @Test
 public void testScopedAliases() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  var dom = goog.dom;\n" +
 "  var DIV = dom.TagName.DIV;\n" +
 "  var div = DOM.createElement(DIV);\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 Node rootNode = parser(sourceCode);
 List<Diagnostic> diagnostics = scopedAliases.process(null, rootNode);
 assertTrue("Expected no diagnostics", diagnostics.isEmpty());

 Node transformedNode = scopedAliases.hotSwapScript(rootNode, null);
 String expectedTransformation = "goog.dom.createElement(goog.dom.TagName.DIV)";
 assertEquals(expectedTransformation, transformedNode.getQualifiedName());
 }

 @Test
 public void testScopedAliasesWithScopeCallAtRoot() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  goog.scope(function() {\n" +
 "    var dom = goog.dom;\n" +
 "    var DIV = dom.TagName.DIV;\n" +
 "    var div = DOM.createElement(DIV);\n" +
 "  });\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 Node rootNode = parser(sourceCode);
 List<Diagnostic> diagnostics = scopedAliases.process(null, rootNode);
 assertTrue("Expected no diagnostics", diagnostics.isEmpty());

 Node transformedNode = scopedAliases.hotSwapScript(rootNode, null);
 String expectedTransformation = "goog.dom.DOM.createElement(goog.dom.TagName.DIV)";
 assertEquals(expectedTransformation, transformedNode.getQualifiedName());
 }

 @Test
 public void testScopedAliasesWithAliasRedefinition() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  var dom = goog.dom;\n" +
 "  var DIV = dom.TagName.DIV;\n" +
 "  var DIV = 'div';\n" +
 "  var div = DOM.createElement(DIV);\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 List<Diagnostic> diagnostics = scopedAliases.process(null, parser(sourceCode));

 // The expected error message is long, so we omit it here.
 // It can be found in the JSC_GOOG_SCOPE_ALIAS_REDEFINED diagnostic type.
 assertFalse("Expected error", diagnostics.isEmpty());
 }

 @Test
 public void testScopedAliasesWithAliasUsageOutsideOfScope() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  var dom = goog.dom;\n" +
 "  var DIV = dom.TagName.DIV;\n" +
 "});\n" +
 "\n" +
 "var div = DOM.createElement(DIV);";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 List<Diagnostic> diagnostics = scopedAliases.process(null, parser(sourceCode));

 // The expected error message is long, so we omit it here.
 // It can be found in the JSC_GOOG_SCOPE_NON_ALIAS_LOCAL diagnostic type.
 assertFalse("Expected error", diagnostics.isEmpty());
 }

 @Test
 public void testScopedAliasesWithThrows() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  try {\n" +
 "    throw new Error('test');\n" +
 "  } catch (e) {\n" +
 "    goog.log(e);\n" +
 "  }\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 List<Diagnostic> diagnostics = scopedAliases.process(null, parser(sourceCode));
 assertTrue("Expected no diagnostics", diagnostics.isEmpty());
 }

 @Test
 public void testScopedAliasesWithReturns() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  return 'test';\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 List<Diagnostic> diagnostics = scopedAliases.process(null, parser(sourceCode));
 assertTrue("Expected no diagnostics", diagnostics.isEmpty());
 }

 @Test
 public void testScopedAliasesWithThis() {
 CompilerOptions options = new CompilerOptions();
 options.setAliasTransformation(AliasTransformation.WEAK);
 options.setAliasTransformationHandler(new DefaultAliasTransformationHandler());

 AbstractCompiler compiler = createCompiler();
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
 "  this.dom = goog.dom;\n" +
 "});";

 ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
 List<Diagnostic> diagnostics = scopedAliases.process(null, parser(sourceCode));
 assertTrue("Expected no diagnostics", diagnostics.isEmpty());
 }
}