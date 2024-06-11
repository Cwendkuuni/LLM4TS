package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(null, System.in);
            fail("IllegalArgumentException not thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream("invalid", System.in);
            fail("ArchiveException not thrown for unknown archiver name");
        } catch (ArchiveException e) {
            // ArchiveException expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException not thrown for null output stream");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(null, System.out);
            fail("IllegalArgumentException not thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream("invalid", System.out);
            fail("ArchiveException not thrown for unknown archiver name");
        } catch (ArchiveException e) {
            // ArchiveException expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(null);
            fail("IllegalArgumentException not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

}
