import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraFieldTest {
    
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileData_invalidData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(new byte[0], 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testZip64ExtendedInformationExtraField_constructor_invalidArguments() {
        new Zip64ExtendedInformationExtraField(null, null);
    }
    
    @Test
    public void testZip64ExtendedInformationExtraField_constructor_validArguments() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }
    
    @Test
    public void testZip64ExtendedInformationExtraField_settersAndGetters() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4321L);
        ZipLong diskStartNumber = new ZipLong(9876L);
        
        extraField.setSize(size);
        assertEquals(size, extraField.getSize());
        
        extraField.setCompressedSize(compressedSize);
        assertEquals(compressedSize, extraField.getCompressedSize());
        
        extraField.setRelativeHeaderOffset(relativeHeaderOffset);
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        
        extraField.setDiskStartNumber(diskStartNumber);
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }
}