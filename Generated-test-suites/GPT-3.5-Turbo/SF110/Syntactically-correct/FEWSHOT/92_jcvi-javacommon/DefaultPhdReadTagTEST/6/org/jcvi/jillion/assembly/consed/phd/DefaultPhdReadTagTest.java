package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    private final String type = "type";
    private final String source = "source";
    private final Range ungappedRange = new Range(1, 10);
    private final Date date = new Date();
    private final String comment = "comment";
    private final String freeFormData = "freeFormData";

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(source, phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(comment, phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(phdReadTag1, phdReadTag2);
    }

    @Test
    public void testNotEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Range differentRange = new Range(5, 15);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, differentRange, date, comment, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag2);
    }
}