
package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag defaultPhdReadTag;

    @Before
    public void setUp() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free-form data";

        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGetType() {
        assertEquals("type", defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("source", defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 5);
        assertEquals(range, defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        assertNotNull(defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("free-form data", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        assertNotNull(defaultPhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag otherPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5),
                new Date(), "comment", "free-form data");

        assertTrue(defaultPhdReadTag.equals(otherPhdReadTag));
    }
}
