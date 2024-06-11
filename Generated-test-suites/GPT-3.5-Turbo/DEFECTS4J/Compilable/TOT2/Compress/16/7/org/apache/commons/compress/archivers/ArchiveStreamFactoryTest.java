package org.apache.commons.compress.archivers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArguments_Success() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[1]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
            assertNotNull(inputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ExceptionThrown() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[1]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null, input);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected IllegalArgumentException
        } catch (ArchiveException ex) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArguments_Success() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
            assertNotNull(outputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ExceptionThrown() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(null, output);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected IllegalArgumentException
        } catch (ArchiveException ex) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_ValidStream_Success() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[1]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream inputStream = factory.createArchiveInputStream(input);
            assertNotNull(inputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_InvalidStream_ExceptionThrown() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected IllegalArgumentException
        } catch (ArchiveException ex) {
            fail("Wrong exception thrown");
        }
    }

}