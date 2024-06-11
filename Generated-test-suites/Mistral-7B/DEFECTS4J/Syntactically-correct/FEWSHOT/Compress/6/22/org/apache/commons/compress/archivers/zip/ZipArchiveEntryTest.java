package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.BytesConsumer;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test class for ZipArchiveEntry.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testCreateZipArchiveEntry() {
        // Test creating a new ZipArchiveEntry with an empty constructor
        ZipArchiveEntry zipEntry = new ZipArchiveEntry();
        assertNull(zipEntry.getName());
        assertEquals(0, zipEntry.getSize());
        assertEquals(0, zipEntry.getMethod());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(0, zipEntry.getExternalAttributes());
        assertNull(zipEntry.getExtraFields());

        // Test creating a new ZipArchiveEntry with a File object
        File file = new File("test.txt");
        zipEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(file.length(), zipEntry.getSize());
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());
        assertNull(zipEntry.getInternalAttributes());
        assertNull(zipEntry.getExternalAttributes());
        assertNull(zipEntry.getExtraFields());

        // Test creating a new ZipArchiveEntry with a ZipEntry object
        ZipEntry zipEntryFromZip = new ZipEntry("test.txt");
        zipEntry = new ZipArchiveEntry(zipEntryFromZip);
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(zipEntryFromZip.getSize(), zipEntry.getSize());
        assertEquals(zipEntryFromZip.getMethod(), zipEntry.getMethod());
        assertEquals(zipEntryFromZip.getInternalAttributes(), zipEntry.getInternalAttributes());
        assertEquals(zipEntryFromZip.getExternalAttributes(), zipEntry.getExternalAttributes());
        assertArrayEquals(zipEntryFromZip.getExtra(), zipEntry.getExtra());

        // Test creating a new ZipArchiveEntry with a ZipArchiveEntry object
        ZipArchiveEntry zipEntryFromZipArchive = new ZipArchiveEntry("test.txt");
        zipEntry = new ZipArchiveEntry(zipEntryFromZipArchive);
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(zipEntryFromZipArchive.getSize(), zipEntry.getSize());
        assertEquals(zipEntryFromZipArchive.getMethod(), zipEntry.getMethod());
        assertEquals(zipEntryFromZipArchive.getInternalAttributes(), zipEntry.getInternalAttributes());
        assertEquals(zipEntryFromZipArchive.getExternalAttributes(), zipEntry.getExternalAttributes());
        assertArrayEquals(zipEntryFromZipArchive.getExtra(), zipEntry.getExtra());
    }

    @Test
    public void testGetSetInternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setInternalAttributes(ZipEntry.FILE_FLAG);
        assertEquals(ZipEntry.FILE_FLAG, zipEntry.getInternalAttributes());
    }

    @Test
    public void testGetSetExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setExternalAttributes(0xFFL);
        assertEquals(0xFFL, zipEntry.getExternalAttributes());
    }

    @Test
    public void testGetSetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setUnixMode(0644);
        assertEquals(0644, zipEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData);
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.COMMENT, "Test Comment", new byte[0]);

        zipEntry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        assertArrayEquals(extraData, zipEntry.getExtraField(ZipShort.CREATE_TIME).getData());
        assertArrayEquals(new byte[0], zipEntry.getExtraField(ZipShort.COMMENT).getData());
        assertNull(zipEntry.getExtraField(ZipShort.UNDEFINED));
    }

    @Test
    public void testAddExtraFields() {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData);
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.COMMENT, "Test Comment", new byte[0]);

        zipEntry.addExtraField(extraField1);
        zipEntry.addExtraField(extraField2);

        assertArrayEquals(extraData, zipEntry.getExtraField(ZipShort.CREATE_TIME).getData());
        assertArrayEquals(new byte[0], zipEntry.getExtraField(ZipShort.COMMENT).getData());
        assertNull(zipEntry.getExtraField(ZipShort.UNDEFINED));
    }

    @Test
    public void testAddAsFirstExtraField() {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData);
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.COMMENT, "Test Comment", new byte[0]);

        zipEntry.addAsFirstExtraField(extraField1);
        zipEntry.addAsFirstExtraField(extraField2);

        assertArrayEquals(extraData, zipEntry.getExtraField(ZipShort.CREATE_TIME).getData());
        assertArrayEquals(new byte[0], zipEntry.getExtraField(ZipShort.COMMENT).getData());
        assertNull(zipEntry.getExtraField(ZipShort.UNDEFINED));
    }

    @Test
    public void testRemoveExtraField() {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField1 = new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData);
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.COMMENT, "Test Comment", new byte[0]);

        zipEntry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        zipEntry.removeExtraField(ZipShort.CREATE_TIME);

        assertNull(zipEntry.getExtraField(ZipShort.CREATE_TIME));
        assertArrayEquals(new byte[0], zipEntry.getExtraField(ZipShort.COMMENT).getData());
        assertNull(zipEntry.getExtraField(ZipShort.UNDEFINED));
    }

    @Test
    public void testParseExtraData() throws IOException {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BytesConsumer consumer = IOUtils.toBytesConsumer(out);
        zipEntry.setExtra(consumer);
        zipEntry.setExtraFields(new ZipExtraField[]{new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData)});
        consumer.consume(extraData);
        consumer.flush();
        byte[] parsedExtraData = zipEntry.getExtra();

        assertEquals(extraData.length, parsedExtraData.length);
        for (int i = 0; i < extraData.length; i++) {
            assertEquals(extraData[i], parsedExtraData[i]);
        }
    }

    @Test
    public void testParseCentralDirectoryExtraData() throws IOException {
        byte[] extraData = new byte[]{(byte) 0x08, (byte) 0x01, 0x00, 0x00, (byte) 0x02, 0x00, (byte) 0x03};
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");

        byte[] centralDirectoryExtraData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[]{
                new ZipExtraField(ZipShort.CREATE_TIME, "Test Create Time", extraData)
        });

        zipEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        byte[] parsedExtraData = zipEntry.getExtraField(ZipShort.CREATE_TIME).getData();

        assertEquals(extraData.length, parsedExtraData.length);
        for (int i = 0; i < extraData.length; i++) {
            assertEquals(extraData[i], parsedExtraData[i]);
        }
    }
}