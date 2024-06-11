package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertTrue(result instanceof ArArchiveInputStream);
        
        in = new ByteArrayInputStream(new byte[0]);
        result = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertTrue(result instanceof ZipArchiveInputStream);
        
        in = new ByteArrayInputStream(new byte[0]);
        result = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertTrue(result instanceof TarArchiveInputStream);
        
        in = new ByteArrayInputStream(new byte[0]);
        result = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertTrue(result instanceof JarArchiveInputStream);
        
        in = new ByteArrayInputStream(new byte[0]);
        result = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertTrue(result instanceof CpioArchiveInputStream);
        
        in = new ByteArrayInputStream(new byte[0]);
        result = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertTrue(result instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        ArchiveOutputStream result = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertTrue(result instanceof ArArchiveOutputStream);
        
        out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertTrue(result instanceof ZipArchiveOutputStream);
        
        out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertTrue(result instanceof TarArchiveOutputStream);
        
        out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertTrue(result instanceof JarArchiveOutputStream);
        
        out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertTrue(result instanceof CpioArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        };
        
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertTrue(result instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNonMarkSupportedStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        
        factory.createArchiveInputStream(in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNoMatchingArchiver() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[] { 0x01 });
        factory.createArchiveInputStream(in);
    }
    
}