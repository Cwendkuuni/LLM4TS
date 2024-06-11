package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {

  @Test
  public void testProcess() {
    // Given: a simple JavaScript code snippet
    String sourceCode = "function foo(a, b) { var c = a + b; return c; }";

    // When: processing the JavaScript code with RenameVars
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
    Node externs = null;
    Node root = AstHelper.parse(sourceCode, null, null, null, null, null, null, null, null, null, null);
    renameVars.process(externs, root);

    // Then: the processed code should not contain any name conflicts
    Node processedCode = compiler.getProcessedSource();
    assertEquals(sourceCode, processedCode.toString());

    // When: processing the JavaScript code with RenameVars again
    renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
    renameVars.process(externs, root);

    // Then: the processed code should be the same as before
    processedCode = compiler.getProcessedSource();
    assertEquals(sourceCode, processedCode.toString());

    // When: processing a JavaScript code snippet with name conflicts
    sourceCode = "function foo(a, b) { var a = b; return a; }";

    // Then: RenameVars should throw an exception
    assertThrows(IllegalArgumentException.class, () -> {
      renameVars.process(externs, AstHelper.parse(sourceCode, null, null, null, null, null, null, null, null, null, null));
    });
  }

  @Test
  public void testGetVariableMap() {
    // Given: a JavaScript code snippet
    String sourceCode = "function foo(a, b) { var c = a + b; return c; }";

    // When: processing the JavaScript code with RenameVars
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
    Node externs = null;
    Node root = AstHelper.parse(sourceCode, null, null, null, null, null, null, null, null, null, null);
    renameVars.process(externs, root);

    // Then: the variable map should contain the name mappings
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals(1, variableMap.size());
    assertEquals("a", variableMap.lookupName("c"));
    assertNull(variableMap.lookupName("a"));
    assertNull(variableMap.lookupName("b"));
  }

  @Test
  public void testOkToRenameVar() {
    // Given: a RenameVars instance
    RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);

    // When: checking whether a local variable name is okay to rename
    String localVariableName = "localVar";
    assertTrue(renameVars.okToRenameVar(localVariableName, true));

    // When: checking whether a global variable name is okay to rename
    String globalVariableName = "globalVar";
    assertTrue(renameVars.okToRenameVar(globalVariableName, false));

    // When: checking whether a reserved variable name is okay to rename
    String reservedVariableName = "Math";
    assertFalse(renameVars.okToRenameVar(reservedVariableName, false));

    // When: checking whether an exported variable name is okay to rename
    String exportedVariableName = "exports";
    assertFalse(renameVars.okToRenameVar(exportedVariableName, false));
  }

  private static AstHelper AstHelper;

  static {
    try {
      AstHelper = new AstHelper();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}