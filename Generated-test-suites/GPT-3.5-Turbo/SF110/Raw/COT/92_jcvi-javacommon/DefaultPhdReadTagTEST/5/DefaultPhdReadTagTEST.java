
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

  @Test
  public void testGetType() {
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    String expectedType = "type";
    String actualType = tag.getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetSource() {
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    String expectedSource = "source";
    String actualSource = tag.getSource();
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testGetUngappedRange() {
    Range expectedRange = null;
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", expectedRange, new Date(), "comment", "freeFormData");
    Range actualRange = tag.getUngappedRange();
    assertEquals(expectedRange, actualRange);
  }

  @Test
  public void testGetDate() {
    Date expectedDate = new Date();
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, expectedDate, "comment", "freeFormData");
    Date actualDate = tag.getDate();
    assertEquals(expectedDate, actualDate);
  }

  @Test
  public void testGetComment() {
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    String expectedComment = "comment";
    String actualComment = tag.getComment();
    assertEquals(expectedComment, actualComment);
  }

  @Test
  public void testGetFreeFormData() {
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    String expectedFreeFormData = "freeFormData";
    String actualFreeFormData = tag.getFreeFormData();
    assertEquals(expectedFreeFormData, actualFreeFormData);
  }

  @Test
  public void testHashCode() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }

  @Test
  public void testEquals() {
    DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    assertTrue(tag.equals(tag));
  }

  @Test
  public void testEqualsWithDifferentType() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source", null, new Date(), "comment", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source", null, new Date(), "comment", "freeFormData");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testEqualsWithDifferentSource() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source1", null, new Date(), "comment", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source2", null, new Date(), "comment", "freeFormData");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testEqualsWithDifferentUngappedRange() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testEqualsWithDifferentDate() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, new Date(1), "comment", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, new Date(2), "comment", "freeFormData");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testEqualsWithDifferentComment() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment1", "freeFormData");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment2", "freeFormData");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testEqualsWithDifferentFreeFormData() {
    DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData1");
    DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData2");
    assertFalse(tag1.equals(tag2));
  }

}
