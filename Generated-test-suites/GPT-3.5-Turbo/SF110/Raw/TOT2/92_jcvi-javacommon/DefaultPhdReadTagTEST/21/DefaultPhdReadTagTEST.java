import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals("TestType", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals("TestSource", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals(new Range(1, 5), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), date, "TestComment", "TestFreeFormData");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals("TestComment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals("TestFreeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 5), new Date(), "TestComment", "TestFreeFormData");
        assertTrue(tag1.equals(tag2));
    }
}