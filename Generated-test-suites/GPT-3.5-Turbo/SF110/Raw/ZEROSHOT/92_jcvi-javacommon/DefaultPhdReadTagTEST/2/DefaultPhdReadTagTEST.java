package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "Type";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, tag.getType());
    }

    @Test
    public void testGetSource() {
        String source = "Source";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, source, null, null, null, null);
        assertEquals(source, tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, ungappedRange, null, null, null);
        assertEquals(ungappedRange, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, date, null, null);
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        String comment = "Comment";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        assertEquals(comment, tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "Free Form Data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", ungappedRange, date, "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", ungappedRange, date, "Comment", "Free Form Data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", ungappedRange, date, "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", ungappedRange, date, "Comment", "Free Form Data");
        assertEquals(tag1, tag2);
    }
}