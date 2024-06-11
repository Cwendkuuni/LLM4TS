package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.sevenz.Coders.Coder;
import org.junit.Before;
import org.junit.Test;

public class CodersTest {

    private ByteArrayInputStream is;
    private ByteArrayOutputStream os;
    private byte[] password;
    private Coder coder;
    
    @Before
    public void setUp() {
        is = mock(ByteArrayInputStream.class);
        os = mock(ByteArrayOutputStream.class);
        password = new byte[0];
        coder = mock(Coder.class);
    }

    @Test
    public void testAddDecoder() throws Exception {
        assertNull(Coders.addDecoder(is, coder, password));
    }

    @Test
    public void testAddEncoder() throws Exception {
        assertNull(Coders.addEncoder(os, SevenZMethod.LZMA, password));
    }
}