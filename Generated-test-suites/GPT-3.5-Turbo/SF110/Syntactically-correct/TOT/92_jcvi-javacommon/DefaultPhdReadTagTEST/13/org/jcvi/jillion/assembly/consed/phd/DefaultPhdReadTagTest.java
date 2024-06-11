
package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Type1", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Source1", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "Data1");
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), date, "Comment1", "Data1");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Comment1", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Data1", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertTrue(tag1.equals(tag2));
    }
}
