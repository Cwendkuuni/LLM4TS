package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocation_returnNull() {
        ExternalStorageLocation location = this.dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "path");
        Assert.assertNull(location);
    }

    @Test
    public void upload_noException() {
        this.dummyPayloadStorage.upload("path", null, 0);
    }

    @Test
    public void download_returnNull() {
        InputStream stream = this.dummyPayloadStorage.download("path");
        Assert.assertNull(stream);
    }

    @Test
    public void getLocation_withNull_returnNull() {
        ExternalStorageLocation location = this.dummyPayloadStorage.getLocation(
                null, 
                null, 
                null);
        Assert.assertNull(location);
    }

    @Test
    public void upload_withNull_noException() {
        this.dummyPayloadStorage.upload(null, null, 0);
    }

    @Test
    public void download_withNull_returnNull() {
        InputStream stream = this.dummyPayloadStorage.download(null);
        Assert.assertNull(stream);
    }
}
