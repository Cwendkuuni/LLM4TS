package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testConstructor() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("Type2", source, ungappedRange, date, comment, freeFormData);
        
        assertEquals(tag1, tag2);
        assertEquals(tag2, tag1);
        assertEquals(tag1, tag1);
        assertEquals(tag2, tag2);
        assertEquals(tag3, tag3);
    }
    
}