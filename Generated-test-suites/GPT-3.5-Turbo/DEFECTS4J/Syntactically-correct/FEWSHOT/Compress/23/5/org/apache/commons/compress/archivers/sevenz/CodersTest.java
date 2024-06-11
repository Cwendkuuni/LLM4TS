package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() throws Exception {
        CodecDecoder decoder = mock(CodecDecoder.class);
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[0];

        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.COPY, decoder);
        Coders.coderTable[0] = coderId;
        
        Coders.addDecoder(input, coder, null);
        
        verify(decoder).decode(input, coder, null);
    }
    
    @Test(expected=IOException.class)
    public void testAddDecoderUnsupportedMethod() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[0];

        Coders.addDecoder(input, coder, null);
    }
    
    @Test
    public void testAddEncoder() throws Exception {
        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.COPY, new MockEncoder());
        Coders.coderTable[0] = coderId;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Coders.addEncoder(out, SevenZMethod.COPY, null);
        
        assertEquals(1, out.toByteArray().length);
    }
    
    @Test(expected=IOException.class)
    public void testAddEncoderUnsupportedMethod() throws Exception {
        Coders.addEncoder(null, SevenZMethod.BZIP2, null);
    }
    
    @Test
    public void testCopyDecoder() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.CargerBase decoder = new Coders.CopyDecoder();
        InputStream input = mock(InputStream.class);
        
        InputStream result = decoder.decode(input, coder, null);
        
        assertEquals(input, result);
    }
    
    @Test
    public void testCopyEncoder() throws Exception {
        Coders.CargerBase encoder = new Coders.CopyDecoder();
        OutputStream output = mock(OutputStream.class);
        
        OutputStream result = encoder.encode(output, null);
        
        assertEquals(output, result);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testCopyEncoderUnsupported() throws Exception {
        Coders.CargerBase encoder = new Coders.CopyDecoder();
        encoder.encode(null, null);
    }
    
    private class MockEncoder extends CoderBase {
        @Override
        OutputStream encode(OutputStream out, byte[] password)
                throws IOException {
            out.write(0);
            return out;
        }
    }
}