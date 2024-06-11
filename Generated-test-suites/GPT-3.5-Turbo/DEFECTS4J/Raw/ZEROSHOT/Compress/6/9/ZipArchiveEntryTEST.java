import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveEntryTest {

    private ZipArchiveEntry zipArchiveEntry;

    @Before
    public void setUp() {
        zipArchiveEntry = new ZipArchiveEntry("testfile.txt");
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        zipArchiveEntry.setMethod(5);
        Assert.assertEquals(5, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        zipArchiveEntry.setInternalAttributes(10);
        Assert.assertEquals(10, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        zipArchiveEntry.setExternalAttributes(20);
        Assert.assertEquals(20, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        zipArchiveEntry.setUnixMode(3);
        Assert.assertEquals(3, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        Assert.assertEquals(0, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        zipArchiveEntry.setPlatform(3);
        Assert.assertEquals(3, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField extraField = new ZipExtraField("test", new byte[0]);
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField extraField = new ZipExtraField("test", new byte[0]);
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(new ZipShort("test"));
        Assert.assertEquals(0, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testGetExtraField() {
        ZipExtraField extraField = new ZipExtraField("test", new byte[0]);
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertEquals(extraField, zipArchiveEntry.getExtraField(new ZipShort("test")));
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testfile.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(zipArchiveEntry.isDirectory());
        zipArchiveEntry.setName("directory/");
        Assert.assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testSetName() {
        zipArchiveEntry.setName("newfile.txt");
        Assert.assertEquals("newfile.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals("testfile.txt".hashCode(), zipArchiveEntry.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("testfile.txt");
        Assert.assertTrue(zipArchiveEntry.equals(zipArchiveEntry2));
    }
}