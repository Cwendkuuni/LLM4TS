package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jcvi.jillion.core.*;

import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, tag.getType());
    }

    @Test
    public void testGetSource() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(source, tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(ungappedRange, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(comment, tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        final String type = "Type";
        final String source = "Source";
        final Range ungappedRange = new Range(1, 10);
        final Date date = new Date();
        final String comment = "Comment";
        final String freeFormData = "Free Form Data";

        final String differentType = "Different Type";
        final String differentSource = "Different Source";
        final Range differentUngappedRange = new Range(2, 20);
        final Date differentDate = new Date(123456789);
        final String differentComment = "Different Comment";
        final String differentFreeFormData = "Different Free Form Data";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(differentType, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type, differentSource, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type, source, differentUngappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag(type, source, ungappedRange, differentDate, comment, freeFormData);
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, differentComment, freeFormData);
        DefaultPhdReadTag tag8 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, differentFreeFormData);

        assertEquals(tag1, tag2);

        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1, tag4);
        assertNotEquals(tag1, tag5);
        assertNotEquals(tag1, tag6);
        assertNotEquals(tag1, tag7);
        assertNotEquals(tag1, tag8);
    }
}