import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import weka.core.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import weka.classifiers.bayes.net.search.ci.*;

public class ICSSearchAlgorithmTest {
  private ICSSearchAlgorithm ics;

  @Before
  public void setUp() {
    ics = new ICSSearchAlgorithm();
  }

  @Test
  public void testSetAndGetMaxCardinality() {
    // Typical use case
    ics.setMaxCardinality(3);
    Assert.assertEquals(3, ics.getMaxCardinality());

    // Edge case
    ics.setMaxCardinality(0);
    Assert.assertEquals(0, ics.getMaxCardinality());

    // Error scenario
    try {
      ics.setMaxCardinality(-1);
      Assert.fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("MaxCardinality cannot be negative", e.getMessage());
    }
  }

  @Test
  public void testName() {
    // Typical use case
    Instances instances = new Instances();
    ics.setInstances(instances);
    Assert.assertEquals("TestName", ics.name(0));

    // Edge case
    instances.deleteAttributeAt(0);
    Assert.assertNull(ics.name(0));

    // Error scenario
    try {
      ics.name(-1);
      Assert.fail("Expected an IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      Assert.assertEquals("Index out of bounds for attribute name", e.getMessage());
    }
  }

  @Test
  public void testMaxn() {
    // Typical use case
    Instances instances = new Instances();
    ics.setInstances(instances);
    Assert.assertEquals(10, ics.maxn());

    // Edge case
    instances.deleteAttributeAt(1);
    instances.deleteAttributeAt(2);
    Assert.assertEquals(8, ics.maxn());
  }

  // Add tests for the remaining methods

}