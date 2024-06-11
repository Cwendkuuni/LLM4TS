/*
 * Copyright 2022 Netflix, Inc.
 */

package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void getLocation_ShouldReturnNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path"); 
        assertNull("Expected null", location);
    }

    @Test(expected = NullPointerException.class)
    public void upload_ShouldNotThrowException() {
        InputStream payload = null;
        dummyPayloadStorage.upload("path", payload, 123L);
    }

    @Test
    public void download_ShouldReturnNull() {
        InputStream downloadedStream = dummyPayloadStorage.download("path");
        assertNull(downloadedStream);
    }
}