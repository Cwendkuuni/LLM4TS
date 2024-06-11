package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "example_type";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(type, phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        String source = "example_source";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", source, new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(source, phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(0, 10);
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "freeFormData");
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "freeFormData");
        assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        String comment = "example_comment";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), comment, "freeFormData");
        assertEquals(comment, phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "example_free_form_data";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", freeFormData);
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(tag1.equals(tag2));
    }
}