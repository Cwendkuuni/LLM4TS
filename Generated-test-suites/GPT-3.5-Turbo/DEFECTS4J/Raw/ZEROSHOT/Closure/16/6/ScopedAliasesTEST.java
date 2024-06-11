import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {
  
  @Test
  public void testProcess() {
    String[] externs = {
      "/**",
      " * @fileoverview Externs for the Closure Compiler",
      " * @externs",
      " */",
      "var goog = {};",
      "goog.scope = function(fn) { fn.call(goog); };"
    };
  
    String[] testCode = {
      "goog.scope(function() {",
      "  var dom = goog.dom;",
      "  var DIV = dom.TagName.DIV;",
      "  dom.createElement(DIV);",
      "});"
    };
  
    // Set up the test compiler
    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
  
    // Create the test externs input
    List<SourceFile> externFiles = new ArrayList<SourceFile>();
    for (String extern : externs) {
      externFiles.add(SourceFile.fromCode("externs", extern));
    }
  
    // Create the test code input
    List<SourceFile> codeFiles = new ArrayList<SourceFile>();
    for (String code : testCode) {
      codeFiles.add(SourceFile.fromCode("code", code));
    }
  
    // Compile the test files
    Result result = compiler.compile(externFiles, codeFiles, new CompilerOptions());
  
    // Check the compiled code
    // ...
  }
}