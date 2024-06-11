import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.jscomp.test_utils.AbstractCompilerTestCase;

public class ScopedAliasesTest extends AbstractCompilerTestCase {

 @Test
 public void testScopedAliases() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  var dom = goog.dom; \n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  var element = dom.createElement(DIV);\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertCompilationSucceeded(results);
 assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
     results.get(0).getOutputContent().toString());
 }

 @Test
 public void testSimpleScopeFunction() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  function test() {\n" +
         "    return 'test';\n" +
         "  }\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertCompilationSucceeded(results);

 assertEquals("function test(){\n" +
     "  [native code]\n" +
     "}\n" +
     "test", results.get(0).getOutputContent().toString());
 }

 @Test
 public void testInvalidScopeFunctionCall() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  goog.scope(function() {\n" +
         "  });\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "goog.scope used improperly: The call to goog.scope must be alone in a single statement.");
 }

 @Test
 public void testScopeFunctionWithThis() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  this.test = 'test';\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "goog.scope references 'this'");
 }

 @Test
 public void testScopeFunctionWithReturn() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  return 'test';\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "goog.scope uses return");
 }

 @Test
 public void testScopeFunctionWithThrow() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  throw new Error('test');\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "goog.scope uses throw");
 }

 @Test
 public void testScopeFunctionWithRedefinedAlias() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  var goog = {\n" +
         "    dom: {\n" +
         "      TagName: {\n" +
         "        DIV: 'DIV'\n" +
         "      }\n" +
         "    }\n" +
         "  };\n" +
         "  var DIV = goog.dom.TagName.DIV;\n" +
         "});\n" +
         "goog.scope(function() {\n" +
         "  var DIV = 'DIV';\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "alias DIV is assigned a value more than once");
 }

 @Test
 public void testScopeFunctionWithNonAliasLocalVariable() {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
 compiler.setOptions(options);

 String sourceCode = "goog.scope(function() {\n" +
         "  var test = 0;\n" +
         "});";

 SimpleSourceFile source = new SimpleSourceFile("Test.java", sourceCode);
 List<AbstractCompiler.CompilationResult> results =
     compiler.compile(new Iterable<SourceFile>() {
       @Override
       public Iterator<SourceFile> iterator() {
         return Iterators.singleton(source).iterator();
       }
     });

 assertFailure(results, "local variable test is in a goog.scope and is not an alias");
 }
}