
package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "data");
        assertEquals("type", tag.getType());

        // Edge case: type is null
        tag = new DefaultPhdReadTag(null, "source", null, new Date(), "comment", "data");
        assertNull(tag.getType());
    }

    @Test
    public void testGetSource() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "data");
        assertEquals("source", tag.getSource());

        // Edge case: source is null
        tag = new DefaultPhdReadTag("type", null, null, new Date(), "comment", "data");
        assertNull(tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        // Typical use case
        Range ungappedRange = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "data");
        assertEquals(ungappedRange, tag.getUngappedRange());

        // Edge case: ungappedRange is null
        tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "data");
        assertNull(tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, date, "comment", "data");
        assertEquals(date, tag.getDate());

        // Edge case: date is null
        tag = new DefaultPhdReadTag("type", "source", null, null, "comment", "data");
        assertNull(tag.getDate());
    }

    @Test
    public void testGetComment() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());

        // Edge case: comment is null
        tag = new DefaultPhdReadTag("type", "source", null, new Date(), null, "data");
        assertNull(tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());

        // Edge case: freeFormData is null
        tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", null);
        assertNull(tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        // Typical use case
        Range ungappedRange = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        // Edge case: all fields are null
        tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        // Typical use case: tags are equal
        Range ungappedRange = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "data");
        assertTrue(tag1.equals(tag2));

        // Typical use case: tags are not equal
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("otherType", "otherSource", ungappedRange, new Date(), "comment", "data");
        assertFalse(tag1.equals(tag3));

        // Edge case: both tags are null
        assertFalse(tag1.equals(null));

        // Edge case: comparing with an object of a different class
        assertFalse(tag1.equals("tag"));

        // Edge case: all fields are null - tags are equal
        tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertTrue(tag1.equals(tag2));

        // Edge case: all fields are null - tags are not equal
        tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        tag3 = new DefaultPhdReadTag("otherType", null, null, null, null, null);
        assertFalse(tag1.equals(tag3));
    }
}
