
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

   @Test
   public void testEncodedLength() {
      assertEquals(3, Utf8.encodedLength("abc"));
      assertEquals(6, Utf8.encodedLength("中国"));
      assertEquals(10, Utf8.encodedLength("hello world"));
      assertEquals(0, Utf8.encodedLength(""));
   }

   @Test
   public void testEncodedLengthGeneral() {
      assertEquals(3, Utf8.encodedLengthGeneral("abc", 0));
      assertEquals(6, Utf8.encodedLengthGeneral("中国", 0));
      assertEquals(5, Utf8.encodedLengthGeneral("hello", 1));
   }

   @Test
   public void testIsWellFormed() {
      byte[] validBytes = { 97, 98, 99 };
      byte[] invalidBytes = { 97, -100, 99 };

      assertTrue(Utf8.isWellFormed(validBytes));
      assertFalse(Utf8.isWellFormed(invalidBytes));
   }

   @Test
   public void testIsWellFormedWithOffsets() {
      byte[] validBytes = { 97, 98, 99 };
      byte[] invalidBytes = { 97, -100, 99 };

      assertTrue(Utf8.isWellFormed(validBytes, 0, validBytes.length));
      assertFalse(Utf8.isWellFormed(invalidBytes, 0, invalidBytes.length));
   }

   @Test
   public void testIsWellFormedSlowPath() {
      byte[] validBytes = { -62, -96, -103 };
      byte[] invalidBytes = { -62, -96, -100 };

      assertTrue(Utf8.isWellFormedSlowPath(validBytes, 0, validBytes.length));
      assertFalse(Utf8.isWellFormedSlowPath(invalidBytes, 0, invalidBytes.length));
   }
}
