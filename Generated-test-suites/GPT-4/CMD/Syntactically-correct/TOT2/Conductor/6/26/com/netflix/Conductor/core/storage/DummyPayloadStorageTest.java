
/*
 * Copyright 2022 Netflix, Inc.
*/

package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage;
    private static final String PATH = "test/path";

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testLocation() {
        ExternalStorageLocation externalLocation = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.ERROR, PATH);
        assertNull(externalLocation);
    }

    @Test
    public void testUpload() {
        long payloadSize = 3L;
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        dummyPayloadStorage.upload(PATH, payload, payloadSize);
        // No Exception means, it has been successfully uploaded.
    }

    @Test
    public void testDownload() {
        InputStream downloadedData = dummyPayloadStorage.download(PATH);
        assertNull(downloadedData);
    }

    @Test(expected = NullPointerException.class)
    public void testUploadNullPath() {
        dummyPayloadStorage.upload(null, new ByteArrayInputStream(new byte[0]), 1L);
    }

    @Test(expected = NullPointerException.class)
    public void testDownloadNullPath() {
        dummyPayloadStorage.download(null);
    }

}

