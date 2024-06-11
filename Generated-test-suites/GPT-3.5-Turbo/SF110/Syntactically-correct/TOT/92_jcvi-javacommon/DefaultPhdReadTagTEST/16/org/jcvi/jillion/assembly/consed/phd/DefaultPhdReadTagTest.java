import static org.junit.Assert.assertEquals;

import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        String type = readTag.getType();
        assertEquals("Type1", type);
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        String source = readTag.getSource();
        assertEquals("Source1", source);
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        Range ungappedRange = readTag.getUngappedRange();
        assertEquals(range, ungappedRange);
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), date, "Comment1", "Data1");
        Date getDate = readTag.getDate();
        assertEquals(date, getDate);
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        String comment = readTag.getComment();
        assertEquals("Comment1", comment);
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        String freeFormData = readTag.getFreeFormData();
        assertEquals("Data1", freeFormData);
    }

    @Test
    public void testHashCode() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        int hashCode1 = readTag1.hashCode();
        int hashCode2 = readTag2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        assertEquals(readTag1, readTag2);
    }
    
}