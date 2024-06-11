import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static Node createSourceNode(String jsSource) {
    SourceFile sf = new SourceFile("test.js", null, jsSource, new NodeFactory());
    return (Node) sf.ast;
  }

  private static void runRenameVars(String jsSource, boolean localRenamingOnly,
      boolean preserveAnonymousFunctionNames, boolean generatePseudoNames) {
    CompilerOptions options = new CompilerOptions();
    options.setGenerateSourceMaps(false);
    options.setOutputFile("output.js");
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Node input = createSourceNode(jsSource);

    RenameVars renameVars = new RenameVars(compiler, null, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, null, null, null);
    renameVars.process(input, null);

    List<String> lines = Lists.newArrayList(jsSource.split("\n"));
    List<String> outputLines = Arrays.asList(new File("output.js").getContent()
        .split("\n"));

    for (int i = 0; i < lines.size(); ++i) {
      String inputLine = lines.get(i);
      String outputLine = outputLines.get(i);
      assertEquals("Line " + (i + 1) + " input != output", inputLine, outputLine);
    }
  }

  @Test
  public void testNoRenaming() {
    String jsSource = "function foo(a, b) {};";
    runRenameVars(jsSource, false, false, false);
  }

  @Test
  public void testLocalRenaming() {
    String jsSource = "function foo(fa, fb) {\n" +
        "  var c = function(d, e) { return fa; }\n" +
        "};";
    runRenameVars(jsSource, true, false, false);

    String expected = "function foo(a, b) {\n" +
        "  var a0 = function(d, e) { return a; }\n" +
        "};";
    runRenameVars(jsSource, true, false, false);
  }

  @Test
  public void testGlobalRenaming() {
    String jsSource = "var a = 1, b = 2, c = 3;";
    runRenameVars(jsSource, false, false, false);

    String expected = "var a0 = 1, b0 = 2, c0 = 3;";
    runRenameVars(jsSource, false, false, false);
  }

  @Test
  public void testAnonymousFunctionPreserved() {
    String jsSource = "function foo() {\n" +
        "  var a = function() { return 1; };\n" +
        "}";
    runRenameVars(jsSource, false, true, false);

    String expected = "function foo() {\n" +
        "  var a = function() { return 1; };\n" +
        "}";
    runRenameVars(jsSource, false, true, false);
  }

  @Test
  public void testNoPseudoNames() {
    String jsSource = "function foo() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  var d = function() {};" +
        "}";
    runRenameVars(jsSource, false, false, false);

    String expected = "function foo() {" +
        "  var a0 = 1, b0 = 2, c0 = 3," +
        "      d0 = function() {};" +
        "}";
    runRenameVars(jsSource, false, false, false);
  }

  @Test
  public void testPseudoNames() {
    String jsSource = "function foo() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  var d = function() {};" +
        "}";
    runRenameVars(jsSource, false, false, true);

    String expected = "function foo() {" +
        "  var $a$0 = 1, $b$0 = 2, $c$0 = 3," +
        "      $d$$ = function() {};" +
        "}";
    runRenameVars(jsSource, false, false, true);
  }

  @Test
  public void testOverlappingNames() {
    String jsSource = "function foo() {" +
        "  var a = 1, b = 2;" +
        "  function a() {}" +
        "}";
    runRenameVars(jsSource, false, false, false);

    String expected = "function foo() {" +
        "  var a0 = 1, b0 = 2," +
        "      a1 = function() {}" +
        "}";
    runRenameVars(jsSource, false, false, false);
  }

  @Test
  public void testExportedNames() {
    String jsSource = "export function foo(a) {}";
    runRenameVars(jsSource, false, false, false);

    String expected = "export function foo(a) {}";
    runRenameVars(jsSource, false, false, false);
  }

  @Test
  public void testLocalExportedNames() {
    String jsSource = "function foo() {" +
        "  var a = 1;" +
        "  export var a;" +
        "}";
    runRenameVars(jsSource, true, false, false);

    String expected = "function foo() {" +
        "  var a0 = 1;" +
        "  export var a0;" +
        "}";
    runRenameVars(jsSource, true, false, false);
  }

  @Test
  public void testRenamingWithPreviousMap() {
    String jsSource = "function foo(a, b) {" +
        "  var c = function(d, e) { return a; };" +
        "  function bar(f, g) { return b; }" +
        "}";

    // The first time
    runRenameVars(jsSource, true, false, false);

    String expectedFirst = "function foo(a, b) {" +
        "  var a0 = function(d, e) { return a; }," +
        "      b0 = function(f, g) { return b; }" +
        "}";
    runRenameVars(jsSource, true, false, false);

    // The second time, we reuse previous map
    runRenameVars(jsSource, true, false, false);

    String expectedSecond = "function foo(a, b) {" +
        "  var a1 = function(d, e) { return a; }," +
        "      b1 = function(f, g) { return b; }" +
        "}";
    runRenameVars(jsSource, true, false, false);
  }

  @Test
  public void testRenamingWithPreviousMapAndPreservingAnonymousFunctionNames() {
    String jsSource = "function foo(a, b) {" +
        "  var c = function(d, e) { return a; };" +
        "  function bar(f, g) { return b; }" +
        "}";

    // The first time
    runRenameVars(jsSource, true, false, false);

    String expectedFirst = "function foo(a, b) {" +
        "  var a0 = function(d, e) { return a; }," +
        "      b0 = function(f, g) { return b; }" +
        "}";
    runRenameVars(jsSource, true, false, false);

    // The second time, we preserve anonymous function names
    runRenameVars(jsSource, true, true, false);

    String expectedSecond = "function foo(a, b) {" +
        "  var c = function(d, e) { return a; }," +
        "      b = function(f, g) { return b; }" +
        "}";
    runRenameVars(jsSource, true, true, false);
  }
}