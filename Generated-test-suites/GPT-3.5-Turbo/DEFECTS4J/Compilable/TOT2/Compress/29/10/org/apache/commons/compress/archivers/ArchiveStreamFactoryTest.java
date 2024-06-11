package org.apache.commons.compress.archivers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamForAr() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
        } catch (Exception ex) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamForAr() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream));
        } catch (Exception ex) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream("invalid", inputStream);
            fail("Exception should have been thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof ArchiveException);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            factory.createArchiveOutputStream("invalid", outputStream);
            fail("Exception should have been thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof ArchiveException);
        }
    }

    // Add more test cases for other methods and scenarios as needed

}