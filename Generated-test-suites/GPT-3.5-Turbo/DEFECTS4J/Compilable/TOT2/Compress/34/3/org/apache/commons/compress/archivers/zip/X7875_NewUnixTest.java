package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testGetSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        assertEquals(123, x7875.getUID());
    }

    @Test
    public void testGetSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(456);
        assertEquals(456, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getLocalFileDataLength().getValue());
    }

    // Add more test cases for other methods

}