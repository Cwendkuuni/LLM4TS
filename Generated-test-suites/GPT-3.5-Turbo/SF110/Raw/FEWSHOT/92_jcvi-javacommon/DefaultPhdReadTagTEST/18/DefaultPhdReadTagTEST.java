package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jcvi.jillion.core.Range;
import java.util.Date;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "form data1");
        assertEquals("type1", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "form data1");
        assertEquals("source1", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), date, "comment1", "form data1");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "form data1");
        assertEquals("comment1", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "form data1");
        assertEquals("form data1", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testEqualsWithDifferentType() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        String tag2 = "not a DefaultPhdReadTag object";
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentComment() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment2", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentDate() {
        Range range = new Range(1, 5);
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, date1, "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, date2, "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentFreeFormData() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data2");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentSource() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source2", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentType() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentUngappedRange() {
        Range range1 = new Range(1, 5);
        Range range2 = new Range(2, 6);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range1, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range2, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullComment() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), null, "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullDate() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, null, "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullFreeFormData() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullSource() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", null, range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullType() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, "source1", range, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithNullUngappedRange() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", null, new Date(), "comment1", "form data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "form data1");
        assertFalse(tag1.equals(tag2));
    }
}