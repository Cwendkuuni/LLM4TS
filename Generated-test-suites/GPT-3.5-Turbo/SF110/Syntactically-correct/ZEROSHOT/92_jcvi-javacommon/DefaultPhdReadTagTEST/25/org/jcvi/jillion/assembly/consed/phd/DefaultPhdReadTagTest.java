package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

   @Test
   public void testGetType() {
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals("type",readTag.getType());
   }

   @Test
   public void testGetSource() {
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals("source",readTag.getSource());
   }

   @Test
   public void testGetUngappedRange() {
      Range range = new Range(1, 10);
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
      assertEquals(range,readTag.getUngappedRange());
   }

   @Test
   public void testGetDate() {
      Date date = new Date();
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "data");
      assertEquals(date,readTag.getDate());
   }

   @Test
   public void testGetComment() {
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals("comment",readTag.getComment());
   }

   @Test
   public void testGetFreeFormData() {
      DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals("data",readTag.getFreeFormData());
   }

   @Test
   public void testEquals() {
      DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals(readTag1,readTag2);
   }

   @Test
   public void testHashCode() {
      DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
      assertEquals(readTag1.hashCode(),readTag2.hashCode());
   }
}