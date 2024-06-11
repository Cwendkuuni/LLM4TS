package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

	@Test
	public void testParseOctal() {
		byte[] buffer = new byte[10];
		buffer[0] = 49;
		buffer[1] = 50;
		buffer[2] = 51;

		long result = TarUtils.parseOctal(buffer, 0, 3);

		assertEquals(123, result);
	}

	@Test
	public void testParseName() {
		byte[] buffer = new byte[5];
		buffer[0] = 116;
		buffer[1] = 101;
		buffer[2] = 115;
		buffer[3] = 116;

		String result = TarUtils.parseName(buffer, 0, 4);

		assertEquals("test", result);
	}

	@Test
	public void testFormatNameBytes() {
		String name = "test";
		byte[] buf = new byte[5];

		int result = TarUtils.formatNameBytes(name, buf, 0, 5);

		assertEquals(5, result);
		assertEquals(116, buf[0]);
		assertEquals(101, buf[1]);
		assertEquals(115, buf[2]);
		assertEquals(116, buf[3]);
		assertEquals(0, buf[4]);
	}

	@Test
	public void testFormatUnsignedOctalString() {
		byte[] buffer = new byte[5];

		TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);

		assertEquals(49, buffer[0]);
		assertEquals(50, buffer[1]);
		assertEquals(51, buffer[2]);
		assertEquals(48, buffer[3]);
		assertEquals(48, buffer[4]);
	}

	@Test
	public void testFormatOctalBytes() {
		byte[] buf = new byte[6];

		int result = TarUtils.formatOctalBytes(123, buf, 0, 6);

		assertEquals(6, result);
		assertEquals(49, buf[0]);
		assertEquals(50, buf[1]);
		assertEquals(51, buf[2]);
		assertEquals(32, buf[3]);
		assertEquals(0, buf[4]);
		assertEquals(0, buf[5]);
	}

	@Test
	public void testFormatLongOctalBytes() {
		byte[] buf = new byte[6];

		int result = TarUtils.formatLongOctalBytes(123, buf, 0, 6);

		assertEquals(6, result);
		assertEquals(49, buf[0]);
		assertEquals(50, buf[1]);
		assertEquals(51, buf[2]);
		assertEquals(32, buf[3]);
		assertEquals(0, buf[4]);
		assertEquals(32, buf[5]);
	}

	@Test
	public void testFormatCheckSumOctalBytes() {
		byte[] buf = new byte[6];

		int result = TarUtils.formatCheckSumOctalBytes(123, buf, 0, 6);

		assertEquals(6, result);
		assertEquals(49, buf[0]);
		assertEquals(50, buf[1]);
		assertEquals(51, buf[2]);
		assertEquals(0, buf[3]);
		assertEquals(32, buf[4]);
		assertEquals(32, buf[5]);
	}

	@Test
	public void testComputeCheckSum() {
		byte[] buf = new byte[5];
		buf[0] = 49;
		buf[1] = 50;
		buf[2] = 51;
		buf[3] = 52;

		long result = TarUtils.computeCheckSum(buf);

		assertEquals(190, result);
	}

}