import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class CodersTest {

    @Test
    public void testAddDecoder_Copy() {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Coders.Coder coder = new Coders.Coder(SevenZMethod.COPY, new byte[0]);
        byte[] password = new byte[0];
        
        try {
            InputStream decoded = Coders.addDecoder(input, coder, password);
            assertNotNull(decoded);
            byte[] result = new byte[data.length];
            decoded.read(result);
            assertArrayEquals(data, result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAddEncoder_Deflate() {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = new byte[0];
        
        try {
            OutputStream encoded = Coders.addEncoder(output, method, password);
            assertNotNull(encoded);
            encoded.write(data);
            encoded.flush();
            byte[] result = output.toByteArray();
            assertNotNull(result);
            assertNotEquals(data.length, result.length);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCoderTableInitialization() {
        Coders.CoderId[] table = Coders.coderTable;
        assertNotNull(table);
        assertEquals(6, table.length);
    }

    // Add more test cases for other methods as needed

}