import static org.junit.Assert.*;
import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    // Test ProcessLabels class functionality
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    
    // Test enterScope method
    assertEquals(1, processLabels.namespaceStack.size());
    
    // Test exitScope method
    processLabels.exitScope(null);
    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testDefaultNameSupplier() {
    // Test DefaultNameSupplier class functionality
    RenameLabels.DefaultNameSupplier defaultNameSupplier = new RenameLabels.DefaultNameSupplier();
    
    // Test generateNextName method
    assertEquals("a", defaultNameSupplier.get());
    assertEquals("b", defaultNameSupplier.get());
  }

  @Test
  public void testLabelInfo() {
    // Test LabelInfo class functionality
    RenameLabels.LabelInfo labelInfo = new RenameLabels.LabelInfo(1);
    
    // Test referenced field
    assertFalse(labelInfo.referenced);
    
    // Test id field
    assertEquals(1, labelInfo.id);
  }

  @Test
  public void testLabelNamespace() {
    // Test LabelNamespace class functionality
    RenameLabels.LabelNamespace labelNamespace = new RenameLabels.LabelNamespace();
    
    // Test renameMap field
    assertNotNull(labelNamespace.renameMap);
  }
}