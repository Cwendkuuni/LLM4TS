import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archiver name and input stream
        String archiverName = "ZIP";
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x07, 0x4b, 0x50, 0xda, 0x04, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10});
        ArchiveInputStream result = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(result);
        PowerMockito.verifyStatic();
        ZipArchiveInputStream zipArchiveInputStream = PowerMockito.mock(ZipArchiveInputStream.class);
        PowerMockito.argThat(arg -> archiverName.equalsIgnoreCase(arg.getName())).thenReturn(zipArchiveInputStream);
        result.close();

        // Test with invalid archiver name
        archiverName = "INVALID";
        inputStream = new ByteArrayInputStream(new byte[]{0x07, 0x4b, 0x50, 0xda, 0x04, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10});
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            PowerMockito.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with null input stream
        inputStream = null;
        PowerMockito.mockStatic(ZipArchiveInputStream.class);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            PowerMockito.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with input stream without mark support
        inputStream = new ByteArrayInputStream(new byte[]{0x07, 0x4b, 0x50, 0xda, 0x04, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10});
        PowerMockito.mockStatic(ZipArchiveInputStream.class);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            PowerMockito.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archiver name and output stream
        String archiverName = "ZIP";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream result = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(result);
        PowerMockito.verifyStatic();
        ZipArchiveOutputStream zipArchiveOutputStream = PowerMockito.mock(ZipArchiveOutputStream.class);
        PowerMockito.argThat(arg -> archiverName.equalsIgnoreCase(arg.getName())).thenReturn(zipArchiveOutputStream);
        result.close();

        // Test with invalid archiver name
        archiverName = "INVALID";
        outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            PowerMockito.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with null output stream
        outputStream = null;
        PowerMockito.mockStatic(ZipArchiveOutputStream.class);
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            PowerMockito.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        // Test with valid archiver name and bytes
        String archiverName = "ZIP";
        byte[] bytes = {0x07, 0x4b, 0x50, 0xda, 0x04, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArchiveInputStream result = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(result);
        PowerMockito.verifyStatic();
        ZipArchiveInputStream zipArchiveInputStream = PowerMockito.mock(ZipArchiveInputStream.class);
        PowerMockito.argThat(arg -> archiverName.equalsIgnoreCase(arg.getName())).thenReturn(zipArchiveInputStream);
        result.close();

        // Test with invalid archiver name
        archiverName = "INVALID";
        bytes = new byte[]{0x07, 0x4b, 0x50, 0xda, 0x04, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10};
        inputStream = new ByteArrayInputStream(bytes);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            PowerMockito.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            inputStream.close();
        }

        // Test with empty bytes
        bytes = new byte[0];
        inputStream = new ByteArrayInputStream(bytes);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            PowerMockito.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            inputStream.close();
        }
    }
}