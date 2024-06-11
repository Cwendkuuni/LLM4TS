
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage systemUnderTest;

    @Before
    public void setUp() {
        systemUnderTest = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertEquals(systemUnderTest.getLocation(null, null, null), null);
    }

    @Test
    public void testUpload() {
        InputStream inputStream = null;
        systemUnderTest.upload("", inputStream, 0L);
    }

    @Test
    public void testDownload() {
        assertEquals(systemUnderTest.download(""), null);
    }
}
