package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag phdReadTag;

    @Before
    public void setUp() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGetType() {
        assertEquals("Type", phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("Source", phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        assertNotNull(phdReadTag.getUngappedRange());
        assertEquals(1, phdReadTag.getUngappedRange().getStart());
        assertEquals(10, phdReadTag.getUngappedRange().getEnd());
    }

    @Test
    public void testGetDate() {
        assertNotNull(phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("FreeForm", phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        assertNotNull(phdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(1, 10),
                new Date(), "Comment", "FreeForm");
        assertEquals(phdReadTag, other);
    }
}