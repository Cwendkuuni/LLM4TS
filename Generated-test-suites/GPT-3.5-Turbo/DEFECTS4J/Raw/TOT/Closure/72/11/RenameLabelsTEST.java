import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.RenameLabels;

public class RenameLabelsTest {
  
  private AbstractCompiler compiler;
  private RenameLabels renameLabels;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    renameLabels = new RenameLabels(compiler);
  }
  
  @Test
  public void testProcess_noLabels_shouldNotRenameOrRemoveAnyLabels() {
    // Arrange
    String inputCode = "function foo() { return true; }";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that labels are not renamed or removed
    Object[] labels = renameLabels.namespaceStack.getLast().renameMap.keySet().toArray();
    Assert.assertEquals(0, labels.length);
  }
  
  @Test
  public void testProcess_singleLabel_shouldRenameLabel() {
    // Arrange
    String inputCode = "function foo() { myLabel: return true; }";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that the label is renamed
    String oldName = "myLabel";
    String newName = renameLabels.names.get(0);
    Assert.assertNotEquals(oldName, newName);
    Assert.assertTrue(renameLabels.namespaceStack.getLast().renameMap.containsKey(newName));
    Assert.assertFalse(renameLabels.namespaceStack.getLast().renameMap.containsKey(oldName));
  }
  
  @Test
  public void testProcess_unusedLabel_shouldRemoveLabel() {
    // Arrange
    String inputCode = "function foo() { unusedLabel: return true; }";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that the label is removed
    Assert.assertTrue(renameLabels.namespaceStack.getLast().renameMap.isEmpty());
  }
  
  @Test
  public void testProcess_referencedLabel_shouldNotRemoveLabel() {
    // Arrange
    String inputCode = "function foo() { referencedLabel: return true; }";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that the referenced label is not removed
    String referencedLabel = "referencedLabel";
    Assert.assertTrue(renameLabels.namespaceStack.getLast().renameMap.containsKey(referencedLabel));
  }
  
  @Test
  public void testProcess_breakWithNamedLabel_shouldRenameLabelInBreak() {
    // Arrange
    String inputCode = 
      "function foo() { " +
      "  myLabel: " +
      "  while (true) {" +
      "    break myLabel;" +
      "  }" +
      "}";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that the label in the break statement is renamed
    String oldName = "myLabel";
    String newName = renameLabels.names.get(0);
    Assert.assertNotEquals(oldName, newName);
    Assert.assertEquals(newName, compiler.jsRoot.getLastChild().getLastChild().getLastChild().getFirstChild().getString());
  }
  
  @Test
  public void testProcess_continueWithNamedLabel_shouldRenameLabelInContinue() {
    // Arrange
    String inputCode = 
      "function foo() { " +
      "  myLabel: " +
      "  while (true) {" +
      "    continue myLabel;" +
      "  }" +
      "}";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that the label in the continue statement is renamed
    String oldName = "myLabel";
    String newName = renameLabels.names.get(0);
    Assert.assertNotEquals(oldName, newName);
    Assert.assertEquals(newName, compiler.jsRoot.getLastChild().getLastChild().getLastChild().getFirstChild().getString());
  }
  
  @Test
  public void testProcess_nestedLabels_shouldRenameEachLabel() {
    // Arrange
    String inputCode = 
      "function foo() { " +
      "  outerLabel: {" +
      "    innerLabel: {" +
      "      return true;" +
      "    }" +
      "  }" +
      "}";
    compiler.compileCode(inputCode);
    
    // Act
    renameLabels.process(compiler.externsRoot, compiler.jsRoot);
    
    // Assert
    // Verify that each label is renamed appropriately
    String oldOuterName = "outerLabel";
    String oldInnerName = "innerLabel";
    String newOuterName = renameLabels.names.get(0);
    String newInnerName = renameLabels.names.get(1);
    Assert.assertNotEquals(oldOuterName, newOuterName);
    Assert.assertNotEquals(oldInnerName, newInnerName);
    Assert.assertEquals(newOuterName, compiler.jsRoot.getLastChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getString());
    Assert.assertEquals(newInnerName, compiler.jsRoot.getLastChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getString());
  }
  
}