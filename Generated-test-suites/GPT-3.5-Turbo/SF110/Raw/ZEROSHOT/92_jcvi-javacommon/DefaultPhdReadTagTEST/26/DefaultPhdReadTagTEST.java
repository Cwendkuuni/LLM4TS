import org.junit.*;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

  @Test
  public void testGetType() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(type, defaultPhdReadTag.getType());
  }
  
  @Test
  public void testGetSource() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(source, defaultPhdReadTag.getSource());
  }
  
  @Test
  public void testGetUngappedRange() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
  }
  
  @Test
  public void testGetDate() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(date, defaultPhdReadTag.getDate());
  }
  
  @Test
  public void testGetComment() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(comment, defaultPhdReadTag.getComment());
  }
  
  @Test
  public void testGetFreeFormData() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
  }
  
  @Test
  public void testHashCode() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    int expected = 31 * (31 * (31 * (31 * (31 * (31 * 1 + comment.hashCode()) + date.hashCode()) + freeFormData.hashCode()) + source.hashCode()) + type.hashCode()) + ungappedRange.hashCode();
    assertEquals(expected, defaultPhdReadTag.hashCode());
  }
  
  @Test
  public void testEquals() {
    String type = "Type";
    String source = "Source";
    Range ungappedRange = new Range(1, 10);
    Date date = new Date();
    String comment = "Comment";
    String freeFormData = "Free Form Data";
    DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
  }
  
}