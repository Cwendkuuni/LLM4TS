import static com.google.javascript.jscomp.JSCereal.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.rhino.Node;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class VarCheckTest {

  private static Compiler compile(String inputFileName, String expectedOutputFileName)
      throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setExternsInputFile(new File("externs.js"));
    options.setOutputFile(new File(expectedOutputFileName));
    options.setWarningLevel(JSError.ALL);
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = compiler.compile(options, inputFileName, null, null, diagnostics);
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());
    return compiler;
  }

  @org.junit.Test
  public void testVarCheck() throws IOException {
    Compiler compiler = compile("TestFile1.js", "TestFile1-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), null);
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());

    CompilerInput input = compiler.getExternsInput();
    Node externs = input.getAstRoot(compiler);
    VarCheck varCheckInstance = new VarCheck(compiler, true);
    varCheckInstance.process(externs, null);

    compiler.dispose();
  }

  @org.junit.Test
  public void testUndefinedVarError() throws IOException {
    Compiler compiler = compile("TestFile2.js", "TestFile2-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler);
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = Node.newProgram();

    Node undeclaredVarNode = Node.newName("undeclaredVar");
    rootNode.addChildToBack(undeclaredVarNode);

    varCheck.process(null, rootNode);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), rootNode);

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());
    assertEquals(
        "variable undefinedVar is undefined",
        diagnostics.getError(0).getMessage());

    compiler.dispose();
  }

  @org.junit.Test
  public void testViolatedModuleDependencyError() throws IOException {
    Compiler compiler = compile("TestFile3.js", "TestFile3-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler);
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = Node.newProgram();

    // TestFile3.js
    Node module1 = Node.newBlock(1, "module1");
    rootNode.addChildToBack(module1);

    Node var1 = Node.newName("var1");
    module1.addChildToBack(Node.newVar("var1"));
    module1.addChildToBack(var1);

    Node module2 = Node.newBlock(1, "module2");
    rootNode.addChildToBack(module2);

    Node var2 = Node.newName("var2");
    module2.addChildToBack(Node.newVar("var2"));
    module2.addChildToBack(var2);

    Node importStatement = Node.newImport(Token.IMPORT, "module1");
    module2.addChildToBack(importStatement);

    varCheck.process(null, rootNode);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), rootNode);

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("module module2 cannot reference var1, defined " +
            "in module module1, since module module1 loads after " +
            "module module2", diagnostics.getError(0).getMessage());

    compiler.dispose();
  }

  @org.junit.Test
  public void testMissingModuleDependencyWarning() throws IOException {
    Compiler compiler = compile("TestFile4.js", "TestFile4-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler);
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = Node.newProgram();

    // TestFile4.js
    Node module1 = Node.newBlock(1, "module1");
    rootNode.addChildToBack(module1);

    Node var1 = Node.newName("var1");
    module1.addChildToBack(Node.newVar("var1"));
    module1.addChildToBack(var1);

    Node module2 = Node.newBlock(1, "module2");
    rootNode.addChildToBack(module2);

    Node var2 = Node.newName("var2");
    module2.addChildToBack(Node.newVar("var2"));
    module2.addChildToBack(var2);

    Node importStatement = Node.newImport(Token.IMPORT, "module3"); // Wrong module
    module2.addChildToBack(importStatement);

    varCheck.process(null, rootNode);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), rootNode);

    assertEquals(1, diagnostics.getNumberOfWarnings());
    assertEquals("missing module dependency; module module2 should depend " +
            "on module module3 because it references var2",
            diagnostics.getWarning(0).getMessage());

    compiler.dispose();
  }

  @org.junit.Test
  public void testStrictModuleDependencyError() throws IOException {
    Compiler compiler = compile("TestFile5.js", "TestFile5-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler, true);
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = Node.newProgram();

    // TestFile5.js
    Node module1 = Node.newBlock(1, "module1");
    rootNode.addChildToBack(module1);

    Node var1 = Node.newName("var1");
    module1.addChildToBack(Node.newVar("var1"));
    module1.addChildToBack(var1);

    Node module2 = Node.newBlock(1, "module2");
    rootNode.addChildToBack(module2);

    Node var2 = Node.newName("var2");
    module2.addChildToBack(Node.newVar("var2"));
    module2.addChildToBack(var2);

    Node importStatement = Node.newImport(Token.IMPORT, "module1"); // Wrong module
    module2.addChildToBack(importStatement);

    varCheck.process(null, rootNode);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), rootNode);

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("module module2 cannot reference var1, defined " +
            "in module module1", diagnostics.getError(0).getMessage());

    compiler.dispose();
  }

  @org.junit.Test
  public void testNameReferenceInExternsWarning() throws IOException {
    Compiler compiler = compile("TestFile6.js", "TestFile6-compiled.js");
    CompilerPass varCheck = new VarCheck(compiler);
    Diagnostics diagnostics = new Diagnostics();
    Node rootNode = Node.newProgram();

    // TestFile6.js
    Node externs = Node.newBlock(1, "externs");
    rootNode.addChildToBack(externs);

    Node var1 = Node.newName("var1");
    externs.addChildToBack(Node.newVar("var1"));
    externs.addChildToBack(var1);

    Node module1 = Node.newBlock(1, "module1");
    rootNode.addChildToBack(module1);

    Node var2 = Node.newName("var2");
    module1.addChildToBack(Node.newVar("var2"));
    module1.addChildToBack(var2);

    Node getPropStatement = Node.newGetProp(Token.GETPROP, Node.newName("externs"), var1);
    module1.addChildToBack(getPropStatement);

    varCheck.process(null, rootNode);
    diagnostics.clear();
    compiler.process(new Node(Token.PROGRAM), rootNode);

    assertEquals(1, diagnostics.getNumberOfWarnings());
    assertEquals("accessing name var1 in externs has no effect",
            diagnostics.getWarning(0).getMessage());

    compiler.dispose();
  }
}