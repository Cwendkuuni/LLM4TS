

package com.netflix.conductor.core.storage;

import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dps = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
    	
        ExternalStorageLocation location = dps.getLocation(null, null, null);
        
        assertNull(location);
    }

    @Test
    public void testUpload() {
        try {
            dps.upload(null, null, 0);
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    public void testDownload() {
        InputStream stream = dps.download(null);
        
        assertNull(stream);
    }
}

