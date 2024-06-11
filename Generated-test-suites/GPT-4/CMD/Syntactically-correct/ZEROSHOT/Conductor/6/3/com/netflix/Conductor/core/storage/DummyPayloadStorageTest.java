package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage
            .getLocation(Operation.WRITE, PayloadType.TASK_OUTPUT, "path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        InputStream mockInputStream = mock(InputStream.class);
        try {
            dummyPayloadStorage.upload("path", mockInputStream, 2048);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDownload() {
        InputStream downloadStream = dummyPayloadStorage.download("path");
        assertNull(downloadStream);
    }
}
