package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

import java.util.Date;

public class DefaultPhdReadTagTest {

  // Test data
  private final String type = "TestType";
  private final String source = "TestSource";
  private final Range ungappedRange = new Range(1, 10);
  private final Date date = new Date();
  private final String comment = "TestComment";
  private final String freeFormData = "TestFreeFormData";

  // Constructor test
  @Test
  public void testConstructor() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertNotNull(phdReadTag);
  }

  // Test getType()
  @Test
  public void testGetType() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(type, phdReadTag.getType());
  }

  // Test getSource()
  @Test
  public void testGetSource() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(source, phdReadTag.getSource());
  }

  // Test getUngappedRange()
  @Test
  public void testGetUngappedRange() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
  }

  // Test getDate()
  @Test
  public void testGetDate() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(date, phdReadTag.getDate());
  }

  // Test getComment()
  @Test
  public void testGetComment() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(comment, phdReadTag.getComment());
  }

  // Test getFreeFormData()
  @Test
  public void testGetFreeFormData() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
  }

  // Test hashCode()
  @Test
  public void testHashCode() {
    DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
  }

  // Test equals()
  @Test
  public void testEquals() {
    DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertTrue(phdReadTag1.equals(phdReadTag2));
  }

  // Test equals() with different object
  @Test
  public void testEqualsDifferentObject() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Object obj = new Object();
    Assert.assertFalse(phdReadTag.equals(obj));
  }

  // Test equals() with null
  @Test
  public void testEqualsNull() {
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    Assert.assertFalse(phdReadTag.equals(null));
  }
}