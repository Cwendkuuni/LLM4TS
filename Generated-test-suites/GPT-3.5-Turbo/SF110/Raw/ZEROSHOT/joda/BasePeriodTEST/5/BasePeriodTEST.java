package org.joda.time.base;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {
  
  private BasePeriod basePeriod;
  
  @Before
  public void setUp() {
    basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
  }
  
  @Test
  public void testGetPeriodType() {
    assertEquals(PeriodType.time(), basePeriod.getPeriodType());
  }
  
  @Test
  public void testGetValue() {
    assertEquals(1, basePeriod.getValue(0));
    assertEquals(2, basePeriod.getValue(1));
    assertEquals(3, basePeriod.getValue(2));
    assertEquals(4, basePeriod.getValue(3));
    assertEquals(5, basePeriod.getValue(4));
    assertEquals(6, basePeriod.getValue(5));
    assertEquals(7, basePeriod.getValue(6));
    assertEquals(8, basePeriod.getValue(7));
  }
  
  @Test
  public void testToDurationFrom() {
    // To be implemented
  }
  
  @Test
  public void testToDurationTo() {
    // To be implemented
  }
  
  @Test
  public void testSetPeriod() {
    // To be implemented
  }
  
  @Test
  public void testSetField() {
    // To be implemented
  }
  
  @Test
  public void testAddField() {
    // To be implemented
  }
  
  @Test
  public void testMergePeriod() {
    // To be implemented
  }
  
  @Test
  public void testAddPeriod() {
    // To be implemented
  }
  
  @Test
  public void testSetValue() {
    // To be implemented
  }
  
  @Test
  public void testSetValues() {
    // To be implemented
  }
}