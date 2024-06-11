package com.netflix.conductor.core.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Mock
    private ExternalPayloadStorage.Operation operation;

    @Mock
    private ExternalPayloadStorage.PayloadType payloadType;

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        initMocks(this);
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, "Path"));
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("Path"));
    }

    @Test
    public void testUpload() {
        InputStream inputStream = new ByteArrayInputStream("Testing".getBytes());
        dummyPayloadStorage.upload("Path", inputStream, 7);
    }
}
