import static org.junit.Assert.*;

import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {
  
  private Compiler compiler;
  
  @Before
  public void setUp() {
    compiler = new Compiler();
  }
  
  @Test
  public void testCollapseVariableDeclarations_singleVarDeclaration_success() {
    String input = "var a = 1;";
    String expectedOutput = "var a = 1;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_multipleVarDeclarations_success() {
    String input = "var a = 1; var b = 2; var c = 3;";
    String expectedOutput = "var a = 1, b = 2, c = 3;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_varAndAssignTogether_success() {
    String input = "var a; a = 1; var b = 2; var c = 3;";
    String expectedOutput = "var a, b = 2, c = 3; a = 1;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_assignsOnly_success() {
    String input = "a = true; b = true; var c = true;";
    String expectedOutput = "var c = true, b = true, a = true;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_singleVarDeclarationWithJSDoc_success() {
    String input = "/** @type {number} */ var a = 1;";
    String expectedOutput = "/** @type {number} */ var a = 1;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_duplicateVarsWithJSDoc_success() {
    String input = "/** @type {number} */ var a = 1; /** @type {number} */ var a = 2;";
    String expectedOutput = "/** @type {number} */ var a = 2;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_multipleVarDeclarationsAndAssigns_success() {
    String input = "var a = 1; a = 2; var b = 3; var c = 4; b = 5;";
    String expectedOutput = "var a = 2, b = 5, c = 4;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_noVarDeclarationsNoChange_success() {
    String input = "a = 1; b = 2; c = 3;";
    String expectedOutput = "a = 1; b = 2; c = 3;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_emptyCode_noChange_success() {
    String input = "";
    String expectedOutput = "";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_multipleVarWithAssignmentsForReuse_success() {
    String input = "var a = 1; if (foo) { a = 2; } else { a = 3; }";
    String expectedOutput = "var a = foo ? 2 : 3;";
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  private void assertCollapseVariableDeclarations(String input, String expectedOutput) {
    List<SourceFile> inputFiles = Arrays.asList(SourceFile.fromCode("input.js", input));
    
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    compiler.initOptions(options);
    pass.process(compiler.parseInputs(inputFiles), compiler.getRoot());

    assertEquals(expectedOutput, compiler.toSource());
    assertTrue(compiler.getErrors().length == 0);
  }
  
  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarations_invalidInput_fail() {
    String input = "var a = 1; var b = 2; var c = 3;";
    String expectedOutput = "var a = 1, b = 2, c = 4;"; // Invalid expected output
    
    assertCollapseVariableDeclarations(input, expectedOutput);
  }
  
  @Test(expected = JSError.class)
  public void testCollapseVariableDeclarations_compileError_throwJSError() {
    String input = "var a = 1; a = {b: 2};";
    
    List<SourceFile> inputFiles = Arrays.asList(SourceFile.fromCode("input.js", input));
    
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    compiler.initOptions(options);
    pass.process(compiler.parseInputs(inputFiles), compiler.getRoot());
    
    assertEquals(1, compiler.getErrors().length);
    assertEquals(JSError.Type.INVALID_ASSIGNMENT, compiler.getErrors()[0].getType());
    assertEquals("assignment to property b of a value with unknown type", compiler.getErrors()[0].getDescription());
    assertEquals(2, compiler.getErrors()[0].getLineNumber());
    assertEquals(17, compiler.getErrors()[0].getCharno());
  }
}