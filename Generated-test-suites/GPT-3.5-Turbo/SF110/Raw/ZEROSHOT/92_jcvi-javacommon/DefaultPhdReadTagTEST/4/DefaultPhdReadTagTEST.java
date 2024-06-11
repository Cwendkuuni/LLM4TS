package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, tag.getType());
    }

    @Test
    public void testGetSource() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(source, tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(ungappedRange, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(comment, tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(freeFormData, tag.getFreeFormData());
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

        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
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

        Assert.assertEquals(tag1, tag2);
    }
}