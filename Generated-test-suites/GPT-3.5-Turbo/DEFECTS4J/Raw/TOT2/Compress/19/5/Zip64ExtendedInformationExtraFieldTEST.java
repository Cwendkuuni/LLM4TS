import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField zipExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), zipExtraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField zipExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), zipExtraField.getLocalFileDataLength());
    }

    // Add more test cases for each method

}