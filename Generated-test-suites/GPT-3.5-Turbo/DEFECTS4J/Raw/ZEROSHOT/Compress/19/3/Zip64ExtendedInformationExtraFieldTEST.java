import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = Zip64ExtendedInformationExtraField.HEADER_ID;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expected = new ZipShort(16);
        ZipShort actual = new Zip64ExtendedInformationExtraField().getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expected = new ZipShort(28);
        ZipShort actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithSizes() {
        byte[] expected = new byte[16];
        expected[0] = 0;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 0;
        expected[5] = 0;
        expected[6] = 0;
        expected[7] = 0;
        expected[8] = 0;
        expected[9] = 0;
        expected[10] = 0;
        expected[11] = 0;
        expected[12] = 0;
        expected[13] = 0;
        expected[14] = 0;
        expected[15] = 0;
        byte[] actual = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(0), new ZipEightByteInteger(0)).getLocalFileDataData();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithInvalidSizes() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(0), null).getLocalFileDataData();
    }

    @Test
    public void testGetLocalFileDataDataWithoutSizes() {
        byte[] expected = new byte[0];
        byte[] actual = new Zip64ExtendedInformationExtraField().getLocalFileDataData();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryDataWithAllFields() {
        byte[] expected = new byte[28];
        expected[0] = 0;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 0;
        expected[5] = 0;
        expected[6] = 0;
        expected[7] = 0;
        expected[8] = 0;
        expected[9] = 0;
        expected[10] = 0;
        expected[11] = 0;
        expected[12] = 0;
        expected[13] = 0;
        expected[14] = 0;
        expected[15] = 0;
        expected[16] = 0;
        expected[17] = 0;
        expected[18] = 0;
        expected[19] = 0;
        expected[20] = 0;
        expected[21] = 0;
        expected[22] = 0;
        expected[23] = 0;
        expected[24] = 0;
        expected[25] = 0;
        expected[26] = 0;
        expected[27] = 0;
        byte[] actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryData();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryDataWithoutFields() {
        byte[] expected = new byte[0];
        byte[] actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryData();
        assertEquals(expected, actual);
    }

    // Continue testing for all other methods...

}