/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import org.junit.Test;
import org.junit.Assert;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Assert.assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }
    
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        try {
            dummyPayloadStorage.upload(null, null, 0);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Should not thrown exception as implementation is dummy and doesn't do anything");
        }     
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Assert.assertNull(dummyPayloadStorage.download(null));
    }

    @Test
    public void testGetLocationWithInvalidParameters() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Assert.assertNull(dummyPayloadStorage.getLocation(null, PayloadType.TASK_INPUT, "nonexistent-path"));
    }

    @Test
    public void testUploadWithInvalidParameters() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        try {
            InputStream payload = new InputStream() {
                @Override
                public int read() {
                    return -1; // end of stream
                }
            };
            dummyPayloadStorage.upload("nonexistent-path", payload, 0);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Should not thrown exception as implementation is dummy and doesn't do anything");
        }  
    }

    @Test
    public void testDownloadWithInvalidParameters() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Assert.assertNull(dummyPayloadStorage.download("nonexistent-path"));
    }
}