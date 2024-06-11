import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag phdReadTag;

    @Before
    public void setUp() {
        phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
    }

    @Test
    public void testGetType() {
        String expected = "type";
        String actual = phdReadTag.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSource() {
        String expected = "source";
        String actual = phdReadTag.getSource();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUngappedRange() {
        Range expected = new Range(1, 5);
        Range actual = phdReadTag.getUngappedRange();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDate() {
        Date expected = new Date();
        Date actual = phdReadTag.getDate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetComment() {
        String expected = "comment";
        String actual = phdReadTag.getComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFreeFormData() {
        String expected = "freeFormData";
        String actual = phdReadTag.getFreeFormData();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = phdReadTag.hashCode();
        int actual = phdReadTag.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_sameObject() {
        boolean expected = true;
        boolean actual = phdReadTag.equals(phdReadTag);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_nullObject() {
        boolean expected = false;
        boolean actual = phdReadTag.equals(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_differentClass() {
        boolean expected = false;
        boolean actual = phdReadTag.equals(new Object());
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_equalObject() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        boolean expected = true;
        boolean actual = phdReadTag.equals(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_notEqualObject() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type2", "source2", new Range(1, 5), new Date(), "comment", "freeFormData");
        boolean expected = false;
        boolean actual = phdReadTag.equals(other);
        assertEquals(expected, actual);
    }

}