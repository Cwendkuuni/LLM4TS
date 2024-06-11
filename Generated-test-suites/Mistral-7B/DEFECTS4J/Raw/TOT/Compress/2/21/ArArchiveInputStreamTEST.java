import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        // Test with valid data
        final byte[] inputData = new byte[]{
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 
                'A'.byteValue(), 'R'.byteValue(), 'C'.byteValue(), 
                'H'.byteValue(), 'E'.byteValue(), 'A'.byteValue(), 
                'D'.byteValue(), 'E'.byteValue(), 
                'R'.byteValue(), 'I'.byteValue(), 'S'.byteValue(), 
                't'.byteValue(), 'e'.byteValue(), 's'.byteValue(), 
                't'.byteValue(), ' ' .byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), 0x3e, 0x0a, 
                'A'.byteValue(), 'R'.byteValue(), 'C'.byteValue(), 
                'H'.byteValue(), 'E'.byteValue(), 'A'.byteValue(), 
                'D'.byteValue(), 'E'.byteValue(), 
                'T'.byteValue(), 'r'.byteValue(), 'i'.byteValue(), 
                'm'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a, 
                'N'.byteValue(), 'A'.byteValue(), 'M'.byteValue(), 
                'E'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a, 
                'E'.byteValue(), 'O'.byteValue(), 'F'.byteValue(), 
                'F'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a,
                -1
        };

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry expectedEntry1 = new ArArchiveEntry("test1.txt", 10L);
        ArArchiveEntry expectedEntry2 = new ArArchiveEntry("test2.txt", 11L);
        ArArchiveEntry expectedEntry3 = new ArArchiveEntry("test3.txt", 12L);

        ArArchiveEntry actualEntry1 = arInputStream.getNextArEntry();
        Assert.assertNotNull(actualEntry1);
        Assert.assertEquals(expectedEntry1, actualEntry1);

        ArArchiveEntry actualEntry2 = arInputStream.getNextArEntry();
        Assert.assertNotNull(actualEntry2);
        Assert.assertEquals(expectedEntry2, actualEntry2);

        ArArchiveEntry actualEntry3 = arInputStream.getNextArEntry();
        Assert.assertNotNull(actualEntry3);
        Assert.assertEquals(expectedEntry3, actualEntry3);

        ArArchiveEntry actualEntry4 = arInputStream.getNextArEntry();
        Assert.assertNull(actualEntry4);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidData() throws IOException {
        // Test with invalid data
        final byte[] inputData = new byte[]{
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 
                'A'.byteValue(), 'R'.byteValue(), 'C'.byteValue(), 
                'H'.byteValue(), 'E'.byteValue(), 'A'.byteValue(), 
                'D'.byteValue(), 'E'.byteValue(), 
                'R'.byteValue(), 'I'.byteValue(), 'S'.byteValue(), 
                't'.byteValue(), 'e'.byteValue(), 's'.byteValue(), 
                't'.byteValue(), ' ' .byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '0'.byteValue(), 0x3e, 0x0a,
                'A'.byteValue(), 'R'.byteValue(), 'C'.byteValue(), 
                'H'.byteValue(), 'E'.byteValue(), 'A'.byteValue(), 
                'D'.byteValue(), 'E'.byteValue(), 
                'T'.byteValue(), 'r'.byteValue(), 'i'.byteValue(), 
                'm'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a,
                'N'.byteValue(), 'A'.byteValue(), 'M'.byteValue(), 
                'E'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a,
                'E'.byteValue(), 'O'.byteValue(), 'F'.byteValue(), 
                'F'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), ' '.byteValue(), '1'.byteValue(), 
                '1'.byteValue(), 0x3e, 0x0a,
                'A'.byteValue() // missing 0a at the end
        };

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.getNextArEntry();
    }
}