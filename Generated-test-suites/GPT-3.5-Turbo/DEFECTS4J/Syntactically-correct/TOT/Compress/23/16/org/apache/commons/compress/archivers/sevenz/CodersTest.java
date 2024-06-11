import org.junit.Test;
import org.apache.commons.compress.archivers.sevenz.Coders;
import java.io.*;
import static org.junit.Assert.*;

public class CodersTest {

	@Test
	public void testAddDecoderCopy() throws IOException {
		InputStream is = new ByteArrayInputStream("test string".getBytes());
		Coders.Coder coder = new Coders.Coder();
		coder.decompressionMethodId = new byte[]{0x03, 0x03};
		byte[] password = null;
		InputStream result = Coders.addDecoder(is, coder, password);
		byte[] data = new byte[11];
		result.read(data);
		assertEquals("test string", new String(data));
	}

	@Test
	public void testAddDecoderLZMA() throws IOException {
		// Creating compressed input stream
		byte[] input = new byte[] {0x5D, 0x00, 0x40, 0x18, 0x6E, 0x99, 0x0E, 0x02, 0x85, 0x19, 0x09, 0x14, 0x0E, 0x25, 0x3D};
		LZMAInputStream compressed = new LZMAInputStream(new ByteArrayInputStream(input));
		
		Coders.Coder coder = new Coders.Coder();
		coder.decompressionMethodId = new byte[]{0x05, 0x01};
		byte[] password = null;
		InputStream result = Coders.addDecoder(compressed, coder, password);
		
		// Reading decompressed data
		byte[] data = new byte[11];
		result.read(data);
		
		// Asserting data
		assertEquals("test string", new String(data));
	}
	
	@Test
	public void testAddEncoderDeflate() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream result = Coders.addEncoder(out, SevenZMethod.DEFLATE, null);
		byte[] input = "test string".getBytes();
		result.write(input);
		result.close();
		byte[] compressed = out.toByteArray();
		assertNotEquals(input, compressed);
	}
	
	@Test
	public void testAddEncoderAES256SHA256() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream result = Coders.addEncoder(out, SevenZMethod.AES256SHA256, null);
		byte[] input = "test string".getBytes();
		result.write(input);
		result.close();
		byte[] compressed = out.toByteArray();
		assertNotEquals(input, compressed);
	}

}