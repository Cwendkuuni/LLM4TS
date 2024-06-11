package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(source, defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(date, defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(comment, defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * (31 * 1 + comment.hashCode()) + date.hashCode()) + freeFormData.hashCode()) + source.hashCode()) + type.hashCode()) + ungappedRange.hashCode();

        assertEquals(expectedHashCode, defaultPhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

}