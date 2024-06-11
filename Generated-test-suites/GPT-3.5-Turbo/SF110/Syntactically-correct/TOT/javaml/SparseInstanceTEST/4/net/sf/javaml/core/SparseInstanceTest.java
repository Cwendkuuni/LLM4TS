import static org.junit.Assert.*;
import org.junit.Test;

public class SparseInstanceTest {

  @Test
  public void testSetNoAttributes() {
    SparseInstance inst = new SparseInstance();
    inst.setNoAttributes(10);
    int result = inst.noAttributes();
    assertEquals(10, result);
  }

  @Test
  public void testSetNoAttributesZero() {
    SparseInstance inst = new SparseInstance();
    inst.setNoAttributes(0);
    int result = inst.noAttributes();
    assertEquals(0, result);
  }

  @Test
  public void testSetNoAttributesNegative() {
    SparseInstance inst = new SparseInstance();
    inst.setNoAttributes(-5);
    int result = inst.noAttributes();
    assertEquals(-5, result);
  }

  @Test
  public void testSparseInstanceNoArgs() {
    SparseInstance inst = new SparseInstance();
    int result = inst.noAttributes();
    assertEquals(-1, result);
  }

  @Test
  public void testSparseInstanceWithNoArgsAndDefaultValue() {
    SparseInstance inst = new SparseInstance();
    double result = inst.value(0);
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  public void testSparseInstanceWithNoArgsAndClassValue() {
    Object classValue = "Test";
    SparseInstance inst = new SparseInstance(classValue);
    assertEquals(classValue, inst.classValue());
  }

  @Test
  public void testSparseInstanceWithNoArgsAndAllParams() {
    Object classValue = "Test";
    SparseInstance inst = new SparseInstance(10, 0.0, classValue);
    int result = inst.noAttributes();
    assertEquals(10, result);
    double val = inst.value(0);
    assertEquals(0.0, val, 0.0001);
    assertEquals(classValue, inst.classValue());
  }

  @Test
  public void testSparseInstanceWithDataVector() {
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    SparseInstance inst = new SparseInstance(dataVector);
    int result = inst.noAttributes();
    assertEquals(5, result);
    double val = inst.value(1);
    assertEquals(2.0, val, 0.0001);
    assertNull(inst.classValue());
  }

  @Test
  public void testSparseInstanceWithDefaultValue() {
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    SparseInstance inst = new SparseInstance(dataVector, 0.5);
    double val = inst.value(2);
    assertEquals(0.0, val, 0.0001);
  }

  @Test
  public void testSparseInstanceWithClassValue() {
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    Object classValue = "Test";
    SparseInstance inst = new SparseInstance(dataVector, classValue);
    assertEquals(classValue, inst.classValue());
  }

  @Test
  public void testSparseInstanceWithAllParams() {
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    Object classValue = "Test";
    SparseInstance inst = new SparseInstance(dataVector, 0.5, classValue);
    double val = inst.value(3);
    assertEquals(3.0, val, 0.0001);
    assertEquals(classValue, inst.classValue());
  }

  @Test
  public void testInitiate() {
    SparseInstance inst = new SparseInstance();
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    inst.initiate(dataVector);
    int result = inst.noAttributes();
    assertEquals(5, result);
    double val = inst.value(1);
    assertEquals(2.0, val, 0.0001);
  }

  @Test
  public void testValue() {
    SparseInstance inst = new SparseInstance();
    double[] dataVector = {1.5, 2.0, 0.0, 3.0, 0.0};
    inst.initiate(dataVector);
    double val = inst.value(0);
    assertEquals(1.5, val, 0.0001);
  }

  @Test
  public void testClear() {
    SparseInstance inst = new SparseInstance();
    double[] dataVector = {1.0, 2.0, 0.0, 3.0, 0.0};
    inst.initiate(dataVector);
    inst.clear();
    assertTrue(inst.isEmpty());
  }

  // ... continue with test cases for the other methods ...

}