package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() throws Exception {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        // Simply testing that no exception is thrown, as the method does not have a return value or any logic
        InputStream testInputStream = InputStream.nullInputStream();
        dummyPayloadStorage.upload("", testInputStream, 0);
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download(""));
    }
}