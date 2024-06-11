import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        // Arrange
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500L);

        // Act
        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(size, compressedSize);

        // Assert
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        // Arrange
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100L);
        ZipLong diskStart = new ZipLong(1);

        // Act
        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(size, compressedSize,
                        relativeHeaderOffset, diskStart);

        // Assert
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSize() {
        // Arrange
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500L);

        // Act
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    // TODO: Add more test cases to cover all public methods, edge cases, and exception scenarios

}