import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  private RenameLabels renameLabels;
  
  @Before
  public void setUp() {
    renameLabels = new RenameLabels();
  }
  
  @Test
  public void testProcess_withNoLabels_shouldNotRenameAnyLabels() {
    String input = "function foo() {\n" 
                 + "  var a = 1;\n"
                 + "}";
    
    String expectedOutput = "function foo() {\n" 
                          + "  var a = 1;\n"
                          + "}";
    
    assertEquals(expectedOutput, renameLabels.process(input));
  }
  
  @Test
  public void testProcess_withUnusedLabel_shouldRemoveLabel() {
    String input = "function foo() {\n" 
                 + "  a: {\n"
                 + "    var a = 1;\n"
                 + "  }\n"
                 + "}";
    
    String expectedOutput = "function foo() {\n" 
                          + "  {\n"
                          + "    var a = 1;\n"
                          + "  }\n"
                          + "}";
    
    assertEquals(expectedOutput, renameLabels.process(input));
  }
  
  @Test
  public void testProcess_withUsedLabel_shouldRenameLabel() {
    String input = "function foo() {\n" 
                 + "  a: {\n"
                 + "    var a = 1;\n"
                 + "    break a;\n"
                 + "  }\n"
                 + "}";
    
    String expectedOutput = "function foo() {\n" 
                          + "  b: {\n"
                          + "    var b = 1;\n"
                          + "    break b;\n"
                          + "  }\n"
                          + "}";
    
    assertEquals(expectedOutput, renameLabels.process(input));
  }
  
  @Test
  public void testGetNameForId_withValidId_shouldReturnCorrectName() {
    assertEquals("a", renameLabels.getNameForId(1));
    assertEquals("b", renameLabels.getNameForId(2));
    assertEquals("c", renameLabels.getNameForId(3));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetNameForId_withInvalidId_shouldThrowIndexOutOfBoundsException() {
    renameLabels.getNameForId(0);
  }
  
  @Test
  public void testGetLabelInfo_withExistingLabel_shouldReturnLabelInfo() {
    LabelInfo labelInfo = new LabelInfo(1);
    renameLabels.namespaceStack.peek().renameMap.put("a", labelInfo);
    
    assertEquals(labelInfo, renameLabels.getLabelInfo("a"));
  }
  
  @Test
  public void testGetLabelInfo_withNonExistingLabel_shouldReturnNull() {
    assertNull(renameLabels.getLabelInfo("a"));
  }
  
  @Test
  public void testLabelInfoConstructor_withInitializedId_shouldSetId() {
    LabelInfo labelInfo = new LabelInfo(1);
    assertEquals(1, labelInfo.id);
  }
  
  @Test
  public void testLabelInfoConstructor_withInitializedReferenced_shouldSetReferencedToFalse() {
    LabelInfo labelInfo = new LabelInfo(1);
    assertFalse(labelInfo.referenced);
  }
  
  @Test
  public void testProcess_withLabels_shouldRenameAndRemoveLabelsCorrectly() {
    String input = "function bar() {\n"
                 + "  a: {\n"
                 + "    b: {\n"
                 + "      foo();\n"
                 + "    }\n"
                 + "  }\n"
                 + "  a: {\n"
                 + "    b: break a;\n"
                 + "  }\n"
                 + "}";
    
    String expectedOutput = "function bar() {\n"
                          + "  a: {\n"
                          + "    b: {\n"
                          + "      foo();\n"
                          + "    }\n"
                          + "  }\n"
                          + "  a: {\n"
                          + "    b: break b;\n"
                          + "  }\n"
                          + "}";
    
    assertEquals(expectedOutput, renameLabels.process(input));
  }

}