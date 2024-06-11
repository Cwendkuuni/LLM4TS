
package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testWithEncoding() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        IOContext result = ioContext.withEncoding(encoding);

        assertEquals(encoding, result.getEncoding());
    }
}
