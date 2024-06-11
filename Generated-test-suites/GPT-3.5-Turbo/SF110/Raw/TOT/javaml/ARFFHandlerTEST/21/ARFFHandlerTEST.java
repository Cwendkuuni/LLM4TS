import static org.junit.Assert.*;
import org.junit.*;

public class ARFFHandlerTest {

  // Test case for loadARFF(File file)
  @Test
  public void testLoadARFF_File() {
    // Typical use case
    File typicalFile = new File("typical.arff"); 
    Dataset typicalDataset = ARFFHandler.loadARFF(typicalFile);
    assertNotNull(typicalDataset);
    assertEquals(10, typicalDataset.size());

    // Edge case: Empty file
    File emptyFile = new File("empty.arff"); 
    Dataset emptyDataset = ARFFHandler.loadARFF(emptyFile);
    assertNotNull(emptyDataset);
    assertTrue(emptyDataset.isEmpty());

    // Error scenario: Non-existent file
    File nonExistentFile = new File("nonexistent.arff"); 
    try {
      Dataset dataset = ARFFHandler.loadARFF(nonExistentFile);
      fail("Expected FileNotFoundException");
    } catch (FileNotFoundException e) {
      // FileNotFoundException expected
    }
  }

  // Test case for loadARFF(File file, int classIndex)
  @Test
  public void testLoadARFF_FileAndIndex() {
    // Typical use case
    File typicalFile = new File("typical.arff");
    int typicalClassIndex = 4;
    Dataset typicalDataset = ARFFHandler.loadARFF(typicalFile, typicalClassIndex);
    assertNotNull(typicalDataset);
    assertEquals(10, typicalDataset.size());

    // Edge case: Empty file
    File emptyFile = new File("empty.arff");
    int emptyClassIndex = 0;
    Dataset emptyDataset = ARFFHandler.loadARFF(emptyFile, emptyClassIndex);
    assertNotNull(emptyDataset);
    assertTrue(emptyDataset.isEmpty());

    // Error scenario: Non-existent file
    File nonExistentFile = new File("nonexistent.arff");
    int nonExistentClassIndex = 2;
    try {
      Dataset dataset = ARFFHandler.loadARFF(nonExistentFile, nonExistentClassIndex);
      fail("Expected FileNotFoundException");
    } catch (FileNotFoundException e) {
      // FileNotFoundException expected
    }
  }

}