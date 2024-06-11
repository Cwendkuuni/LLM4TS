import org.junit.Test;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testParseFromLocalFileDataWithValidInput() {
        Zip64ExtendedInformationExtraField zip64Ext = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        try {
            zip64Ext.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            fail("Unexpected ZipException: " + e.getMessage());
        }
        assertNotNull("Size should not be null", zip64Ext.getSize());
        assertNotNull("Compressed size should not be null", zip64Ext.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidInput() throws ZipException {
        Zip64ExtendedInformationExtraField zip64Ext = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00}; // Missing data
        zip64Ext.parseFromLocalFileData(data, 0, data.length);
    }
    
    // Add more test cases here for the other public methods in Zip64ExtendedInformationExtraField class

}