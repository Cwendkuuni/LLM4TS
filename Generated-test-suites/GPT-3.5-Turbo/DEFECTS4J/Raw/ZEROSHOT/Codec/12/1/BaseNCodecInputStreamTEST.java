import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.BaseNCodec;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException{
        byte[] data = new byte[]{0,1,2,3,4,5,6,7,8,9};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        for(int i = 0; i < data.length; i++){
            int b = inputStream.read();
            assertEquals((int)data[i], b);
        }
        int b = inputStream.read();
        assertEquals(-1, b);
        inputStream.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException{
        byte[] data = new byte[]{0,1,2,3,4,5,6,7,8,9};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        byte[] buffer = new byte[5];
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        int bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        for(int i = 0; i < 5; i++){
            assertEquals(data[i], buffer[i]);
        }
        bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        for(int i = 0; i < 5; i++){
            assertEquals(data[i + 5], buffer[i]);
        }
        bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(-1, bytesRead);
        inputStream.close();
    }

    @Test
    public void testMarkSupported(){
        byte[] data = new byte[]{0,1,2,3,4,5,6,7,8,9};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        assertFalse(inputStream.markSupported());
        inputStream.close();
    }
}