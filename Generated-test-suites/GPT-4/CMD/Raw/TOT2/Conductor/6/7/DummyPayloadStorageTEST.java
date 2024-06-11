
/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

/**
 * Test class for {@link DummyPayloadStorage}
 */
public class DummyPayloadStorageTest{

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation(){
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null,null,null);
        assertNull(location);
        location = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath");
        assertNull(location);
    }

    @Test
    public void testUploadCase(){
        // This function does not do anything in actual DummyPayloadStorage Class. So, if no error occurs, it's treated as 'pass'
        assertDoesNotThrow(() -> dummyPayloadStorage.upload("dummyPath", null, 5));
    }

    @Test
    public void testDownload(){
        InputStream result = dummyPayloadStorage.download("dummyPath");
        assertNull(result);

        result = dummyPayloadStorage.download(null);
        assertNull(result);
    }
}
