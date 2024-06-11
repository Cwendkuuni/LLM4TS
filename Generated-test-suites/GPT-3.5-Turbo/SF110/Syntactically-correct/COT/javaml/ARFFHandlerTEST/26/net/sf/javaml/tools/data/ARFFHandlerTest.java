package net.sf.javaml.tools.data;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

  @Test
  public void testLoadARFF() {
    // Test loading ARFF file
    File file = new File("test.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file);
      Assert.assertNotNull(dataset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadARFFWithClassIndex() {
    // Test loading ARFF file with class index
    File file = new File("test.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file, 0);
      Assert.assertNotNull(dataset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadARFFWithInvalidFile() {
    // Test loading ARFF file with invalid file path
    File file = new File("invalid.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file);
      Assert.assertNull(dataset);
      Assert.fail("Expected FileNotFoundException was not thrown.");
    } catch (FileNotFoundException e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testLoadARFFWithInvalidClassIndex() {
    // Test loading ARFF file with invalid class index
    File file = new File("test.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file, -1);
      Assert.assertNotNull(dataset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadARFFWithNoData() {
    // Test loading ARFF file with no data
    File file = new File("empty.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file);
      Assert.assertNotNull(dataset);
      Assert.assertEquals(0, dataset.size());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadARFFWithNaNValues() {
    // Test loading ARFF file with NaN values
    File file = new File("nan.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file);
      Assert.assertNotNull(dataset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}