import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.Date;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    private ZipArchiveEntry zipEntry;

    @Before
    public void setUp() {
        zipEntry = new ZipArchiveEntry("testFile.txt");
    }

    @Test
    public void testConstructorWithName() {
        assertEquals("testFile.txt", zipEntry.getName());
    }

    @Test
    public void testSetMethod() {
        zipEntry.setMethod(1);
        assertEquals(1, zipEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        zipEntry.setInternalAttributes(123);
        assertEquals(123, zipEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        zipEntry.setExternalAttributes(456L);
        assertEquals(456L, zipEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        zipEntry.setUnixMode(755);
        assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        zipEntry.setPlatform(3);
        assertEquals(3, zipEntry.getPlatform());
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField extraField = new TestExtraField();
        zipEntry.addExtraField(extraField);
        assertEquals(1, zipEntry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField extraField = new TestExtraField();
        zipEntry.addExtraField(extraField);
        zipEntry.removeExtraField(new ZipShort(1));
        assertEquals(0, zipEntry.getExtraFields().length);
    }

    @Test(expected = ZipException.class)
    public void testSetExtraWithInvalidData() throws ZipException {
        zipEntry.setExtra(new byte[]{0, 1, 2, 3});
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipEntry2 = new ZipArchiveEntry("testFile.txt");
        assertTrue(zipEntry.equals(zipEntry2));
    }

    private class TestExtraField implements ZipExtraField {

        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(1);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }
    }
}