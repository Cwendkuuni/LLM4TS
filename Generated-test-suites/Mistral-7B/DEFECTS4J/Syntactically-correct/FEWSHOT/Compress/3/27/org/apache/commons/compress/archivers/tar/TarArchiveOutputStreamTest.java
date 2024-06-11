package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testCreateArchiveWithEmptyFile() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(
                new ByteArrayOutputStream(1024))) {

            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putNextEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new ByteArrayInputStream(tarOut.toByteArray()));
            ArchiveEntry archiveEntry = tarIn.getNextEntry();
            assertEquals("test.txt", archiveEntry.getName());
            assertNull(archiveEntry.getSize());
            assertNull(archiveEntry.getTimeStamp());
            tarIn.close();
        }
    }

    @Test
    public void testCreateArchiveWithNormalFile() throws IOException {
        String testFilePath = "test_file.txt";
        String archiveFileName = "test_archive.tar";

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(
                new FileOutputStream(archiveFileName))) {

            TarArchiveEntry entry = new TarArchiveEntry(new File(testFilePath));
            tarOut.putNextEntry(entry);
            tarOut.write(new byte[] { 'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '\n' });
            tarOut.closeArchiveEntry();
            tarOut.finish();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new FileInputStream(archiveFileName));
            ArchiveEntry archiveEntry = tarIn.getNextEntry();
            assertEquals("test_file.txt", archiveEntry.getName());
            assertEquals(Files.size(new File(testFilePath).toPath()),
                    archiveEntry.getSize());
            InputStream entryStream = tarIn.getInputStream();
            byte[] buffer = new byte[10];
            int len = entryStream.read(buffer);
            assertEquals("Test File\n", new String(Arrays.copyOfRange(buffer, 0, len),
                    StandardCharsets.UTF_8));
            tarIn.closeEntry();
            tarIn.close();

            Files.delete(new File(testFilePath).toPath());
            new File(archiveFileName).delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateArchiveWithLongFileName() throws IOException {
        String testFilePath = "test_long_file_name.txt";
        String archiveFileName = "test_archive.tar";

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(
                new FileOutputStream(archiveFileName))) {
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            TarArchiveEntry entry = new TarArchiveEntry(new File(testFilePath));
            tarOut.putNextEntry(entry);
            tarOut.write(new byte[0]); // NUL terminator for long file name
            tarOut.write(entry.getName().getBytes(StandardCharsets.UTF_8));
            tarOut.closeArchiveEntry();
            tarOut.finish();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new FileInputStream(archiveFileName));
            ArchiveEntry archiveEntry = tarIn.getNextEntry();
            assertEquals("test_long_file_name.txt", archiveEntry.getName());
            tarIn.close();

            Files.delete(new File(testFilePath).toPath());
            new File(archiveFileName).delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateArchiveWithLongFileNameError() throws IOException {
        String testFilePath = "test_long_file_name.txt";
        String archiveFileName = "test_archive.tar";

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(
                new FileOutputStream(archiveFileName))) {
            TarArchiveEntry entry = new TarArchiveEntry(new File(testFilePath));
            tarOut.putNextEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new FileInputStream(archiveFileName));
            assertThrows(RuntimeException.class, () -> tarIn.getNextEntry());
            tarIn.close();

            Files.delete(new File(testFilePath).toPath());
            new File(archiveFileName).delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateArchiveWithLongFileNameTruncate() throws IOException {
        String testFilePath = "test_long_file_name.txt";
        String archiveFileName = "test_archive.tar";

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(
                new FileOutputStream(archiveFileName))) {
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

            TarArchiveEntry entry = new TarArchiveEntry(new File(testFilePath));
            tarOut.putNextEntry(entry);
            tarOut.write("test_long_file_name.txt".substring(0, TarConstants.NAMELEN));
            tarOut.closeArchiveEntry();
            tarOut.finish();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new FileInputStream(archiveFileName));
            ArchiveEntry archiveEntry = tarIn.getNextEntry();
            assertEquals("test_long_file_name.txt", archiveEntry.getName());
            tarIn.close();

            Files.delete(new File(testFilePath).toPath());
            new File(archiveFileName).delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}