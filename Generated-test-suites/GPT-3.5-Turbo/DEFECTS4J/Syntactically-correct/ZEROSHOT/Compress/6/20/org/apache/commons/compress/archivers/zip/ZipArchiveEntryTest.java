import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveEntryTest {
    private ZipArchiveEntry zipEntry;

    @Before
    public void setUp() {
        zipEntry = new ZipArchiveEntry("testEntry");
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testEntry", zipEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(zipEntry.isDirectory());
    }

    @Test
    public void testGetLastModifiedDate() {
        Date lastModifiedDate = zipEntry.getLastModifiedDate();
        Assert.assertNotNull(lastModifiedDate);
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(zipEntry.equals(new ZipArchiveEntry("testEntry")));
        Assert.assertFalse(zipEntry.equals(new ZipArchiveEntry("otherEntry")));
    }

    @Test
    public void testSetterAndGetter() {
        zipEntry.setMethod(5);
        Assert.assertEquals(5, zipEntry.getMethod());

        zipEntry.setInternalAttributes(10);
        Assert.assertEquals(10, zipEntry.getInternalAttributes());

        zipEntry.setExternalAttributes(20);
        Assert.assertEquals(20, zipEntry.getExternalAttributes());

        ZipArchiveEntry extraField = new ZipArchiveEntry("extraField");
        zipEntry.addExtraField(extraField);
        Assert.assertEquals(1, zipEntry.getExtraFields().length);
        Assert.assertEquals(extraField, zipEntry.getExtraFields()[0]);

        zipEntry.removeExtraField(new ZipShort(20));
        Assert.assertEquals(0, zipEntry.getExtraFields().length);

        zipEntry.setExtra(new byte[]{1, 2, 3});
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, zipEntry.getExtra());

        zipEntry.setExtra(new byte[]{4, 5, 6});
        Assert.assertArrayEquals(new byte[]{4, 5, 6}, zipEntry.getExtra());
    }
}