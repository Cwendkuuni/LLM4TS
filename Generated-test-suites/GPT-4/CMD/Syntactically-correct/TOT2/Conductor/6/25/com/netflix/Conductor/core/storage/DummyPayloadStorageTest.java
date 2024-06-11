package com.netflix.conductor.core.storage;
import org.junit.Assert;
   import org.junit.Test;

   public class DummyPayloadStorageTest {
      @Test
      public void testGetLocation() {
         DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
         Assert.assertNull(dummyPayloadStorage.getLocation(null, null, null));
      }

      @Test
      public void testUpload() {
         DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
         dummyPayloadStorage.upload(null, null, 0);
         Assert.assertTrue(true);  // pass if no exceptions are thrown
      }

      @Test
      public void testDownload() {
         DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
         Assert.assertNull(dummyPayloadStorage.download(null));
      }
   }