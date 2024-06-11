package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag phdReadTag;

    @Before
    public void setUp() {
        phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
    }

    @Test
    public void testGetType() {
        assertEquals("type", phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("source", phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        assertEquals(new Range(1, 5), phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        assertNotNull(phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("freeFormData", phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals(phdReadTag.hashCode(), samePhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag differentPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(2, 6), new Date(), null, "freeFormData");
        
        assertTrue(phdReadTag.equals(samePhdReadTag));
        assertFalse(phdReadTag.equals(differentPhdReadTag));
    }
}