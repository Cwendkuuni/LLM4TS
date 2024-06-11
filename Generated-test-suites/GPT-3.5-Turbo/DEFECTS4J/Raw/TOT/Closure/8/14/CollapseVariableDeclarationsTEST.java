import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CollapseVariableDeclarationsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private Node root;
  private CollapseVariableDeclarations pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    pass = new CollapseVariableDeclarations(compiler);
  }

  @Test
  public void testCollapseVariableDeclarations_SingleDeclaration() {
    // Arrange
    String input = "var a;";
    String expected = "var a;";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarations_MultipleDeclarations() {
    // Arrange
    String input = "var a;\nvar b = 1;\nvar c = 2;";
    String expected = "var a, b = 1, c = 2;";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarations_WithAssignment() {
    // Arrange
    String input = "a = true;\nb = true;\nvar c = true;";
    String expected = "var c = b = a = true;";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarations_InsideFunction() {
    // Arrange
    String input = "function test() {\nvar a;\nvar b = 1;\nvar c = 2;\n}";
    String expected = "function test() {\nvar a, b = 1, c = 2;\n}";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarations_PartialFunctionDeclaration() {
    // Arrange
    String input = "function test() {\nvar a;\nconsole.log(a);\nvar b = 1;\nvar c = 2;\n}";
    String expected = "function test() {\nvar a;\nconsole.log(a);\nvar b = 1, c = 2;\n}";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarations_MultipleOccurrencesInFunction() {
    // Arrange
    String input = "function test() {\nvar a;\nconsole.log(a);\na = 1;\nconsole.log(a);\n}";
    String expected = "function test() {\nvar a;\nconsole.log(a);\na = 1;\nconsole.log(a);\n}";
    Node rootNode = parse(input);
  
    // Act
    pass.process(null, rootNode);
    String actual = compiler.toSource(rootNode);
  
    // Assert
    assertEquals(expected, actual);
  }

  private Node parse(String code) {
    return compiler.parseTestCode(code);
  }

}