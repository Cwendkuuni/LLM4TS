
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {
  
  @Test
  public void testGetType() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(type, phdReadTag.getType());
  }

  @Test
  public void testGetSource() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(source, phdReadTag.getSource());
  }

  @Test
  public void testGetUngappedRange() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(ungappedRange, phdReadTag.getUngappedRange());
  }

  @Test
  public void testGetDate() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(date, phdReadTag.getDate());
  }

  @Test
  public void testGetComment() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(comment, phdReadTag.getComment());
  }

  @Test
  public void testGetFreeFormData() {
    String type = "testType";
    String source = "testSource";
    Range ungappedRange = new Range(0, 10);
    Date date = new Date();
    String comment = "testComment";
    String freeFormData = "testFreeFormData";
    
    DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    assertEquals(freeFormData, phdReadTag.getFreeFormData());
  }
  
}
