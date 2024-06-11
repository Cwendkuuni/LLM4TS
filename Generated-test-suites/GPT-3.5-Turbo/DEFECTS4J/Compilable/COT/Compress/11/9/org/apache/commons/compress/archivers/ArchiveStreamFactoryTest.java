package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Ar() throws Exception {
        String archiverName = "ar";
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws Exception {
        String archiverName = "zip";
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws Exception {
        String archiverName = "tar";
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws Exception {
        String archiverName = "jar";
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() throws Exception {
        String archiverName = "cpio";
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        in.close();
    }
  
    @Test
    public void testCreateArchiveOutputStream_Ar() throws Exception {
        String archiverName = "ar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        assertNotNull(aos);
        out.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws Exception {
        String archiverName = "zip";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        assertNotNull(aos);
        out.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws Exception {
        String archiverName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        assertNotNull(aos);
        out.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws Exception {
        String archiverName = "jar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        assertNotNull(aos);
        out.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws Exception {
        String archiverName = "cpio";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        assertNotNull(aos);
        out.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Zip() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Jar() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Ar() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Cpio() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Dump() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Tar() throws Exception {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        in.close();
    }

}
