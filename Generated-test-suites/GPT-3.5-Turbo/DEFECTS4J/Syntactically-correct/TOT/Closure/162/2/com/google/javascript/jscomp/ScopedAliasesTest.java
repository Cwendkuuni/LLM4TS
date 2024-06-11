import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopedAliasesTest {
  
  private ScopedAliases scopedAliases;
  
  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases();
  }
  
  @Test
  public void testProcess_withValidGoogScopeBlock_shouldApplyAliasesAndRemoveAliasDefinitionsAndCollapseScopes() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "});";
                   
    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV);";
    
    // Act
    String actualOutput = scopedAliases.process(input);
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testProcess_withInvalidGoogScopeBlock_shouldReturnNull() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "  var invalid = 123;\n" +
                   "});";
                   
    // Act
    String actualOutput = scopedAliases.process(input);
    
    // Assert
    assertNull(actualOutput);
  }
  
  @Test
  public void testProcess_withEmptyGoogScopeBlock_shouldDoNothingAndReturnInput() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "});";
                   
    // Act
    String actualOutput = scopedAliases.process(input);
    
    // Assert
    assertEquals(input, actualOutput);
  }
  
  @Test
  public void testHotSwapScript_withValidGoogScopeBlock_shouldApplyAliasesAndRemoveAliasDefinitionsAndCollapseScopes() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "});";
                   
    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV);";
    
    // Act
    String actualOutput = scopedAliases.hotSwapScript(input);
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testHotSwapScript_withInvalidGoogScopeBlock_shouldReturnNull() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "  var dom = goog.dom;\n" +
                   "  var DIV = dom.TagName.DIV;\n" +
                   "  dom.createElement(DIV);\n" +
                   "  var invalid = 123;\n" +
                   "});";
                   
    // Act
    String actualOutput = scopedAliases.hotSwapScript(input);
    
    // Assert
    assertNull(actualOutput);
  }
  
  @Test
  public void testHotSwapScript_withEmptyGoogScopeBlock_shouldDoNothingAndReturnInput() {
    // Arrange
    String input = "goog.scope(function() {\n" +
                   "});";
                   
    // Act
    String actualOutput = scopedAliases.hotSwapScript(input);
    
    // Assert
    assertEquals(input, actualOutput);
  }
}