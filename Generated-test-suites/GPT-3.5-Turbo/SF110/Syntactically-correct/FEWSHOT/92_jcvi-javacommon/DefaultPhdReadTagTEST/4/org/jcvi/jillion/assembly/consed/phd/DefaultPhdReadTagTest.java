package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        String type = "type";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, "", null, null, "", "");
        assertEquals(type, tag.getType());
    }
    
    @Test
    public void testGetSource() {
        String source = "source";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", source, null, null, "", "");
        assertEquals(source, tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", ungappedRange, null, "", "");
        assertEquals(ungappedRange, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, date, "", "");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        String comment = "comment";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, comment, "");
        assertEquals(comment, tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        String freeFormData = "data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, "", freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("", "", null, null, "", "");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("", "", null, null, "", "");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("", "", null, null, "", "");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("", "", null, null, "", "");
        assertEquals(true, tag1.equals(tag2));
    }
}