
/*
 * Copyright 2022 Your Company, Inc.
 */
package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.Before;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void init() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);

        assertNull(location);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLocationErrorTest() {
        dummyPayloadStorage.getLocation(null, null, "");
    }

    @Test
    public void uploadTest() {
        InputStream testPayload = new ByteArrayInputStream("Test Payload".getBytes());
        dummyPayloadStorage.upload("/test/path", testPayload, testPayload.available());

        // Since DummyPayloadStorage.upload() doesn't perform any action, we don't expect any side effects.
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadErrorTest() {
        dummyPayloadStorage.upload(null, null, 0);
    }

    @Test
    public void downloadTest() {
        InputStream inStream = dummyPayloadStorage.download("/test/path");

        assertNull(inStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void downloadErrorTest() {
        dummyPayloadStorage.download(null);
    }
}
