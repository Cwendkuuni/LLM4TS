package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage mockedExternalPayloadStorage;

    @Before
    public void setup() {
        mockedExternalPayloadStorage = mock(ExternalPayloadStorage.class);
    }

    @Test
    public void testGetLocationReturnsNull() {
        assertNull(new DummyPayloadStorage().getLocation(
                mock(Operation.class), PayloadType.JSON, ""));
    }

    @Test
    public void testUploadDoesNothing() {
        new DummyPayloadStorage().upload("", mock(InputStream.class), 0);
    }

    @Test
    public void testDownloadReturnsNull() {
        when(mockedExternalPayloadStorage.download(any(String.class)))
                .thenReturn(mock(InputStream.class));

        assertNull(new DummyPayloadStorage().download(""));
    }
}