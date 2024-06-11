/*
 * Test class for DummyPayloadStorage
 */
package com.netflix.conductor.core.storage;

import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest
{
    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp()
    {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation()
    {
        assertNull(this.dummyPayloadStorage.getLocation(null, null, null));
        // add more tests for edge cases and exceptions as per requirement.
    }

    @Test
    public void testUpload()
    {
        try {
            this.dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        // add more tests for edge cases and exceptions as per requirement.
    }

    @Test
    public void testDownload()
    {
        assertNull(this.dummyPayloadStorage.download(null));
        // add more tests for edge cases and exceptions as per requirement.
    }
}